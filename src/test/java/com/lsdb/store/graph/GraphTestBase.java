package com.lsdb.store.graph;

import org.junit.Before;

import java.util.Arrays;

public class GraphTestBase {

    protected Harness harness;

    @Before
    public void setUpHarness() {

        harness = new Harness();

        Vertex v0 = TestUtil.newTestVertex();
        Vertex v1 = TestUtil.newTestVertex();
        Edge e0 = TestUtil.newTestEdge();

        v0.setProperty(TestUtil.TEST_VERTEX_PROP_KEY, TestUtil.TEST_VERTEX_0_PROP_VAL);
        v1.setProperty(TestUtil.TEST_VERTEX_PROP_KEY, TestUtil.TEST_VERTEX_1_PROP_VAL);
        e0.setProperty(TestUtil.TEST_EDGE_PROP_KEY, TestUtil.TEST_EDGE_0_PROP_VAL);

        e0.setHeadVertexId(v0.getId());
        e0.setTailVertexId(v1.getId());
        v0.setIncomingEdgeIds(Arrays.asList(e0.getId()));
        v1.setOutgoingEdgeIds(Arrays.asList(e0.getId()));

        harness.setVertexList(Arrays.asList(v0, v1));
        harness.setEdgeList(Arrays.asList(e0));
    }

    protected Vertex getVertex0() { return harness.getVertexList().get(0); }

    protected Vertex getVertex1() { return harness.getVertexList().get(1); }

    protected Edge getEdge0() { return harness.getEdgeList().get(0); }
}
