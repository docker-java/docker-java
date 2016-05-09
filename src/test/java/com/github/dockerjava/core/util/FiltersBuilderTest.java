package com.github.dockerjava.core.util;

import com.google.common.collect.Maps;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * @author Vincent Latombe <vincent@latombe.net>
 */
public class FiltersBuilderTest {

    @Test
    public void newFiltersShouldBeEquals() {
        assertEquals(new FiltersBuilder(), new FiltersBuilder());
    }

    @Test
    public void newFiltersShouldHaveEqualHashcode() {
        assertEquals(new FiltersBuilder().hashCode(), new FiltersBuilder().hashCode());
    }

    @Test
    public void filtersWithEqualContentShouldBeEquals() {
        assertEquals(new FiltersBuilder().withContainers("foo"), new FiltersBuilder().withContainers("foo"));
        assertEquals(new FiltersBuilder().withLabels("alpha=val"), new FiltersBuilder().withLabels("alpha=val"));
    }

    @Test
    public void filtersWithEqualContentShouldHaveEqualHashcode() {
        assertEquals(new FiltersBuilder().withContainers("foo").hashCode(), new FiltersBuilder().withContainers("foo").hashCode());
        assertEquals(new FiltersBuilder().withLabels("alpha=val").hashCode(), new FiltersBuilder().withLabels("alpha=val").hashCode());
    }

    @Test
    public void withLabelsMapShouldBeEqualsToVarargs() {
        Map<String, String> map = Maps.newHashMap();
        map.put("alpha", "val");
        assertEquals(new FiltersBuilder().withLabels("alpha=val"), new FiltersBuilder().withLabels(map));

        map = Maps.newHashMap();
        map.put("alpha", "val");
        map.put("beta", "val1");
        assertEquals(new FiltersBuilder().withLabels("alpha=val", "beta=val1"), new FiltersBuilder().withLabels(map));
    }

    @Test
    public void filtersWithDifferentContentShouldntBeEquals() {
        assertNotEquals(new FiltersBuilder().withContainers("foo"), new FiltersBuilder().withContainers("bar"));
    }

    @Test
    public void filtersWithDifferentContentShouldntHaveEqualHashcode() {
        assertNotEquals(new FiltersBuilder().withContainers("foo").hashCode(), new FiltersBuilder().withContainers("bar").hashCode());
    }
}
