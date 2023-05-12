package heven.holt.component.impl

import android.app.Application
import androidx.annotation.CallSuper
import heven.holt.component.application.IApplicationLifecycle
import heven.holt.component.application.IModuleNotifyChanged

abstract class ModuleImpl : IModuleLifecycle {
    private var isInit = false
    private var moduleApplicationList: List<IApplicationLifecycle>? = null

    @CallSuper
    override fun onCreate(app: Application) {
        if (isInit) {
            throw RuntimeException("ModuleImpl can only be initialized once")
        }
        moduleApplicationList = initApplication()
        moduleApplicationList?.forEach {
            it.onCreate(app)
        }
        isInit = true
    }

    override fun onDestroy() {
        moduleApplicationList?.forEach {
            it.onDestroy()
        }
        isInit = false
        moduleApplicationList = null
    }

    override fun onModuleChanged(app: Application) {
        val targetApplicationList = moduleApplicationList ?: return
        targetApplicationList
            .filterIsInstance<IModuleNotifyChanged>()
            .forEach {
                it.onModuleChanged(app)
            }
    }
}