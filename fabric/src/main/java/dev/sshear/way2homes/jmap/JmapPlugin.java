package dev.sshear.way2homes.jmap;

import dev.sshear.way2homes.Constants;
import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.client.IClientPlugin;
import journeymap.api.v2.client.JourneyMapPlugin;

@JourneyMapPlugin(apiVersion = "2.0.0")
public class JmapPlugin implements IClientPlugin {

    private IClientAPI jmApi = null;
    private JmapEventListener eventListener;

    private static JmapPlugin INSTANCE;

    public JmapPlugin() {
        INSTANCE = this;
    }

    @Override
    public void initialize(IClientAPI jmApi) {
        this.jmApi = jmApi;
        this.eventListener = new JmapEventListener(jmApi);
        Constants.LOG.info("JourneyMap plugin for Way2Homes initialized.");
    }

    @Override
    public String getModId() {
        return Constants.MOD_ID;
    }
}
