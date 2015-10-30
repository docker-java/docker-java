package com.github.dockerjava.api.model;

import com.google.common.collect.Maps;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author Vincent Latombe <vincent@latombe.net>
 */
public class FiltersTest {

    @Test
    public void newFiltersShouldBeEquals() {
        Assert.assertEquals(new Filters(), new Filters());
    }

    @Test
    public void newFiltersShouldHaveEqualHashcode() {
        Assert.assertEquals(new Filters().hashCode(), new Filters().hashCode());
    }

    @Test
    public void filtersWithEqualContentShouldBeEquals() {
        Assert.assertEquals(new Filters().withContainers("foo"), new Filters().withContainers("foo"));
        Assert.assertEquals(new Filters().withLabels("alpha=val"), new Filters().withLabels("alpha=val"));
    }

    @Test
    public void filtersWithEqualContentShouldHaveEqualHashcode() {
        Assert.assertEquals(new Filters().withContainers("foo").hashCode(), new Filters().withContainers("foo").hashCode());
        Assert.assertEquals(new Filters().withLabels("alpha=val").hashCode(), new Filters().withLabels("alpha=val").hashCode());
    }

    @Test
    public void withLabelsMapShouldBeEqualsToVarargs() {
        Map<String, String> map = Maps.newHashMap();
        map.put("alpha", "val");
        Assert.assertEquals(new Filters().withLabels("alpha=val"), new Filters().withLabels(map));

        map = Maps.newHashMap();
        map.put("alpha", "val");
        map.put("beta", "val1");
        Assert.assertEquals(new Filters().withLabels("alpha=val", "beta=val1"), new Filters().withLabels(map));
    }

    @Test
    public void filtersWithDifferentContentShouldntBeEquals() {
        Assert.assertNotEquals(new Filters().withContainers("foo"), new Filters().withContainers("bar"));
    }

    @Test
    public void filtersWithDifferentContentShouldntHaveEqualHashcode() {
        Assert.assertNotEquals(new Filters().withContainers("foo").hashCode(), new Filters().withContainers("bar").hashCode());
    }
}
