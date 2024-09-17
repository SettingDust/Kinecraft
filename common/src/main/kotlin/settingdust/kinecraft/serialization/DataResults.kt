package settingdust.kinecraft.serialization

import com.mojang.serialization.DataResult

fun <R> DataResult<R>.unwrap(): R {
    return result().orElseThrow { IllegalStateException(error().orElseThrow().message()) }
}

fun <R> DataResult<R>.orNull(): R? {
    return result().orElse(null)
}
