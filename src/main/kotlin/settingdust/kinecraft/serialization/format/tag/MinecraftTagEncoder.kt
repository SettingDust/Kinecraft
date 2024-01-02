package settingdust.kinecraft.serialization.format.tag

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.Tag

@ExperimentalSerializationApi
interface MinecraftTagEncoder : Encoder, CompositeEncoder {
    val nbt: MinecraftTag

    fun encodeTag(tag: Tag)
}
