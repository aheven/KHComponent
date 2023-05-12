package heven.holt.component.support

import android.os.Handler
import android.os.Looper
import androidx.annotation.AnyThread
import androidx.annotation.UiThread
import heven.holt.component.error.RouterRuntimeException
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

object Utils {
    val COUNTER = AtomicInteger(0)

    // 单线程的线程池
    private val workPool = Executors.newSingleThreadExecutor()

    /**
     * 主线程的Handler
     */
    private val h = Handler(Looper.getMainLooper())

    @AnyThread
    fun postActionToWorkThread(r: Runnable) {
        workPool.submit(r)
    }

    /**
     * 在主线程延迟执行任务
     */
    @AnyThread
    fun postDelayActionToMainThread(@UiThread r: Runnable, delayMillis: Long) {
        h.postDelayed(r, delayMillis)
    }

    /**
     * 是否是主线程
     */
    fun isMainThread(): Boolean {
        return Thread.currentThread() == Looper.getMainLooper().thread
    }

    fun checkMainThread() {
        if (!isMainThread()){
            throw RouterRuntimeException("the thread is not main thread!")
        }
    }
}