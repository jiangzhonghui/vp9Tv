LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE           := ffmpeg_vn

LOCAL_SRC_FILES        := ffmpeg/lib/$(TARGET_ARCH_ABI)/libffmpeg_vn.so

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := vp9_proxy

LOCAL_CFLAGS := -DNDEBUG \
				-DHAVE_CONFIG_H \
				-DEV_STANDALONE \
				-DEV_CONFIG_H="\"ev_config_android.h\""

LOCAL_C_INCLUDES += $(LOCAL_PATH)/ffmpeg/include/

LOCAL_SRC_FILES := ev.c \
	event.c \
	cmdutils.c \
	ffmpeg_opt.c \
	ffmpeg_filter.c \
	ffmpeg.c \
	vp9_proxy.cpp \
	jni_caller.cpp \
	
LOCAL_LDLIBS := -llog -ljnigraphics -lz -lm
LOCAL_SHARED_LIBRARIES := ffmpeg_vn

include $(BUILD_SHARED_LIBRARY)
