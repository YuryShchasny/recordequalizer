#include <jni.h>
#include "AudioEngine.h"
#include "Log.h"

static AudioEngine *engine = nullptr;

static JavaVM *jvm = nullptr;
static jclass storeListener = nullptr;

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
    if (storeListener != nullptr) {
        env->DeleteGlobalRef(storeListener);
        storeListener = nullptr;
        LOGD("Listener removed successfully");
    } else {
        LOGD("No listener to remove");
    }
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
        LOGD("Engine is null, you must call createEngine before calling setPlaybackDeviceId method");
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

JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_nativeChangeLeftChannel(JNIEnv *env, jobject thiz,
                                                                       jboolean enabled) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling nativeChangeLeftChannel method");
        return;
    }
    engine->changeLeftChannel(enabled);
}
JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_nativeChangeRightChannel(JNIEnv *env, jobject thiz,
                                                                        jboolean enabled) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling nativeChangeRightChannel method");
        return;
    }
    engine->changeRightChannel(enabled);
}

JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_nativeInitializeEqualizer(JNIEnv *env, jobject thiz,
                                                                         jfloat amplitude,
                                                                         jint frequenciesSize,
                                                                         jintArray frequencies,
                                                                         jfloatArray gains,
                                                                         jboolean leftChannel,
                                                                         jboolean rightChannel) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling nativeInitializeEqualizer method");
        return;
    }
    jint *nativeFrequencies = env->GetIntArrayElements(frequencies, NULL);
    jfloat *nativeGains = env->GetFloatArrayElements(gains, NULL);
    engine->setEqualizer(frequenciesSize, nativeFrequencies, nativeGains);
    engine->setAmplitude(amplitude);
    engine->changeLeftChannel(leftChannel);
    engine->changeRightChannel(rightChannel);
}

JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_nativeSetFrequencyGain(JNIEnv *env, jobject thiz,
                                                                      jint frequency, jfloat gain) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling nativeSetFrequencyGain method");
        return;
    }
    engine->setGain(frequency, gain);
}

JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_nativeSetAmplitudeGain(JNIEnv *env, jobject thiz,
                                                                      jfloat gain) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling nativeSetAmplitudeGain method");
        return;
    }
    engine->setAmplitude(gain);
}
JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_nativeSetProfile(JNIEnv *env, jobject thiz,
                                                                jfloat amplitude, jfloatArray gains,
                                                                jboolean leftChannel,
                                                                jboolean rightChannel) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling nativeSetProfile method");
        return;
    }
    jfloat *nativeGains = env->GetFloatArrayElements(gains, NULL);
    engine->setFrequencyGains(nativeGains);
    engine->setAmplitude(amplitude);
    engine->changeLeftChannel(leftChannel);
    engine->changeRightChannel(rightChannel);
}

JNIEXPORT void JNICALL
Java_com_sb_audio_1processor_NativeAudioEngine_nativeAddListener(JNIEnv *env, jobject thiz,
                                                                 jobject callback) {
    if (engine == nullptr) {
        LOGD("Engine is null, you must call createEngine before calling nativeAddListener method");
        return;
    }
    env->GetJavaVM(&jvm);
    if (storeListener != nullptr) {
        LOGD("Listener already exists");
        return;
    }
    storeListener = static_cast<jclass>(env->NewGlobalRef(callback));
    if (storeListener != nullptr) {
        std::function<void(std::vector<float>)> onAudioDataReady = [&](
                std::vector<float> waveform) {
            JNIEnv *env;
            bool didAttach = false;

            if (jvm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
                jvm->AttachCurrentThread(&env, nullptr);
                didAttach = true;
            }
            jclass clazz = env->GetObjectClass(storeListener);
            jmethodID storeMethod = env->GetMethodID(clazz, "onAudioDataReady", "([F)V");
            if (!storeMethod) {
                env->DeleteLocalRef(clazz);
                if (didAttach) jvm->DetachCurrentThread();
                return;
            }
            jfloatArray jData = env->NewFloatArray(waveform.size());
            env->SetFloatArrayRegion(jData, 0, waveform.size(), waveform.data());
            env->CallVoidMethod(storeListener, storeMethod, jData);
            env->DeleteLocalRef(jData);
            env->DeleteLocalRef(clazz);
            if (didAttach) jvm->DetachCurrentThread();
        };
        engine->setAudioDataCallback(onAudioDataReady);
    }
}
}