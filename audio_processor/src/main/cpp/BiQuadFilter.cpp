#include "BiQuadFilter.h"

BiQuadFilter::BiQuadFilter() : x1(0), x2(0), y1(0), y2(0) {}

void BiQuadFilter::setLowPass(int frequency, float Q, float gainDB, int sampleRate) {
    auto A = (float) pow(10, gainDB / 40.0f); // Преобразование дБ в коэффициент усиления
    auto omega = (float) (2.0f * M_PI * frequency / sampleRate);
    float alpha = sin(omega) / (2.0f * Q);
    float a0 =  1 + alpha / A;

    b0 =  1 + alpha * A;
    b1 = -2 * cos(omega);
    b2 =  1 - alpha * A;
    a1 = -2 * cos(omega);
    a2 =  1 - alpha / A;

    // Нормализация коэффициентов
    b0 /= a0; b1 /= a0; b2 /= a0;
    a1 /= a0; a2 /= a0;
}

float BiQuadFilter::process(float inSample) {
    float outSample = b0 * inSample + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
    x2 = x1;
    x1 = inSample;
    y2 = y1;
    y1 = outSample;
    return outSample;
}
