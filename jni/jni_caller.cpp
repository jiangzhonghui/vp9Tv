#include <jni.h>

extern jint native_start_proxy(JNIEnv *pEnv, jobject pObj);
extern jint native_get_port(JNIEnv *pEnv, jobject pObj);
extern jint native_stop(JNIEnv *pEnv, jobject pObj);
extern jdouble native_get_speed(JNIEnv *pEnv, jobject pObj, jint index);
extern jdouble native_get_expected_speed(JNIEnv *pEnv, jobject pObj, jint index);
extern jdouble native_get_durations(JNIEnv *pEnv, jobject pObj, jint index);
extern jint native_get_last_error(JNIEnv *pEnv, jobject pObj);
extern jint native_get_number_connection(JNIEnv *pEnv, jobject pObj);
extern jint native_get_number_segment(JNIEnv *pEnv, jobject pObj);
extern jint native_get_bytes(JNIEnv *pEnv, jobject pObj);
extern jdouble native_get_download_time(JNIEnv *pEnv, jobject pObj);

#define NUM_OF_METHOD 11

jint JNI_OnLoad(JavaVM* pVm, void* reserved) {
	JNIEnv* env;
	if (pVm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
		return -1;
	}
	JNINativeMethod nm[NUM_OF_METHOD];
	nm[0].name = "native_start_proxy";
	nm[0].signature = "()I";
	nm[0].fnPtr = (void*) native_start_proxy;

	nm[1].name = "native_get_port";
	nm[1].signature = "()I";
	nm[1].fnPtr = (void*) native_get_port;

	nm[2].name = "native_stop";
	nm[2].signature = "()I";
	nm[2].fnPtr = (void*) native_stop;

	nm[3].name = "native_get_speed";
	nm[3].signature = "(I)D";
	nm[3].fnPtr = (void*) native_get_speed;

	nm[4].name = "native_get_durations";
	nm[4].signature = "(I)D";
	nm[4].fnPtr = (void*) native_get_durations;

	nm[5].name = "native_get_expected_speed";
	nm[5].signature = "(I)D";
	nm[5].fnPtr = (void*) native_get_expected_speed;

	nm[6].name = "native_get_last_error";
	nm[6].signature = "()I";
	nm[6].fnPtr = (void*) native_get_last_error;

	nm[7].name = "native_get_number_connection";
	nm[7].signature = "()I";
	nm[7].fnPtr = (void*) native_get_number_connection;

	nm[8].name = "native_get_number_segment";
	nm[8].signature = "()I";
	nm[8].fnPtr = (void*) native_get_number_segment;

	nm[9].name = "native_get_bytes";
	nm[9].signature = "()I";
	nm[9].fnPtr = (void*) native_get_bytes;

	nm[10].name = "native_get_download_time";
	nm[10].signature = "()D";
	nm[10].fnPtr = (void*) native_get_download_time;

	jclass cls = env->FindClass("tv/vp9/videoproxy/MyService");

	//Register methods with env->RegisterNatives.
	env->RegisterNatives(cls, nm, NUM_OF_METHOD);
	return JNI_VERSION_1_6;
}
