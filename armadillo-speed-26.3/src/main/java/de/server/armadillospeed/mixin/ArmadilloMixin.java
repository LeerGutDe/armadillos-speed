package de.server.armadillospeed.mixin;

import net.minecraft.world.entity.animal.armadillo.Armadillo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla baby age begins at -24000 ticks and breeding cooldown at +6000.
 * Every server tick, vanilla advances these by one toward zero. We add
 * another 39 / subtract another 9 to reach zero in ~600 ticks (30 seconds).
 * The age is changed only for armadillos; other mobs are untouched.
 */
@Mixin(Armadillo.class)
public abstract class ArmadilloMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void armadillospeed$shortenAge(CallbackInfo ci) {
        Armadillo armadillo = (Armadillo) (Object) this;
        if (armadillo.level().isClientSide()) return;
        int age = armadillo.getAge();
        if (age < 0) {
            armadillo.setAge(Math.min(0, age + 39));
        } else if (age > 0) {
            armadillo.setAge(Math.max(0, age - 9));
        }
    }
}
