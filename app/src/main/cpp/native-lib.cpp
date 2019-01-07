#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_cxria_gallery_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    for (int i = 0; i < 100; ++i) {
        i++;
    }
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
