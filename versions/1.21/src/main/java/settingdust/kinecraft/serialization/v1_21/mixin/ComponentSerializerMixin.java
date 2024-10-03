package settingdust.kinecraft.serialization.v1_21.mixin;

import kotlinx.serialization.encoding.Decoder;
import kotlinx.serialization.encoding.Encoder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import settingdust.kinecraft.serialization.ComponentSerializer;
import settingdust.kinecraft.serialization.v1_21.ComponentSerializerImpl;

@Mixin(value = ComponentSerializer.class, remap = false)
public class ComponentSerializerMixin {
    /**
     * @author SettingDust
     * @reason 1.20 implementation
     */
    @Overwrite
    public void serialize(@NotNull final Encoder encoder, final Component component) {
        ComponentSerializerImpl.INSTANCE.serialize(encoder, component);
    }

    /**
     * @author SettingDust
     * @reason 1.20 implementation
     */
    @Overwrite
    public Component deserialize(@NotNull final Decoder decoder) {
        return ComponentSerializerImpl.INSTANCE.deserialize(decoder);
    }
}
