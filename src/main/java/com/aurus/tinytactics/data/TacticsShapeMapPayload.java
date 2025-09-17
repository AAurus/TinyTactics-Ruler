package com.aurus.tinytactics.data;

import com.aurus.tinytactics.TinyTactics;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TacticsShapeMapPayload(TacticsShapeMap map) implements CustomPayload {
    public static final Identifier TACTICS_SHAPE_MAP_PAYLOAD_ID = Identifier.of(TinyTactics.MOD_ID,
            "tactics_shape_map");
    public static final CustomPayload.Id<TacticsShapeMapPayload> ID = new Id<>(TACTICS_SHAPE_MAP_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, TacticsShapeMapPayload> CODEC = PacketCodec.tuple(
            TacticsShapeMap.PACKET_CODEC,
            TacticsShapeMapPayload::map, TacticsShapeMapPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
