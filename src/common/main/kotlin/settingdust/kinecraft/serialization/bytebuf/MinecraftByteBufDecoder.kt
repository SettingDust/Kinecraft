package settingdust.kinecraft.serialization.bytebuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder

@ExperimentalSerializationApi
interface MinecraftByteBufDecoder : Decoder, CompositeDecoder {
    val minecraftByteBuf: MinecraftByteBuf
}
