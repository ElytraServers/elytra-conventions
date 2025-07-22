package cn.elytra.gradle.conventions.internal

import org.gradle.api.Project
import kotlin.reflect.typeOf

internal object Util {

	internal inline fun suppressException(block: () -> Unit) {
		try {
			block()
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	inline fun <reified T> Project.findPropertyAndCast(propertyName: String): T? {
		val propValue = findProperty(propertyName) ?: return null
		return when(typeOf<T>()) {
			typeOf<String>() -> propValue as T
			typeOf<Boolean>() -> when(propValue) {
				is Boolean -> propValue as T
				is String -> propValue.toBoolean() as T
				else -> error("Unexpected type of property $propertyName")
			}
			typeOf<Int>() -> when(propValue) {
				is Int -> propValue as T
				is String -> propValue.toInt() as T
				else -> error("Unexpected type of property $propertyName")
			}
			else -> error("Property $propertyName is not of type ${T::class}")
		}
	}

	/**
	 * Read the value or throw the customized exception given from the [throwableCtor].
	 */
	inline fun <T> Result<T>.getOrThrow(throwableCtor: (Throwable) -> Throwable) = try {
		getOrThrow()
	} catch(e: Throwable) {
		throw throwableCtor(e)
	}

}
