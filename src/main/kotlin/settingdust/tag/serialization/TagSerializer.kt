package settingdust.tag.serialization

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.*

@ExperimentalSerializationApi
val TagsModule = SerializersModule {
    polymorphicDefaultDeserializer(Tag::class) { TagSerializer }
    polymorphicDefaultSerializer(Tag::class) { TagSerializer }
}

@ExperimentalSerializationApi
@OptIn(InternalSerializationApi::class)
@Serializer(forClass = Tag::class)
object TagSerializer : KSerializer<Tag> {
    // Will attach type info to result
    private val polymorphicSerializer = PolymorphicSerializer(Tag::class)
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor(Tag::class.simpleName!!, PolymorphicKind.SEALED) {
            element(CompoundTag::class.simpleName!!, defer { CompoundTagSerializer.descriptor })
            element(EndTag::class.simpleName!!, defer { EndTagSerializer.descriptor })
            element(StringTag::class.simpleName!!, defer { StringTagSerializer.descriptor })
            element(ByteTag::class.simpleName!!, defer { ByteTagSerializer.descriptor })
            element(DoubleTag::class.simpleName!!, defer { DoubleTagSerializer.descriptor })
            element(FloatTag::class.simpleName!!, defer { FloatTagSerializer.descriptor })
            element(IntTag::class.simpleName!!, defer { IntTagSerializer.descriptor })
            element(LongTag::class.simpleName!!, defer { LongTagSerializer.descriptor })
            element(ShortTag::class.simpleName!!, defer { ShortTagSerializer.descriptor })
            element(ListTag::class.simpleName!!, defer { ListTagSerializer.descriptor })
            element(ByteArrayTag::class.simpleName!!, defer { ByteArrayTagSerializer.descriptor })
            element(IntArrayTag::class.simpleName!!, defer { IntArrayTagSerializer.descriptor })
            element(LongArrayTag::class.simpleName!!, defer { LongArrayTagSerializer.descriptor })
        }

    override fun deserialize(decoder: Decoder) =
        if (decoder is TagDecoder) decoder.decodeTag()
        // Need type info if isn't TagDecoder
        else polymorphicSerializer.deserialize(decoder)


    override fun serialize(encoder: Encoder, value: Tag) {
        if (encoder is TagEncoder)
            when (value) {
                is CompoundTag -> encoder.encodeSerializableValue(CompoundTagSerializer, value)
                is EndTag -> encoder.encodeSerializableValue(EndTagSerializer, value)
                is StringTag -> encoder.encodeSerializableValue(StringTagSerializer, value)
                is ByteTag -> encoder.encodeSerializableValue(ByteTagSerializer, value)
                is DoubleTag -> encoder.encodeSerializableValue(DoubleTagSerializer, value)
                is FloatTag -> encoder.encodeSerializableValue(FloatTagSerializer, value)
                is IntTag -> encoder.encodeSerializableValue(IntTagSerializer, value)
                is LongTag -> encoder.encodeSerializableValue(LongTagSerializer, value)
                is ShortTag -> encoder.encodeSerializableValue(ShortTagSerializer, value)
                is ListTag -> encoder.encodeSerializableValue(ListTagSerializer, value)
                is ByteArrayTag -> encoder.encodeSerializableValue(ByteArrayTagSerializer, value)
                is IntArrayTag -> encoder.encodeSerializableValue(IntArrayTagSerializer, value)
                is LongArrayTag -> encoder.encodeSerializableValue(LongArrayTagSerializer, value)
            }
        // Need type info if isn't TagEncoder
        else polymorphicSerializer.serialize(encoder, value)
    }
}

@ExperimentalSerializationApi
@Serializer(forClass = CompoundTag::class)
object CompoundTagSerializer : KSerializer<CompoundTag> {
    private val serializer = MapSerializer(String.serializer(), TagSerializer)
    override val descriptor = SerialDescriptor(CompoundTag::class.simpleName!!, serializer.descriptor)
    override fun deserialize(decoder: Decoder) =
        CompoundTag().apply { serializer.deserialize(decoder).forEach { put(it.key, it.value) } }

    override fun serialize(encoder: Encoder, value: CompoundTag) =
        serializer.serialize(encoder, value.allKeys.associateWith { value[it]!! })
}

@ExperimentalSerializationApi
@Serializer(forClass = EndTag::class)
object EndTagSerializer : KSerializer<EndTag> {
    override val descriptor = PrimitiveSerialDescriptor(EndTag::class.simpleName!!, PrimitiveKind.BYTE)
    override fun deserialize(decoder: Decoder) = EndTag.INSTANCE.also { decoder.decodeByte() }!!
    override fun serialize(encoder: Encoder, value: EndTag) = encoder.encodeByte(0)
}

@ExperimentalSerializationApi
@Serializer(forClass = StringTag::class)
object StringTagSerializer : KSerializer<StringTag> {
    override val descriptor = PrimitiveSerialDescriptor(StringTag::class.simpleName!!, PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder) = StringTag.valueOf(decoder.decodeString())!!
    override fun serialize(encoder: Encoder, value: StringTag) = encoder.encodeString(value.asString)
}

/**
 * NumericTag
 */
@ExperimentalSerializationApi
@Serializer(forClass = ByteTag::class)
object ByteTagSerializer : KSerializer<ByteTag> {
    override val descriptor = PrimitiveSerialDescriptor(ByteTag::class.simpleName!!, PrimitiveKind.BYTE)
    override fun deserialize(decoder: Decoder) = ByteTag.valueOf(decoder.decodeByte())!!
    override fun serialize(encoder: Encoder, value: ByteTag) = encoder.encodeByte(value.asByte)
}

