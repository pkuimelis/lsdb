###LSDB: trippy-fast graph database

LSDB is a property-graph database influenced by [Neo4j](http://https://neo4j.com/) and [CruxDB](https://github.com/juxt/crux).

LSDB is made up of two services: LSDB-compute and LSDB-store. These services communicate over gRPC.

LSDB-compute parses, plans and executes [CYPHER](https://neo4j.com/docs/cypher-manual/current/) queries. Query plans are composed of internal and leaf (requiring access to underlying vertices and edges) nodes. To evaluate leaf nodes, LSDB-compute contacts LSDB-store and requests the necessary data.
In other words, LSDB-store can be thought of as roughly implementing the operations in Neo4j which incur [database hits](https://neo4j.com/docs/cypher-manual/current/execution-plans/db-hits/).

For storage, LSDB-store uses [LMDB](http://www.lmdb.tech/doc/), a high-performance, transactional key value storage engine implemented as a memory mapped B-Tree.

LSDB-store should have utility as a standalone service for projects which require a storage engine with property-graph semantics but not the features of a full-blown database.