#include <__algorithm/clamp.h>
#include "Equalizer.h"
#include "Log.h"

void Equalizer::initialize(int frequenciesSize, const int *frequencies, const float *frequencyGains,
                           int sampleRate) {
    mSampleRate = sampleRate;
    mFrequenciesSize = frequenciesSize;
    mFrequencies = new int[frequenciesSize];
    mFrequencyGains = new float[frequenciesSize];
    mEqFilters = new BiQuadFilter[frequenciesSize];
    for (int i = 0; i < frequenciesSize; ++i) {
        mFrequencies[i] = frequencies[i];
        mFrequencyGains[i] = frequencyGains[i];
        mEqFilters[i] = *new BiQuadFilter();
        mEqFilters[i].setPeakingEQ(mFrequencies[i], 1.0f, mFrequencyGains[i], sampleRate);
    }

}

void Equalizer::process(int16_t *frame) const {
    for (int i = 0; i < 2; ++i) {
        float sample = (float) frame[i] / 32768.0f;
        for (int band = 0; band < mFrequenciesSize; ++band) {
            sample = mEqFilters[band].process(sample);
        }
        sample = std::clamp(sample, -1.0f, 1.0f);
        frame[i] = static_cast<int16_t>(std::round(sample * 32768.0f));
    }
}

void Equalizer::updateGain(int index, float gain) {
    mFrequencyGains[index] = gain;
    mEqFilters[index].setPeakingEQ(mFrequencies[index], 1.0f, mFrequencyGains[index], mSampleRate);
}
