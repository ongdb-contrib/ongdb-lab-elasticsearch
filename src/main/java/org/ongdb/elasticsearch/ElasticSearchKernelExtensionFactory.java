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

import java.util.logging.Logger;

import static org.neo4j.kernel.configuration.Settings.*;

public class ElasticSearchKernelExtensionFactory extends KernelExtensionFactory<ElasticSearchKernelExtensionFactory.Dependencies> {

    private final static Logger LOGGER = Logger.getLogger(ElasticSearchKernelExtensionFactory.class.getName());

    public static final String SERVICE_NAME = "ELASTIC_SEARCH";

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
        System.out.println("elasticsearch.index_spec:" +config.get(ElasticSearchSettings.indexSpec));
        System.out.println("elasticsearch.discovery:" + config.get(ElasticSearchSettings.discovery));
        System.out.println("elasticsearch.include_id_field:" + config.get(ElasticSearchSettings.includeIDField));
        System.out.println("elasticsearch.include_labels_field:" + config.get(ElasticSearchSettings.includeLabelsField));

        return new ElasticSearchExtension(dependencies.getGraphDatabaseService(),
                config.get(ElasticSearchSettings.hostName),
                config.get(ElasticSearchSettings.indexSpec),
                config.get(ElasticSearchSettings.discovery),
                config.get(ElasticSearchSettings.includeIDField),
                config.get(ElasticSearchSettings.includeLabelsField));
    }

    public interface Dependencies {
        GraphDatabaseService getGraphDatabaseService();

        Config getConfig();
    }
}

