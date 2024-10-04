package settingdust.kinecraft.serialization.mixin;

import kotlinx.serialization.json.JsonConfiguration;
import kotlinx.serialization.modules.SerializersModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settingdust.kinecraft.serialization.JsonHooksKt;

@Mixin(targets = "kotlinx.serialization.json.JsonImpl", remap = false)
public abstract class JsonImplMixin {

    @Inject(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lkotlinx/serialization/json/JsonImpl;validateConfiguration()V",
            unsafe = true
        )
    )
    private void block_placement_predicates$recordDiscriminatorMode(
        final JsonConfiguration configuration, final SerializersModule _module, final CallbackInfo ci
    ) {
        JsonHooksKt.setJsonConfiguration(configuration);
    }

    @Inject(
        method = "<init>",
        at = @At(
            value = "RETURN"
        )
    )
    private void block_placement_predicates$clearDiscriminatorMode(
        final JsonConfiguration configuration, final SerializersModule _module, final CallbackInfo ci
    ) {
        JsonHooksKt.setJsonConfiguration(null);
    }
}
