#ifndef RECORDEQUALIZER_BIQUADFILTER_H
#define RECORDEQUALIZER_BIQUADFILTER_H

#include <cmath>

class BiQuadFilter {
private:
    float a1{}, a2{}, b0{}, b1{}, b2{};
    float x1, x2, y1, y2;

public:
    BiQuadFilter();

    void setLowPass(int frequency, float Q, float gainDB, int sampleRate);

    float process(float inSample);
};

#endif //RECORDEQUALIZER_BIQUADFILTER_H
