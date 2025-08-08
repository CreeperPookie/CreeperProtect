package creeperpookie.creeperprotect.mixins;

import creeperpookie.creeperprotect.CreeperProtect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity
{
	@Shadow private int currentFuseTime;

	protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Unique
	private static boolean creeperIgnitionDisabled()
	{
		return !CreeperProtect.getConfig().getValue("enable_creeper_ignition").equalsIgnoreCase("true");
	}

	@Unique
	private boolean creeperFollowingEnabled()
	{
		return CreeperProtect.getConfig().getValue("prevent_following_players").equalsIgnoreCase("true");
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void tick(CallbackInfo ci)
	{
		if (creeperIgnitionDisabled())
		{
			ci.cancel();
			this.currentFuseTime = 0;
			super.tick();
		}
	}

	@Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
	public void setTarget(LivingEntity target, CallbackInfo ci)
	{
		if (creeperFollowingEnabled())
		{
			ci.cancel();
			super.setTarget(null);
		}
	}

	@Inject(method = "setFuseSpeed", at = @At("HEAD"), cancellable = true)
	public void setFuseSpeed(int fuseSpeed, CallbackInfo ci)
	{
		if (creeperIgnitionDisabled() && fuseSpeed > 0)
		{
			ci.cancel();
			this.dataTracker.set(CreeperEntity.FUSE_SPEED, -1);
		}
	}

	@Inject(method = "explode", at = @At("HEAD"), cancellable = true)
	public void explodeCreeper(CallbackInfo ci)
	{
		if (creeperIgnitionDisabled()) ci.cancel();
	}

	@Inject(method = "isIgnited", at = @At("HEAD"), cancellable = true)
	public void isIgnited(CallbackInfoReturnable<Boolean> cir)
	{
		if (creeperIgnitionDisabled())
		{
			cir.cancel();
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "ignite", at = @At("HEAD"), cancellable = true)
	public void ignite(CallbackInfo ci)
	{
		if (creeperIgnitionDisabled())
		{
			ci.cancel();
			this.dataTracker.set(CreeperEntity.IGNITED, false);
		}
	}
}