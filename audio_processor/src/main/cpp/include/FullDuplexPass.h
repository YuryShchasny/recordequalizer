#ifndef SAMPLES_FULLDUPLEXPASS_H
#define SAMPLES_FULLDUPLEXPASS_H

#include <memory>
#include "Equalizer.h"
#include "Effect.h"
#include "Log.h"

class FullDuplexPass : public oboe::FullDuplexStream {
public:

    std::shared_ptr<std::vector<Effect *>> effects;
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

            *output++ = processOut[0];
            *output++ = processOut[1];
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
        for (Effect *effect : *effects) {
            if(effect) {
                effect->process(frame);
            }
        }
        if (equalizer) {
            equalizer->process(frame);
        }
        return frame;
    }
};

#endif //SAMPLES_FULLDUPLEXPASS_H