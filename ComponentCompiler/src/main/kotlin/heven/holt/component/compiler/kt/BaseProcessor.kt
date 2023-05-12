package heven.holt.component.compiler.kt

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toTypeName

fun KSFunctionDeclaration.returnTypeToTypeName(): TypeName? {
    return returnType?.run {
        resolve().toTypeName()
    }
}

abstract class BaseProcessor : SymbolProcessor {

    private var isProcessed = false

    final override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isProcessed) {
        } else {
            isProcessed = true
            doProcess(resolver)
        }
        return emptyList()
    }

    abstract fun doProcess(resolver: Resolver): List<KSAnnotated>
}