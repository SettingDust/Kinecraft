package settingdust.kinecraft.serialization.format.bytebuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder

@ExperimentalSerializationApi
interface MinecraftByteBufEncoder : Encoder, CompositeEncoder {
    val minecraftByteBuf: MinecraftByteBuf
}
