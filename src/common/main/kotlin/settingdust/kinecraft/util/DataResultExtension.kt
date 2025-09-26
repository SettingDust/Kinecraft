package settingdust.kinecraft.util

import com.mojang.serialization.DataResult

interface DataResultExtension {
    companion object : DataResultExtension by ServiceLoaderUtil.findService() {
        fun <R> DataResult<R>.orNull(): R? {
            return result().orElse(null)
        }
    }

    fun <R> DataResult<R>.unwrap(): R

    val <R> DataResult<R>.errorMessage: String?
}
