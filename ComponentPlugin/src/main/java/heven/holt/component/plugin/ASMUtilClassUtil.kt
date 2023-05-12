package heven.holt.component.plugin

import javassist.ClassPool
import javassist.CtConstructor
import javassist.CtMethod

object ASMUtilClassUtil {
    fun getClassBytes(moduleNameMap: Map<String, String>): ByteArray {
        val moduleNames = moduleNameMap.keys
        val classPool = ClassPool.getDefault()
        val listClass = classPool.get("java.util.List")
        val asmUtilClassPath = "heven.holt.component.support.ASMUtil"
        val asmUtilClass =
            classPool.getOrNull(asmUtilClassPath) ?: classPool.makeClass(asmUtilClassPath)

        if (asmUtilClass.isFrozen) {
            println("${KComponentPlugin.TAG}, asmUtilClass is frozen, will call defrost() method")
            asmUtilClass.defrost()
        }

        //生成 getModuleNames 方法
        val getModuleNamesMethod = CtMethod(listClass, "getModuleNames", emptyArray(), asmUtilClass)
        getModuleNamesMethod.modifiers = javassist.Modifier.PUBLIC or javassist.Modifier.STATIC
        getModuleNamesMethod.genericSignature = "(Ljava/util/List<Ljava/lang/String;>;)V"

        val getModuleNamesMethodBodySb = StringBuilder()
        getModuleNamesMethodBodySb.append("{")
        getModuleNamesMethodBodySb.append("java.util.List list = new java.util.ArrayList();")
        moduleNames.forEach {
            getModuleNamesMethodBodySb.append("list.add(\"$it\");")
        }
        getModuleNamesMethodBodySb.append("return list;")
        getModuleNamesMethodBodySb.append("}")
        getModuleNamesMethod.setBody(getModuleNamesMethodBodySb.toString())
        asmUtilClass.getDeclaredMethods("getModuleNames").forEach {
            asmUtilClass.removeMethod(it)
        }
        asmUtilClass.addMethod(getModuleNamesMethod)

        //生成 findModuleApplicationAsmImpl 方法
        val interfaceIModuleLifecycle =
            classPool.makeInterface("heven.holt.component.impl.IModuleLifecycle")
        val findModuleApplicationAsmImplMethod = CtMethod(
            interfaceIModuleLifecycle,
            "findModuleApplicationAsmImpl",
            arrayOf(classPool.get("java.lang.String")),
            asmUtilClass
        )
        findModuleApplicationAsmImplMethod.modifiers =
            javassist.Modifier.PUBLIC or javassist.Modifier.STATIC
        val findModuleApplicationAsmImplMethodBodySb = StringBuilder()
        findModuleApplicationAsmImplMethodBodySb.append("{")
        if (moduleNames.isEmpty()) {
            findModuleApplicationAsmImplMethodBodySb.append("return null;")
            val tempClass = classPool.makeClass("heven.holt.component.impl.AppModuleGenerated")
            tempClass.addConstructor(
                CtConstructor(
                    arrayOf(),
                    tempClass
                ).apply {
                    modifiers = javassist.Modifier.PUBLIC
                }
            )
            findModuleApplicationAsmImplMethodBodySb.append("return new com.xiaojinzi.component.impl.AppModuleGenerated();")
        } else {
            moduleNames.forEachIndexed { index, item ->
                val targetClassFullName = moduleNameMap[item]!!.removeSuffix(".class")
                classPool.makeClass(targetClassFullName).apply {
                    addConstructor(
                        CtConstructor(
                            arrayOf(),
                            this
                        ).apply {
                            modifiers = javassist.Modifier.PUBLIC
                        }
                    )
                }
                if (index != 0){
                    findModuleApplicationAsmImplMethodBodySb.append(" else ")
                }
                findModuleApplicationAsmImplMethodBodySb.append("if (\"$item\".equals($1)) {")
                findModuleApplicationAsmImplMethodBodySb.append(
                    "return new ${targetClassFullName}();"
                )
                findModuleApplicationAsmImplMethodBodySb.append("}")
            }
            findModuleApplicationAsmImplMethodBodySb.append(" else {return null;}")
        }
        findModuleApplicationAsmImplMethodBodySb.append("}")
        findModuleApplicationAsmImplMethod.setBody(
            findModuleApplicationAsmImplMethodBodySb.toString(),
        )
        asmUtilClass.getDeclaredMethods("findModuleApplicationAsmImpl").forEach {
            asmUtilClass.removeMethod(it)
        }
        asmUtilClass.addMethod(findModuleApplicationAsmImplMethod)

        return asmUtilClass.toBytecode()
    }
}