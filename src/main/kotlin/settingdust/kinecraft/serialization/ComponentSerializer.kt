package settingdust.kinecraft.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import net.minecraft.network.chat.Component

@OptIn(ExperimentalSerializationApi::class)
object ComponentSerializer : KSerializer<Component> {
    override val descriptor =
        SerialDescriptor("net.minecraft.network.chat.Component", JsonElement.serializer().descriptor)

    override fun deserialize(decoder: Decoder): Component {
        return Component.Serializer.fromJson(
            decoder.decodeSerializableValue(
                if (decoder is JsonDecoder) GsonElementSerializer else GsonElementAsStringSerializer(),
            ),
        )!!
    }

    override fun serialize(encoder: Encoder, value: Component) {
        encoder.encodeSerializableValue(
            if (encoder is JsonEncoder) GsonElementSerializer else GsonElementAsStringSerializer(),
            Component.Serializer.toJsonTree(value),
        )
    }
}
