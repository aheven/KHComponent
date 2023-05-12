package heven.holt.component.compiler.kt

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import heven.holt.component.ComponentConstants
import heven.holt.component.ComponentUtil
import heven.holt.component.anno.ModuleAppAnno
import heven.holt.component.anno.support.ComponentGeneratedAnno
import heven.holt.component.anno.support.ModuleApplicationAnno
import heven.holt.component.packageName
import heven.holt.component.simpleClassName

class ModuleProcessor(
    environment: SymbolProcessorEnvironment,
    private val logger: KSPLogger = environment.logger,
    private val componentModuleName: String = (environment.options["ModuleName"]
        ?: environment.options["HOST"]) ?: throw NullHostException,
    private val codeGenerator: CodeGenerator = environment.codeGenerator
) : BaseHostProcessor(environment) {
    private val moduleImplClassName = ClassName(
        packageName = ComponentConstants.MODULE_IMPL_CLASS_NAME.packageName(),
        ComponentConstants.MODULE_IMPL_CLASS_NAME.simpleClassName()
    )

    private val iApplicationLifecycleClassName = ClassName(
        packageName = ComponentConstants.APPLICATION_LIFECYCLE_INTERFACE_CLASS_NAME.packageName(),
        ComponentConstants.APPLICATION_LIFECYCLE_INTERFACE_CLASS_NAME.simpleClassName()
    )

    private val priority: Int = environment.options["Priority"]?.toIntOrNull() ?: 0

    override fun doProcess(resolver: Resolver): List<KSAnnotated> {
        // 模块 Application 的
        val moduleAppAnnotatedList: List<KSClassDeclaration> = resolver
            .getSymbolsWithAnnotation(
                annotationName = ModuleAppAnno::class.qualifiedName!!
            )
            .mapNotNull { it as? KSClassDeclaration }
            .filterNot { it.qualifiedName == null }
            .toList()

        val packageNameStr = "heven.holt.component.impl"
        val classNameStr =
            ComponentUtil.transformHostForClass(componentModuleName) + ComponentUtil.MODULE

        logger.warn("moduleAppAnnotatedList = $moduleAppAnnotatedList")

        val typeSpec = TypeSpec
            .classBuilder(name = classNameStr)
            .addModifiers(KModifier.FINAL)
            .superclass(moduleImplClassName)
            .addAnnotation(mClassNameAndroidKeepAnno)
            .addAnnotation(ModuleApplicationAnno::class)
            .addAnnotation(ComponentGeneratedAnno::class)
            .addProperty(
                PropertySpec.builder(
                    name = "moduleName",
                    type = String::class,
                    KModifier.PUBLIC,
                    KModifier.OVERRIDE
                )
                    .initializer(CodeBlock.of("%S", componentModuleName))
                    .build()
            )
            .addProperty(
                PropertySpec.builder(
                    name = "priority",
                    type = Int::class,
                    KModifier.PUBLIC,
                    KModifier.OVERRIDE
                )
                    .initializer(CodeBlock.of("%L", priority))
                    .build()
            ).apply {
                initApplication(
                    typeSpecBuilder = this,
                    moduleAppAnnotatedList = moduleAppAnnotatedList
                )
            }
            .build()

        val fileSpec = FileSpec
            .builder(
                packageName = packageNameStr,
                fileName = classNameStr
            )
            .addType(typeSpec = typeSpec)
            .build()

        try {
            codeGenerator.createNewFile(
                dependencies = Dependencies.ALL_FILES,
                packageName = fileSpec.packageName,
                fileName = fileSpec.name
            ).use {
                it.write(fileSpec.toString().toByteArray())
            }
        } catch (e: Exception) {
            //iGone
        }

        return moduleAppAnnotatedList
    }

    private fun initApplication(
        typeSpecBuilder: TypeSpec.Builder,
        moduleAppAnnotatedList: List<KSClassDeclaration>
    ) {
        val tempStr = moduleAppAnnotatedList
            .joinToString { item ->
                "${item.qualifiedName!!.asString()}()"
            }

        typeSpecBuilder
            .addFunction(
                FunSpec.builder("initApplication")
                    .returns(
                        mClassNameListKt.parameterizedBy(
                            iApplicationLifecycleClassName
                        )
                    )
                    .addModifiers(
                        KModifier.OVERRIDE,
                        KModifier.PUBLIC,
                    )
                    .addStatement(
                        format = "return listOf(\n$tempStr\n)"
                    )
                    .build()
            )
    }
}

@AutoService(SymbolProcessorProvider::class)
class ModuleProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ModuleProcessor(environment)
    }
}