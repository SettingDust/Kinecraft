package settingdust.kinecraft.serialization

import com.mojang.serialization.JsonOps
import org.quiltmc.qkl.library.serialization.CodecFactory
import org.quiltmc.qkl.library.serialization.annotation.CodecSerializable
import kotlin.test.Test
import kotlin.test.assertEquals

class CodecTest {
    @CodecSerializable
    data class TestData(val map: Map<Type, Int>, val list: List<Int>, val nested: NestTestData)

    @CodecSerializable
    data class NestTestData(val map: Map<Type, Int>, val list: List<Int>)

    val codecFactory = CodecFactory { }

    @CodecSerializable
    enum class Type {
        A, B, C, D
    }

    @Test
    fun test() {
        val data = TestData(
            mapOf(Type.A to 0, Type.B to 1, Type.C to 2, Type.D to 3),
            listOf(0, 1, 2, 3, 4),
            NestTestData(emptyMap(), listOf(0, 1, 2, 3, 4))
        )
        val codec = codecFactory.create<TestData>()
        val json = codec.encodeStart(JsonOps.INSTANCE, data).result().orElseThrow()
        val result = codec.parse(JsonOps.INSTANCE, json)
        assertEquals(data, result.result().orElseThrow())
    }
}
