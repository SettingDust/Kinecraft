package settingdust.kinecraft.serialization.v1_20

import com.google.gson.JsonElement
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import net.minecraft.network.chat.Component

object ComponentSerializerImpl {
    init {
        Component.Serializer.toJson(Component.empty())
    }

    fun serialize(encoder: Encoder, value: Component) {
        encoder.encodeSerializableValue(
            encoder.serializersModule.serializer<JsonElement>(),
            Component.Serializer.toJsonTree(value),
        )
    }

    fun deserialize(decoder: Decoder) =
        Component.Serializer.fromJson(
            decoder.decodeSerializableValue(decoder.serializersModule.serializer<JsonElement>())
        )!!
}