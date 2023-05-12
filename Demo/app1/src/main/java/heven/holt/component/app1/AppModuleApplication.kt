package heven.holt.component.app1

import android.app.Application
import android.util.Log
import heven.holt.component.anno.ModuleAppAnno
import heven.holt.component.application.IComponentApplication
import heven.holt.component.application.IModuleNotifyChanged

@ModuleAppAnno
class AppModuleApplication : IComponentApplication, IModuleNotifyChanged {
    override fun onCreate(app: Application) {
        Log.d("AppModuleApplication", "onCreate called")
    }

    override fun onDestroy() {
        Log.d("AppModuleApplication", "onDestroy called")
    }

    override fun onModuleChanged(app: Application) {
        Log.d("AppModuleApplication", "onModuleChanged called")
    }
}