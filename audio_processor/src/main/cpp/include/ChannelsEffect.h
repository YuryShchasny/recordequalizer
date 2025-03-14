
#ifndef RECORDEQUALIZER_CHANNELSEFFECT_H
#define RECORDEQUALIZER_CHANNELSEFFECT_H

#include "Effect.h"
#include "Channel.h"

class ChannelsEffect : public Effect {
public:

    ChannelsEffect() = default;

    explicit ChannelsEffect(bool rightMute, bool leftMute)
            :mRightMute(rightMute), mLeftMute(leftMute) {}

    void process(int16_t *frame) const override {
        if (!frame[0] || !frame[1]) return;
        if(mLeftMute) {
            frame[0] = 0;
        }
        if(mRightMute) {
            frame[1] = 0;
        }
    }

    void setMute(Channel channel, bool value) {
        if (channel == Channel::LEFT) {
            mLeftMute = value;
        } else if (channel == Channel::RIGHT) {
            mRightMute = value;
        }
    }

private:
    bool mRightMute = false;
    bool mLeftMute = false;
};

#endif //RECORDEQUALIZER_CHANNELSEFFECT_H
