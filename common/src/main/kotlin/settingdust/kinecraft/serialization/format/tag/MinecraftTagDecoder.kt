package settingdust.kinecraft.serialization.format.tag

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import net.minecraft.nbt.Tag

@ExperimentalSerializationApi
interface MinecraftTagDecoder : Decoder, CompositeDecoder {
    val nbt: MinecraftTag

    fun decodeTag(): Tag
}
