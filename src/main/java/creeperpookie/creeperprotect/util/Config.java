package creeperpookie.creeperprotect.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import creeperpookie.creeperprotect.CreeperProtect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Config
{
	private final File configFile;
	private final JsonObject config;

	public Config(File configFile)
	{
		this.configFile = configFile;
		StringBuilder json = new StringBuilder();
		Scanner in;
		try
		{
			if (!this.configFile.exists())
			{
				new File(this.configFile.getAbsolutePath().substring(0, this.configFile.getAbsolutePath().length() - (CreeperProtect.MODID.length() + ".cfg".length() + 1))).mkdirs();
				this.configFile.createNewFile();
				FileWriter fileWriter = new FileWriter(this.configFile);
				fileWriter.write("{\n\t\"enable_creeper_ignition\": false,\n\t\"prevent_following_players\": false\n}");
				fileWriter.flush();
				fileWriter.close();
			}
			in = new Scanner(this.configFile);
			while (in.hasNextLine())
			{
				json.append(in.nextLine());
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		config = JsonParser.parseString(json.toString()).getAsJsonObject();
	}

	public boolean hasValue(String key)
	{
		return config.has(key);
	}

	public String getValue(String key)
	{
		return config.get(key).getAsString().trim();
	}

	public void set(String key, String value)
	{
		if (hasValue(key))
		{
			config.remove(key);
		}
		config.add(key, new JsonPrimitive(value));
		try(FileWriter fileWriter = new FileWriter(configFile))
		{
			fileWriter.write("");
			fileWriter.write(config.toString());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}