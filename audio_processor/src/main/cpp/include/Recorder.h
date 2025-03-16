#ifndef RECORDER_H
#define RECORDER_H

#include <oboe/Oboe.h>
#include <fstream>
#include <vector>

class Recorder {
public:
    Recorder();

    bool startRecording(int sampleRate, int numChannels);

    void close();

    void writeFrame(const int16_t *frameData);

private:
    std::ofstream outputFile;
    int sampleRate = 44100;
    int numChannels = 2;
    uint32_t dataSize = 0;
    bool isRecording = false;

    void writeWavHeader();

    void updateWavHeader();

    std::string getCurrentDateTime();
};

#endif // RECORDER_H
