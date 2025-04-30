#ifndef RECORDEQUALIZER_TESTENGINE_H
#define RECORDEQUALIZER_TESTENGINE_H

#include <memory>
#include "Equalizer.h"
#include "Effect.h"
#include "Log.h"
#include "Recorder.h"
#include "SinGenerator.h"
#include "EffectsGenerator.h"

class TestEngine {
public:
    TestEngine();

    std::shared_ptr<std::vector<Effect *>> effects;

    std::unique_ptr<SinGenerator> sinGenerator;

    std::unique_ptr<Recorder> recorder;
    bool isRecording = false;

    void testFrequency(int frequency);

private:

    const int SAMPLE_RATE = 48000;
    const int CHANNEL_COUNT = 2;
    const int TIME_S = 5;

    void setRecording(bool enable);

    void clearRecordings();

    int16_t *processFrame(int16_t frame[2]) const;
};

#endif //RECORDEQUALIZER_TESTENGINE_H
