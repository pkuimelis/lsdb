[![pkuimelis](https://circleci.com/gh/pkuimelis/lsdb.svg?style=svg)](https://circleci.com/gh/pkuimelis/lsdb)

### LSDB: trippy-fast graph database

LSDB is a [property-graph database](https://en.wikipedia.org/wiki/Graph_database#Labeled-property_graph) influenced by [Neo4j](https://neo4j.com/) and [CruxDB](https://github.com/juxt/crux).

LSDB is made up of two services: LSDB-compute and LSDB-store. These services communicate using the gRPC protocol.

LSDB-compute parses, plans and executes [CYPHER](https://neo4j.com/docs/cypher-manual/current/) queries. LSDB query plans are composed of internal and leaf (requiring access to underlying vertices and edges) nodes. To evaluate leaf nodes, LSDB-compute contacts LSDB-store and requests the necessary data.
In other words, the LSDB-store service API roughly implements the operations in Neo4j that incur [database hits](https://neo4j.com/docs/cypher-manual/current/execution-plans/db-hits/).

For storage, LSDB-store uses [LMDB](http://www.lmdb.tech/doc/), a high-performance, transactional key value storage engine implemented as a memory mapped B-Tree. LMDB was chosen specifically for its unmatched read performance on spinning disk drives.

LSDB-store should have utility as a standalone service for projects which require a storage engine with property-graph semantics but not the features of a full-blown database.
