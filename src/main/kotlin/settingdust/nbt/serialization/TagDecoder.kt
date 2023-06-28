package settingdust.nbt.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import net.minecraft.nbt.Tag

@ExperimentalSerializationApi
interface TagDecoder : Decoder, CompositeDecoder {
    val nbt: MinecraftTag
    fun decodeTag(): Tag
}
