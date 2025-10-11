package settingdust.kinecraft.serialization.nbt

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import net.minecraft.nbt.*
import settingdust.kinecraft.serialization.PolymorphicSurrogateSerializer
import settingdust.kinecraft.serialization.SealedClassSerializer
import settingdust.kinecraft.serialization.defer

@ExperimentalSerializationApi
fun NbtsModule(needClassDiscriminator: Boolean = true) = SerializersModule {
    polymorphic(Tag::class) {
        fun <T : Tag> factory(serializer: KSerializer<T>) =
            if (needClassDiscriminator) PolymorphicSurrogateSerializer(serializer) else serializer

        subclass(CompoundTag::class, factory(CompoundTagSerializer))
        subclass(EndTag::class, factory(EndTagSerializer))
        subclass(StringTag::class, factory(StringTagSerializer))
        subclass(ByteTag::class, factory(ByteTagSerializer))
        subclass(DoubleTag::class, factory(DoubleTagSerializer))
        subclass(FloatTag::class, factory(FloatTagSerializer))
        subclass(IntTag::class, factory(IntTagSerializer))
        subclass(LongTag::class, factory(LongTagSerializer))
        subclass(ShortTag::class, factory(ShortTagSerializer))
        subclass(ListTag::class, factory(ListTagSerializer))
        subclass(ByteArrayTag::class, factory(ByteArrayTagSerializer))
        subclass(IntArrayTag::class, factory(IntArrayTagSerializer))
        subclass(LongArrayTag::class, factory(LongArrayTagSerializer))
    }
    val nbtSerializer = NbtSerializer(needClassDiscriminator)
    polymorphicDefaultDeserializer(Tag::class) { nbtSerializer }
    polymorphicDefaultSerializer(Tag::class) { nbtSerializer }
    contextual(nbtSerializer)
}

@ExperimentalSerializationApi
@OptIn(InternalSerializationApi::class)
class NbtSerializer internal constructor(private val needClassDiscriminator: Boolean = true) : KSerializer<Tag> {
    companion object {
        val Default = NbtSerializer()
        val NoClassDiscriminator = NbtSerializer(false)

        fun create(needClassDiscriminator: Boolean = true) =
            if (needClassDiscriminator) Default else NoClassDiscriminator
    }

    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("nbt", PolymorphicKind.SEALED) {
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

    private val polymorphicNbtSerializer by lazy { PolymorphicNbtSerializer.create(needClassDiscriminator) }

    override fun deserialize(decoder: Decoder): Tag =
        if (decoder is MinecraftNBTDecoder) {
            decoder.decodeNBT()
        } // Need type info if isn't TagDecoder
        else {
            require(needClassDiscriminator) { "Deserialize need class discriminator" }
            decoder.decodeSerializableValue(polymorphicNbtSerializer)
        }

    override fun serialize(encoder: Encoder, value: Tag) =
        if (encoder is MinecraftNBTEncoder) {
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
                else -> throw IllegalArgumentException("Unknown tag type: $value")
            }
        } else {
            encoder.encodeSerializableValue(polymorphicNbtSerializer, value)
        }
}

@ExperimentalSerializationApi
@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
class PolymorphicNbtSerializer(private val needClassDiscriminator: Boolean = true) :
    SealedClassSerializer<Tag>(
        "nbt",
        Tag::class,
        arrayOf(
            CompoundTag::class,
            EndTag::class,
            StringTag::class,
            ByteTag::class,
            DoubleTag::class,
            FloatTag::class,
            IntTag::class,
            LongTag::class,
            ShortTag::class,
            ListTag::class,
            ByteArrayTag::class,
            IntArrayTag::class,
            LongArrayTag::class,
        ),
        listOf(
            CompoundTagSerializer,
            EndTagSerializer,
            StringTagSerializer,
            ByteTagSerializer,
            DoubleTagSerializer,
            FloatTagSerializer,
            IntTagSerializer,
            LongTagSerializer,
            ShortTagSerializer,
            ListTagSerializer,
            ByteArrayTagSerializer,
            IntArrayTagSerializer,
            LongArrayTagSerializer
        )
            .map { if (needClassDiscriminator) PolymorphicSurrogateSerializer(it) else it }
            .toList().toTypedArray()
    ),
    KSerializer<Tag> {
    companion object {
        val Default = PolymorphicNbtSerializer()
        val NoClassDiscriminator = PolymorphicNbtSerializer(false)

        fun create(needClassDiscriminator: Boolean = true) =
            if (needClassDiscriminator) Default else NoClassDiscriminator
    }
}


@ExperimentalSerializationApi
object CompoundTagSerializer : KSerializer<CompoundTag> {
    override val descriptor =
        SerialDescriptor(
            "nbt_compound",
            mapSerialDescriptor(String.serializer().descriptor, NbtSerializer.Default.descriptor)
        )

    override fun deserialize(decoder: Decoder) =
        CompoundTag().apply {
            MapSerializer(String.serializer(), decoder.serializersModule.serializer<Tag>())
                .deserialize(decoder)
                .forEach { put(it.key, it.value) }
        }

