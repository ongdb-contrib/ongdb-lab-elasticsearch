package org.ongdb.elasticsearch;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.config.Setting;
import org.neo4j.graphdb.factory.Description;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.extension.ExtensionType;
import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.impl.spi.KernelContext;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.ongdb.properties.OngdbProperties;

import static org.neo4j.kernel.configuration.Settings.*;

public class ElasticSearchKernelExtensionFactory extends KernelExtensionFactory<ElasticSearchKernelExtensionFactory.Dependencies> {

    public static final String SERVICE_NAME = "ELASTIC_SEARCH";

    private static boolean TEST_LOAD_INDEX_SPEC = false;

    public static void setTestLoadIndexSpec(boolean testLoadIndexSpec) {
        TEST_LOAD_INDEX_SPEC = testLoadIndexSpec;
    }

    @Description("Settings for the Elastic Search Extension")
    public static abstract class ElasticSearchSettings {
        public static Setting<String> hostName = setting("elasticsearch.host_name", STRING, (String) null);
        public static Setting<String> indexSpec = setting("elasticsearch.index_spec", STRING, (String) null);
        public static Setting<Boolean> discovery = setting("elasticsearch.discovery", BOOLEAN, "false");
        public static Setting<Boolean> includeIDField = setting("elasticsearch.include_id_field", BOOLEAN, "true");
        public static Setting<Boolean> includeLabelsField = setting("elasticsearch.include_labels_field", BOOLEAN, "true");
        // todo settings for label, property, indexName
    }

    public ElasticSearchKernelExtensionFactory() {
        super(ExtensionType.DATABASE, SERVICE_NAME);
    }

    @Override
    public Lifecycle newInstance(KernelContext kernelContext, Dependencies dependencies) {
        Config config = dependencies.getConfig();

        System.out.println("elasticsearch.host_name:" + config.get(ElasticSearchSettings.hostName));
        System.out.println("elasticsearch.discovery:" + config.get(ElasticSearchSettings.discovery));
        System.out.println("elasticsearch.include_id_field:" + config.get(ElasticSearchSettings.includeIDField));
        System.out.println("elasticsearch.include_labels_field:" + config.get(ElasticSearchSettings.includeLabelsField));

        String indexSpec = OngdbProperties.getConfigurationByKey("elasticsearch.index_spec");
        indexSpec = config.get(ElasticSearchSettings.hostName) == null ? null : indexSpec;
        if (TEST_LOAD_INDEX_SPEC) {
            indexSpec = config.get(ElasticSearchSettings.indexSpec);
        }
        System.out.println("elasticsearch.index_spec:" + indexSpec);

        return new ElasticSearchExtension(dependencies.getGraphDatabaseService(),
                config.get(ElasticSearchSettings.hostName),
                indexSpec,
                config.get(ElasticSearchSettings.discovery),
                config.get(ElasticSearchSettings.includeIDField),
                config.get(ElasticSearchSettings.includeLabelsField));
    }

    public interface Dependencies {
        GraphDatabaseService getGraphDatabaseService();

        Config getConfig();
    }
}

