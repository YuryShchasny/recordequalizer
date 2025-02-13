#ifndef OBOE_AUDIOENGING_H
#define OBOE_AUDIOENGING_H

#include <jni.h>
#include <oboe/Oboe.h>
#include <string>
#include <thread>
#include "FullDuplexPass.h"

class AudioEngine : public oboe::AudioStreamCallback {
public:
    AudioEngine();

    void setRecordingDeviceId(int32_t deviceId);

    void setPlaybackDeviceId(int32_t deviceId);

    void changeLeftChannel(bool enabled);

    void changeRightChannel(bool enabled);

    void play();

    void stop();

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

    std::unique_ptr<FullDuplexPass> mDuplexStream;
    std::shared_ptr<oboe::AudioStream> mRecordingStream;
    std::shared_ptr<oboe::AudioStream> mPlayStream;

    oboe::Result openStreams();

    void closeStreams();

    void closeStream(std::shared_ptr<oboe::AudioStream> &stream);

    oboe::AudioStreamBuilder *setupCommonStreamParameters(
            oboe::AudioStreamBuilder *builder);

    oboe::AudioStreamBuilder *setupRecordingStreamParameters(
            oboe::AudioStreamBuilder *builder, int32_t sampleRate);

    oboe::AudioStreamBuilder *setupPlaybackStreamParameters(
            oboe::AudioStreamBuilder *builder);

    void warnIfNotLowLatency(std::shared_ptr<oboe::AudioStream> &stream);
};

#endif  // OBOE_AUDIOENGING_H
