package settingdust.kinecraft.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.descriptors.mapSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import net.minecraft.nbt.ByteArrayTag
import net.minecraft.nbt.ByteTag
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.EndTag
import net.minecraft.nbt.FloatTag
import net.minecraft.nbt.IntArrayTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.LongArrayTag
import net.minecraft.nbt.LongTag
import net.minecraft.nbt.ShortTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import settingdust.kinecraft.serialization.format.tag.MinecraftTagDecoder
import settingdust.kinecraft.serialization.format.tag.MinecraftTagEncoder

@ExperimentalSerializationApi
val TagsModule = SerializersModule {
    polymorphic(Tag::class) {
        subclass(CompoundTag::class, CompoundTagSerializer)
        subclass(EndTag::class, EndTagSerializer)
        subclass(StringTag::class, StringTagSerializer)
        subclass(ByteTag::class, ByteTagSerializer)
        subclass(DoubleTag::class, DoubleTagSerializer)
        subclass(FloatTag::class, FloatTagSerializer)
        subclass(IntTag::class, IntTagSerializer)
        subclass(LongTag::class, LongTagSerializer)
        subclass(ShortTag::class, ShortTagSerializer)
        subclass(ListTag::class, ListTagSerializer)
        subclass(ByteArrayTag::class, ByteArrayTagSerializer)
        subclass(IntArrayTag::class, IntArrayTagSerializer)
        subclass(LongArrayTag::class, LongArrayTagSerializer)
    }
    polymorphicDefaultDeserializer(Tag::class) { TagSerializer }
    polymorphicDefaultSerializer(Tag::class) { TagSerializer }
}

@OptIn(ExperimentalSerializationApi::class)
val PolymorphicTagsModule = SerializersModule {
    polymorphic(Tag::class) {
        subclass(CompoundTag::class, PolymorphicSurrogateSerializer(CompoundTagSerializer))
        subclass(EndTag::class, PolymorphicSurrogateSerializer(EndTagSerializer))
        subclass(StringTag::class, PolymorphicSurrogateSerializer(StringTagSerializer))
        subclass(ByteTag::class, PolymorphicSurrogateSerializer(ByteTagSerializer))
        subclass(DoubleTag::class, PolymorphicSurrogateSerializer(DoubleTagSerializer))
        subclass(FloatTag::class, PolymorphicSurrogateSerializer(FloatTagSerializer))
        subclass(IntTag::class, PolymorphicSurrogateSerializer(IntTagSerializer))
        subclass(LongTag::class, PolymorphicSurrogateSerializer(LongTagSerializer))
        subclass(ShortTag::class, PolymorphicSurrogateSerializer(ShortTagSerializer))
        subclass(ListTag::class, PolymorphicSurrogateSerializer(ListTagSerializer))
        subclass(ByteArrayTag::class, PolymorphicSurrogateSerializer(ByteArrayTagSerializer))
        subclass(IntArrayTag::class, PolymorphicSurrogateSerializer(IntArrayTagSerializer))
        subclass(LongArrayTag::class, PolymorphicSurrogateSerializer(LongArrayTagSerializer))
    }
    polymorphicDefaultDeserializer(Tag::class) { TagSerializer }
    polymorphicDefaultSerializer(Tag::class) { TagSerializer }
}

@OptIn(ExperimentalSerializationApi::class)
object TagPolymorphicSerializer :
    SealedClassSerializer<Tag>(
        Tag::class.simpleName!!,
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
        arrayOf(
            PolymorphicSurrogateSerializer(CompoundTagSerializer),
            PolymorphicSurrogateSerializer(EndTagSerializer),
            PolymorphicSurrogateSerializer(StringTagSerializer),
            PolymorphicSurrogateSerializer(ByteTagSerializer),
            PolymorphicSurrogateSerializer(DoubleTagSerializer),
            PolymorphicSurrogateSerializer(FloatTagSerializer),
            PolymorphicSurrogateSerializer(IntTagSerializer),
            PolymorphicSurrogateSerializer(LongTagSerializer),
            PolymorphicSurrogateSerializer(ShortTagSerializer),
            PolymorphicSurrogateSerializer(ListTagSerializer),
            PolymorphicSurrogateSerializer(ByteArrayTagSerializer),
            PolymorphicSurrogateSerializer(IntArrayTagSerializer),
            PolymorphicSurrogateSerializer(LongArrayTagSerializer),
        ),
    ),
    KSerializer<Tag> {

}

@ExperimentalSerializationApi
@OptIn(InternalSerializationApi::class)
object TagSerializer : KSerializer<Tag> {
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
        if (decoder is MinecraftTagDecoder) {
            decoder.decodeTag()
        } // Need type info if isn't TagDecoder
        else {
            TagPolymorphicSerializer.deserialize(decoder)
        }

    override fun serialize(encoder: Encoder, value: Tag) {
        if (encoder is MinecraftTagEncoder) {
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
        } // Need type info if isn't TagEncoder
        else {
            TagPolymorphicSerializer.serialize(encoder, value)
        }
    }
}

@ExperimentalSerializationApi
object CompoundTagSerializer : KSerializer<CompoundTag> {
    override val descriptor =
        SerialDescriptor(
            CompoundTag::class.simpleName!!,
            mapSerialDescriptor(String.serializer().descriptor, defer { TagSerializer.descriptor }),
        )