@ExperimentalSerializationApi
@Serializer(forClass = DoubleTag::class)
object DoubleTagSerializer : KSerializer<DoubleTag> {
    override val descriptor = PrimitiveSerialDescriptor(DoubleTag::class.simpleName!!, PrimitiveKind.DOUBLE)
    override fun deserialize(decoder: Decoder) = DoubleTag.valueOf(decoder.decodeDouble())!!
    override fun serialize(encoder: Encoder, value: DoubleTag) = encoder.encodeDouble(value.asDouble)
}

@ExperimentalSerializationApi
@Serializer(forClass = FloatTag::class)
object FloatTagSerializer : KSerializer<FloatTag> {
    override val descriptor = PrimitiveSerialDescriptor(FloatTag::class.simpleName!!, PrimitiveKind.FLOAT)

    override fun deserialize(decoder: Decoder) = FloatTag.valueOf(decoder.decodeFloat())!!
    override fun serialize(encoder: Encoder, value: FloatTag) = encoder.encodeFloat(value.asFloat)
}

@ExperimentalSerializationApi
@Serializer(forClass = IntTag::class)
object IntTagSerializer : KSerializer<IntTag> {
    override val descriptor = PrimitiveSerialDescriptor(IntTag::class.simpleName!!, PrimitiveKind.INT)
    override fun deserialize(decoder: Decoder) = IntTag.valueOf(decoder.decodeInt())!!
    override fun serialize(encoder: Encoder, value: IntTag) = encoder.encodeInt(value.asInt)
}

@ExperimentalSerializationApi
@Serializer(forClass = LongTag::class)
object LongTagSerializer : KSerializer<LongTag> {
    override val descriptor = PrimitiveSerialDescriptor(LongTag::class.simpleName!!, PrimitiveKind.LONG)
    override fun deserialize(decoder: Decoder) = LongTag.valueOf(decoder.decodeLong())!!
    override fun serialize(encoder: Encoder, value: LongTag) = encoder.encodeLong(value.asLong)
}

@ExperimentalSerializationApi
@Serializer(forClass = ShortTag::class)
object ShortTagSerializer : KSerializer<ShortTag> {
    override val descriptor = PrimitiveSerialDescriptor(ShortTag::class.simpleName!!, PrimitiveKind.SHORT)
    override fun deserialize(decoder: Decoder) = ShortTag.valueOf(decoder.decodeShort())!!
    override fun serialize(encoder: Encoder, value: ShortTag) = encoder.encodeShort(value.asShort)
}


@ExperimentalSerializationApi
@Serializer(forClass = ListTag::class)
object ListTagSerializer : KSerializer<ListTag> {
    private val serializer = ListSerializer(TagSerializer)
    override val descriptor = SerialDescriptor(ListTag::class.simpleName!!, serializer.descriptor)
    override fun deserialize(decoder: Decoder) = ListTag().apply { addAll(serializer.deserialize(decoder)) }
    override fun serialize(encoder: Encoder, value: ListTag) = serializer.serialize(encoder, value)
}

@ExperimentalSerializationApi
@Serializer(forClass = ByteArrayTag::class)
object ByteArrayTagSerializer : KSerializer<ByteArrayTag> {
    private val serializer = ByteArraySerializer()
    override val descriptor = SerialDescriptor(ByteArrayTag::class.simpleName!!, serializer.descriptor)
    override fun deserialize(decoder: Decoder) = ByteArrayTag(serializer.deserialize(decoder))
    override fun serialize(encoder: Encoder, value: ByteArrayTag) = serializer.serialize(encoder, value.asByteArray)
}

@ExperimentalSerializationApi
@Serializer(forClass = IntArrayTag::class)
object IntArrayTagSerializer : KSerializer<IntArrayTag> {
    private val serializer = IntArraySerializer()
    override val descriptor = SerialDescriptor(IntArrayTag::class.simpleName!!, serializer.descriptor)
    override fun deserialize(decoder: Decoder) = IntArrayTag(serializer.deserialize(decoder))
    override fun serialize(encoder: Encoder, value: IntArrayTag) = serializer.serialize(encoder, value.asIntArray)
}

@ExperimentalSerializationApi
@Serializer(forClass = LongArrayTag::class)
object LongArrayTagSerializer : KSerializer<LongArrayTag> {
    private val serializer = LongArraySerializer()
    override val descriptor = SerialDescriptor(LongArrayTag::class.simpleName!!, serializer.descriptor)
    override fun deserialize(decoder: Decoder) = LongArrayTag(serializer.deserialize(decoder))
    override fun serialize(encoder: Encoder, value: LongArrayTag) = serializer.serialize(encoder, value.asLongArray)
}

@OptIn(ExperimentalSerializationApi::class)
private fun defer(deferred: () -> SerialDescriptor): SerialDescriptor = object : SerialDescriptor {

    private val original: SerialDescriptor by lazy(deferred)

    override val serialName: String
        get() = original.serialName
    override val kind: SerialKind
        get() = original.kind
    override val elementsCount: Int
        get() = original.elementsCount

    override fun getElementName(index: Int): String = original.getElementName(index)
    override fun getElementIndex(name: String): Int = original.getElementIndex(name)
    override fun getElementAnnotations(index: Int): List<Annotation> = original.getElementAnnotations(index)
    override fun getElementDescriptor(index: Int): SerialDescriptor = original.getElementDescriptor(index)
    override fun isElementOptional(index: Int): Boolean = original.isElementOptional(index)
}
