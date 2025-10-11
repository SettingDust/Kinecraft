package settingdust.kinecraft.serialization.nbt

import com.mojang.brigadier.StringReader
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.minecraft.nbt.*
import settingdust.kinecraft.serialization.JsonContentPolymorphicSerializer

@ExperimentalSerializationApi
fun JsonNbtsModule(needClassDiscriminator: Boolean = true) = SerializersModule {
    contextual(NbtJsonContentPolymorphicSerializer(needClassDiscriminator))
    contextual(CompoundTagSerializer)
    // SNBT will ignore the EndTag. It won't be encoded in JSON. Should be used
    // contextual(StringifiedNbtPrimitiveSerializer.cast<EndTag>())
    contextual(StringifiedNbtPrimitiveSerializer.cast<StringTag>())
    contextual(StringifiedNbtPrimitiveSerializer.cast<ByteTag>())
    contextual(StringifiedNbtPrimitiveSerializer.cast<DoubleTag>())
    contextual(StringifiedNbtPrimitiveSerializer.cast<FloatTag>())
    contextual(StringifiedNbtPrimitiveSerializer.cast<IntTag>())
    contextual(StringifiedNbtPrimitiveSerializer.cast<LongTag>())
    contextual(StringifiedNbtPrimitiveSerializer.cast<ShortTag>())
    contextual(StringifiedNbtPrimitiveSerializer.cast<ListTag>())
    contextual(PolymorphicNbtSerializer.create(needClassDiscriminator).cast<ByteArrayTag>())
    contextual(PolymorphicNbtSerializer.create(needClassDiscriminator).cast<IntArrayTag>())
    contextual(PolymorphicNbtSerializer.create(needClassDiscriminator).cast<LongArrayTag>())
}

private fun <T> KSerializer<in T>.cast() = this as KSerializer<T>

private val PARSER = TagParser(StringReader(""))

class NbtJsonContentPolymorphicSerializer(private val needClassDiscriminator: Boolean = true) :
    JsonContentPolymorphicSerializer<Tag>(Tag::class) {

    @OptIn(ExperimentalSerializationApi::class)
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Tag> {
        if (element is JsonPrimitive) {
            return StringifiedNbtPrimitiveSerializer
        }
        if (element is JsonArray) {
            return ListTagSerializer
        }
        if (element is JsonObject) {
            val type = element["type"]?.jsonPrimitive?.contentOrNull
            return when (type) {
                ByteArrayTagSerializer.descriptor.serialName,
                IntArrayTagSerializer.descriptor.serialName,
                LongArrayTagSerializer.descriptor.serialName -> PolymorphicNbtSerializer.create(needClassDiscriminator)

                else -> CompoundTagSerializer
            }
        }
        return NbtSerializer.create(needClassDiscriminator)
    }
}

object StringifiedNbtPrimitiveSerializer : KSerializer<Tag> {
    override val descriptor = PrimitiveSerialDescriptor("nbt", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Tag {
        val input = decoder.decodeString()
        return PARSER.type(input)
    }

    override fun serialize(encoder: Encoder, value: Tag) = encoder.encodeString(SnbtPrinterTagVisitor().visit(value))
}