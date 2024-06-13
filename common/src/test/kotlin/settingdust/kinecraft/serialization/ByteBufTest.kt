package settingdust.kinecraft.serialization

import io.netty.buffer.Unpooled
import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.network.FriendlyByteBuf
import org.junit.jupiter.api.Test
import settingdust.kinecraft.serialization.format.bytebuf.MinecraftByteBuf
import settingdust.kinecraft.serialization.format.bytebuf.decodeFromByteBuf
import settingdust.kinecraft.serialization.format.bytebuf.encodeWithSerializers
import kotlin.test.assertEquals

@OptIn(ExperimentalSerializationApi::class)
class ByteBufTest {
    private val minecraftByteBuf = MinecraftByteBuf
    private val buf =
        FriendlyByteBuf(Unpooled.buffer()).also {
            it.writeByte(1)
            // testData
            it.writeUtf("string")
            it.writeInt(1)
            it.writeBoolean(true)
            it.writeDouble(1.0)
            it.writeFloat(1.0f)
            it.writeLong(1)
            it.writeShort(1)
            it.writeByte(1)
            it.writeChar('a'.code)

            it.writeVarInt(3)
            it.writeUtf("a")
            it.writeUtf("b")
            it.writeUtf("c")

            it.writeVarInt(3)
            it.writeByte(1)
            it.writeByte(2)
            it.writeByte(3)

            it.writeVarInt(3)
            it.writeInt(1)
            it.writeInt(2)
            it.writeInt(3)

            it.writeVarInt(3)
            it.writeLong(1)
            it.writeLong(2)
            it.writeLong(3)

            // testDataList
            it.writeVarInt(1)
            it.writeUtf("nested string")
            it.writeInt(2)
            it.writeBoolean(false)
            it.writeDouble(2.0)
            it.writeFloat(2.0f)
            it.writeLong(2)
            it.writeShort(2)
            it.writeByte(2)
            it.writeChar('b'.code)

            it.writeVarInt(3)
            it.writeUtf("d")
            it.writeUtf("e")
            it.writeUtf("f")

            it.writeVarInt(3)
            it.writeByte(4)
            it.writeByte(5)
            it.writeByte(6)

            it.writeVarInt(3)
            it.writeInt(4)
            it.writeInt(5)
            it.writeInt(6)

            it.writeVarInt(3)
            it.writeLong(4)
            it.writeLong(5)
            it.writeLong(6)
        }

    @Test
    fun encodeToBuf() {
        val buf =
            FriendlyByteBuf(Unpooled.buffer()).encodeWithSerializers(testData, minecraftByteBuf)
        assertEquals(1.toByte(), buf.readByte())

        // testData
        assertEquals("string", buf.readUtf())
        assertEquals(1, buf.readInt())
        assertEquals(true, buf.readBoolean())
        assertEquals(1.0, buf.readDouble())
        assertEquals(1.0f, buf.readFloat())
        assertEquals(1L, buf.readLong())
        assertEquals(1.toShort(), buf.readShort())
        assertEquals(1.toByte(), buf.readByte())
        assertEquals('a', buf.readChar())

        assertEquals(3, buf.readVarInt())
        assertEquals("a", buf.readUtf())
        assertEquals("b", buf.readUtf())
        assertEquals("c", buf.readUtf())

        assertEquals(3, buf.readVarInt())
        assertEquals(1, buf.readByte())
        assertEquals(2, buf.readByte())
        assertEquals(3, buf.readByte())

        assertEquals(3, buf.readVarInt())
        assertEquals(1, buf.readInt())
        assertEquals(2, buf.readInt())
        assertEquals(3, buf.readInt())

        assertEquals(3, buf.readVarInt())
        assertEquals(1, buf.readLong())
        assertEquals(2, buf.readLong())
        assertEquals(3, buf.readLong())

        // testDataList
        assertEquals(1, buf.readVarInt())
        assertEquals("nested string", buf.readUtf())
        assertEquals(2, buf.readInt())
        assertEquals(false, buf.readBoolean())
        assertEquals(2.0, buf.readDouble())
        assertEquals(2.0f, buf.readFloat())
        assertEquals(2L, buf.readLong())
        assertEquals(2.toShort(), buf.readShort())
        assertEquals(2.toByte(), buf.readByte())
        assertEquals('b', buf.readChar())

        assertEquals(3, buf.readVarInt())
        assertEquals("d", buf.readUtf())
        assertEquals("e", buf.readUtf())
        assertEquals("f", buf.readUtf())

        assertEquals(3, buf.readVarInt())
        assertEquals(4, buf.readByte())
        assertEquals(5, buf.readByte())
        assertEquals(6, buf.readByte())

        assertEquals(3, buf.readVarInt())
        assertEquals(4, buf.readInt())
        assertEquals(5, buf.readInt())
        assertEquals(6, buf.readInt())

        assertEquals(3, buf.readVarInt())
        assertEquals(4, buf.readLong())
        assertEquals(5, buf.readLong())
        assertEquals(6, buf.readLong())
    }

    @Test
    fun decodeFromBuf() {
        assertEquals(testData, minecraftByteBuf.decodeFromByteBuf(buf))
    }
}
