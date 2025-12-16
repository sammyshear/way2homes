package dev.sshear.way2homes.platform;

import dev.sshear.way2homes.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        //? if <1.21.9
        /*return !FMLLoader.isProduction();*/
        //? if >=1.21.9
        return !FMLLoader.getCurrent().isProduction();
    }
}
