package heven.holt.component

/**
 * 组件化配置类
 */
class Config constructor(builder: Builder = Builder()) {

    val isInitRouterAsync: Boolean
    val isAutoRegisterModule: Boolean
    val isOptimizeInit: Boolean
    val notifyModuleChangedDelayTime:Long

    init {
        isInitRouterAsync = builder.isInitRouterAsync
        isOptimizeInit = builder.isOptimizeInit
        isAutoRegisterModule = builder.isAutoRegisterModule
        notifyModuleChangedDelayTime = builder.notifyModuleChangedDelayTime
    }

    class Builder {
        // 默认路由部分的初始化是异步的
        var isInitRouterAsync = true
        var isOptimizeInit = false
        var isAutoRegisterModule = false
        var notifyModuleChangedDelayTime = 0L

        fun initRouterAsync(isInitRouterAsync: Boolean): Builder {
            this.isInitRouterAsync = isInitRouterAsync
            return this
        }

        fun optimizeInit(isOptimizeInit: Boolean): Builder {
            this.isOptimizeInit = isOptimizeInit
            return this
        }

        fun autoRegisterModule(isAutoRegisterModule: Boolean): Builder {
            this.isAutoRegisterModule = isAutoRegisterModule
            return this
        }

        fun notifyModuleChangedDelayTime(notifyModuleChangedDelayTime: Long): Builder {
            this.notifyModuleChangedDelayTime = notifyModuleChangedDelayTime
            return this
        }

        fun build():Config{
            val config = Config(this)
            return config
        }
    }
}