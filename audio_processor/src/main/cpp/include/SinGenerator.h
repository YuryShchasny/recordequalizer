#ifndef RECORDEQUALIZER_SINGENERATOR_H
#define RECORDEQUALIZER_SINGENERATOR_H

#include <cstdint>
#include <cmath>

class SinGenerator {
public:
    SinGenerator(int frequency, int sampleRate, float amplitude = 0.5f);
    void generateFrame(int16_t *frame);
private:
    int mFrequency;
    int mSampleRate;
    float mAmplitude;
    float mPhase;
    float mPhaseIncrement;
};

#endif //RECORDEQUALIZER_SINGENERATOR_H
