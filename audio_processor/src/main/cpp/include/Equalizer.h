
#ifndef RECORDEQUALIZER_EQUALIZER_H
#define RECORDEQUALIZER_EQUALIZER_H

#include <memory>
#include "BiQuadFilter.h"
#include "Effect.h"

class Equalizer : public Effect {
public:
    Equalizer() = default;

    void initialize(int frequenciesSize, const int *frequencies, const float *frequencyGains, int sampleRate);

    void process(int16_t frame[2]) const override;

    void updateGain(int index, float gain);

    bool isReady() const;

private:
    bool mIsReady = false;
    int mSampleRate = 0;
    int mFrequenciesSize = 0;
    int *mFrequencies;
    float *mFrequencyGains;
    BiQuadFilter *mEqFilters;
};

#endif //RECORDEQUALIZER_EQUALIZER_H
