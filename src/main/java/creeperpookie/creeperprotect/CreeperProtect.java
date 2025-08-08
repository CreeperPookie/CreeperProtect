package creeperpookie.creeperprotect;

import creeperpookie.creeperprotect.util.Config;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;

public class CreeperProtect implements ModInitializer
{
	public static final String MODID = "creeperprotect";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	private static final Config config = new Config(new File(Paths.get("").toAbsolutePath() + "\\config\\" + MODID + "\\" + MODID + ".cfg"));

	@Override
	public void onInitialize()
	{
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Successfully initialized CreeperProtect");
	}

	public static Config getConfig()
	{
		return config;
	}
}