#include "EffectsGenerator.h"

std::shared_ptr <std::vector<Effect *>> EffectsGenerator::getEffects() {
    std::shared_ptr<std::vector<Effect *>> effects = std::make_shared<std::vector<Effect *>>();
    Equalizer *mEqualizer = new Equalizer();
    const int frequencies[] = { 31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000 };
    const float frequencyGains[] = { 0.0f, 0.0f, 0.0f, 10.0f, -10.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
    mEqualizer->initialize(10, frequencies, frequencyGains, 48000);
    AmplitudeEffect *mAmplitudeEffect = new AmplitudeEffect(0.0f);
    CompressEffect *mCompressEffect = new CompressEffect(-10.0f, 4.0f);
    ChannelsEffect *mChannelsEffect = new ChannelsEffect(false, false);
    effects->push_back(mCompressEffect);
    effects->push_back(mEqualizer);
    effects->push_back(mAmplitudeEffect);
    effects->push_back(mChannelsEffect);
    return effects;
}
