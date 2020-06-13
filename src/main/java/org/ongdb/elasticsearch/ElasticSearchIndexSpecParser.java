package org.ongdb.elasticsearch;
/*
 *
 * Data Lab - graph database organization.
 *
 */

import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.singletonList;

/**
 * @author Yc-Ma
 * @PACKAGE_NAME: org.neo4j.elasticsearch.ElasticSearchIndexSpecParser
 * @Description: TODO
 * @date 2020/6/12 17:51
 */
public class ElasticSearchIndexSpecParser {

    private final static Logger LOGGER = Logger.getLogger(ElasticSearchIndexSpecParser.class.getName());
    /**
     * 支持匹配中文标签
     **/
    private final static Pattern INDEX_SPEC_RE = Pattern.compile("(?<indexname>[a-z][a-z_-\\u4e00-\\u9fa5]+):(?<label>[A-Za-z0-9_\\u4e00-\\u9fa5]+)\\((?<props>[^\\)]+)\\)");
    private final static Pattern PROPS_SPEC_RE = Pattern.compile("((?!=,)([A-Za-z0-9_]+))+");

    public static Map<String, List<ElasticSearchIndexSpec>> parseIndexSpec(String spec) throws ParseException {
        if (spec == null) {
            return Collections.emptyMap();
        }
        Map<String, List<ElasticSearchIndexSpec>> map = new LinkedHashMap<>();
        Matcher matcher = INDEX_SPEC_RE.matcher(spec);
        while (matcher.find()) {

            Matcher propsMatcher = PROPS_SPEC_RE.matcher(matcher.group("props"));
            Set<String> props = new HashSet<>();
            while (propsMatcher.find()) {
                props.add(propsMatcher.group());
            }

            String label = matcher.group("label");

            if (map.containsKey(label)) {
                LOGGER.info("Defining twice the index will be overwritten");
                throw new ParseException(matcher.group(), 0);
            }
            map.put(label, singletonList(new ElasticSearchIndexSpec(matcher.group("indexname"), props)));
        }
        return map;
    }

}
