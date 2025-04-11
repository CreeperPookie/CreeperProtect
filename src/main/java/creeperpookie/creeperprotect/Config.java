package creeperpookie.creeperprotect;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = CreeperProtectMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue PREVENT_FOLLOWING = BUILDER
            .comment("Toggle if Creepers are allowed to follow players")
            .define("prevent_following", false);

    private static final ForgeConfigSpec.BooleanValue PREVENT_IGNITION = BUILDER
            .comment("Toggle if Creepers are allowed to explode")
            .define("prevent_ignition", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    private static boolean preventFollowing;
    private static boolean preventIgnition;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        preventFollowing = PREVENT_FOLLOWING.get();
        preventIgnition = PREVENT_IGNITION.get();
    }

    public static boolean canPreventFollowing()
    {
        return preventFollowing;
    }

    public static boolean canPreventIgnition()
    {
        return preventIgnition;
    }
}