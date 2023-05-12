package heven.holt.component.impl.application

import androidx.annotation.AnyThread
import androidx.annotation.UiThread
import heven.holt.component.Component.getApplication
import heven.holt.component.Component.requiredConfig
import heven.holt.component.ComponentUtil
import heven.holt.component.impl.IModuleLifecycle
import heven.holt.component.support.ASMUtil
import heven.holt.component.support.LogUtil
import heven.holt.component.support.Utils

object ModuleManager {

    private val moduleApplicationMap: MutableMap<String, IModuleLifecycle> = HashMap()

    fun findModuleApplication(moduleName: String): IModuleLifecycle? {
        var result: IModuleLifecycle? = null
        if (requiredConfig().isOptimizeInit) {
            LogUtil.log("\"$moduleName\" will try to load by bytecode")
            result = ASMUtil.findModuleApplicationAsmImpl(
                ComponentUtil.transformHostForClass(moduleName)
            )
        } else {
            LogUtil.log("\"$moduleName\" will try to load by reflection")
            try {
                //先找到正常的
                val clazz =
                    Class.forName(ComponentUtil.genHostModuleApplicationClassName(moduleName))
                result = clazz.newInstance() as IModuleLifecycle
            } catch (ignore: Exception) {
                //Ignore
            }
            if (result == null) {
                // 找默认的
                val clazz = Class.forName(
                    ComponentUtil.genDefaultHostModuleApplicationClassName(moduleName)
                )
                result = clazz.newInstance() as IModuleLifecycle
            }
        }
        return result
    }

    @UiThread
    private fun register(module: IModuleLifecycle) {
        if (moduleApplicationMap.containsKey(module.moduleName)) {
            LogUtil.loge("The module \"" + module.moduleName + "\" is already registered")
        } else {
            LogUtil.logw("The module \"" + module.moduleName + "\" is ready to register")
            // 标记已经注册
            moduleApplicationMap[module.moduleName] = module
            // 模块的 Application 的 onCreate 执行
            module.onCreate(getApplication())
            // 路由的部分的注册, 可选的异步还是同步
            val r = Runnable {
                notifyModuleChanged()
            }
            // 路由是否异步初始化
            if (requiredConfig().isInitRouterAsync) {
                Utils.postActionToWorkThread(r)
            } else {
                r.run()
            }
        }
    }

    /**
     * 自动注册, 需要开启 [heven.holt.component.Config.Builder.optimizeInit]
     * 表示使用 Gradle 插件优化初始化
     */
    fun autoRegister() {
        if (!requiredConfig().isOptimizeInit) {
            LogUtil.logw("you can't use this method to register module. Because you not turn on 'optimizeInit' by calling method 'Config.Builder.optimizeInit(true)' when you init")
        }
        val moduleNames = ASMUtil.getModuleNames()
        if (moduleNames.isNotEmpty()) {
            registerArr(*moduleNames.toTypedArray())
        }
    }

    /**
     * 注册业务模块, 可以传多个名称
     */
    fun registerArr(vararg hosts: String) {
        val appList: MutableList<IModuleLifecycle> = ArrayList(hosts.size)
        for (host in hosts) {
            val moduleApplication = findModuleApplication(host)
            if (moduleApplication == null) {
                LogUtil.log("模块 '$host' 加载失败")
            } else {
                appList.add(moduleApplication)
            }
        }
        // 处理优先级, 数值大的先加载
        appList.sortWith { o1, o2 -> o2.priority - o1.priority }
        for (moduleApplication in appList) {
            register(moduleApplication)
        }
    }

    @UiThread
    private fun unregister(module: IModuleLifecycle) {
        moduleApplicationMap.remove(module.moduleName)
        module.onDestroy()
        Utils.postActionToWorkThread {
            notifyModuleChanged()
        }
    }

    fun unregister(moduleName: String) {
        val module = moduleApplicationMap[moduleName]
        if (module == null) {
            LogUtil.log("模块 '$moduleName' 卸载失败")
        } else {
            unregister(module)
        }
    }

    @AnyThread
    private fun notifyModuleChanged() {
        // 当前的值
        val compareValue = Utils.COUNTER.incrementAndGet()
        Utils.postDelayActionToMainThread(
            {
                // 说明没有改变过
                if (compareValue == Utils.COUNTER.get()) {
                    doNotifyModuleChanged()
                }
            }, requiredConfig().notifyModuleChangedDelayTime
        )
    }

    private fun doNotifyModuleChanged() {
        LogUtil.logw("doNotifyModuleChanged will be called")
        val application = getApplication()
        moduleApplicationMap.values.forEach {
            it.onModuleChanged(application)
        }
    }
}