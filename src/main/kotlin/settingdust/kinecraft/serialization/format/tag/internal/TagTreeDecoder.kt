package settingdust.kinecraft.serialization.format.tag.internal

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.internal.NamedValueDecoder
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.ByteArrayTag
import net.minecraft.nbt.ByteTag
import net.minecraft.nbt.CollectionTag
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.FloatTag
import net.minecraft.nbt.IntArrayTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.LongArrayTag
import net.minecraft.nbt.LongTag
import net.minecraft.nbt.ShortTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import settingdust.kinecraft.serialization.format.tag.MinecraftTag
import settingdust.kinecraft.serialization.format.tag.MinecraftTagDecoder

@OptIn(ExperimentalSerializationApi::class)
internal fun <T> MinecraftTag.readTag(tag: Tag, deserializer: DeserializationStrategy<T>): T {
    val decoder =
        when (tag) {
            is ByteArrayTag -> ByteArrayTagDecoder(this, tag)
            is IntArrayTag -> IntArrayTagDecoder(this, tag)
            is LongArrayTag -> LongArrayTagDecoder(this, tag)
            is ListTag -> ListTagDecoder(this, tag)
            is CompoundTag -> CompoundTagDecoder(this, tag)
            else -> RootTagDecoder(this, tag)
        }
    return decoder.decodeSerializableValue(deserializer)
}

@OptIn(InternalSerializationApi::class)
@ExperimentalSerializationApi
private sealed class TagTreeDecoder(
    final override val nbt: MinecraftTag,
    open val value: Tag,
) : NamedValueDecoder(), MinecraftTagDecoder {
    override val serializersModule: SerializersModule
        get() = nbt.serializersModule

    protected val configuration = nbt.configuration

    protected abstract fun currentTag(tag: String): Tag

    private fun currentObject() = currentTagOrNull?.let { currentTag(it) } ?: value

    override fun decodeTag() = currentObject()

    override fun composeName(parentName: String, childName: String): String = childName

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        val currentObject = currentObject()
        return when (descriptor.kind) {
            StructureKind.LIST ->
                when (descriptor) {
                    ByteArraySerializer().descriptor ->
                        ByteArrayTagDecoder(nbt, currentObject as ByteArrayTag)
                    IntArraySerializer().descriptor ->
                        IntArrayTagDecoder(nbt, currentObject as IntArrayTag)
                    LongArraySerializer().descriptor ->
                        LongArrayTagDecoder(nbt, currentObject as LongArrayTag)
                    else -> ListTagDecoder(nbt, currentObject as ListTag)
                }
            StructureKind.MAP -> CompoundTagMapDecoder(nbt, currentObject as CompoundTag)
            else ->
                if (currentObject is CompoundTag) {
                    CompoundTagDecoder(nbt, currentObject)
                } else {
                    RootTagDecoder(nbt, currentObject)
                }
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        // Nothing
    }

    override fun decodeTaggedEnum(tag: String, enumDescriptor: SerialDescriptor) =
        enumDescriptor.getElementIndex(decodeString())

    // There isn't null in NBT
    override fun decodeTaggedNotNullMark(tag: String) = true

    override fun decodeTaggedBoolean(tag: String) =
        when (val byte = (currentTag(tag) as ByteTag).asByte) {
            0.toByte() -> false
            1.toByte() -> true
            else -> throw IllegalArgumentException("Byte $byte isn't valid boolean value")
        }

    override fun decodeTaggedByte(tag: String) = (currentTag(tag) as ByteTag).asByte

    override fun decodeTaggedShort(tag: String) = (currentTag(tag) as ShortTag).asShort

    override fun decodeTaggedInt(tag: String) = (currentTag(tag) as IntTag).asInt

    override fun decodeTaggedLong(tag: String) = (currentTag(tag) as LongTag).asLong

    override fun decodeTaggedFloat(tag: String) = (currentTag(tag) as FloatTag).asFloat

    override fun decodeTaggedDouble(tag: String) = (currentTag(tag) as DoubleTag).asDouble

    override fun decodeTaggedChar(tag: String) = (currentTag(tag) as IntTag).asInt.toChar()

    override fun decodeTaggedString(tag: String) = (currentTag(tag) as StringTag).asString
}

