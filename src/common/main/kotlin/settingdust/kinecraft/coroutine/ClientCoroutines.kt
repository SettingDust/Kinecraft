package settingdust.kinecraft.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import net.minecraft.client.Minecraft
import settingdust.kinecraft.event.ClientLifecycleEvents

object ClientCoroutines {
    private var clientScope: CoroutineScope? = null

    init {
        ClientLifecycleEvents.CLIENT_STOPPING.register { it.closeScope() }
    }

    val Minecraft.scope: CoroutineScope
        get() = clientScope ?: synchronized(this) {
            CoroutineScope(SupervisorJob() + this.asCoroutineDispatcher())
                .also { clientScope = it }
        }

    fun Minecraft.closeScope() {
        clientScope?.cancel()
        clientScope = null
    }
}