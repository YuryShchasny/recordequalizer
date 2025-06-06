#include <cassert>
#include <utility>
#include "Log.h"
#include "AudioEngine.h"

using namespace oboe;

AudioEngine::AudioEngine() {
    mEqualizer = new Equalizer();
    mAmplitudeEffect = new AmplitudeEffect(0.0f);
    mCompressEffect = new CompressEffect(-10.0f, 4.0f);
    mChannelsEffect = new ChannelsEffect(false, false);
    mEffects->push_back(mEqualizer);
    mEffects->push_back(mAmplitudeEffect);
    mEffects->push_back(mChannelsEffect);
}

void AudioEngine::setRecordingDeviceId(int32_t deviceId) {
    mRecordingDeviceId = deviceId;
}

void AudioEngine::setPlaybackDeviceId(int32_t deviceId) {
    mPlaybackDeviceId = deviceId;
}

void AudioEngine::play(bool withRecord) {
    bool success = openStreams(withRecord) == Result::OK;
    if (success) {
        mIsPlaying = true;
    } else {
        LOGD("failed to open input output streams");
    }
}

void AudioEngine::stop() {
    if (mDuplexStream) {
        mDuplexStream->setRecording(false);
    }
    closeStreams();
    mIsPlaying = false;
}

void AudioEngine::destroy() {
    if (mDuplexStream) {
        mDuplexStream->setRecording(false);
        mDuplexStream->clearRecordings();
    }
    closeStreams();
    mIsPlaying = false;
    for (Effect *effect: *mEffects) {
        delete effect;
    }
}

bool AudioEngine::isPlaying() const {
    return mIsPlaying;
}

Result AudioEngine::closeStreams() {
    Result result;
    if (mDuplexStream) {
        result = mDuplexStream->stop();
        if (result != Result::OK) return result;
    }
    result = closeStream(mPlayStream);
    if (result != Result::OK) return result;
    result = closeStream(mRecordingStream);
    mDuplexStream.reset();
    return result;
}

Result AudioEngine::openStreams(bool withRecording) {
    AudioStreamBuilder inBuilder, outBuilder;
    setupPlaybackStreamParameters(&outBuilder);
    Result result = outBuilder.openStream(mPlayStream);
    if (result != oboe::Result::OK) {
        LOGD("Failed to open output stream. Error %s", convertToText(result));
        mSampleRate = oboe::kUnspecified;
        return result;
    } else {
        mSampleRate = mPlayStream->getSampleRate();
    }
    warnIfNotLowLatency(mPlayStream);

    setupRecordingStreamParameters(&inBuilder, mSampleRate);
    result = inBuilder.openStream(mRecordingStream);
    if (result != oboe::Result::OK) {
        LOGD("Failed to open input stream. Error %s", convertToText(result));
        closeStream(mPlayStream);
        return result;
    }
    warnIfNotLowLatency(mRecordingStream);

    mDuplexStream = std::make_unique<FullDuplexPass>();
    mDuplexStream->setSharedInputStream(mRecordingStream);
    mDuplexStream->setSharedOutputStream(mPlayStream);
    if (mFrequenciesSize > 0) {
        mEqualizer->initialize(mFrequenciesSize, mFrequencies, mFrequencyGains, mSampleRate);
    }
    if (mOnAudioReadyCallback) {
        mDuplexStream->onAudioDataReady = std::move(mOnAudioReadyCallback);
    }
    mDuplexStream->effects = mEffects;
    if (withRecording) {
        mDuplexStream->setRecording(true);
    }
    mDuplexStream->start();
    return result;
}


AudioStreamBuilder *AudioEngine::setupRecordingStreamParameters(
        oboe::AudioStreamBuilder *builder, int32_t sampleRate) {
    builder->setDeviceId(mRecordingDeviceId)
            ->setDirection(oboe::Direction::Input)
            ->setSampleRate(sampleRate)
            ->setChannelCount(mChannelCount);
    return setupCommonStreamParameters(builder);
}

AudioStreamBuilder *AudioEngine::setupPlaybackStreamParameters(
        oboe::AudioStreamBuilder *builder) {
    builder->setDataCallback(this)
            ->setErrorCallback(this)
            ->setDeviceId(mPlaybackDeviceId)
            ->setDirection(oboe::Direction::Output)
            ->setChannelCount(mChannelCount);

    return setupCommonStreamParameters(builder);
}

