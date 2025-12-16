package dev.sshear.way2homes.jmap;

import dev.sshear.way2homes.Constants;
import dev.sshear.way2homes.Home;
import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.client.IClientPlugin;
import journeymap.api.v2.client.JourneyMapPlugin;
import journeymap.api.v2.client.event.ClientEvent;
import journeymap.api.v2.common.waypoint.Waypoint;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;
import java.util.Map;

@JourneyMapPlugin(apiVersion = "2.0.0")
public class JmapPlugin implements IClientPlugin {

    private IClientAPI jmApi = null;

    @Override
    public void initialize(IClientAPI jmApi) {
        this.jmApi = jmApi;
        JmapEventListener eventListener = new JmapEventListener(jmApi);
        NeoForge.EVENT_BUS.register(eventListener);
    }

    @Override
    public String getModId() {
        return Constants.MOD_ID;
    }
}
