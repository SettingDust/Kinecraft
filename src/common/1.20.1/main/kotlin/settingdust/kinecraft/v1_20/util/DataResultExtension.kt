package settingdust.kinecraft.v1_20.util

import com.mojang.serialization.DataResult
import settingdust.kinecraft.util.DataResultExtension
import kotlin.jvm.optionals.getOrNull

class DataResultExtension : DataResultExtension {
    override fun <R> DataResult<R>.unwrap(): R = getOrThrow(false) {}

    override val <R> DataResult<R>.errorMessage
        get() = error().getOrNull()?.message()
}