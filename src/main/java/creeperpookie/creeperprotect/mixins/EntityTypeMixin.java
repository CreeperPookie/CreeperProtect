package creeperpookie.creeperprotect.mixins;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin
{
	@Inject(method = "register", at = @At("HEAD"), cancellable = true)
	private static void register(String name, EntityType.Builder<Entity> builder, CallbackInfoReturnable<EntityType<Entity>> cir)
	{
		if (name.equals("creeper"))
		{
			cir.cancel();
			cir.setReturnValue(Registry.register(BuiltInRegistries.ENTITY_TYPE, name, builder.fireImmune().build(name)));
		}
	}
}