package heven.holt.component.support

import android.util.Log
import androidx.annotation.AnyThread
import heven.holt.component.Component.isDebug

/**
 * 用于打印日志
 * time   : 2019/01/25
 *
 * @author : xiaojinzi
 */
object LogUtil {

    private const val TAG = "-------- Component --------"

    @AnyThread
    fun loge(message: String) {
        loge(TAG, message)
    }

    @AnyThread
    fun loge(tag: String, message: String) {
        if (isDebug) {
            Log.e(tag, message)
        }
    }

    @AnyThread
    fun logw(message: String) {
        logw(TAG, message)
    }

    @AnyThread
    fun logw(tag: String, message: String) {
        if (isDebug) {
            Log.w(tag, message)
        }
    }

    @AnyThread
    fun log(message: String) {
        log(TAG, message)
    }

    @AnyThread
    fun log(tag: String, message: String) {
        if (isDebug) {
            Log.d(tag, message)
        }
    }

}