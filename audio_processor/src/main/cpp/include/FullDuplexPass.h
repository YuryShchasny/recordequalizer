#ifndef SAMPLES_FULLDUPLEXPASS_H
#define SAMPLES_FULLDUPLEXPASS_H

#include <memory>

class FullDuplexPass : public oboe::FullDuplexStream {
public:
    bool leftChannelEnabled = true;
    bool rightChannelEnabled = true;

    virtual oboe::DataCallbackResult
    onBothStreamsReady(
            const void *inputData,
            int numInputFrames,
            void *outputData,
            int numOutputFrames) {

        const int16_t *input = static_cast<const int16_t *>(inputData);
        int16_t *output = static_cast<int16_t *>(outputData);

        int32_t samplesPerFrame = getOutputStream()->getChannelCount();
        int32_t numInputSamples = numInputFrames * samplesPerFrame;
        int32_t numOutputSamples = numOutputFrames * samplesPerFrame;

        int32_t samplesToProcess = std::min(numInputSamples, numOutputSamples);
        for (int32_t i = 0; i < samplesToProcess; i++) {
            if (i % 2 == 0) {
                if (leftChannelEnabled) {
                    *output++ = processSample(*input++);
                } else {
                    *output++ = 0;
                    *input++;
                }
            } else {
                if (rightChannelEnabled) {
                    *output++ = processSample(*input++);
                } else {
                    *output++ = 0;
                    *input++;
                }
            }
        }

        int32_t samplesLeft = numOutputSamples - numInputSamples;
        for (int32_t i = 0; i < samplesLeft; i++) {
            *output++ = 0;
        }

        return oboe::DataCallbackResult::Continue;
    }

private:
    static int16_t processSample(int16_t input) {
        auto mInput = (int32_t) (input);
        mInput = mInput * 3;
        if (mInput > SHRT_MAX) {
            mInput = SHRT_MAX;
        } else if (mInput < SHRT_MIN) {
            mInput = SHRT_MIN;
        }
        return (int16_t) mInput;
    }
};

#endif //SAMPLES_FULLDUPLEXPASS_H