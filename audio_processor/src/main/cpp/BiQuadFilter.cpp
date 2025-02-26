#include "BiQuadFilter.h"

BiQuadFilter::BiQuadFilter() : x1(0), x2(0), y1(0), y2(0) {}

void BiQuadFilter::setPeakingEQ(int frequency, float Q, float gainDB, int sampleRate) {
    auto A = (float) pow(10, gainDB / 40.0f); // Преобразование дБ в коэффициент усиления
    auto omega = (float) (2.0f * M_PI * frequency / sampleRate);
    float alpha = sin(omega) / (2.0f * Q);

    float b0 =  1 + alpha * A;
    float b1 = -2 * cos(omega);
    float b2 =  1 - alpha * A;
    float a0 =  1 + alpha / A;
    float a1 = -2 * cos(omega);
    float a2 =  1 - alpha / A;

    // Нормализация коэффициентов
    b0 /= a0;
    b1 /= a0;
    b2 /= a0;
    a1 /= a0;
    a2 /= a0;

    this->b0 = b0;
    this->b1 = b1;
    this->b2 = b2;
    this->a1 = a1;
    this->a2 = a2;
}

float BiQuadFilter::process(float inSample) {
    float outSample = b0 * inSample + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
    x2 = x1;
    x1 = inSample;
    y2 = y1;
    y1 = outSample;
    return outSample;
}
