package com.github.dockerjava.api;

import com.github.dockerjava.api.model.Binds;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.api.model.ResponseItem;
import com.google.common.reflect.ClassPath.ClassInfo;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static com.google.common.reflect.ClassPath.from;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.object.IsCompatibleType.typeCompatibleWith;
import static org.junit.Assert.assertThat;

/**
 * @author Kanstantsin Shautsou
 */
public class ModelsSerializableTest {
    private static final Logger LOG = LoggerFactory.getLogger(ModelsSerializableTest.class);

    private List<String> excludeClasses = Arrays.asList(
            Binds.class.getName(),
            BuildResponseItem.class.getName(),
            PullResponseItem.class.getName(),
            PushResponseItem.class.getName(),
            ResponseItem.class.getName()
    );

    @Test
    public void allModelsSerializable() throws IOException, NoSuchFieldException, IllegalAccessException {
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        for (ClassInfo classInfo : from(contextClassLoader).getTopLevelClasses("com.github.dockerjava.api.model")) {
            if (classInfo.getName().endsWith("Test")) {
                continue;
            }

            final Class<?> aClass = classInfo.load();
            if (aClass.getProtectionDomain().getCodeSource().getLocation().getPath().endsWith("test-classes/")
                    || aClass.isEnum()) {
                continue;
            }

            LOG.debug("aClass: {}", aClass);
            assertThat(aClass, typeCompatibleWith(Serializable.class));

            final Object serialVersionUID = FieldUtils.readDeclaredStaticField(aClass, "serialVersionUID", true);
            if (!excludeClasses.contains(aClass.getName())) {
                assertThat(serialVersionUID, instanceOf(Long.class));
                assertThat("Follow devel docs", (Long) serialVersionUID, is(1L));
            }
        }
    }
}
