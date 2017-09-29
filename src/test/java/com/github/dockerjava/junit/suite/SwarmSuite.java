package com.github.dockerjava.junit.suite;

import com.github.dockerjava.junit.category.SwarmModeIntegration;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;

//@RunWith(Categories.class)
@Categories.IncludeCategory(SwarmModeIntegration.class)
public class SwarmSuite {
}
