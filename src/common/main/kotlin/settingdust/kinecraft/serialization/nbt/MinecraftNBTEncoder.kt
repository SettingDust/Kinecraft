package settingdust.kinecraft.serialization.nbt

import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.Tag

interface MinecraftNBTEncoder : Encoder, CompositeEncoder {
    val nbt: MinecraftNBT

    fun encodeTag(tag: Tag)
}