package heven.holt.component.impl

import androidx.annotation.UiThread
import heven.holt.component.anno.support.CheckClassNameAnno
import heven.holt.component.application.IApplicationLifecycle
import heven.holt.component.application.IModuleNotifyChanged
import heven.holt.component.support.IBaseLifecycle

@UiThread
@CheckClassNameAnno
interface IModuleFragmentLifecycle {
    /**
     *注册 Fragment
     */
//    fun initFragment()

    /**
     * 反注册 Fragment
     */
//    fun destroyFragment()
}

@UiThread
@CheckClassNameAnno
interface IModuleLifecycle : IModuleFragmentLifecycle, IBaseLifecycle, IModuleNotifyChanged {
    /**
     * 模块名字
     */
    val moduleName: String

    /**
     * 模块优先级
     */
    val priority: Int

    /**
     * 模块配置的 Application 类
     */
    fun initApplication(): List<IApplicationLifecycle>
}