package heven.holt.component.user

import android.app.Application
import android.util.Log
import heven.holt.component.anno.ModuleAppAnno
import heven.holt.component.application.IComponentApplication

@ModuleAppAnno
class UserModuleApplication : IComponentApplication {

    override fun onCreate(app: Application) {
        Log.d("UserModuleApplication", "onCreate called")
    }

    override fun onDestroy() {
        Log.d("UserModuleApplication", "onDestroy called")
    }
}