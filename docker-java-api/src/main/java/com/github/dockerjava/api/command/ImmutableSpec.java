package com.github.dockerjava.api.command;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Value.Style(
        jdkOnly = true,

        defaultAsDefault = true,

        get = {"get*", "is*"},

        depluralize = true,
        depluralizeDictionary = {
                "alias:aliases",
        },

        typeImmutable = "*Spec",

        visibility = Value.Style.ImplementationVisibility.PUBLIC
)
@interface ImmutableSpec {

}
