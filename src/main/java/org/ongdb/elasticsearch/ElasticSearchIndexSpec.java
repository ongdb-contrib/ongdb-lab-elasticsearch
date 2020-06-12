package org.ongdb.elasticsearch;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import java.util.Set;

class ElasticSearchIndexSpec {

    private String indexName;
	private Set<String> properties;
    
    public ElasticSearchIndexSpec(String indexName, Set<String> properties) {
        this.indexName = indexName;
        this.properties = properties;
    }
    
    public String getIndexName() {
		return indexName;
	}
    
    public Set<String> getProperties() {
		return properties;
	}

	@Override
    public String toString() {
        String s = this.getClass().getSimpleName() + " " + indexName + ": (";
        for (String p: properties) {
            s += p + ",";
        }
        s += ")";
        return s;
    }
}
