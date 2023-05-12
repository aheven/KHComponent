package heven.holt.component.application

import heven.holt.component.support.IHost

/**
 * 每一个实现类都必须返回对应的 Host
 */
interface IComponentHostApplication : IApplicationLifecycle, IHost, IModuleNotifyChanged {
    /**
     * 获取优先级
     */
    val priority: Int
}