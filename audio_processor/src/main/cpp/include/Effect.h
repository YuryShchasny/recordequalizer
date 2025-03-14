#ifndef RECORDEQUALIZER_EFFECT_H
#define RECORDEQUALIZER_EFFECT_H

#include <memory>

class Effect {
public:
    virtual ~Effect() = default;

    virtual void process(int16_t *frame) const = 0;
};

#endif //RECORDEQUALIZER_EFFECT_H
