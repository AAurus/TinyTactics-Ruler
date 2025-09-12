package com.aurus.tinytactics.data;

import com.aurus.tinytactics.TinyTactics;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TacticsRulerMapPayload(TacticsRulerMap map) implements CustomPayload {
    public static final Identifier TACTICS_RULER_MAP_PAYLOAD_ID = Identifier.of(TinyTactics.MOD_ID,
            "tactics_ruler_map");
    public static final CustomPayload.Id<TacticsRulerMapPayload> ID = new Id<>(TACTICS_RULER_MAP_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, TacticsRulerMapPayload> CODEC = PacketCodec.tuple(
            TacticsRulerMap.PACKET_CODEC,
            TacticsRulerMapPayload::map, TacticsRulerMapPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
