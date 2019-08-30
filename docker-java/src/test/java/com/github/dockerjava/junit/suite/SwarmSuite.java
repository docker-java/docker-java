package com.github.dockerjava.junit.suite;

import com.github.dockerjava.junit.category.SwarmModeIntegration;
import org.junit.experimental.categories.Categories;

//@RunWith(Categories.class)
@Categories.IncludeCategory(SwarmModeIntegration.class)
public class SwarmSuite {
}
