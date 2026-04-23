package vegabobo.languageselector

import android.os.Handler
import android.os.Looper

interface IRootListener {
    fun onRootReceived()
}

object RootReceivedListener {
    private val mainHandler = Handler(Looper.getMainLooper())
    var callback: IRootListener? = null

    fun setListener(inCallback: IRootListener?) {
        callback = inCallback
    }

    fun onRootReceived() {
        val listener = callback ?: return
        if (Looper.myLooper() == Looper.getMainLooper()) {
            listener.onRootReceived()
        } else {
            // RootService.bind 只能在主线程调用，这里统一切回主线程再分发回调。
            mainHandler.post { listener.onRootReceived() }
        }
    }

    fun destroy() {
        callback = null
    }
}
