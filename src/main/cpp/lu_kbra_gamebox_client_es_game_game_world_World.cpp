#include <jni.h>
#include <cstdio>
#include "lu_kbra_gamebox_client_es_game_game_world_World.h"

/*
 * Class:     lu_kbra_gamebox_client_es_game_game_world_World
 * Method:    ntv_genCells_render
 * Signature: (Ljava/util/ArrayList;Llu/kbra/gamebox/client/es/game/game/scenes/world/entities/CellInstanceEmitter;)V
 */
JNIEXPORT void JNICALL Java_lu_kbra_gamebox_client_es_game_game_world_World_ntv_1genCells_1render(JNIEnv* env, jobject thisObj, jobject posList, jobject emitter) {

}

/*
 * Class:     lu_kbra_gamebox_client_es_game_game_world_World
 * Method:    ntv_genToxins_render
 * Signature: (Ljava/util/ArrayList;Llu/kbra/gamebox/client/es/game/game/scenes/world/entities/WorldParticleEmitter;)V
 */
JNIEXPORT void JNICALL Java_lu_kbra_gamebox_client_es_game_game_world_World_ntv_1genToxins_1render (JNIEnv* env, jobject thisObj, jobject posList, jobject emitter) {

}

/*
 * Class:     lu_kbra_gamebox_client_es_game_game_world_World
 * Method:    ntv_genPlants_render
 * Signature: (Ljava/util/ArrayList;Llu/kbra/gamebox/client/es/game/game/scenes/world/entities/WorldParticleEmitter;)V
 */
JNIEXPORT void JNICALL Java_lu_kbra_gamebox_client_es_game_game_world_World_ntv_1genPlants_1render(JNIEnv* env, jobject thisObj, jobject posList, jobject emitter) {
	jclass emitterClass = env->GetObjectClass(emitter);
	jmethodID emitterClassGetParticleMethod = env->GetMethodID(emitterClass, "getParticles", "()[Llu/kbra/gamebox/client/es/engine/geom/instance/Instance;");
	jobjectArray partArray = (jobjectArray) env->CallObjectMethod(emitter, emitterClassGetParticleMethod); // Instance[}
	jsize length = env->GetArrayLength((jarray) partArray);

	jclass listClass = env->FindClass("java/util/List");// env->GetObjectClass(posList);
	jmethodID listClassGetMethod = env->GetMethodID(listClass, "get", "(I)Ljava/lang/Object;");

	jclass instanceClass = env->FindClass("lu/kbra/gamebox/client/es/engine/geom/instance/Instance");
	jmethodID instanceClassGetTransformMethod = env->GetMethodID(instanceClass, "getTransform", "()Llu/kbra/gamebox/client/es/engine/utils/transform/Transform;");
	jmethodID instanceClassGetBuffersMethod = env->GetMethodID(instanceClass, "getBuffers", "()[Ljava/lang/Object;");

	jclass transform3DClass = env->FindClass("lu/kbra/gamebox/client/es/engine/utils/transform/Transform3D");
	jmethodID transform3DClassGetTranslationMethod = env->GetMethodID(transform3DClass, "getTranslation", "()Lorg/joml/Vector3f;");
	jmethodID transform3DClassUpdateMatrixMethod = env->GetMethodID(transform3DClass, "updateMatrix", "()Lorg/joml/Matrix4f;");

	jclass vector3fClass = env->FindClass("org/joml/Vector3f");
	jmethodID vector3fClassSetMethod = env->GetMethodID(vector3fClass, "set", "(FFF)Lorg/joml/Vector3f;");

	jclass vector2fClass = env->FindClass("org/joml/Vector2f");
	jfieldID vector2fClassXField = env->GetFieldID(vector2fClass, "x", "F");
	jfieldID vector2fClassYField = env->GetFieldID(vector2fClass, "y", "F");

	jclass noiseGeneratorClass = env->FindClass("lu/kbra/gamebox/client/es/game/game/utils/NoiseGenerator");
	jmethodID noiseGeneratorClassNoiseMethod = env->GetMethodID(noiseGeneratorClass, "noise", "(Lorg/joml/Vector2f;)F");

	jclass worldClass = env->GetObjectClass(thisObj);
	jfieldID worldClassHumidityGenField = env->GetFieldID(worldClass, "humidityGen", "Llu/kbra/gamebox/client/es/game/game/utils/NoiseGenerator;");
	jobject humidityGen = env->GetObjectField(thisObj, worldClassHumidityGenField);

	jclass jomlMathClass = env->FindClass("org/joml/Math");
	jmethodID jomlMathClassClampMethod = env->GetStaticMethodID(jomlMathClass, "clamp", "(FFF)F");

	jclass floatClass = env->FindClass("java/lang/Float");
	jmethodID floatClassConstructorMethod = env->GetMethodID(floatClass, "<init>", "(F)V");

	for (jsize i = 0; i < length; i++) {
		jobject instance = env->GetObjectArrayElement(partArray, i); // Instance
		jobject pos = env->CallObjectMethod(posList, listClassGetMethod, i);// Vector2f

		jobject transform = env->CallObjectMethod(instance, instanceClassGetTransformMethod);// Transform3D extends Transform
		jobject translation = env->CallObjectMethod(transform, transform3DClassGetTranslationMethod);// Vector3f

		jfloat x = env->GetFloatField(pos, vector2fClassXField);// float
		jfloat y = env->GetFloatField(pos, vector2fClassYField);// float

		jfloat z = lu_kbra_gamebox_client_es_game_game_world_World_Y_OFFSET * i;

		env->CallObjectMethod(translation, vector3fClassSetMethod, x, y, z);// Vector3f#set(x, y, z)

		env->CallObjectMethod(transform, transform3DClassUpdateMatrixMethod);// Transform3D extends Transform#updateMatrix()

		jobjectArray buffers = (jobjectArray) env->CallObjectMethod(instance, instanceClassGetBuffersMethod);// Object[]
		float value = env->CallFloatMethod(humidityGen, noiseGeneratorClassNoiseMethod, pos); // float
		value = env->CallStaticFloatMethod(jomlMathClass, jomlMathClassClampMethod, (float) 0.6, (float) 2, value * 5);

		env->SetObjectArrayElement(buffers, 0, env->NewObject(floatClass, floatClassConstructorMethod, value));

		env->DeleteLocalRef(buffers);
		env->DeleteLocalRef(transform);
		env->DeleteLocalRef(pos);
		env->DeleteLocalRef(instance);
	}
}
