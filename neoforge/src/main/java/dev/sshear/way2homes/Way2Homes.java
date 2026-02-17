package dev.sshear.way2homes;


import dev.sshear.way2homes.platform.Services;
import dev.sshear.way2homes.platform.services.IPlatformHelper;
import dev.sshear.way2homes.xaeros.XaerosEventListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class Way2Homes {

    public static IEventBus MOD_EVENT_BUS;

    public Way2Homes(IEventBus eventBus) {
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.
        Constants.LOG.info("Hello from Way2Homes.");

        MOD_EVENT_BUS = eventBus;

        IPlatformHelper platformHelper = Services.PLATFORM;
        if (platformHelper.isModLoaded("xaerominimap")) {
            Constants.LOG.info("Xaero's Minimap is installed.");
            eventBus.register(new XaerosEventListener());
        }
    }
}
