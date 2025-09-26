package settingdust.kinecraft.serialization.bytebuf.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.network.FriendlyByteBuf
import settingdust.kinecraft.serialization.bytebuf.MinecraftByteBuf
import settingdust.kinecraft.serialization.bytebuf.MinecraftByteBufDecoder

@ExperimentalSerializationApi
internal class ByteBufDecoder(
    val byteBuf: FriendlyByteBuf,
    override val minecraftByteBuf: MinecraftByteBuf,
    override val serializersModule: SerializersModule = minecraftByteBuf.serializersModule,
) : AbstractDecoder(), MinecraftByteBufDecoder {
    override fun decodeSequentially() = true

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int {
        return byteBuf.readVarInt()
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        return 0
    }

    override fun decodeNotNullMark(): Boolean {
        return byteBuf.readByte() != 0.toByte()
    }

    override fun decodeBoolean(): Boolean {
        return byteBuf.readBoolean()
    }

    override fun decodeByte(): Byte {
        return byteBuf.readByte()
    }

    override fun decodeShort(): Short {
        return byteBuf.readShort()
    }

    override fun decodeInt(): Int {
        return byteBuf.readInt()
    }

    override fun decodeLong(): Long {
        return byteBuf.readLong()
    }

    override fun decodeFloat(): Float {
        return byteBuf.readFloat()
    }

    override fun decodeDouble(): Double {
        return byteBuf.readDouble()
    }

    override fun decodeChar(): Char {
        return byteBuf.readChar()
    }

    override fun decodeString(): String {
        return byteBuf.readUtf()
    }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        return byteBuf.readVarInt()
    }

    override fun decodeNull(): Nothing? {
        return null
    }
}
