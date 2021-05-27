package com.github.dockerjava.api.model;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateConfigResponse;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class DockerObjectArchTest {

    static JavaClasses CLASSES = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .importPackagesOf(
            Container.class,
            CreateConfigResponse.class
        );

    @Test
    void modelClassMustExtendDockerObject() {
        classes()
            .that().areNotEnums()
            .and().areNotInterfaces()
            .and().areNotAnnotatedWith(Deprecated.class)
            .and().doNotImplement(ResultCallback.class)
            .and().doNotImplement(DockerCmdExecFactory.class)
            .and().doNotBelongToAnyOf(DockerObjectAccessor.class, Binds.class, Links.class, VolumesFrom.class)
            .should().beAssignableTo(DockerObject.class)
            .check(CLASSES);
    }
}
