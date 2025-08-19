package com.aurus.tinytactics.components;

import com.aurus.tinytactics.TinyTactics;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RulerMapPayload(RulerMap map) implements CustomPayload {
    public static final Identifier BLOCK_POS_MAP_PAYLOAD_ID = Identifier.of(TinyTactics.MOD_ID, "block_pos_map");
    public static final CustomPayload.Id<RulerMapPayload> ID = new Id<>(BLOCK_POS_MAP_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, RulerMapPayload> CODEC = PacketCodec.tuple(RulerMap.PACKET_CODEC, RulerMapPayload::map, RulerMapPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}