    override fun deserialize(decoder: Decoder) =
        CompoundTag().apply {
            MapSerializer(String.serializer(), decoder.serializersModule.serializer<Tag>())
                .deserialize(decoder)
                .forEach { put(it.key, it.value) }
        }

    override fun serialize(encoder: Encoder, value: CompoundTag) =
        MapSerializer(String.serializer(), encoder.serializersModule.serializer<Tag>())
            .serialize(encoder, value.allKeys.associateWith { value[it]!! })
}

@ExperimentalSerializationApi
object EndTagSerializer : KSerializer<EndTag> {
    override val descriptor =
        PrimitiveSerialDescriptor(EndTag::class.simpleName!!, PrimitiveKind.BYTE)

    override fun deserialize(decoder: Decoder) =
        EndTag.INSTANCE.also {
            val byte = decoder.decodeByte()
            require(byte == 0.toByte()) { "EndTag require value 0b but ${byte}b" }
        }

    override fun serialize(encoder: Encoder, value: EndTag) = encoder.encodeByte(0)
}

@ExperimentalSerializationApi
object StringTagSerializer : KSerializer<StringTag> {
    override val descriptor =
        PrimitiveSerialDescriptor(StringTag::class.simpleName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = StringTag.valueOf(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: StringTag) =
        encoder.encodeString(value.asString)
}

/** NumericTag */
@ExperimentalSerializationApi
object ByteTagSerializer : KSerializer<ByteTag> {
    override val descriptor =
        PrimitiveSerialDescriptor(ByteTag::class.simpleName!!, PrimitiveKind.BYTE)

    override fun deserialize(decoder: Decoder) = ByteTag.valueOf(decoder.decodeByte())

    override fun serialize(encoder: Encoder, value: ByteTag) = encoder.encodeByte(value.asByte)
}

@ExperimentalSerializationApi
object DoubleTagSerializer : KSerializer<DoubleTag> {
    override val descriptor =
        PrimitiveSerialDescriptor(DoubleTag::class.simpleName!!, PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder) = DoubleTag.valueOf(decoder.decodeDouble())

    override fun serialize(encoder: Encoder, value: DoubleTag) =
        encoder.encodeDouble(value.asDouble)
}

@ExperimentalSerializationApi
object FloatTagSerializer : KSerializer<FloatTag> {
    override val descriptor =
        PrimitiveSerialDescriptor(FloatTag::class.simpleName!!, PrimitiveKind.FLOAT)

    override fun deserialize(decoder: Decoder) = FloatTag.valueOf(decoder.decodeFloat())

    override fun serialize(encoder: Encoder, value: FloatTag) = encoder.encodeFloat(value.asFloat)
}

@ExperimentalSerializationApi
object IntTagSerializer : KSerializer<IntTag> {
    override val descriptor =
        PrimitiveSerialDescriptor(IntTag::class.simpleName!!, PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder) = IntTag.valueOf(decoder.decodeInt())

    override fun serialize(encoder: Encoder, value: IntTag) = encoder.encodeInt(value.asInt)
}

@ExperimentalSerializationApi
object LongTagSerializer : KSerializer<LongTag> {
    override val descriptor =
        PrimitiveSerialDescriptor(LongTag::class.simpleName!!, PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder) = LongTag.valueOf(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: LongTag) = encoder.encodeLong(value.asLong)
}

@ExperimentalSerializationApi
object ShortTagSerializer : KSerializer<ShortTag> {
    override val descriptor =
        PrimitiveSerialDescriptor(ShortTag::class.simpleName!!, PrimitiveKind.SHORT)

    override fun deserialize(decoder: Decoder) = ShortTag.valueOf(decoder.decodeShort())

    override fun serialize(encoder: Encoder, value: ShortTag) = encoder.encodeShort(value.asShort)
}

@ExperimentalSerializationApi
object ListTagSerializer : KSerializer<ListTag> {
    override val descriptor =
        SerialDescriptor(
            ListTag::class.simpleName!!,
            listSerialDescriptor(defer { TagSerializer.descriptor }),
        )

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
    override val descriptor =
        SerialDescriptor(ByteArrayTag::class.simpleName!!, serializer.descriptor)

    override fun deserialize(decoder: Decoder) = ByteArrayTag(serializer.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: ByteArrayTag) =
        serializer.serialize(encoder, value.asByteArray)
}

@ExperimentalSerializationApi
object IntArrayTagSerializer : KSerializer<IntArrayTag> {
    private val serializer = IntArraySerializer()
    override val descriptor =
        SerialDescriptor(IntArrayTag::class.simpleName!!, serializer.descriptor)

    override fun deserialize(decoder: Decoder) = IntArrayTag(serializer.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: IntArrayTag) =
        serializer.serialize(encoder, value.asIntArray)
}

@ExperimentalSerializationApi
object LongArrayTagSerializer : KSerializer<LongArrayTag> {
    private val serializer = LongArraySerializer()
    override val descriptor =
        SerialDescriptor(LongArrayTag::class.simpleName!!, serializer.descriptor)

    override fun deserialize(decoder: Decoder) = LongArrayTag(serializer.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: LongArrayTag) =
        serializer.serialize(encoder, value.asLongArray)
}
