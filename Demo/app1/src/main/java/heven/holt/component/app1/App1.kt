package heven.holt.component.app1

import android.app.Application
import heven.holt.component.Component
import heven.holt.component.Config

class App1 : Application() {
    override fun onCreate() {
        super.onCreate()

        Component.init(
            this,
            true,
            Config.Builder()
                .optimizeInit(true)
                .autoRegisterModule(true)
                .build()
        )
    }
}