package settingdust.kinecraft.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import net.minecraft.network.chat.Component

/**
 * Need to add one [com.google.gson.JsonElement] serializer to serializers module
 * @see [GsonElementSerializer]
 * @see [GsonElementAsStringSerializer]
 */
@OptIn(ExperimentalSerializationApi::class)
object ComponentSerializer : KSerializer<Component> {
    override val descriptor =
        SerialDescriptor(
            "net.minecraft.network.chat.Component",
            JsonElement.serializer().descriptor
        )

    override fun deserialize(decoder: Decoder): Component {
        return Component.Serializer.fromJson(
            decoder.decodeSerializableValue(
                decoder.serializersModule.serializer<com.google.gson.JsonElement>()
            ),
        )!!
    }

    override fun serialize(encoder: Encoder, value: Component) {
        encoder.encodeSerializableValue(
            encoder.serializersModule.serializer<com.google.gson.JsonElement>(),
            Component.Serializer.toJsonTree(value),
        )
    }
}
