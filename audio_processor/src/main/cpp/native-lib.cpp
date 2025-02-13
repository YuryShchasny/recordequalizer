#include <jni.h>
#include "AudioEngine.h"
#include "Log.h"

static AudioEngine *engine = nullptr;

extern "C" {
JNIEXPORT jboolean JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_create(JNIEnv *env, jobject thiz) {
    if (engine == nullptr) {
        engine = new AudioEngine();
    }
    return (engine != nullptr) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_delete(JNIEnv *env, jobject thiz) {
    if (engine) {
        engine->stop();
        delete engine;
        engine = nullptr;
    }
}

JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_play(JNIEnv *env, jobject thiz) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling play method");
        return;
    }
    engine->play();
}

JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_stop(JNIEnv *env, jobject thiz) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling stop method");
        return;
    }
    engine->stop();
}

JNIEXPORT jboolean JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_isPlaying(JNIEnv *env, jobject thiz) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling isPlaying method");
        return false;
    }
    return engine->isPlaying();
}
JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_setRecordingDeviceId(JNIEnv *env, jobject thiz,
                                                                    jint deviceId) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling setRecordingDeviceId method");
        return;
    }
    engine->setRecordingDeviceId(deviceId);
}

JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_setPlaybackDeviceId(JNIEnv *env, jobject thiz,
                                                                   jint deviceId) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling setRecordingDeviceId method");
        return;
    }
    engine->setPlaybackDeviceId(deviceId);
}

JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_setDefaultStreamValues(JNIEnv *env, jobject thiz,
                                                                      jint sampleRate,
                                                                      jint framesPerBurst) {
    oboe::DefaultStreamValues::SampleRate = (int32_t) sampleRate;
    oboe::DefaultStreamValues::FramesPerBurst = (int32_t) framesPerBurst;
}
}

extern "C"
JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_nativeChangeLeftChannel(JNIEnv *env, jobject thiz,
                                                                       jboolean enabled) {
    engine->changeLeftChannel(enabled);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_nativeChangeRightChannel(JNIEnv *env, jobject thiz,
                                                                        jboolean enabled) {
    engine->changeRightChannel(enabled);
}