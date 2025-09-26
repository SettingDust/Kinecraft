package settingdust.kinecraft.util

import kotlin.time.Duration.Companion.milliseconds

val Int.ticks get() = 50.milliseconds * this