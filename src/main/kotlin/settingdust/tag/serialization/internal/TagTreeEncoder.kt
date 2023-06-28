package settingdust.tag.serialization.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.internal.NamedValueEncoder
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.*
import settingdust.tag.serialization.MinecraftTag
import settingdust.tag.serialization.TagEncoder
import settingdust.tag.serialization.TagSerializer

@OptIn(ExperimentalSerializationApi::class)
internal fun <T> MinecraftTag.writeTag(value: T, serializer: SerializationStrategy<T>): Tag {
    lateinit var result: Tag
    val encoder = CompoundTagEncoder(this) { result = it }
    encoder.encodeSerializableValue(serializer, value)
    return result
}

@OptIn(InternalSerializationApi::class)
@ExperimentalSerializationApi
private sealed class TagTreeEncoder(
    final override val nbt: MinecraftTag,
    protected val tagConsumer: (Tag) -> Unit
) : NamedValueEncoder(), TagEncoder {
    override val serializersModule: SerializersModule
        get() = nbt.serializersModule

    protected val configuration = nbt.configuration

    override fun encodeTag(tag: Tag) = encodeSerializableValue(TagSerializer, tag)

    override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int) = configuration.encodeDefaults

    override fun composeName(parentName: String, childName: String) = childName

    abstract fun putTag(key: String, tag: Tag)
    abstract fun getCurrent(): Tag


    // There isn't null in NBT
    override fun encodeNotNullMark() {}
    override fun encodeNull() {}
    override fun encodeTaggedNull(tag: String) {}

    override fun encodeTaggedInt(tag: String, value: Int) = putTag(tag, IntTag.valueOf(value))
    override fun encodeTaggedByte(tag: String, value: Byte) = putTag(tag, ByteTag.valueOf(value))
    override fun encodeTaggedShort(tag: String, value: Short) = putTag(tag, ShortTag.valueOf(value))
    override fun encodeTaggedLong(tag: String, value: Long) = putTag(tag, LongTag.valueOf(value))
    override fun encodeTaggedFloat(tag: String, value: Float) = putTag(tag, FloatTag.valueOf(value))
    override fun encodeTaggedDouble(tag: String, value: Double) = putTag(tag, DoubleTag.valueOf(value))
    override fun encodeTaggedBoolean(tag: String, value: Boolean) = putTag(tag, ByteTag.valueOf(if (value) 1 else 0))
    override fun encodeTaggedChar(tag: String, value: Char) = putTag(tag, IntTag.valueOf(value.code))
    override fun encodeTaggedString(tag: String, value: String) = putTag(tag, StringTag.valueOf(value))
    override fun encodeTaggedEnum(tag: String, enumDescriptor: SerialDescriptor, ordinal: Int) =
        putTag(tag, StringTag.valueOf(enumDescriptor.getElementName(ordinal)))

    override fun encodeTaggedValue(tag: String, value: Any) =
        when (value) {
            is ByteArray -> putTag(tag, ByteArrayTag(value))
            is IntArray -> putTag(tag, IntArrayTag(value))
            is LongArray -> putTag(tag, LongArrayTag(value))
            else -> putTag(tag, StringTag.valueOf(value.toString()))
        }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        val consumer =
            if (currentTagOrNull == null) tagConsumer
            else { tag -> putTag(currentTag, tag) }
        val encoder = when (descriptor.kind) {
            StructureKind.LIST -> when (descriptor) {
                ByteArraySerializer().descriptor -> CollectionTagEncoder(nbt, ByteArrayTag(emptyList()), consumer)
                IntArraySerializer().descriptor -> CollectionTagEncoder(nbt, IntArrayTag(emptyList()), consumer)
                LongArraySerializer().descriptor -> CollectionTagEncoder(nbt, LongArrayTag(emptyList()), consumer)
                else -> CollectionTagEncoder(nbt, ListTag(), consumer)
            }

            StructureKind.MAP -> CompoundTagMapEncoder(nbt, consumer)
            else -> CompoundTagEncoder(nbt, consumer)
        }
        return encoder
    }

    override fun endEncode(descriptor: SerialDescriptor) {
        tagConsumer(getCurrent())
    }
}

@ExperimentalSerializationApi
private open class CompoundTagEncoder(nbt: MinecraftTag, tagConsumer: (Tag) -> Unit) :
    TagTreeEncoder(nbt, tagConsumer) {
    protected val compound = CompoundTag()

    override fun putTag(key: String, tag: Tag) {
        compound.put(key, tag)
    }

    override fun getCurrent() = compound

    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        if (value != null) super.encodeNullableSerializableElement(descriptor, index, serializer, value)
    }
}

@ExperimentalSerializationApi
private class CompoundTagMapEncoder(nbt: MinecraftTag, tagConsumer: (Tag) -> Unit) :
    CompoundTagEncoder(nbt, tagConsumer) {
    private lateinit var key: String
    private var isKey = true
    override fun putTag(key: String, tag: Tag) {
        if (isKey) {
            this.key = when (tag) {
                is CollectionTag<*>, is CompoundTag -> throw IllegalStateException("Map key shouldn't be list or compound")
                else -> tag.asString
            }
            isKey = false
        } else {
            compound.put(this.key, tag)
            isKey = true
        }
    }
}

@ExperimentalSerializationApi
private class CollectionTagEncoder(nbt: MinecraftTag, private val tag: CollectionTag<*>, tagConsumer: (Tag) -> Unit) :
    TagTreeEncoder(nbt, tagConsumer) {

    override fun elementName(descriptor: SerialDescriptor, index: Int) = index.toString()

    override fun putTag(key: String, tag: Tag) {
        this.tag.addTag(key.toInt(), tag)
    }

    override fun getCurrent() = tag
}
