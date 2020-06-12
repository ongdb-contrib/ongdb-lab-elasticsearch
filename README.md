## ONgDB Elastic{Search} Integration

Integrates ONgDB change-feed with an ElasticSearch cluster.

The different versions of ONgDB (3.5.x are supported on different branches).

### Approach

This Neo4j Kernel Extension updates an ElasticSearch instance or cluster with changes in the graph.

A transaction event listener checks changed Nodes against a given label, renders the whole node as json document and indexes all changes in bulk with ES.

### Installation

* Download the jar from the [latest release].
* Copy to `$NEO4J_HOME/plugins` or for Neo4j community to the plugins folder that you find on the `Options` pane.
* Modify `$NEO4J_HOME/conf/neo4j.conf` accordingly (see the Example section)
* Restart Neo4j

### Example

Suppose that we keep nodes in our Neo4j instance labeled `Person` and
`Place`, and that we want to index the values of the `first_name` and
`last_name` properties of the former and `name` of the latter in two
separate ElasticSearch indices named `people` and `places`. For that,
we would add the following directives to `conf/neo4j.conf`:

```
elasticsearch.host_name=http://localhost:9200
elasticsearch.index_spec=people:Person(first_name,last_name), places:Place(name)
```

With that in place, Neo4j will now track changes to nodes labeled
`Person` or `Place` and keep our ES instance running on
`localhost:9200` in sync.

To perform an initial import, you can force a commit by executing a
Cypher query like:

```
MATCH n:Person
SET n.first_name = n.first_name, n.last_name = n.last_name;

MATCH n:Place
SET n.name = n.name;
```

### ID / Labels fields
By default, the indexes created will contain fields for the Neo4j ID and Labels, named `id` and `labels`. 
These will be auto-created as searchable fields, but, if you'd prefer they not be included,
simply add one or both of these lines to your `conf/neo4j.conf` file.

```
elasticsearch.include_id_field=false 
elasticsearch.include_labels_field=false
```

### Discovery
By default discovery (discovering of nodes within a cluster) is turned off.
If you would like to turn discovery on, use the discovery option.

```
elasticsearch.discovery=true
```

### Developing

To run the tests, run `mvn test`. Make sure that an elastic{search} server is running on
`localhost:9200`.

### Todo

* Support indexing of relationships
