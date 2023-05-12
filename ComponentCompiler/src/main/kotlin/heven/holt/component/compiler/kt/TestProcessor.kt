package heven.holt.component.compiler.kt

import com.google.auto.service.AutoService
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

class TestProcessor(
    environment: SymbolProcessorEnvironment,
    private val logger: KSPLogger = environment.logger
) : BaseHostProcessor(environment) {

    private val TAG = "\nProcessor"

    private fun ClassName.withTypeArguments(arguments: List<TypeName>): TypeName {
        return if (arguments.isEmpty()) {
            this
        } else {
            this.parameterizedBy(arguments)
        }
    }

    override fun doProcess(resolver: Resolver): List<KSAnnotated> {
        val testClass =
            resolver.getClassDeclarationByName("heven.holt.component.app1.TestInterface")
        logger.warn("testClass = $testClass")
        testClass?.run {
            val testFunction = getAllFunctions().first()
            logger.warn("testFunction = $testFunction")
            logger.warn("testFunction.returnType1 = ${testFunction.returnType?.resolve()}")

            val testFunctionReturnType2 = testFunction.returnTypeToTypeName() as? ClassName
            logger.warn("testFunction.returnType2 = $testFunctionReturnType2")

            testFunction.returnType?.resolve()?.run {
                when (val dec = declaration) {
                    is KSClassDeclaration -> {
                        logger.warn("argument=$arguments")
                        val testTypeName =
                            dec.toClassName().withTypeArguments(arguments.map { it.toTypeName() })
                        logger.warn("testTypeNamexxxx = $testTypeName, xxx = ${dec.classKind == ClassKind.ANNOTATION_CLASS}")
                    }
                }
            }
        }
        return emptyList()
    }

    override fun finish() {
        super.finish()
        logger.warn("$TAG finish")
    }

    override fun onError() {
        super.onError()
        logger.warn("$TAG onError")
    }
}

//@AutoService(SymbolProcessorProvider::class)
class TestProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return TestProcessor(environment)
    }
}