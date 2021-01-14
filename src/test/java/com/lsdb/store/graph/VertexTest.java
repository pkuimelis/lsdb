package com.lsdb.store.graph;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class VertexTest extends GraphTestBase {

    @Test
    public void serialization() throws InvalidProtocolBufferException {

        Vertex v0 = getVertex0();

        final UUID id = v0.getId();
        final String label = v0.getLabel();
        final String name = v0.getProperty(TestUtil.TEST_VERTEX_PROP_KEY);

        byte[] serialized = v0.serialize();
        v0 = Vertex.deserialize(serialized);

        Assert.assertEquals(id, v0.getId());
        Assert.assertEquals(label, v0.getLabel());
        Assert.assertEquals(name, v0.getProperty(TestUtil.TEST_VERTEX_PROP_KEY));
    }
}