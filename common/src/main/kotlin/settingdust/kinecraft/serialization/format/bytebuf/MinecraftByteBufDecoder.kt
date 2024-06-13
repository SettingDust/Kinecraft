package settingdust.kinecraft.serialization.format.bytebuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder

@ExperimentalSerializationApi
interface MinecraftByteBufDecoder : Decoder, CompositeDecoder {
    val minecraftByteBuf: MinecraftByteBuf
}
