#ifndef RECORDEQUALIZER_EFFECTSGENERATOR_H
#define RECORDEQUALIZER_EFFECTSGENERATOR_H

#include <memory>
#include "Effect.h"
#include "Equalizer.h"
#include "AmplitudeEffect.h"
#include "CompressEffect.h"
#include "ChannelsEffect.h"
#include <thread>

class EffectsGenerator {
public:
    static std::shared_ptr<std::vector<Effect *>> getEffects();
};

#endif //RECORDEQUALIZER_EFFECTSGENERATOR_H
