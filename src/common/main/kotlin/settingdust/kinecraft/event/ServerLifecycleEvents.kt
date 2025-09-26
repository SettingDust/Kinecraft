package settingdust.kinecraft.event

import net.minecraft.server.MinecraftServer
import settingdust.kinecraft.event.ServerLifecycleEvents.ServerStopped

object ServerLifecycleEvents {
    val SERVER_STOPPED =
        Event<ServerStopped> { callbacks ->
            ServerStopped { server -> callbacks.forEach { it.onServerStopped(server) } }
        }

    fun interface ServerStopped {
        fun onServerStopped(server: MinecraftServer)
    }
}