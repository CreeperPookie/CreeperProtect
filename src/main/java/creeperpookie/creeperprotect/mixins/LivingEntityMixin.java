package creeperpookie.creeperprotect.mixins;

import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable
{
	public LivingEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Shadow public abstract float getMaxHealth();


	@Inject(method = "baseTick", at = @At("HEAD"))
	public void baseTick(CallbackInfo ci)
	{
		if (this.getType() == EntityType.CREEPER)
		{
			this.setFireTicks(0);
			this.setFrozenTicks(0);
		}
	}

	@ModifyVariable(method = "setHealth", at = @At(value = "HEAD"), argsOnly = true)
	public float setHealth(float value)
	{
		if (this.getType() == EntityType.CREEPER) return getMaxHealth();
		else return value;
	}

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
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