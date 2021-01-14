package com.lsdb.store.graph;

import java.util.UUID;

public class TestUtil {

    public static String TEST_VERTEX_LABEL = "person";
    public static String TEST_EDGE_LABEL = "knows";
    public static String TEST_VERTEX_PROP_KEY = "name";
    public static String TEST_VERTEX_0_PROP_VAL = "alice";
    public static String TEST_VERTEX_1_PROP_VAL = "bob";
    public static String TEST_EDGE_PROP_KEY = "year met";
    public static String TEST_EDGE_0_PROP_VAL = "2009";

    public static Vertex newTestVertex() {

        final UUID id = Element.generateId();
        final String label = TEST_VERTEX_LABEL;
        return new Vertex(id, label);
    }

    public static Edge newTestEdge() {

        final UUID id = Element.generateId();
        final String label = TEST_EDGE_LABEL;
        return new Edge(id, label);
    }
}
