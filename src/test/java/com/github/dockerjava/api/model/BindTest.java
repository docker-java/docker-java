package com.github.dockerjava.api.model;

import static com.github.dockerjava.api.model.AccessMode.ro;
import static com.github.dockerjava.api.model.AccessMode.rw;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class BindTest {

    @Test
    public void parseUsingDefaultAccessMode() {
        Bind bind = Bind.parse("/host:/container");
        assertEquals(bind.getPath(), "/host");
        assertEquals(bind.getVolume().getPath(), "/container");
        assertEquals(bind.getAccessMode(), AccessMode.DEFAULT);
        assertEquals(bind.getSecMode(), SELContext.none);
    }

    @Test
    public void parseReadWrite() {
        Bind bind = Bind.parse("/host:/container:rw");
        assertEquals(bind.getPath(), "/host");
        assertEquals(bind.getVolume().getPath(), "/container");
        assertEquals(bind.getAccessMode(), rw);
        assertEquals(bind.getSecMode(), SELContext.none);
    }

    @Test
    public void parseReadOnly() {
        Bind bind = Bind.parse("/host:/container:ro");
        assertEquals(bind.getPath(), "/host");
        assertEquals(bind.getVolume().getPath(), "/container");
        assertEquals(bind.getAccessMode(), ro);
        assertEquals(bind.getSecMode(), SELContext.none);
    }

    @Test
    public void parseSELOnly() {
        Bind bind = Bind.parse("/host:/container:Z");
        assertEquals(bind.getPath(), "/host");
        assertEquals(bind.getVolume().getPath(), "/container");
        assertEquals(bind.getAccessMode(), AccessMode.DEFAULT);
        assertEquals(bind.getSecMode(), SELContext.single);
        
        bind = Bind.parse("/host:/container:z");
        assertEquals(bind.getPath(), "/host");
        assertEquals(bind.getVolume().getPath(), "/container");
        assertEquals(bind.getAccessMode(), AccessMode.DEFAULT);
        assertEquals(bind.getSecMode(), SELContext.shared);
    }

    @Test
    public void parseReadWriteSEL() {
        Bind bind = Bind.parse("/host:/container:rw,Z");
        assertEquals(bind.getPath(), "/host");
        assertEquals(bind.getVolume().getPath(), "/container");
        assertEquals(bind.getAccessMode(), rw);
        assertEquals(bind.getSecMode(), SELContext.single);
    }

    @Test
    public void parseReadOnlySEL() {
        Bind bind = Bind.parse("/host:/container:ro,z");
        assertEquals(bind.getPath(), "/host");
        assertEquals(bind.getVolume().getPath(), "/container");
        assertEquals(bind.getAccessMode(), ro);
        assertEquals(bind.getSecMode(), SELContext.shared);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Bind.*")
    public void parseInvalidAccessMode() {
        Bind.parse("/host:/container:xx");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Bind 'nonsense'")
    public void parseInvalidInput() {
        Bind.parse("nonsense");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Bind 'null'")
    public void parseNull() {
        Bind.parse(null);
    }

    @Test
    public void toStringReadOnly() {
        assertEquals(Bind.parse("/host:/container:ro").toString(), "/host:/container:ro");
    }

    @Test
    public void toStringReadWrite() {
        assertEquals(Bind.parse("/host:/container:rw").toString(), "/host:/container:rw");
    }

    @Test
    public void toStringDefaultAccessMode() {
        assertEquals(Bind.parse("/host:/container").toString(), "/host:/container:rw");
    }

    @Test
    public void toStringReadOnlySEL() {
        assertEquals(Bind.parse("/host:/container:ro,Z").toString(), "/host:/container:ro,Z");
    }

    @Test
    public void toStringReadWriteSEL() {
        assertEquals(Bind.parse("/host:/container:rw,z").toString(), "/host:/container:rw,z");
    }

    @Test
    public void toStringDefaultSEL() {
        assertEquals(Bind.parse("/host:/container:Z").toString(), "/host:/container:rw,Z");
    }

}
