package settingdust.kinecraft.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.serializer
import net.minecraft.SharedConstants
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.Bootstrap
import net.minecraft.world.item.ItemStack
import org.junit.jupiter.api.Test
import settingdust.kinecraft.serialization.format.tag.MinecraftTag
import settingdust.kinecraft.serialization.format.tag.decodeFromTag
import settingdust.kinecraft.serialization.format.tag.encodeToTag
import kotlin.test.assertEquals

@OptIn(ExperimentalSerializationApi::class)
class ItemStackSerializerTest {
    private val minecraftTag = MinecraftTag {
        serializersModule = SerializersModule {
            contextual(ItemStackSerializer)
            contextual(ResourceLocationStringSerializer)
        }
    }
    private var itemTag: CompoundTag
    private var item: ItemStack

    init {
        // Init MC
        SharedConstants.tryDetectVersion()
        Bootstrap.bootStrap()
        itemTag = CompoundTag().apply {
            putString("id", "minecraft:grass")
            putByte("Count", 5.toByte())
            put("tag", CompoundTag().apply { putString("foo", "bar") })
        }
        item = ItemStack.of(itemTag)
    }

    @Test
    fun deserialize() {
        assertEquals(ItemStackSerializer, minecraftTag.serializersModule.serializer<ItemStack>())
        assert(ItemStack.matches(item, minecraftTag.decodeFromTag<ItemStack>(itemTag)))
    }

    @Test
    fun serialize() {
        assertEquals(ItemStackSerializer, minecraftTag.serializersModule.serializer<ItemStack>())
        assertEquals(itemTag, minecraftTag.encodeToTag(item))
    }
}
