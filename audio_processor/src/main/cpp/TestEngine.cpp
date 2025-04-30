#include "TestEngine.h"

TestEngine::TestEngine() {
    effects = EffectsGenerator::getEffects();
    recorder = std::make_unique<Recorder>();
}

void TestEngine::testFrequency(int frequency) {
    setRecording(true);
    sinGenerator = std::make_unique<SinGenerator>(frequency, SAMPLE_RATE);
    int16_t frame[2];
    for (int i = 0; i < SAMPLE_RATE * TIME_S; i++) {
        sinGenerator->generateFrame(frame);
        auto processOut = processFrame(frame);
        if (isRecording && recorder) {
            recorder->writeFrame(processOut);
        }
    }
    setRecording(false);
}

void TestEngine::setRecording(bool enable) {
    if (enable && !isRecording) {
        if (recorder->startRecording(SAMPLE_RATE, CHANNEL_COUNT)) {
            isRecording = true;
        }
    } else if (!enable && isRecording) {
        recorder->close();
        isRecording = false;
    }
}

void TestEngine::clearRecordings() {
    if (!isRecording) {
        recorder->clear();
    }
}

int16_t *TestEngine::processFrame(int16_t *frame) const {
    for (Effect *effect: *effects) {
        if (effect) {
            effect->process(frame);
        }
    }
    return frame;
}
