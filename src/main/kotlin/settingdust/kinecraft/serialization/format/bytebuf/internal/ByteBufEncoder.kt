package settingdust.kinecraft.serialization.format.bytebuf.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.network.FriendlyByteBuf
import settingdust.kinecraft.serialization.format.bytebuf.MinecraftByteBuf
import settingdust.kinecraft.serialization.format.bytebuf.MinecraftByteBufEncoder

@OptIn(ExperimentalSerializationApi::class)
internal class ByteBufEncoder(
    private val byteBuf: FriendlyByteBuf,
    override val minecraftByteBuf: MinecraftByteBuf,
    override val serializersModule: SerializersModule = minecraftByteBuf.serializersModule,
) : AbstractEncoder(), MinecraftByteBufEncoder {
    override fun beginCollection(
        descriptor: SerialDescriptor,
        collectionSize: Int
    ): CompositeEncoder {
        val encoder = super<AbstractEncoder>.beginCollection(descriptor, collectionSize)
        byteBuf.writeVarInt(collectionSize)
        return encoder
    }

    override fun encodeNull() {
        byteBuf.writeByte(0)
    }

    override fun encodeNotNullMark() {
        byteBuf.writeByte(1)
    }

    override fun encodeByte(value: Byte) {
        byteBuf.writeByte(value.toInt())
    }

    override fun encodeBoolean(value: Boolean) {
        byteBuf.writeBoolean(value)
    }

    override fun encodeChar(value: Char) {
        byteBuf.writeChar(value.code)
    }

    override fun encodeDouble(value: Double) {
        byteBuf.writeDouble(value)
    }

    override fun encodeFloat(value: Float) {
        byteBuf.writeFloat(value)
    }

    override fun encodeInt(value: Int) {
        byteBuf.writeInt(value)
    }

    override fun encodeLong(value: Long) {
        byteBuf.writeLong(value)
    }

    override fun encodeShort(value: Short) {
        byteBuf.writeShort(value.toInt())
    }

    override fun encodeString(value: String) {
        byteBuf.writeUtf(value)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        // No-op
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        byteBuf.writeVarInt(index)
    }
}
