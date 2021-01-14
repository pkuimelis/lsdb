package com.lsdb.store.graph;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lsdb.store.serialization.VertexProto;

import java.util.*;
import java.util.stream.Collectors;

public class Vertex extends Element {

    private List<UUID> incomingEdgeIds;
    private List<UUID> outgoingEdgeIds;

    public Vertex(UUID id, String label) { super(id, label); }

    public Vertex(UUID id, String label, List<UUID> incomingEdgeIds, List<UUID> outgoingEdgeIds) {

        super(id, label);
        this.incomingEdgeIds = incomingEdgeIds;
        this.outgoingEdgeIds = outgoingEdgeIds;
    }

    public Vertex(UUID id, String label, HashMap<String, String> properties,
                  List<UUID> incomingEdgeIds, List<UUID> outgoingEdgeIds) {

        super(id, label, properties);
        this.incomingEdgeIds = incomingEdgeIds;
        this.outgoingEdgeIds = outgoingEdgeIds;
    }

    public List<UUID> getIncomingEdgeIds() {
        return incomingEdgeIds;
    }

    public void setIncomingEdgeIds(List<UUID> incomingEdgeIds) {
        this.incomingEdgeIds = incomingEdgeIds;
    }

    public List<UUID> getOutgoingEdgeIds() {
        return outgoingEdgeIds;
    }

    public void setOutgoingEdgeIds(List<UUID> outgoingEdgeIds) {
        this.outgoingEdgeIds = outgoingEdgeIds;
    }

    public static class Builder extends Element.Builder<Builder> {

        private List<UUID> incomingEdgeIds;
        private List<UUID> outgoingEdgeIds;

        @Override
        public Vertex.Builder me() {
            return this;
        }

        public Builder incomingEdgeIds(final List<UUID> incomingEdgeIds) {
            this.incomingEdgeIds = incomingEdgeIds;
            return this;
        }

        public Builder outgoingEdgeIds(final List<UUID> outgoingEdgeIds) {
            this.outgoingEdgeIds = outgoingEdgeIds;
            return this;
        }

        public Vertex build() {
            return new Vertex(this.id, this.label, this.properties, incomingEdgeIds, outgoingEdgeIds);
        }
    }

    public byte[] serialize() {

        final VertexProto.Vertex vertexMessage = VertexProto.Vertex.newBuilder()
                .setId(getId().toString())
                .setLabel(getLabel())
                .putAllProperties(getProperties())
                .addAllIncomingEdgeIds(Optional.ofNullable(getIncomingEdgeIds()).orElseGet(Collections::emptyList)
                        .stream()
                        .map(UUID::toString)
                        .collect(Collectors.toList()))
                .addAllOutgoingEdgeIds(Optional.ofNullable(getOutgoingEdgeIds()).orElseGet(Collections::emptyList)
                        .stream()
                        .map(UUID::toString)
                        .collect(Collectors.toList()))
                .build();
        return vertexMessage.toByteArray();
    }

    public static Vertex deserialize(byte[] serialized) throws InvalidProtocolBufferException {

        final VertexProto.Vertex vertexMessage = VertexProto.Vertex.parseFrom(serialized);
        final UUID id = UUID.fromString(vertexMessage.getId());
        final String label = vertexMessage.getLabel();
        final HashMap<String, String> properties = new HashMap<>(vertexMessage.getPropertiesMap());
        final List<UUID> incomingEdgeIds = vertexMessage.getIncomingEdgeIdsList()
                .stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());
        final List<UUID> outgoingEdgeIds = vertexMessage.getOutgoingEdgeIdsList()
                .stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());

        return new Vertex.Builder()
                .id(id)
                .label(label)
                .properties(properties)
                .incomingEdgeIds(incomingEdgeIds)
                .outgoingEdgeIds(outgoingEdgeIds)
                .build();
    }
}