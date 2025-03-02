#include <cassert>
#include "Log.h"
#include "AudioEngine.h"

using namespace oboe;

AudioEngine::AudioEngine() = default;

void AudioEngine::setRecordingDeviceId(int32_t deviceId) {
    mRecordingDeviceId = deviceId;
    if (isPlaying()) {
        closeStreams();
        openStreams();
    }
}

void AudioEngine::setPlaybackDeviceId(int32_t deviceId) {
    mPlaybackDeviceId = deviceId;
    if (isPlaying()) {
        closeStreams();
        openStreams();
    }
}

void AudioEngine::play() {
    bool success = openStreams() == Result::OK;
    if (success) {
        mIsPlaying = true;
    } else {
        LOGD("failed to open input output streams");
    }
}

void AudioEngine::stop() {
    closeStreams();
    mIsPlaying = false;
}

bool AudioEngine::isPlaying() const {
    return mIsPlaying;
}

void AudioEngine::closeStreams() {
    if(mDuplexStream) {
        mDuplexStream->stop();
    }
    closeStream(mPlayStream);
    closeStream(mRecordingStream);
    mDuplexStream.reset();
}

Result AudioEngine::openStreams() {
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
        mDuplexStream->initEqualizer(mFrequenciesSize, mFrequencies, mFrequencyGains, mSampleRate);
    }
    mDuplexStream->amplitude = mAmplitude;
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

void AudioEngine::closeStream(std::shared_ptr<oboe::AudioStream> &stream) {
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

    if (error == oboe::Result::ErrorDisconnected) {
        LOGD("Restarting AudioStream");
        openStreams();
    }
}

void AudioEngine::changeLeftChannel(bool enabled) {
    mDuplexStream->leftChannelEnabled = enabled;
}

void AudioEngine::changeRightChannel(bool enabled) {
    mDuplexStream->rightChannelEnabled = enabled;
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
            if(mDuplexStream) {
                mDuplexStream->equalizer->updateGain(i, mFrequencyGains[i]);
            }
        }
    }
}

void AudioEngine::setAmplitude(float gain) {
    mAmplitude = gain;
    if(mDuplexStream) {
        mDuplexStream->amplitude = gain;
    }
}