@ExperimentalSerializationApi
private class RootTagDecoder(
    nbt: MinecraftTag,
    private val tag: Tag,
) : TagTreeDecoder(nbt, tag) {
    override fun currentTag(tag: String) = this.tag

    override fun decodeElementIndex(descriptor: SerialDescriptor) = 0
}

@ExperimentalSerializationApi
private class CompoundTagDecoder(
    nbt: MinecraftTag,
    private val compound: CompoundTag,
) : TagTreeDecoder(nbt, compound) {
    private var index = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        while (index < descriptor.elementsCount) {
            val name = descriptor.getTag(index++)
            if (name in compound) return index - 1
        }
        return CompositeDecoder.DECODE_DONE
    }

    override fun currentTag(tag: String) = compound[tag]!!

    override fun decodeCollectionSize(descriptor: SerialDescriptor) = compound.size()

    override fun endStructure(descriptor: SerialDescriptor) {
        if (configuration.ignoreUnknownKeys || descriptor.kind is PolymorphicKind) return

        val names =
            (0 until descriptor.elementsCount).mapTo(HashSet()) { descriptor.getElementName(it) }
        compound.allKeys
            .filter { !names.contains(it) }
            .joinToString(", ")
            .let {
                if (it.isNotBlank()) {
                    throw IllegalArgumentException("$it aren't exist in decoder but compound tag")
                }
            }
    }
}

@ExperimentalSerializationApi
private class CompoundTagMapDecoder(
    nbt: MinecraftTag,
    private val compound: CompoundTag,
) : TagTreeDecoder(nbt, compound) {
    private val keys = compound.allKeys.toList()
    private val size = keys.size * 2
    private var position = -1

    override fun elementName(descriptor: SerialDescriptor, index: Int) = keys[index / 2]!!

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (position < size - 1) {
            position++
            return position
        }
        return CompositeDecoder.DECODE_DONE
    }

    override fun currentTag(tag: String) =
        if (position % 2 == 0) StringTag.valueOf(tag) else compound[tag]!!

    override fun endStructure(descriptor: SerialDescriptor) {
        // do nothing, maps do not have strict keys, so strict mode check is omitted
    }
}

@ExperimentalSerializationApi
private open class CollectionTagDecoder<T : Tag>(
    nbt: MinecraftTag,
    protected val collection: CollectionTag<T>,
) : MinecraftTagDecoder, TagTreeDecoder(nbt, collection) {
    private val size = collection.size
    protected var index = -1

    override fun currentTag(tag: String) = collection[tag.toInt()]!!

    override fun elementName(descriptor: SerialDescriptor, index: Int): String = index.toString()

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (index < size - 1) {
            index++
            return index
        }
        return CompositeDecoder.DECODE_DONE
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor) = collection.size
}

@ExperimentalSerializationApi
private class ListTagDecoder(
    nbt: MinecraftTag,
    list: ListTag,
) : CollectionTagDecoder<Tag>(nbt, list)

@ExperimentalSerializationApi
private class ByteArrayTagDecoder(
    nbt: MinecraftTag,
    array: ByteArrayTag,
) : CollectionTagDecoder<ByteTag>(nbt, array)

@ExperimentalSerializationApi
private class IntArrayTagDecoder(
    nbt: MinecraftTag,
    array: IntArrayTag,
) : CollectionTagDecoder<IntTag>(nbt, array)

@ExperimentalSerializationApi
private class LongArrayTagDecoder(
    nbt: MinecraftTag,
    array: LongArrayTag,
) : CollectionTagDecoder<LongTag>(nbt, array)
