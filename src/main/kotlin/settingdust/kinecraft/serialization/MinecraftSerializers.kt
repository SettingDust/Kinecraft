package settingdust.kinecraft.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

@ExperimentalSerializationApi
object ResourceLocationStringSerializer : KSerializer<ResourceLocation> {
    override val descriptor =
        PrimitiveSerialDescriptor(ResourceLocation::class.simpleName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = ResourceLocation(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ResourceLocation) =
        encoder.encodeString(value.toString())
}

@ExperimentalSerializationApi
object ItemStackSerializer : KSerializer<ItemStack> {
    override val descriptor =
        SerialDescriptor(ItemStack::class.simpleName!!, CompoundTagSerializer.descriptor)

    override fun deserialize(decoder: Decoder) =
        ItemStack.of(decoder.decodeSerializableValue(CompoundTagSerializer))

    override fun serialize(encoder: Encoder, value: ItemStack) =
        encoder.encodeSerializableValue(CompoundTagSerializer, CompoundTag().also(value::save))
}
