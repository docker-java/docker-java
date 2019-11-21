package com.github.dockerjava.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.github.dockerjava.api.annotation.FromPrimitive;
import com.github.dockerjava.api.annotation.FieldName;
import com.github.dockerjava.api.annotation.ToPrimitive;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DockerJavaJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public String[] findEnumValues(Class<?> enumType, Enum<?>[] enumValues, String[] names) {
        Map<String, String> expl = new HashMap<>();
        for (Field f : ClassUtil.getDeclaredFields(enumType)) {
            if (!f.isEnumConstant()) {
                continue;
            }
            FieldName prop = f.getAnnotation(FieldName.class);
            if (prop != null) {
                expl.put(f.getName(), prop.value());
            }
        }
        if (expl.isEmpty()) {
            return super.findEnumValues(enumType, enumValues, names);
        }

        for (int i = 0; i < enumValues.length; ++i) {
            names[i] = expl.getOrDefault(enumValues[i].name(), names[i]);
        }
        return names;
    }

    @Override
    public JsonCreator.Mode findCreatorAnnotation(MapperConfig<?> config, Annotated a) {
        if (a.hasAnnotation(FromPrimitive.class)) {
            return JsonCreator.Mode.DEFAULT;
        }
        return super.findCreatorAnnotation(config, a);
    }

    @Override
    public Boolean hasAsValue(Annotated a) {
        if (a.hasAnnotation(ToPrimitive.class)) {
            return true;
        }
        return super.hasAsValue(a);
    }

    @Override
    public PropertyName findNameForSerialization(Annotated a) {
        FieldName fieldName = a.getAnnotation(FieldName.class);
        if (fieldName != null) {
            return PropertyName.construct(fieldName.value());
        }

        return super.findNameForSerialization(a);
    }

    @Override
    public PropertyName findNameForDeserialization(Annotated a) {
        FieldName fieldName = a.getAnnotation(FieldName.class);
        if (fieldName != null) {
            return PropertyName.construct(fieldName.value());
        }

        return super.findNameForDeserialization(a);
    }
}
