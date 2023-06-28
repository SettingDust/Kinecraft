package settingdust.nbt.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.Tag

@ExperimentalSerializationApi
interface TagEncoder : Encoder, CompositeEncoder {
    val nbt: MinecraftTag
    fun encodeTag(tag: Tag)
}
