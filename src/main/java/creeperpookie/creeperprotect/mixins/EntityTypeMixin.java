package creeperpookie.creeperprotect.mixins;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin
{
	@Inject(method = "register(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;", at = @At("HEAD"), cancellable = true)
	private static void register(RegistryKey<EntityType<?>> key, EntityType.Builder<?> type, CallbackInfoReturnable<EntityType<?>> cir)
	{
		String id = key.getValue().getPath();
		if (id.equals("creeper"))
		{
			cir.cancel();
			cir.setReturnValue(Registry.register(Registries.ENTITY_TYPE, id, type.makeFireImmune().build(key)));
		}
	}
}