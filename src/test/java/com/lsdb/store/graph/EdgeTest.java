package com.lsdb.store.graph;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class EdgeTest extends GraphTestBase {

    @Test
    public void serialization() throws InvalidProtocolBufferException {

        Edge e0 = getEdge0();

        final UUID id = e0.getId();
        final String label = e0.getLabel();
        final String yearMet = e0.getProperty(TestUtil.TEST_EDGE_PROP_KEY);

        byte[] serialized = e0.serialize();
        e0 = Edge.deserialize(serialized);

        Assert.assertEquals(id, e0.getId());
        Assert.assertEquals(label, e0.getLabel());
        Assert.assertEquals(yearMet, e0.getProperty(TestUtil.TEST_EDGE_PROP_KEY));
    }
}