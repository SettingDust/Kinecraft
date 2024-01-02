package settingdust.kinecraft.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import net.minecraft.nbt.ByteArrayTag
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntArrayTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.LongArrayTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import org.junit.jupiter.api.Test
import settingdust.kinecraft.serialization.format.tag.MinecraftTag
import settingdust.kinecraft.serialization.format.tag.decodeFromTag
import settingdust.kinecraft.serialization.format.tag.encodeToTag
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalSerializationApi::class)
class MinecraftTagTest {
    private val minecraftTag = MinecraftTag

    private val tag =
        CompoundTag().apply {
            put(
                "testData",
                CompoundTag().apply {
                    putString("string", "string")
                    putInt("int", 1)
                    putBoolean("boolean", true)
                    putDouble("double", 1.0)
                    putFloat("float", 1.0f)
                    putLong("long", 1L)
                    putShort("short", 1.toShort())
                    putByte("byte", 1.toByte())
                    putInt("char", 'a'.code)
                    put(
                        "list",
                        ListTag().apply {
                            add(StringTag.valueOf("a"))
                            add(StringTag.valueOf("b"))
                            add(StringTag.valueOf("c"))
                        },
                    )
                    putByteArray("byteArray", byteArrayOf(1, 2, 3))
                    putIntArray("intArray", intArrayOf(1, 2, 3))
                    putLongArray("longArray", longArrayOf(1, 2, 3))
                },
            )
            put(
                "testDataList",
                ListTag().apply {
                    add(
                        CompoundTag().apply {
                            putString("string", "nested string")
                            putInt("int", 2)
                            putBoolean("boolean", false)
                            putDouble("double", 2.0)
                            putFloat("float", 2.0f)
                            putLong("long", 2L)
                            putShort("short", 2.toShort())
                            putByte("byte", 2.toByte())
                            putInt("char", 'b'.code)
                            put(
                                "list",
                                ListTag().apply {
                                    add(StringTag.valueOf("d"))
                                    add(StringTag.valueOf("e"))
                                    add(StringTag.valueOf("f"))
                                },
                            )
                            putByteArray("byteArray", byteArrayOf(4, 5, 6))
                            putIntArray("intArray", intArrayOf(4, 5, 6))
                            putLongArray("longArray", longArrayOf(4, 5, 6))
                        },
                    )
                },
            )
        }

    @Test
    fun encodeToTag() {
        val result = minecraftTag.encodeToTag(testData) as CompoundTag
        val testDataTag = result.getCompound("testData")
        assertEquals("string", testDataTag.getString("string"))
        assertEquals(1, testDataTag.getInt("int"))
        assertEquals(true, testDataTag.getBoolean("boolean"))
        assertEquals(1.0, testDataTag.getDouble("double"))
        assertEquals(1.0f, testDataTag.getFloat("float"))
        assertEquals(1L, testDataTag.getLong("long"))
        assertEquals(1.toShort(), testDataTag.getShort("short"))
        assertEquals(1.toByte(), testDataTag.getByte("byte"))
        assertEquals('a'.code, testDataTag.getInt("char"))
        assertEquals("a", testDataTag.getList("list", Tag.TAG_STRING.toInt()).getString(0))
        assertEquals("b", testDataTag.getList("list", Tag.TAG_STRING.toInt()).getString(1))
        assertEquals("c", testDataTag.getList("list", Tag.TAG_STRING.toInt()).getString(2))
        assertIs<ByteArrayTag>(testDataTag.get("byteArray"))
        assertEquals(1, testDataTag.getByteArray("byteArray")[0])
        assertEquals(2, testDataTag.getByteArray("byteArray")[1])
        assertEquals(3, testDataTag.getByteArray("byteArray")[2])
        assertIs<IntArrayTag>(testDataTag.get("intArray"))
        assertEquals(1, testDataTag.getIntArray("intArray")[0])
        assertEquals(2, testDataTag.getIntArray("intArray")[1])
        assertEquals(3, testDataTag.getIntArray("intArray")[2])
        assertIs<LongArrayTag>(testDataTag.get("longArray"))
        assertEquals(1, testDataTag.getLongArray("longArray")[0])
        assertEquals(2, testDataTag.getLongArray("longArray")[1])
        assertEquals(3, testDataTag.getLongArray("longArray")[2])

        val testDataListTag = result.getList("testDataList", Tag.TAG_COMPOUND.toInt())
        assertEquals("nested string", testDataListTag.getCompound(0).getString("string"))
        assertEquals(2, testDataListTag.getCompound(0).getInt("int"))
        assertEquals(false, testDataListTag.getCompound(0).getBoolean("boolean"))
        assertEquals(2.0, testDataListTag.getCompound(0).getDouble("double"))
        assertEquals(2.0f, testDataListTag.getCompound(0).getFloat("float"))
        assertEquals(2L, testDataListTag.getCompound(0).getLong("long"))
        assertEquals(2.toShort(), testDataListTag.getCompound(0).getShort("short"))
        assertEquals(2.toByte(), testDataListTag.getCompound(0).getByte("byte"))
        assertEquals('b'.code, testDataListTag.getCompound(0).getInt("char"))
        assertEquals(
            "d",
            testDataListTag.getCompound(0).getList("list", Tag.TAG_STRING.toInt()).getString(0)
        )
        assertEquals(
            "e",
            testDataListTag.getCompound(0).getList("list", Tag.TAG_STRING.toInt()).getString(1)
        )
        assertEquals(
            "f",
            testDataListTag.getCompound(0).getList("list", Tag.TAG_STRING.toInt()).getString(2)
        )
        assertIs<ByteArrayTag>(testDataListTag.getCompound(0).get("byteArray"))
        assertEquals(4, testDataListTag.getCompound(0).getByteArray("byteArray")[0])
        assertEquals(5, testDataListTag.getCompound(0).getByteArray("byteArray")[1])
        assertEquals(6, testDataListTag.getCompound(0).getByteArray("byteArray")[2])
        assertIs<IntArrayTag>(testDataListTag.getCompound(0).get("intArray"))
        assertEquals(4, testDataListTag.getCompound(0).getIntArray("intArray")[0])
        assertEquals(5, testDataListTag.getCompound(0).getIntArray("intArray")[1])
        assertEquals(6, testDataListTag.getCompound(0).getIntArray("intArray")[2])
        assertIs<LongArrayTag>(testDataListTag.getCompound(0).get("longArray"))
        assertEquals(4, testDataListTag.getCompound(0).getLongArray("longArray")[0])
        assertEquals(5, testDataListTag.getCompound(0).getLongArray("longArray")[1])
        assertEquals(6, testDataListTag.getCompound(0).getLongArray("longArray")[2])
    }

    @Test
    fun decodeFromTag() {
        assertEquals(testData, minecraftTag.decodeFromTag<NestTestData>(tag))
    }
}
