package settingdust.kinecraft.serialization.nbt

import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import net.minecraft.nbt.Tag

interface MinecraftNBTDecoder : Decoder, CompositeDecoder {
    val nbt: MinecraftNBT

    fun decodeNBT(): Tag
}