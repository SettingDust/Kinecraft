package settingdust.kinecraft.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import net.minecraft.server.MinecraftServer
import settingdust.kinecraft.event.ServerLifecycleEvents
import java.util.concurrent.ConcurrentHashMap

object ServerCoroutines {
    private val scopes = ConcurrentHashMap<MinecraftServer, CoroutineScope>()

    init {
        ServerLifecycleEvents.SERVER_STOPPED.register { it.closeScope() }
    }

    val MinecraftServer.scope: CoroutineScope
        get() = scopes.getOrPut(this) {
            CoroutineScope(SupervisorJob() + this.asCoroutineDispatcher())
        }

    fun MinecraftServer.closeScope() {
        scopes.remove(this)?.cancel()
    }
}