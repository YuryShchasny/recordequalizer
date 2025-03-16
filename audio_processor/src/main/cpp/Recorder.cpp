#include "Recorder.h"
#include "Log.h"

Recorder::Recorder() {}

bool Recorder::startRecording(int sampleRate, int numChannels) {
    if (isRecording) return false;

    this->sampleRate = sampleRate;
    this->numChannels = numChannels;
    std::string filePath = "/data/data/com.sb.recordequalizer/files/" + getCurrentDateTime();
    outputFile.open(filePath, std::ios::binary);
    if (!outputFile.is_open()) {
        return false;
    }
    writeWavHeader();
    isRecording = true;
    return true;
}

void Recorder::close() {
    if (isRecording) {
        updateWavHeader();
        outputFile.close();
        isRecording = false;
    }
}

void Recorder::writeFrame(const int16_t *frameData) {
    if (isRecording && outputFile.is_open()) {
        auto size = sizeof(int16_t) * numChannels;
        outputFile.write(reinterpret_cast<const char *>(frameData), size);
        dataSize += size;
    }
}

void Recorder::writeWavHeader() {
    if (!outputFile.is_open()) return;

    char header[44] = {0};

    memcpy(header, "RIFF", 4);
    *(uint32_t *) (header + 4) = 0;  // Placeholder for file size
    memcpy(header + 8, "WAVE", 4);

    memcpy(header + 12, "fmt ", 4);
    *(uint32_t *) (header + 16) = 16;  // PCM
    *(uint16_t *) (header + 20) = 1;   // Audio format (1 = PCM)
    *(uint16_t *) (header + 22) = numChannels;
    *(uint32_t *) (header + 24) = sampleRate;
    *(uint32_t *) (header + 28) = sampleRate * numChannels * sizeof(int16_t);
    *(uint16_t *) (header + 32) = numChannels * sizeof(int16_t);
    *(uint16_t *) (header + 34) = 16;  // Bits per sample (16)

    memcpy(header + 36, "data", 4);
    *(uint32_t *) (header + 40) = 0;  // Placeholder for data size

    outputFile.write(header, 44);
}

void Recorder::updateWavHeader() {
    if (!outputFile.is_open()) return;

    uint32_t fileSize = 44 + dataSize;
    outputFile.seekp(4, std::ios::beg);
    outputFile.write(reinterpret_cast<const char *>(&fileSize), 4);

    outputFile.seekp(40, std::ios::beg);
    outputFile.write(reinterpret_cast<const char *>(&dataSize), 4);
}

std::string Recorder::getCurrentDateTime() {
    auto now = std::chrono::system_clock::now();
    std::time_t now_time_t = std::chrono::system_clock::to_time_t(now);
    std::tm now_tm = *std::localtime(&now_time_t);
    std::ostringstream oss;
    oss << std::put_time(&now_tm, "%Y-%m-%d %H:%M:%S");
    return oss.str();
}