AudioStreamBuilder *AudioEngine::setupCommonStreamParameters(
        oboe::AudioStreamBuilder *builder) {
    builder->setFormat(mFormat)
            ->setFormatConversionAllowed(true)
            ->setSampleRateConversionQuality(SampleRateConversionQuality::Best)
            ->setSharingMode(oboe::SharingMode::Exclusive)
            ->setPerformanceMode(oboe::PerformanceMode::LowLatency);
    return builder;
}

Result AudioEngine::closeStream(std::shared_ptr<oboe::AudioStream> &stream) {
    if (stream) {
        Result result = stream->stop();
        if (result != oboe::Result::OK) {
            LOGD("Error stopping stream: %s", convertToText(result));
        }
        result = stream->close();
        if (result != oboe::Result::OK) {
            LOGD("Error closing stream: %s", convertToText(result));
        } else {
            LOGD("Successfully closed stream");
        }
        stream.reset();
        return result;
    }
}

void AudioEngine::warnIfNotLowLatency(std::shared_ptr<oboe::AudioStream> &stream) {
    if (stream->getPerformanceMode() != oboe::PerformanceMode::LowLatency) {
        LOGD(
                "Stream is NOT low latency. PerformanceMode: %d \n"
                "Check your requested format, sample rate and channel count",
                stream->getPerformanceMode());
    }
}

DataCallbackResult AudioEngine::onAudioReady(
        oboe::AudioStream *oboeStream, void *audioData, int32_t numFrames) {
    return mDuplexStream->onAudioReady(oboeStream, audioData, numFrames);
}

void AudioEngine::onErrorBeforeClose(oboe::AudioStream *oboeStream,
                                     oboe::Result error) {
    LOGD("%s stream Error before close: %s",
         convertToText(oboeStream->getDirection()),
         convertToText(error));
}

void AudioEngine::onErrorAfterClose(oboe::AudioStream *oboeStream,
                                    oboe::Result error) {
    LOGD("%s stream Error after close: %s",
         oboe::convertToText(oboeStream->getDirection()),
         oboe::convertToText(error));

    closeStreams();
}

void AudioEngine::changeLeftChannel(bool enabled) {
    if (mChannelsEffect) {
        mChannelsEffect->setMute(Channel::LEFT, !enabled);
    }
}

void AudioEngine::changeRightChannel(bool enabled) {
    if (mChannelsEffect) {
        mChannelsEffect->setMute(Channel::RIGHT, !enabled);
    }
}

void AudioEngine::setEqualizer(int frequenciesSize, int *frequencies, float *frequencyGains) {
    mFrequenciesSize = frequenciesSize;
    mFrequencies = frequencies;
    mFrequencyGains = frequencyGains;
}

void AudioEngine::setGain(int frequency, float gain) {
    for (int i = 0; i < mFrequenciesSize; ++i) {
        if (mFrequencies[i] == frequency) {
            mFrequencyGains[i] = gain;
            if(mEqualizer->isReady()) {
                mEqualizer->updateGain(i, mFrequencyGains[i]);
            }
        }
    }
}

void AudioEngine::setFrequencyGains(float *frequencyGains) {
    for (int i = 0; i < mFrequenciesSize; ++i) {
        mFrequencyGains[i] = frequencyGains[i];
        if(mEqualizer->isReady()) {
            mEqualizer->updateGain(i, frequencyGains[i]);
        }
    }
}

void AudioEngine::setAmplitude(float gain) {
    if (mAmplitudeEffect) {
        mAmplitudeEffect->setAmplitude(gain);
    }
}

void AudioEngine::setAudioDataCallback(std::function<void(std::vector<float>)> callback) {
    mOnAudioReadyCallback = callback;
    if (mDuplexStream) {
        mDuplexStream->onAudioDataReady = std::move(callback);
    }
}

void AudioEngine::enableCompressor(bool enabled) {
    if (enabled) {
        bool hasCompressor = std::any_of(mEffects->begin(), mEffects->end(), [](Effect *effect) {
            return typeid(*effect) == typeid(CompressEffect);
        });
        if (!hasCompressor) {
            mEffects->insert(mEffects->begin(), mCompressEffect);
        }
    } else {
        auto it = std::find(mEffects->begin(), mEffects->end(), mCompressEffect);
        if (it != mEffects->end()) {
            mEffects->erase(it);
        }
    }
}
