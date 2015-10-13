package com.github.dockerjava.api.model;

import com.google.common.collect.Maps;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

/**
 * @author Vincent Latombe <vincent@latombe.net>
 */
public class FiltersTest {

    @Test
    public void newFiltersShouldBeEquals() {
        assertEquals(new Filters(), new Filters());
    }

    @Test
    public void filtersWithEqualContentShouldBeEquals() {
        assertEquals(new Filters().withContainers("foo"), new Filters().withContainers("foo"));
        assertEquals(new Filters().withLabels("alpha=val"), new Filters().withLabels("alpha=val"));
    }

    @Test
    public void withLabelsMapShouldBeEquivalentToVarargs() {
        Map<String, String> map = Maps.newHashMap();
        map.put("alpha", "val");
        assertEquals(new Filters().withLabels("alpha=val"), new Filters().withLabels(map));

        map = Maps.newHashMap();
        map.put("alpha", "val");
        map.put("beta", "val1");
        assertEquals(new Filters().withLabels("alpha=val", "beta=val1"), new Filters().withLabels(map));
    }

    @Test
    public void filtersWithDifferentContentShouldntBeEquals() {
        assertNotSame(new Filters().withContainers("foo"), new Filters().withContainers("bar"));
    }
}
