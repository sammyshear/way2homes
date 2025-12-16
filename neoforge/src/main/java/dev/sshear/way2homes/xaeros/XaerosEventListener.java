package dev.sshear.way2homes.xaeros;

import dev.sshear.way2homes.Home;
import dev.sshear.way2homes.payloads.DelHomeDataPayload;
import dev.sshear.way2homes.payloads.HomeDataPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.waypoint.WaypointColor;

import java.util.HashMap;
import java.util.Map;

public class XaerosEventListener {

    Map<String, Home> homes = new HashMap<>();
    Map<String, Home> delHomes = new HashMap<>();

    @SubscribeEvent
    public void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
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

    private void syncWaypoints() {
        for (Home home : homes.values()) {
            var mgr = BuiltInHudModules.MINIMAP.getCurrentSession().getWorldManager();

            var waypointSet = mgr.getCurrentWorld().getCurrentWaypointSet();
            var waypoint = new Waypoint(home.getX(), home.getY(), home.getZ(), home.getName(), home.getName().substring(0, 1),
                    WaypointColor.GRAY);
            waypointSet.add(waypoint);
        }
    }

    public void handleDelHomeDataOnMain(final DelHomeDataPayload payload, final IPayloadContext context) {
        var data = payload.data();
        Home home = new Home(data.x(), data.y(), data.z(), data.name());
        homes.remove(home.getName());
        delHomes.put(data.name(), home);
        syncDelWaypoints();
    }

    private void syncDelWaypoints() {
        for (Home home : delHomes.values()) {
            var mgr = BuiltInHudModules.MINIMAP.getCurrentSession().getWorldManager();

            var waypointSet = mgr.getCurrentWorld().getCurrentWaypointSet();
            for (Waypoint waypoint : waypointSet.getWaypoints()) {
                if (waypoint.getName().equals(home.getName()) && waypoint.getX() == home.getX()
                        && waypoint.getY() == home.getY() && waypoint.getZ() == home.getZ()) {
                    waypointSet.remove(waypoint);
                    break;
                }
            }
        }
        delHomes.clear();
    }
}
