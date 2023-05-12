package heven.holt.component.support

import android.app.Application
import androidx.annotation.UiThread
import heven.holt.component.anno.support.CheckClassNameAnno

/**
 * 基础的生命周期接口
 */
@UiThread
@CheckClassNameAnno
interface IBaseLifecycle {
    @UiThread
    fun onCreate(app: Application)

    @UiThread
    fun onDestroy()
}