package settingdust.kinecraft.serialization

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class GsonElementAsStringSerializer(private val json: Json = Json) : KSerializer<JsonElement> {
    override val descriptor = PrimitiveSerialDescriptor("gson.JsonElementAsString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): JsonElement {
        return json.parseToJsonElement(decoder.decodeString()).asGson()
    }

    override fun serialize(encoder: Encoder, value: JsonElement) {
        encoder.encodeString(json.encodeToString(value.asKotlin()))
    }
}

/**
 * Kotlin bridge with Gson json elements.
 * Have to use Json format
 */
@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
object GsonElementSerializer : KSerializer<JsonElement> {
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("gson.JsonElement", PolymorphicKind.SEALED) {
            // Resolve cyclic dependency in descriptors by late binding
            element("JsonPrimitive", defer { GsonPrimitiveSerializer.descriptor })
            element("JsonNull", defer { JsonNullSerializer.descriptor })
            element("JsonObject", defer { GsonObjectSerializer.descriptor })
            element("JsonArray", defer { GsonArraySerializer.descriptor })
        }

    override fun deserialize(decoder: Decoder): JsonElement {
        require(decoder is JsonDecoder)
        return decoder.decodeJsonElement().asGson()
    }

    override fun serialize(encoder: Encoder, value: JsonElement) {
        require(encoder is JsonEncoder)
        encoder.encodeJsonElement(value.asKotlin())
    }
}

object GsonObjectSerializer : KSerializer<JsonObject> {
    private object JsonObjectDescriptor :
        SerialDescriptor by MapSerializer(String.serializer(), GsonElementSerializer).descriptor {
        @ExperimentalSerializationApi
        override val serialName: String = "gson.JsonObject"
    }

    override val descriptor: SerialDescriptor = JsonObjectDescriptor

    override fun deserialize(decoder: Decoder): JsonObject {
        MapSerializer(String.serializer(), GsonElementSerializer).deserialize(decoder).let {
            val obj = JsonObject()
            it.forEach { (k, v) -> obj.add(k, v) }
            return obj
        }
    }

    override fun serialize(encoder: Encoder, value: JsonObject) {
        MapSerializer(String.serializer(), GsonElementSerializer).serialize(encoder, value.toMap())
    }
}

object GsonArraySerializer : KSerializer<JsonArray> {
    object GsonArrayDescriptor : SerialDescriptor by ListSerializer(GsonElementSerializer).descriptor {
        @ExperimentalSerializationApi
        override val serialName: String = "gson.JsonArray"
    }

    override val descriptor = GsonArrayDescriptor

    override fun deserialize(decoder: Decoder): JsonArray {
        val list = ListSerializer(GsonElementSerializer).deserialize(decoder)
        val array = JsonArray(list.size)
        list.forEach { array.add(it) }
        return array
    }

    override fun serialize(encoder: Encoder, value: JsonArray) {
        val list = mutableListOf<JsonElement>()
        value.forEach { list.add(it) }
        ListSerializer(GsonElementSerializer).serialize(encoder, list)
    }
}

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
object GsonPrimitiveSerializer : KSerializer<JsonPrimitive> {
    override val descriptor = buildSerialDescriptor("gson.JsonPrimitive", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): JsonPrimitive {
        require(decoder is JsonDecoder)
        val value = decoder.decodeJsonElement() as kotlinx.serialization.json.JsonPrimitive
        return value.asGson()
    }

    override fun serialize(encoder: Encoder, value: JsonPrimitive) {
        require(encoder is JsonEncoder)
        encoder.encodeJsonElement(value.asKotlin())
    }
}

@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
object JsonNullSerializer : KSerializer<JsonNull> {
    override val descriptor = buildSerialDescriptor("gson.JsonNull", SerialKind.ENUM)

    override fun deserialize(decoder: Decoder) = JsonNull.INSTANCE.also { decoder.decodeNull() }!!

    override fun serialize(encoder: Encoder, value: JsonNull) {
        encoder.encodeNull()
    }
}

private val jsonObjectMembers = JsonObject::class.memberProperties.single { it.name == "members" }.also {
    it.isAccessible = true
}
// Gson below 2.10 no `asMap`
fun JsonObject.toMap() = jsonObjectMembers.get(this) as MutableMap<String, JsonElement>

fun JsonPrimitive.asKotlin(): kotlinx.serialization.json.JsonPrimitive {
    return when {
        isString -> kotlinx.serialization.json.JsonPrimitive(asString)
        isNumber -> kotlinx.serialization.json.JsonPrimitive(asNumber)
        isBoolean -> kotlinx.serialization.json.JsonPrimitive(asBoolean)
        else -> kotlinx.serialization.json.JsonPrimitive(asString)
    }
}

fun JsonArray.asKotlin(): kotlinx.serialization.json.JsonArray {
    return JsonArray(map { it.asKotlin() })
}

fun JsonObject.asKotlin(): kotlinx.serialization.json.JsonObject {
    return JsonObject(toMap().map { it.key to it.value.asKotlin() }.toMap())
}

fun JsonElement.asKotlin(): kotlinx.serialization.json.JsonElement {
    return when (this) {
        is JsonArray -> asKotlin()
        is JsonObject -> asKotlin()
        is JsonNull -> JsonNull
        is JsonPrimitive -> asKotlin()
        else -> throw IllegalStateException("Unknown type: $this")
    }
}

fun kotlinx.serialization.json.JsonPrimitive.asGson(): JsonPrimitive {
    if (isString) return JsonPrimitive(content)
    longOrNull?.let { return JsonPrimitive(it) }
    content.toBigIntegerOrNull()?.let { return JsonPrimitive(it) }
    doubleOrNull?.let { return JsonPrimitive(it) }
    content.toBigDecimalOrNull()?.let { return JsonPrimitive(it) }
    booleanOrNull?.let { return JsonPrimitive(it) }
    return JsonPrimitive(content)
}

fun kotlinx.serialization.json.JsonArray.asGson(): JsonArray {
    val array = JsonArray(size)
    forEach { array.add(it.asGson()) }
    return array
}

fun kotlinx.serialization.json.JsonObject.asGson(): JsonObject {
    return JsonObject().also { json -> forEach { (k, v) -> json.add(k, v.asGson()) } }
}

fun kotlinx.serialization.json.JsonElement.asGson(): JsonElement {
    return when (this) {
        is kotlinx.serialization.json.JsonArray -> asGson()
        is kotlinx.serialization.json.JsonObject -> asGson()
        is kotlinx.serialization.json.JsonNull -> JsonNull.INSTANCE
        is kotlinx.serialization.json.JsonPrimitive -> asGson()
        else -> throw IllegalStateException("Unknown type: $this")
    }
}
