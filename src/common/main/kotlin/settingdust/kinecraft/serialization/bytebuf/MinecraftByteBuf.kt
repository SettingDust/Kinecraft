package settingdust.kinecraft.serialization.bytebuf

import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.network.FriendlyByteBuf
import settingdust.kinecraft.serialization.bytebuf.internal.ByteBufDecoder
import settingdust.kinecraft.serialization.bytebuf.internal.ByteBufEncoder

// TODO add StreamCodec factory for 1.21+
@ExperimentalSerializationApi
sealed class MinecraftByteBuf(
    val configuration: MinecraftByteBufConfiguration,
    override val serializersModule: SerializersModule
) : SerialFormat {

    companion object Default :
        MinecraftByteBuf(MinecraftByteBufConfiguration(), EmptySerializersModule())

    internal class Impl(
        config: MinecraftByteBufConfiguration,
        serializersModule: SerializersModule
    ) : MinecraftByteBuf(config, serializersModule)

    fun <T> encodeToByteBuf(
        serializer: SerializationStrategy<T>,
        obj: T,
        buf: FriendlyByteBuf
    ): FriendlyByteBuf {
        ByteBufEncoder(buf, this).apply { encodeNullableSerializableValue(serializer, obj) }
        return buf
    }

    fun <T> decodeFromByteBuf(deserializer: DeserializationStrategy<T>, buf: FriendlyByteBuf): T? {
        val decoder = ByteBufDecoder(buf, this)
        return decoder.decodeNullableSerializableValue(deserializer)
    }
}

data class MinecraftByteBufConfiguration(
    val encodeDefaults: Boolean = false,
    val ignoreUnknownKeys: Boolean = false,
)

@ExperimentalSerializationApi
inline fun MinecraftByteBuf(
    from: MinecraftByteBuf = MinecraftByteBuf,
    build: MinecraftByteBufBuilder.() -> Unit
): MinecraftByteBuf = MinecraftByteBufBuilder(from).apply(build).build()

@ExperimentalSerializationApi
class MinecraftByteBufBuilder(from: MinecraftByteBuf) {
    var encodeDefaults = from.configuration.encodeDefaults
    var ignoreUnknownKeys = from.configuration.ignoreUnknownKeys

    var serializersModule = from.serializersModule

    fun build(): MinecraftByteBuf =
        MinecraftByteBuf.Impl(
            MinecraftByteBufConfiguration(encodeDefaults, ignoreUnknownKeys),
            serializersModule
        )
}

@ExperimentalSerializationApi
inline fun <reified T> MinecraftByteBuf.encodeToByteBuf(value: T, buf: FriendlyByteBuf) =
    encodeToByteBuf(serializersModule.serializer(), value, buf)

@ExperimentalSerializationApi
inline fun <reified T> MinecraftByteBuf.decodeFromByteBuf(buf: FriendlyByteBuf): T? =
    decodeFromByteBuf(serializersModule.serializer(), buf)

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> FriendlyByteBuf.encodeWithSerializers(
    value: T,
    minecraftByteBuf: MinecraftByteBuf = MinecraftByteBuf
) = minecraftByteBuf.encodeToByteBuf(minecraftByteBuf.serializersModule.serializer(), value, this)

@ExperimentalSerializationApi
inline fun <reified T> FriendlyByteBuf.decodeWithSerializers(
    minecraftByteBuf: MinecraftByteBuf = MinecraftByteBuf
): T? = minecraftByteBuf.decodeFromByteBuf(minecraftByteBuf.serializersModule.serializer(), this)