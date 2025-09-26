package settingdust.kinecraft.event

import net.minecraft.client.Minecraft
import settingdust.kinecraft.event.ClientLifecycleEvents.ClientStopping

object ClientLifecycleEvents {
    val CLIENT_STOPPING = Event<ClientStopping> { callbacks ->
        ClientStopping { client -> callbacks.forEach { it.onClientStopping(client) } }
    }

    fun interface ClientStopping {
        fun onClientStopping(client: Minecraft)
    }
}