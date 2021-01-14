package com.lsdb.store.graph;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lsdb.store.serialization.EdgeProto;

import java.util.HashMap;
import java.util.UUID;

public class Edge extends Element {

    private UUID headVertexId;
    private UUID tailVertexId;

    public Edge(UUID id, String label) { super(id, label); }

    public Edge(UUID id, String label, UUID headVertexId, UUID tailVertexId) {

        super(id, label);
        this.headVertexId = headVertexId;
        this.tailVertexId = tailVertexId;
    }

    public Edge(UUID id, String label, HashMap<String, String> properties, UUID headVertexId, UUID tailVertexId) {

        super(id, label, properties);
        this.headVertexId = headVertexId;
        this.tailVertexId = tailVertexId;
    }

    public UUID getHeadVertexId() {
        return headVertexId;
    }

    public void setHeadVertexId(UUID headVertexId) {
        this.headVertexId = headVertexId;
    }

    public UUID getTailVertexId() {
        return tailVertexId;
    }

    public void setTailVertexId(UUID tailVertexId) {
        this.tailVertexId = tailVertexId;
    }

    public static class Builder extends Element.Builder<Builder> {

        private UUID headVertexId;
        private UUID tailVertexId;

        @Override
        public Builder me() {
            return this;
        }

        public Builder headVertexId(final UUID headVertexId) {
            this.headVertexId = headVertexId;
            return this;
        }

        public Builder tailVertexId(final UUID tailVertexId) {
            this.tailVertexId = tailVertexId;
            return this;
        }

        public Edge build () {
            return new Edge(this.id, this.label, this.properties, headVertexId, tailVertexId);
        }
    }

    public byte[] serialize() {

        final EdgeProto.Edge edgeMessage = EdgeProto.Edge.newBuilder()
                .setId(getId().toString())
                .setLabel(getLabel())
                .putAllProperties(getProperties())
                .setHeadVertexId(getHeadVertexId().toString())
                .setTailVertexId(getTailVertexId().toString())
                .build();
        return edgeMessage.toByteArray();
    }

    public static Edge deserialize(byte[] serialized) throws InvalidProtocolBufferException {

        final EdgeProto.Edge edgeMessage = EdgeProto.Edge.parseFrom(serialized);
        final UUID id = UUID.fromString(edgeMessage.getId());
        final String label = edgeMessage.getLabel();
        final HashMap<String, String> properties = new HashMap<>(edgeMessage.getPropertiesMap());
        final UUID headVertexId = UUID.fromString(edgeMessage.getHeadVertexId());
        final UUID tailVertexId = UUID.fromString(edgeMessage.getTailVertexId());

        return new Edge.Builder()
                .id(id)
                .label(label)
                .properties(properties)
                .headVertexId(headVertexId)
                .tailVertexId(tailVertexId)
                .build();
    }
}