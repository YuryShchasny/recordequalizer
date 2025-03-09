#ifndef SAMPLES_FULLDUPLEXPASS_H
#define SAMPLES_FULLDUPLEXPASS_H

#include <memory>
#include "Equalizer.h"
#include "Log.h"

class FullDuplexPass : public oboe::FullDuplexStream {
public:

    bool leftChannelEnabled = true;
    bool rightChannelEnabled = true;
    float amplitude = 0.0f;
    std::unique_ptr<Equalizer> equalizer;

    std::function<void(std::vector<float>)> onAudioDataReady;

    void
    initEqualizer(int frequenciesSize, int *frequencies, float *frequencyGains, int sampleRate) {
        equalizer = std::make_unique<Equalizer>();
        equalizer->initialize(frequenciesSize, frequencies, frequencyGains, sampleRate);
    }

    virtual oboe::DataCallbackResult
    onBothStreamsReady(
            const void *inputData,
            int numInputFrames,
            void *outputData,
            int numOutputFrames) {

        const auto *input = static_cast<const int16_t *>(inputData);
        auto *output = static_cast<int16_t *>(outputData);

        int32_t samplesPerFrame = getOutputStream()->getChannelCount();
        int32_t numInputSamples = numInputFrames * samplesPerFrame;
        int32_t numOutputSamples = numOutputFrames * samplesPerFrame;

        int32_t samplesToProcess = std::min(numInputSamples, numOutputSamples);
        std::vector<float> waveform;
        for (int32_t i = 0; i < samplesToProcess; i += 2) {
            int16_t frame[2] = {*input, *(input + 1)};
            input += 2;
            auto processOut = processFrame(frame);

            waveform.push_back(static_cast<float>(processOut[0]) / SHRT_MAX);
            waveform.push_back(static_cast<float>(processOut[1]) / SHRT_MAX);

            if (leftChannelEnabled) {
                *output++ = processOut[0];
            } else {
                *output++ = 0;
            }
            if (rightChannelEnabled) {
                *output++ = processOut[1];
            } else {
                *output++ = 0;
            }
        }

        int32_t samplesLeft = numOutputSamples - numInputSamples;
        for (int32_t i = 0; i < samplesLeft; i++) {
            *output++ = 0;
        }
        if (onAudioDataReady) {
            onAudioDataReady(waveform);
        }
        return oboe::DataCallbackResult::Continue;
    }

private:
    int16_t *processFrame(int16_t frame[2]) const {
        if (equalizer) {
            equalizer->process(frame);
        }
        applyGain(frame);
        return frame;
    }

    void applyGain(int16_t *frame) const {
        float A = pow(10, amplitude / 20.0f);
        for (int i = 0; i < 2; ++i) {
            int32_t sample = static_cast<int32_t>(frame[i]);
            sample = sample * A;
            if (sample > SHRT_MAX) sample = SHRT_MAX;
            if (sample < SHRT_MIN) sample = SHRT_MIN;
            frame[i] = static_cast<int16_t>(sample);
        }
    }
};

#endif //SAMPLES_FULLDUPLEXPASS_H