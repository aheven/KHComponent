package heven.holt.component

import android.app.Application
import androidx.annotation.UiThread
import heven.holt.component.impl.application.ModuleManager
import heven.holt.component.support.LogUtil
import heven.holt.component.support.Utils

object Component {

    const val GITHUB_URL = "https://github.com/xiaojinzi123/KComponent"
    const val DOC_URL = "https://github.com/xiaojinzi123/KComponent/wiki"

    /**
     * 是否初始化过了
     */
    private var isInit = false

    /**
     * 是否是 debug 状态
     */
    var isDebug = false
        private set

    private lateinit var application: Application

    /**
     * 配置对象
     */
    private var mConfig: Config? = null

    /**
     * 初始化
     */
    @UiThread
    fun init(
        application: Application,
        isDebug: Boolean,
        config: Config = Config()
    ) {
        // 做必要的检查
        if (isInit) {
            throw RuntimeException("you have init Component already!")
        }
        Utils.checkMainThread()
        Component.application = application
        Component.isDebug = isDebug
        mConfig = config
        if (isDebug) {
            printComponent()
        }
        // TODO注册
//        application.registerActivityLifecycleCallbacks()
        if (mConfig!!.isOptimizeInit && mConfig!!.isAutoRegisterModule) {
            ModuleManager.autoRegister()
        }
        isInit = true
    }

    fun requiredConfig(): Config {
        checkInit()
        return mConfig!!
    }

    fun getApplication(): Application {
        return application
    }

    private fun checkInit() {
        if (mConfig == null) {
            throw RuntimeException("you must init Component first!")
        }
    }

    private fun printComponent() {
        val sb = StringBuffer()
        sb.append(" \n")

        // 打印logo C
        sb.append("\n")
        sb.append("             *********\n")
        sb.append("          ****        ****\n")
        sb.append("       ****              ****\n")
        sb.append("     ****\n")
        sb.append("    ****\n")
        sb.append("    ****\n")
        sb.append("    ****\n")
        sb.append("     ****\n")
        sb.append("       ****              ****\n")
        sb.append("          ****        ****\n")
        sb.append("             *********\n")
        sb.append("感谢您选择 KComponent 组件化框架. \n有任何问题欢迎提 issue 或者扫描 github 上的二维码进入群聊@群主\n")
        sb.append("Github 地址：$GITHUB_URL \n")
        sb.append("文档地址：$DOC_URL \n")
        LogUtil.logw(sb.toString())
    }
}