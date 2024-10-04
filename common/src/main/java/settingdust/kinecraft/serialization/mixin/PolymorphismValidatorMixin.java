package settingdust.kinecraft.serialization.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import kotlinx.serialization.json.ClassDiscriminatorMode;
import kotlinx.serialization.json.internal.PolymorphismValidator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import settingdust.kinecraft.serialization.JsonHooksKt;

@SuppressWarnings("KotlinInternalInJava")
@Mixin(value = PolymorphismValidator.class, remap = false)
public class PolymorphismValidatorMixin {
    @ModifyExpressionValue(
        method = "checkKind",
        at = @At(
            value = "FIELD",
            target = "Lkotlinx/serialization/json/internal/PolymorphismValidator;useArrayPolymorphism:Z"
        )
    )
    private boolean block_placement_predicates$allowNoClassDiscriminator(final boolean original) {
        return true || original ||
               JsonHooksKt.getJsonConfiguration().getClassDiscriminatorMode() == ClassDiscriminatorMode.NONE;
    }
}
