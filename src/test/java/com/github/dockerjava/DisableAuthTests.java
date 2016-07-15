package com.github.dockerjava;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DisableAuthTests implements IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (Arrays.asList(annotation.getGroups()).contains("integration-auth")) {
            annotation.setEnabled(false);
        }
    }
}
