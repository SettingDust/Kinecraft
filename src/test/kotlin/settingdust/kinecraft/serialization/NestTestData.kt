package settingdust.kinecraft.serialization

import kotlinx.serialization.Serializable

@Serializable
data class NestTestData(
    val testData: TestData,
    val testDataList: List<TestData>,
)

val testData =
    NestTestData(
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
            longArrayOf(1, 2, 3),
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
                longArrayOf(4, 5, 6),
            ),
        ),
    )
