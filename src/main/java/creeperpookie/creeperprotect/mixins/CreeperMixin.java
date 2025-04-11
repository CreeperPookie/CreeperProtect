package creeperpookie.creeperprotect.mixins;

import creeperpookie.creeperprotect.Config;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends Monster implements PowerableMob
{
	@Shadow private int swell;

	protected CreeperMixin(EntityType<? extends Monster> entityType, Level level)
	{
		super(entityType, level);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void tick(CallbackInfo ci)
	{
		if (Config.canPreventIgnition())
		{
			ci.cancel();
			this.swell = 0;
			super.tick();
		}
	}

	@Inject(method = "explodeCreeper", at = @At("HEAD"), cancellable = true)
	public void explodeCreeper(CallbackInfo ci)
	{
		if (Config.canPreventIgnition()) ci.cancel();
	}

	@Inject(method = "isIgnited", at = @At("HEAD"), cancellable = true)
	public void isIgnited(CallbackInfoReturnable<Boolean> cir)
	{
		if (Config.canPreventIgnition())
		{
			cir.cancel();
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "ignite", at = @At("HEAD"), cancellable = true)
	public void ignite(CallbackInfo ci)
	{
		if (Config.canPreventIgnition())
		{
			ci.cancel();
			//this.entityData.set(Creeper.DATA_IS_IGNITED, false);
		}
	}
}