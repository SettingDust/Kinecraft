package settingdust.kinecraft.serialization.nbt

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import net.minecraft.nbt.Tag
import settingdust.kinecraft.serialization.nbt.internal.readNBT
import settingdust.kinecraft.serialization.nbt.internal.writeNBT

data class MinecraftNBTConfiguration(
    val encodeDefaults: Boolean = false,
    val ignoreUnknownKeys: Boolean = false,
)

sealed class MinecraftNBT(
    val configuration: MinecraftNBTConfiguration,
    override val serializersModule: SerializersModule
) : SerialFormat {
    companion object Default : MinecraftNBT(MinecraftNBTConfiguration(), EmptySerializersModule())

    internal class Impl(config: MinecraftNBTConfiguration, serializersModule: SerializersModule) :
        MinecraftNBT(config, serializersModule)

    fun <T> encodeToTag(serializer: SerializationStrategy<T>, value: T): Tag =
        writeNBT(value, serializer)

    fun <T> decodeFromTag(deserializer: DeserializationStrategy<T>, element: Tag): T =
        readNBT(element, deserializer)
}

inline fun MinecraftNBT(
    from: MinecraftNBT = MinecraftNBT,
    build: MinecraftNBTBuilder.() -> Unit
): MinecraftNBT = MinecraftNBTBuilder(from).apply(build).build()

class MinecraftNBTBuilder(from: MinecraftNBT) {
    var encodeDefaults = from.configuration.encodeDefaults
    var ignoreUnknownKeys = from.configuration.ignoreUnknownKeys

    var serializersModule = from.serializersModule

    fun build(): MinecraftNBT =
        MinecraftNBT.Impl(
            MinecraftNBTConfiguration(encodeDefaults, ignoreUnknownKeys), serializersModule
        )
}

inline fun <reified T> MinecraftNBT.encodeToTag(value: T): Tag =
    encodeToTag(serializersModule.serializer(), value)

inline fun <reified T> MinecraftNBT.decodeFromTag(tag: Tag): T =
    decodeFromTag(serializersModule.serializer(), tag)

