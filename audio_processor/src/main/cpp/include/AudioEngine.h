#ifndef OBOE_AUDIOENGING_H
#define OBOE_AUDIOENGING_H

#include <jni.h>
#include <oboe/Oboe.h>
#include <string>
#include <thread>
#include "FullDuplexPass.h"
#include "Equalizer.h"
#include "Effect.h"

#include "AmplitudeEffect.h"
#include "ChannelsEffect.h"
#include "CompressEffect.h"

class AudioEngine : public oboe::AudioStreamCallback {
public:
    AudioEngine();

    void setEqualizer(int frequenciesSize, int *frequencies, float *frequencyGains);

    void setGain(int frequency, float gain);

    void setFrequencyGains(float *frequencyGains);

    void setAmplitude(float gain);

    void setRecordingDeviceId(int32_t deviceId);

    void setPlaybackDeviceId(int32_t deviceId);

    void changeLeftChannel(bool enabled);

    void changeRightChannel(bool enabled);

    void setAudioDataCallback(std::function<void(std::vector<float>)> callback);

    void play();

    void stop();

    void destroy();

    bool isPlaying() const;

    oboe::DataCallbackResult onAudioReady(oboe::AudioStream *oboeStream,
                                          void *audioData, int32_t numFrames) override;

    void onErrorBeforeClose(oboe::AudioStream *oboeStream, oboe::Result error) override;

    void onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error) override;

private:
    bool mIsPlaying = false;
    int32_t mRecordingDeviceId = oboe::kUnspecified;
    int32_t mPlaybackDeviceId = oboe::kUnspecified;
    const oboe::AudioFormat mFormat = oboe::AudioFormat::I16;
    int32_t mSampleRate = oboe::kUnspecified;
    const int32_t mChannelCount = oboe::ChannelCount::Stereo;

    int mFrequenciesSize = 0;
    int *mFrequencies = {};
    float *mFrequencyGains = {};
    std::shared_ptr<std::vector<Effect *>> mEffects = std::make_shared<std::vector<Effect *>>();
    AmplitudeEffect *mAmplitudeEffect;
    ChannelsEffect *mChannelsEffect;
    CompressEffect *mCompressEffect;

    std::function<void(std::vector<float>)> mOnAudioReadyCallback;

    std::unique_ptr<FullDuplexPass> mDuplexStream;
    std::shared_ptr<oboe::AudioStream> mRecordingStream;
    std::shared_ptr<oboe::AudioStream> mPlayStream;

    oboe::Result openStreams();

    oboe::Result closeStreams();

    static oboe::Result closeStream(std::shared_ptr<oboe::AudioStream> &stream);

    oboe::AudioStreamBuilder *setupCommonStreamParameters(
            oboe::AudioStreamBuilder *builder);

    oboe::AudioStreamBuilder *setupRecordingStreamParameters(
            oboe::AudioStreamBuilder *builder, int32_t sampleRate);

    oboe::AudioStreamBuilder *setupPlaybackStreamParameters(
            oboe::AudioStreamBuilder *builder);

    static void warnIfNotLowLatency(std::shared_ptr<oboe::AudioStream> &stream);
};

#endif  // OBOE_AUDIOENGING_H
