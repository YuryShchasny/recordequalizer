
#ifndef RECORDEQUALIZER_COMPRESSEFFECT_H
#define RECORDEQUALIZER_COMPRESSEFFECT_H

#include "Effect.h"

class CompressEffect : public Effect {
public:

    CompressEffect() = default;

    explicit CompressEffect(float threshold_dB, float ratio)
            : mThreshold(std::pow(10.0f, threshold_dB / 20.0f)),
              mRatio(ratio) {}

    void process(int16_t *frame) const override {
        if (!frame[0] || !frame[1]) return;

        for (int i = 0; i < 2; ++i) {
            auto sample = static_cast<float>(frame[i]);
            float absSample = std::fabs(sample);

            if (absSample > mThreshold) {
                sample = (sample > 0 ? 1 : -1) * (mThreshold + (absSample - mThreshold) / mRatio);
            }
            if (sample > SHRT_MAX) sample = SHRT_MAX;
            if (sample < SHRT_MIN) sample = SHRT_MIN;
            frame[i] = static_cast<int16_t>(sample);
        }
    }

    void setThreshold(float value) {
        mThreshold = std::pow(10.0f, value / 20.0f);
    }

    void setRatio(float value) {
        mRatio = value;
    }

private:
    float mThreshold = 0.0f;
    float mRatio = 0.0f;
};



#endif //RECORDEQUALIZER_COMPRESSEFFECT_H
