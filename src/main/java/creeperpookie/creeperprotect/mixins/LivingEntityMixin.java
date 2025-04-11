package creeperpookie.creeperprotect.mixins;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, net.minecraftforge.common.extensions.IForgeLivingEntity
{
	@Shadow public abstract float getMaxHealth();

	public LivingEntityMixin(EntityType<?> entityType, Level level)
	{
		super(entityType, level);
	}

	@Inject(method = "baseTick", at = @At("HEAD"))
	public void baseTick(CallbackInfo ci)
	{
		if (this.getType() == EntityType.CREEPER) this.clearFire();
	}

	@ModifyVariable(method = "setHealth", at = @At(value = "HEAD"), argsOnly = true)
	public float setHealth(float value)
	{
		if (this.getType() == EntityType.CREEPER) return getMaxHealth();
		else return value;
	}

	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	public void hurt(DamageSource p_21016_, float p_21017_, CallbackInfoReturnable<Boolean> cir)
	{
		if (this.getType() == EntityType.CREEPER)
		{
			cir.setReturnValue(false);
			cir.cancel();
		}
	}

	@Inject(method = "kill", at = @At("HEAD"), cancellable = true)
	public void kill(CallbackInfo ci)
	{
		if (this.getType() == EntityType.CREEPER) ci.cancel();
	}
}