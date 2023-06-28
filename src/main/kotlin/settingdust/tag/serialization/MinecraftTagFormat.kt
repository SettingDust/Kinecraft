package settingdust.tag.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.serializer
import net.minecraft.nbt.Tag
import settingdust.tag.serialization.internal.readTag
import settingdust.tag.serialization.internal.writeTag

/**
 * Nbt minecraft tag types format
 */
@ExperimentalSerializationApi
sealed class MinecraftTag(val configuration: MinecraftTagConfiguration, serializersModule: SerializersModule) {
    companion object Default : MinecraftTag(MinecraftTagConfiguration(), EmptySerializersModule())
    internal class Impl(config: MinecraftTagConfiguration, serializersModule: SerializersModule) :
        MinecraftTag(config, serializersModule)

    val serializersModule = serializersModule + TagsModule

    fun <T> encodeToTag(serializer: SerializationStrategy<T>, value: T): Tag =
        writeTag(value, serializer)

    fun <T> decodeFromTag(deserializer: DeserializationStrategy<T>, element: Tag): T =
        readTag(element, deserializer)
}

data class MinecraftTagConfiguration(
    val encodeDefaults: Boolean = false,
    val ignoreUnknownKeys: Boolean = false,
)

@ExperimentalSerializationApi
inline fun MinecraftTag(from: MinecraftTag = MinecraftTag, build: MinecraftTagBuilder.() -> Unit): MinecraftTag =
    MinecraftTagBuilder(from).apply(build).build()

@ExperimentalSerializationApi
class MinecraftTagBuilder(from: MinecraftTag) {
    var encodeDefaults = from.configuration.encodeDefaults
    var ignoreUnknownKeys = from.configuration.ignoreUnknownKeys

    var serializersModule = from.serializersModule

    fun build(): MinecraftTag =
        MinecraftTag.Impl(MinecraftTagConfiguration(encodeDefaults, ignoreUnknownKeys), serializersModule)
}

@ExperimentalSerializationApi
inline fun <reified T> MinecraftTag.encodeToTag(value: T): Tag =
    encodeToTag(serializersModule.serializer(), value)

@ExperimentalSerializationApi
inline fun <reified T> MinecraftTag.decodeFromTag(tag: Tag): T =
    decodeFromTag(serializersModule.serializer(), tag)
