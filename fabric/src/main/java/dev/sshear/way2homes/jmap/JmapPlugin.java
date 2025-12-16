package dev.sshear.way2homes.jmap;

import dev.sshear.way2homes.Constants;
import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.client.IClientPlugin;
import journeymap.api.v2.client.JourneyMapPlugin;
import net.fabricmc.loader.api.FabricLoader;

@JourneyMapPlugin(apiVersion = "2.0.0")
public class JmapPlugin implements IClientPlugin {

    private IClientAPI jmApi = null;

    @Override
    public void initialize(IClientAPI jmApi) {
        this.jmApi = jmApi;
        JmapEventListener eventListener = new JmapEventListener(jmApi);
    }

    @Override
    public String getModId() {
        return Constants.MOD_ID;
    }
}
