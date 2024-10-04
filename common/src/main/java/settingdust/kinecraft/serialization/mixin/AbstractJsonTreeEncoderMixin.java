package settingdust.kinecraft.serialization.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import kotlinx.serialization.descriptors.PrimitiveKind;
import kotlinx.serialization.descriptors.SerialKind;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * <a href="https://github.com/Kotlin/kotlinx.serialization/issues/2463">https://github.com/Kotlin/kotlinx.serialization/issues/2463</a>
 */
@SuppressWarnings("KotlinInternalInJava")
@Mixin(targets = "kotlinx.serialization.json.internal.AbstractJsonTreeEncoder", remap = false)
public class AbstractJsonTreeEncoderMixin {
//    @ModifyVariable(
//        method = "encodeSerializableValue",
//        at = @At(value = "STORE")
//    )
//    private boolean block_placement_predicates$matchKind(
//        boolean isPolymorphicSerializer,
//        SerializationStrategy<?> serializer
//    ) {
//        return serializer.getDescriptor().getKind() instanceof PolymorphicKind;
//    }

    @WrapWithCondition(
        method = "encodeSerializableValue",
        at = @At(
            value = "INVOKE",
            target = "Lkotlinx/serialization/json/internal/PolymorphicKt;checkKind(Lkotlinx/serialization/descriptors/SerialKind;)V"
        )
    )
    private boolean block_placement_predicates$avoidCheckKindIfNoDiscriminator(
        SerialKind kind,
        @Local(ordinal = 1) boolean needDiscriminator
    ) {
        return true || needDiscriminator || !(kind instanceof SerialKind.ENUM || kind instanceof PrimitiveKind);
    }
}
