package net.architecturaldog.bluetools;

import dev.jaxydog.lodestone.Lodestone;
import dev.jaxydog.lodestone.api.CommonLoaded;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlueTools implements ModInitializer {
	public static final String MOD_ID = "bluetools";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
            Lodestone.load(CommonLoaded.class, MOD_ID);
	}
}