    override fun serialize(encoder: Encoder, value: CompoundTag) =
        MapSerializer(String.serializer(), encoder.serializersModule.serializer<Tag>())
            .serialize(encoder, value.allKeys.associateWith { value.get(it)!! })
}

@ExperimentalSerializationApi
object ListTagSerializer : KSerializer<ListTag> {
    override val descriptor =
        SerialDescriptor("nbt_list", listSerialDescriptor(NbtSerializer.Default.descriptor))

    override fun deserialize(decoder: Decoder) =
        ListTag().apply {
            addAll(ListSerializer(decoder.serializersModule.serializer<Tag>()).deserialize(decoder))
        }

    override fun serialize(encoder: Encoder, value: ListTag) =
        ListSerializer(encoder.serializersModule.serializer<Tag>()).serialize(encoder, value)
}

@ExperimentalSerializationApi
object ByteArrayTagSerializer : KSerializer<ByteArrayTag> {
    private val serializer = ByteArraySerializer()
    override val descriptor = SerialDescriptor("nbt_byte_array", serializer.descriptor)

    override fun deserialize(decoder: Decoder) = ByteArrayTag(serializer.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: ByteArrayTag) =
        serializer.serialize(encoder, value.asByteArray)
}

@ExperimentalSerializationApi
object IntArrayTagSerializer : KSerializer<IntArrayTag> {
    private val serializer = IntArraySerializer()
    override val descriptor = SerialDescriptor("nbt_int_array", serializer.descriptor)

    override fun deserialize(decoder: Decoder) = IntArrayTag(serializer.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: IntArrayTag) =
        serializer.serialize(encoder, value.asIntArray)
}

@ExperimentalSerializationApi
object LongArrayTagSerializer : KSerializer<LongArrayTag> {
    private val serializer = LongArraySerializer()
    override val descriptor = SerialDescriptor("nbt_long_array", serializer.descriptor)

    override fun deserialize(decoder: Decoder) = LongArrayTag(serializer.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: LongArrayTag) =
        serializer.serialize(encoder, value.asLongArray)
}

@ExperimentalSerializationApi
object EndTagSerializer : KSerializer<EndTag> {
    override val descriptor = PrimitiveSerialDescriptor("nbt_end", PrimitiveKind.BYTE)

    override fun deserialize(decoder: Decoder): EndTag {
        val byte = decoder.decodeByte()
        require(byte == 0.toByte()) { "EndTag require value 0b but ${byte}b" }
        return EndTag.INSTANCE
    }

    override fun serialize(encoder: Encoder, value: EndTag) = encoder.encodeByte(0)
}

@ExperimentalSerializationApi
object StringTagSerializer : KSerializer<StringTag> {
    override val descriptor = PrimitiveSerialDescriptor("nbt_string", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = StringTag.valueOf(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: StringTag) =
        encoder.encodeString(value.asString)
}

/** NumericTag */
@ExperimentalSerializationApi
object ByteTagSerializer : KSerializer<ByteTag> {
    override val descriptor = PrimitiveSerialDescriptor("nbt_byte", PrimitiveKind.BYTE)

    override fun deserialize(decoder: Decoder) = ByteTag.valueOf(decoder.decodeByte())

    override fun serialize(encoder: Encoder, value: ByteTag) = encoder.encodeByte(value.asByte)
}

@ExperimentalSerializationApi
object DoubleTagSerializer : KSerializer<DoubleTag> {
    override val descriptor = PrimitiveSerialDescriptor("nbt_double", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder) = DoubleTag.valueOf(decoder.decodeDouble())

    override fun serialize(encoder: Encoder, value: DoubleTag) =
        encoder.encodeDouble(value.asDouble)
}

@ExperimentalSerializationApi
object FloatTagSerializer : KSerializer<FloatTag> {
    override val descriptor = PrimitiveSerialDescriptor("nbt_float", PrimitiveKind.FLOAT)

    override fun deserialize(decoder: Decoder) = FloatTag.valueOf(decoder.decodeFloat())

    override fun serialize(encoder: Encoder, value: FloatTag) = encoder.encodeFloat(value.asFloat)
}

@ExperimentalSerializationApi
object IntTagSerializer : KSerializer<IntTag> {
    override val descriptor = PrimitiveSerialDescriptor("nbt_int", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder) = IntTag.valueOf(decoder.decodeInt())

    override fun serialize(encoder: Encoder, value: IntTag) = encoder.encodeInt(value.asInt)
}

@ExperimentalSerializationApi
object LongTagSerializer : KSerializer<LongTag> {
    override val descriptor = PrimitiveSerialDescriptor("nbt_long", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder) = LongTag.valueOf(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: LongTag) = encoder.encodeLong(value.asLong)
}

@ExperimentalSerializationApi
object ShortTagSerializer : KSerializer<ShortTag> {
    override val descriptor = PrimitiveSerialDescriptor("nbt_short", PrimitiveKind.SHORT)

    override fun deserialize(decoder: Decoder) = ShortTag.valueOf(decoder.decodeShort())

    override fun serialize(encoder: Encoder, value: ShortTag) = encoder.encodeShort(value.asShort)
}
