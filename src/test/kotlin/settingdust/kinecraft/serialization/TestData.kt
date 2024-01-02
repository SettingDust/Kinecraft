package settingdust.kinecraft.serialization

import kotlinx.serialization.Serializable

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
    val longArray: LongArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestData

        if (string != other.string) return false
        if (int != other.int) return false
        if (boolean != other.boolean) return false
        if (double != other.double) return false
        if (float != other.float) return false
        if (long != other.long) return false
        if (short != other.short) return false
        if (byte != other.byte) return false
        if (char != other.char) return false
        if (list != other.list) return false
        if (!byteArray.contentEquals(other.byteArray)) return false
        if (!intArray.contentEquals(other.intArray)) return false
        if (!longArray.contentEquals(other.longArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = string.hashCode()
        result = 31 * result + int
        result = 31 * result + boolean.hashCode()
        result = 31 * result + double.hashCode()
        result = 31 * result + float.hashCode()
        result = 31 * result + long.hashCode()
        result = 31 * result + short
        result = 31 * result + byte
        result = 31 * result + char.hashCode()
        result = 31 * result + list.hashCode()
        result = 31 * result + byteArray.contentHashCode()
        result = 31 * result + intArray.contentHashCode()
        result = 31 * result + longArray.contentHashCode()
        return result
    }
}
