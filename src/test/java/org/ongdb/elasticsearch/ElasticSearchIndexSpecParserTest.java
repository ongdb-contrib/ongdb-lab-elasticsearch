package org.ongdb.elasticsearch;
/*
 *
 * Data Lab - graph database organization.
 *
 */
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.ongdb.elasticsearch.ElasticSearchIndexSpecParser.parseIndexSpec;

public class ElasticSearchIndexSpecParserTest {

    @Test
    public void testParseIndexSpec() throws ParseException {
        Map<String, List<ElasticSearchIndexSpec>> rv =
                parseIndexSpec("index_name:Label(foo,bar,quux),other_index_name:OtherLabel(baz,quuxor)");
        assertEquals(2, rv.size());
        assertEquals(new HashSet<>(asList("Label", "OtherLabel")), rv.keySet());
    }

    @Test
    public void testIndexSpecBadSyntax() throws ParseException {
        Map rv = parseIndexSpec("index_name:Label(foo,bar");
        assertEquals(0, rv.size());
        rv = parseIndexSpec("index_name:Label");
        assertEquals(0, rv.size());
        rv = parseIndexSpec("Label");
        assertEquals(0, rv.size());
    }

    @Test(expected=ParseException.class)
    public void testIndexSpecBadSyntaxDuplicateIndex() throws ParseException {
    	Map rv = parseIndexSpec("index_name:Label(foo,bar),index_name:Label(quux)");
    }

    @Test
    public void testCnParseIndexSpec() throws ParseException{
        Map<String, List<ElasticSearchIndexSpec>> rv =
                parseIndexSpec("preorgcnnode:PRE公司中文名称(name,hcode,pcode,hupdatetime,cluster_id),places:Place(name)");
        assertEquals(2, rv.size());
        assertEquals(new HashSet<>(asList("PRE公司中文名称", "Place")), rv.keySet());
    }
}

