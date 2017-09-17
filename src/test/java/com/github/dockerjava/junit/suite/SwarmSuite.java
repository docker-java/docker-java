package com.github.dockerjava.junit.suite;

import com.github.dockerjava.junit.category.SwarmIntegration;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;

@RunWith(Categories.class)
@Categories.IncludeCategory(SwarmIntegration.class)
public class SwarmSuite {
}
