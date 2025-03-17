
#ifndef RECORDEQUALIZER_AMPLITUDEEFFECT_H
#define RECORDEQUALIZER_AMPLITUDEEFFECT_H


#include "Effect.h"

class AmplitudeEffect : public Effect {
public:

    AmplitudeEffect() = default;

    explicit AmplitudeEffect(float amplitude): mAmplitude(amplitude) {}

    void process(int16_t *frame) const override {
        if (!frame[0] || !frame[1]) return;
        float A = pow(10, mAmplitude / 20.0f);
        for (int i = 0; i < 2; ++i) {
            auto sample = static_cast<int32_t>(frame[i]);
            sample = sample * A;
            if (sample > SHRT_MAX) sample = SHRT_MAX;
            if (sample < SHRT_MIN) sample = SHRT_MIN;
            frame[i] = static_cast<int16_t>(sample);
        }
    }

    void setAmplitude(float value) {
        mAmplitude = value;
    }

private:
    float mAmplitude = 0.0f;
};


#endif //RECORDEQUALIZER_AMPLITUDEEFFECT_H
