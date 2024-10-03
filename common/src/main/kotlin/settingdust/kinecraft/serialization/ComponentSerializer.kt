package settingdust.kinecraft.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import net.minecraft.network.chat.Component

object ComponentSerializer : KSerializer<Component> {
    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor =
        SerialDescriptor(
            "net.minecraft.network.chat.Component",
            JsonElement.serializer().descriptor,
        )

    override fun deserialize(decoder: Decoder): Component {
        error("Mixin failed")
    }

    override fun serialize(encoder: Encoder, value: Component) {
        error("Mixin failed")
    }
}
