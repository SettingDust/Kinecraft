package settingdust.kinecraft.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import net.minecraft.nbt.*
import org.junit.jupiter.api.Test
import settingdust.kinecraft.serialization.tag.MinecraftTag
import settingdust.kinecraft.serialization.tag.decodeFromTag
import settingdust.kinecraft.serialization.tag.encodeToTag
import kotlin.test.assertEquals
import kotlin.test.assertIs

@Serializable
data class TestData(
    val string: String,
    val int: Int,
    val boolean: Boolean,
    val double: Double,
    val float: Float,
    val long: Long,
    val short: Short,
    val byte: Byte,
    val char: Char,
    val list: List<String>,
    val byteArray: ByteArray,
    val intArray: IntArray,
    val longArray: LongArray
)

@Serializable
data class NestTestData(
    val testData: TestData,
    val testDataList: List<TestData>
)

@OptIn(ExperimentalSerializationApi::class)
class MinecraftTagTest {
    private val minecraftTag = MinecraftTag
    private val data = NestTestData(
        TestData(
            "string",
            1,
            true,
            1.0,
            1.0f,
            1L,
            1.toShort(),
            1.toByte(),
            'a',
            listOf("a", "b", "c"),
            byteArrayOf(1, 2, 3),
            intArrayOf(1, 2, 3),
            longArrayOf(1, 2, 3)
        ),
        listOf(
            TestData(
                "nested string",
                2,
                false,
                2.0,
                2.0f,
                2L,
                2.toShort(),
                2.toByte(),
                'b',
                listOf("d", "e", "f"),
                byteArrayOf(4, 5, 6),
                intArrayOf(4, 5, 6),
                longArrayOf(4, 5, 6)
            )
        )
    )

    private val tag = CompoundTag().apply {
        put("testData", CompoundTag().apply {
            putString("string", "string")
            putInt("int", 1)
            putBoolean("boolean", true)
            putDouble("double", 1.0)
            putFloat("float", 1.0f)
            putLong("long", 1L)
            putShort("short", 1.toShort())
            putByte("byte", 1.toByte())
            putInt("char", 'a'.code)
            put("list", ListTag().apply {
                add(StringTag.valueOf("a"))
                add(StringTag.valueOf("b"))
                add(StringTag.valueOf("c"))
            })
            putByteArray("byteArray", byteArrayOf(1, 2, 3))
            putIntArray("intArray", intArrayOf(1, 2, 3))
            putLongArray("longArray", longArrayOf(1, 2, 3))
        })
        put("testDataList", ListTag().apply {
            add(CompoundTag().apply {
                putString("string", "nested string")
                putInt("int", 2)
                putBoolean("boolean", false)
                putDouble("double", 2.0)
                putFloat("float", 2.0f)
                putLong("long", 2L)
                putShort("short", 2.toShort())
                putByte("byte", 2.toByte())
                putInt("char", 'b'.code)
                put("list", ListTag().apply {
                    add(StringTag.valueOf("d"))
                    add(StringTag.valueOf("e"))
                    add(StringTag.valueOf("f"))
                })
                putByteArray("byteArray", byteArrayOf(4, 5, 6))
                putIntArray("intArray", intArrayOf(4, 5, 6))
                putLongArray("longArray", longArrayOf(4, 5, 6))
            })
        })
    }

    @Test
    fun encodeToTag() {
        val result = minecraftTag.encodeToTag(data) as CompoundTag
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
        assertEquals("d", testDataListTag.getCompound(0).getList("list", Tag.TAG_STRING.toInt()).getString(0))
        assertEquals("e", testDataListTag.getCompound(0).getList("list", Tag.TAG_STRING.toInt()).getString(1))
        assertEquals("f", testDataListTag.getCompound(0).getList("list", Tag.TAG_STRING.toInt()).getString(2))
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
        val result = minecraftTag.decodeFromTag<NestTestData>(tag)
        val testData = result.testData
        assertEquals("string", testData.string)
        assertEquals(1, testData.int)
        assertEquals(true, testData.boolean)
        assertEquals(1.0, testData.double)
        assertEquals(1.0f, testData.float)
        assertEquals(1L, testData.long)
        assertEquals(1.toShort(), testData.short)
        assertEquals(1.toByte(), testData.byte)
        assertEquals('a', testData.char)
        assertEquals("a", testData.list[0])
        assertEquals("b", testData.list[1])
        assertEquals("c", testData.list[2])
        assertEquals(1, testData.byteArray[0])
        assertEquals(2, testData.byteArray[1])
        assertEquals(3, testData.byteArray[2])
        assertEquals(1, testData.intArray[0])
        assertEquals(2, testData.intArray[1])
        assertEquals(3, testData.intArray[2])
        assertEquals(1, testData.longArray[0])
        assertEquals(2, testData.longArray[1])
        assertEquals(3, testData.longArray[2])

        val testDataList = result.testDataList[0]
        assertEquals("nested string", testDataList.string)
        assertEquals(2, testDataList.int)
        assertEquals(false, testDataList.boolean)
        assertEquals(2.0, testDataList.double)
        assertEquals(2.0f, testDataList.float)
        assertEquals(2L, testDataList.long)
        assertEquals(2.toShort(), testDataList.short)
        assertEquals(2.toByte(), testDataList.byte)
        assertEquals('b', testDataList.char)
        assertEquals("d", testDataList.list[0])
        assertEquals("e", testDataList.list[1])
        assertEquals("f", testDataList.list[2])
        assertEquals(4, testDataList.byteArray[0])
        assertEquals(5, testDataList.byteArray[1])
        assertEquals(6, testDataList.byteArray[2])
        assertEquals(4, testDataList.intArray[0])
        assertEquals(5, testDataList.intArray[1])
        assertEquals(6, testDataList.intArray[2])
        assertEquals(4, testDataList.longArray[0])
        assertEquals(5, testDataList.longArray[1])
        assertEquals(6, testDataList.longArray[2])
    }
}
