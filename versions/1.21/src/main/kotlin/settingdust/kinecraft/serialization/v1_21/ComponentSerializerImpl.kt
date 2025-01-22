package settingdust.kinecraft.serialization.v1_21

import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization

object ComponentSerializerImpl {
    init {
        requireNotNull(ComponentSerialization.CODEC)
    }

    fun serialize(encoder: Encoder, value: Component) {
        encoder.encodeSerializableValue(
            encoder.serializersModule.serializer<JsonElement>(),
            ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, value).orThrow,
        )
    }

    fun deserialize(decoder: Decoder) =
        ComponentSerialization.CODEC.parse(
            JsonOps.INSTANCE,
            decoder.decodeSerializableValue(decoder.serializersModule.serializer<JsonElement>()),
        ).orThrow
}