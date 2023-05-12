package heven.holt.component.support

import android.app.Application
import android.util.Log
import heven.holt.component.anno.ModuleAppAnno
import heven.holt.component.application.IComponentApplication

@ModuleAppAnno
class SupportModuleApplication : IComponentApplication {
    override fun onCreate(app: Application) {
        Log.d("SupportModuleApplicatio", "onCreate called")
    }

    override fun onDestroy() {
        Log.d("SupportModuleApplicatio", "onDestroy called")

    }
}