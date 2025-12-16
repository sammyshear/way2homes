package dev.sshear.payloads;

import dev.sshear.way2homes.Constants;
import dev.sshear.way2homes.HomeData;
import dev.sshear.way2homes.HomeDataCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record HomeDataPayload(HomeData data)
        implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "home_data");

    public static final Type<HomeDataPayload> TYPE =
            new Type<>(ID);

    /**
     * Dummy codec – never used on the server.
     */
    public static final StreamCodec<FriendlyByteBuf, HomeDataPayload>
            STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public HomeDataPayload decode(FriendlyByteBuf buf) {
                    return new HomeDataPayload(
                            HomeDataCodec.read(buf.nioBuffer())
                    );                }

                @Override
                public void encode(
                        FriendlyByteBuf buf,
                        HomeDataPayload payload
                ) {
                    HomeDataCodec.write(
                            buf.nioBuffer(),
                            payload.data()
                    );
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}