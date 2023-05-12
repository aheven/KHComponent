package heven.holt.component.compiler.kt

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.ClassName
import heven.holt.component.ComponentConstants
import heven.holt.component.packageName
import heven.holt.component.simpleClassName

abstract class BaseHostProcessor(open val environment: SymbolProcessorEnvironment) :
    BaseProcessor() {
    companion object {
        val NullHostException = RuntimeException(
            """the host must not be null,you must define host in build.gradle file,such as:
            defaultConfig {
                minSdkVersion 14
                targetSdkVersion 27
                versionCode 1
                versionName "1.0"
            
                javaCompileOptions {
                    annotationProcessorOptions {
                        arguments = [HOST: "user"]
                    }
                }
            }
            """.trimIndent()
        )
    }

    val mClassNameListKt: ClassName = ClassName(
        packageName = ComponentConstants.KOTLIN_LIST.packageName(),
        ComponentConstants.KOTLIN_LIST.simpleClassName()
    )

    val mClassNameAndroidKeepAnno: ClassName = ClassName(
        packageName = ComponentConstants.ANDROID_ANNOTATION_KEEP.packageName(),
        ComponentConstants.ANDROID_ANNOTATION_KEEP.simpleClassName()
    )
}