package dev.sshear.way2homes.payloads;

import dev.sshear.way2homes.Constants;
import dev.sshear.way2homes.HomeData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//? if <1.21.11 {
import net.minecraft.resources.ResourceLocation;
 //?} elif >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?}

import java.nio.charset.StandardCharsets;

public record DelHomeDataPayload(HomeData data)
        implements CustomPacketPayload {

    //? if <1.21.11 {
    public static final Type<DelHomeDataPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "del_home_data"));
     //?} elif >=1.21.11 {
    /*public static final Type<DelHomeDataPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "del_home_data"));
    *///?}

    public static final StreamCodec<RegistryFriendlyByteBuf, DelHomeDataPayload>
            STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public DelHomeDataPayload decode(RegistryFriendlyByteBuf buf) {
                    // Read directly from FriendlyByteBuf
                    HomeData data = readHomeData(buf);
                    return new DelHomeDataPayload(data);
                }

                @Override
                public void encode(
                        RegistryFriendlyByteBuf buf,
                        DelHomeDataPayload payload
                ) {
                    writeHomeData(buf, payload.data());
                }
            };

    private static HomeData readHomeData(RegistryFriendlyByteBuf buf) {
        // Read string (name)
        int nameLength = buf.readVarInt();
        byte[] nameBytes = new byte[nameLength];
        buf.readBytes(nameBytes);
        String name = new String(nameBytes, StandardCharsets.UTF_8);

        // Read coordinates
        int x = buf.readVarInt();
        int y = buf.readVarInt();
        int z = buf.readVarInt();

        return new HomeData(name, x, y, z);
    }

    private static void writeHomeData(RegistryFriendlyByteBuf buf, HomeData data) {
        // Write string (name)
        byte[] nameBytes = data.name().getBytes(StandardCharsets.UTF_8);
        buf.writeVarInt(nameBytes.length);
        buf.writeBytes(nameBytes);

        // Write coordinates
        buf.writeVarInt(data.x());
        buf.writeVarInt(data.y());
        buf.writeVarInt(data.z());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
