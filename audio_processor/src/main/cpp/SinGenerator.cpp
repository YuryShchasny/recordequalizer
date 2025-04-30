#include "SinGenerator.h"

SinGenerator::SinGenerator(int frequency, int sampleRate, float amplitude)
        : mFrequency(frequency), mSampleRate(sampleRate), mAmplitude(amplitude), mPhase(0.0f){
    mPhaseIncrement = 2.0f * M_PI * mFrequency / mSampleRate;
}

void SinGenerator::generateFrame(int16_t *frame) {
    float sample = mAmplitude * sin(mPhase);
    mPhase += mPhaseIncrement;
    if (mPhase >= 2.0f * M_PI) {
        mPhase -= 2.0f * M_PI;
    }

    int16_t intSample = static_cast<int16_t>(std::round(sample * SHRT_MAX));
    frame[0] = intSample;
    frame[1] = intSample;
}