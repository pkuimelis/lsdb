package com.lsdb.store.graph;

import java.util.List;

public class Harness {

    private List<Vertex> vertexList;
    private List<Edge> edgeList;

    public List<Edge> getEdgeList() { return edgeList; }

    public List<Vertex> getVertexList() { return vertexList; }

    public void setVertexList(List<Vertex> vertexList) { this.vertexList = vertexList; }

    public void setEdgeList(List<Edge> edgeList) { this.edgeList = edgeList; }
}
