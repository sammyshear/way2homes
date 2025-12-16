package dev.sshear.way2homes.payloads;

import dev.sshear.way2homes.Constants;
import dev.sshear.way2homes.HomeData;
import dev.sshear.way2homes.HomeDataCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.nio.ByteBuffer;

public record DelHomeDataPayload(HomeData data)
        implements CustomPacketPayload {

    public static final Type<DelHomeDataPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "del_home_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, DelHomeDataPayload>
            STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public DelHomeDataPayload decode(RegistryFriendlyByteBuf buf) {
                    ByteBuffer nio = buf.nioBuffer();
                    HomeData data = HomeDataCodec.read(nio);
                    return new DelHomeDataPayload(data);
                }

                @Override
                public void encode(
                        RegistryFriendlyByteBuf buf,
                        DelHomeDataPayload payload
                ) {
                    ByteBuffer nio = buf.nioBuffer();
                    HomeDataCodec.write(nio, payload.data());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
