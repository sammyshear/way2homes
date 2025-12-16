package dev.sshear.payloads;

import dev.sshear.way2homes.Constants;
import dev.sshear.way2homes.HomeData;
import dev.sshear.way2homes.HomeDataCodec;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class HomePayloadSender {

    private static final ResourceLocation IDENTIFIER =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "home_data");

    public static void sendHomeData(
            org.bukkit.entity.Player player,
            HomeData data
    ) {
        ServerPlayer nms = ((CraftPlayer) player).getHandle();

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        writeHomeData(buf, data);

        //? if <1.21.4 {
        /*DiscardedPayload payload = new DiscardedPayload(IDENTIFIER, buf.asByteBuf());
         *///?} elif >=1.21.4 {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        DiscardedPayload payload = new DiscardedPayload(IDENTIFIER, bytes);
        //?}

        nms.connection.send(
                new ClientboundCustomPayloadPacket(payload)
        );
    }

    private static void writeHomeData(FriendlyByteBuf buf, HomeData data) {
        // Write string (name)
        byte[] nameBytes = data.name().getBytes(StandardCharsets.UTF_8);
        buf.writeVarInt(nameBytes.length);
        buf.writeBytes(nameBytes);

        // Write coordinates as varints
        buf.writeVarInt(data.x());
        buf.writeVarInt(data. y());
        buf.writeVarInt(data.z());
    }

}
