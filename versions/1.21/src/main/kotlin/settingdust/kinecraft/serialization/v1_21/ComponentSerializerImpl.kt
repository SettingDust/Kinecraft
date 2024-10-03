package settingdust.kinecraft.serialization.v1_21

import com.mojang.serialization.JsonOps
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization

object ComponentSerializerImpl {
    init {
        ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, Component.empty())
    }

    fun serialize(encoder: Encoder, value: Component) {
        encoder.encodeSerializableValue(
            encoder.serializersModule.serializer<Tag>(),
            ComponentSerialization.CODEC.encodeStart(NbtOps.INSTANCE, value).orThrow,
        )
    }

    fun deserialize(decoder: Decoder) =
        ComponentSerialization.CODEC.parse(
                NbtOps.INSTANCE,
                decoder.decodeSerializableValue(decoder.serializersModule.serializer<Tag>()),
            )
            .orThrow
}