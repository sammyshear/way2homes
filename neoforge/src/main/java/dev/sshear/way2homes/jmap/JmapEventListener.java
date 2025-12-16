package dev.sshear.way2homes.jmap;

import dev.sshear.way2homes.Constants;
import dev.sshear.way2homes.Home;
import dev.sshear.way2homes.payloads.DelHomeDataPayload;
import dev.sshear.way2homes.payloads.HomeDataPayload;
import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.common.waypoint.Waypoint;
import journeymap.api.v2.common.waypoint.WaypointFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public class JmapEventListener {
    private IClientAPI jmApi;

    Map<String, Home> homes = new HashMap<>();
    Map<String, Home> delHomes = new HashMap<>();

    Map<String, Waypoint> waypoints = new HashMap<>();

    public JmapEventListener(IClientAPI jmApi) {
        this.jmApi = jmApi;
    }

    private void syncDelWaypoints() {
        for (Home home : delHomes.values()) {
            Waypoint waypoint = waypoints.get(home.getName());
            homes.remove(home.getName());
            waypoints.remove(home.getName());
            jmApi.removeWaypoint(Constants.MOD_ID, waypoint);
        }

        delHomes.clear();
    }

    private void syncWaypoints() {
        for (Home home : homes.values()) {
            try {
                Waypoint waypoint = WaypointFactory.createClientWaypoint(Constants.MOD_ID,
                        new BlockPos(home.getX(), home.getY(), home.getZ()), home.getName(),
                        Minecraft.getInstance().level.dimension(), true);
                waypoints.put(home.getName(), waypoint);
                jmApi.addWaypoint(Constants.MOD_ID, waypoint);
            } catch (NullPointerException e) {
                Constants.LOG.error("Cannot create waypoint in null dimension for home: {}", home.getName());
            }
        }
    }

    @SubscribeEvent
    public void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").optional();
        registrar.playToClient(
                HomeDataPayload.TYPE,
                HomeDataPayload.STREAM_CODEC,
                this::handleHomeDataOnMain
        );

        registrar.playToClient(
                DelHomeDataPayload.TYPE,
                DelHomeDataPayload.STREAM_CODEC,
                this::handleDelHomeDataOnMain
        );
    }

    public void handleHomeDataOnMain(final HomeDataPayload payload, final IPayloadContext context) {
        var data = payload.data();
        Home home = new Home(data.x(), data.y(), data.z(), data.name());
        homes.put(data.name(), home);
        syncWaypoints();
    }

    public void handleDelHomeDataOnMain(final DelHomeDataPayload payload, final IPayloadContext context) {
        var data = payload.data();
        Home home = new Home(data.x(), data.y(), data.z(), data.name());
        delHomes.put(data.name(), home);
        syncDelWaypoints();
    }

}
