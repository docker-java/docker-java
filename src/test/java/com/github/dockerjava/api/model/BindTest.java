package com.github.dockerjava.api.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.dockerjava.api.model.AccessMode.ro;
import static com.github.dockerjava.api.model.AccessMode.rw;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

public class BindTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void parseUsingDefaultAccessMode() {
        Bind bind = Bind.parse("/host:/container");
        assertThat(bind.getPath(), is("/host"));
        assertThat(bind.getVolume().getPath(), is("/container"));
        assertThat(bind.getAccessMode(), is(AccessMode.DEFAULT));
        assertThat(bind.getSecMode(), is(SELContext.none));
        assertThat(bind.getNoCopy(), nullValue());
        assertThat(bind.getPropagationMode(), is(PropagationMode.DEFAULT_MODE));
    }

    @Test
    public void parseReadWrite() {
        Bind bind = Bind.parse("/host:/container:rw");
        assertThat(bind.getPath(), is("/host"));
        assertThat(bind.getVolume().getPath(), is("/container"));
        assertThat(bind.getAccessMode(), is(rw));
        assertThat(bind.getSecMode(), is(SELContext.none));
        assertThat(bind.getNoCopy(), nullValue());
        assertThat(bind.getPropagationMode(), is(PropagationMode.DEFAULT_MODE));
    }

    @Test
    public void parseReadWriteNoCopy() {
        Bind bind = Bind.parse("/host:/container:rw,nocopy");
        assertThat(bind.getPath(), is("/host"));
        assertThat(bind.getVolume().getPath(), is("/container"));
        assertThat(bind.getAccessMode(), is(rw));
        assertThat(bind.getSecMode(), is(SELContext.none));
        assertThat(bind.getNoCopy(), is(true));
        assertThat(bind.getPropagationMode(), is(PropagationMode.DEFAULT_MODE));
    }

    @Test
    public void parseReadWriteShared() {
        Bind bind = Bind.parse("/host:/container:rw,shared");
        assertThat(bind.getPath(), is("/host"));
        assertThat(bind.getVolume().getPath(), is("/container"));
        assertThat(bind.getAccessMode(), is(rw));
        assertThat(bind.getSecMode(), is(SELContext.none));
        assertThat(bind.getNoCopy(), nullValue());
        assertThat(bind.getPropagationMode(), is(PropagationMode.SHARED));
    }

    @Test
    public void parseReadWriteSlave() {
        Bind bind = Bind.parse("/host:/container:rw,slave");
        assertThat(bind.getPath(), is("/host"));
        assertThat(bind.getVolume().getPath(), is("/container"));
        assertThat(bind.getAccessMode(), is(rw));
        assertThat(bind.getSecMode(), is(SELContext.none));
        assertThat(bind.getNoCopy(), nullValue());
        assertThat(bind.getPropagationMode(), is(PropagationMode.SLAVE));
    }

    @Test
    public void parseReadWritePrivate() {
        Bind bind = Bind.parse("/host:/container:rw,private");
        assertThat(bind.getPath(), is("/host"));
        assertThat(bind.getVolume().getPath(), is("/container"));
        assertThat(bind.getAccessMode(), is(rw));
        assertThat(bind.getSecMode(), is(SELContext.none));
        assertThat(bind.getNoCopy(), nullValue());
        assertThat(bind.getPropagationMode(), is(PropagationMode.PRIVATE));
    }

    @Test
    public void parseReadOnly() {
        Bind bind = Bind.parse("/host:/container:ro");
        assertThat(bind.getPath(), is("/host"));
        assertThat(bind.getVolume().getPath(), is("/container"));
        assertThat(bind.getAccessMode(), is(ro));
        assertThat(bind.getSecMode(), is(SELContext.none));
        assertThat(bind.getNoCopy(), nullValue());
        assertThat(bind.getPropagationMode(), is(PropagationMode.DEFAULT_MODE));
    }

    @Test
    public void parseSELOnly() {
        Bind bind = Bind.parse("/host:/container:Z");
        assertThat(bind.getPath(), is("/host"));
        assertThat(bind.getVolume().getPath(), is("/container"));
        assertThat(bind.getAccessMode(), is(AccessMode.DEFAULT));
        assertThat(bind.getSecMode(), is(SELContext.single));
        assertThat(bind.getNoCopy(), nullValue());
        assertThat(bind.getPropagationMode(), is(PropagationMode.DEFAULT_MODE));

        bind = Bind.parse("/host:/container:z");
        assertThat(bind.getPath(), is("/host"));
        assertThat(bind.getVolume().getPath(), is("/container"));
        assertThat(bind.getAccessMode(), is(AccessMode.DEFAULT));
        assertThat(bind.getSecMode(), is(SELContext.shared));
        assertThat(bind.getNoCopy(), nullValue());
        assertThat(bind.getPropagationMode(), is(PropagationMode.DEFAULT_MODE));
    }

    @Test
    public void parseReadWriteSEL() {
        Bind bind = Bind.parse("/host:/container:rw,Z");
        assertThat(bind.getPath(), is("/host"));
        assertThat(bind.getVolume().getPath(), is("/container"));
        assertThat(bind.getAccessMode(), is(rw));
        assertThat(bind.getSecMode(), is(SELContext.single));
        assertThat(bind.getNoCopy(), nullValue());
        assertThat(bind.getPropagationMode(), is(PropagationMode.DEFAULT_MODE));
    }

    @Test
    public void parseReadOnlySEL() {
        Bind bind = Bind.parse("/host:/container:ro,z");
        assertThat(bind.getPath(), is("/host"));
        assertThat(bind.getVolume().getPath(), is("/container"));
        assertThat(bind.getAccessMode(), is(ro));
        assertThat(bind.getSecMode(), is(SELContext.shared));
        assertThat(bind.getNoCopy(), nullValue());
        assertThat(bind.getPropagationMode(), is(PropagationMode.DEFAULT_MODE));
    }

    @Test
    public void parseInvalidAccessMode() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage( "Error parsing Bind");

        Bind.parse("/host:/container:xx");
    }

    @Test
    public void parseInvalidInput() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing Bind 'nonsense'");

        Bind.parse("nonsense");
    }

    @Test
    public void parseNull() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing Bind 'null'");

        Bind.parse(null);
    }

    @Test
    public void toStringReadOnly() {
        assertThat(Bind.parse("/host:/container:ro").toString(), is("/host:/container:ro"));
    }

    @Test
    public void toStringReadWrite() {
        assertThat(Bind.parse("/host:/container:rw").toString(), is("/host:/container:rw"));
    }

    @Test
    public void toStringReadWriteNoCopy() {
        assertThat(Bind.parse("/host:/container:rw,nocopy").toString(), is("/host:/container:rw,nocopy"));
    }

    @Test
    public void toStringReadWriteShared() {
        assertThat(Bind.parse("/host:/container:rw,shared").toString(), is("/host:/container:rw,shared"));
    }

    @Test
    public void toStringReadWriteSlave() {
        assertThat(Bind.parse("/host:/container:rw,slave").toString(), is("/host:/container:rw,slave"));
    }

    @Test
    public void toStringReadWritePrivate() {
        assertThat(Bind.parse("/host:/container:rw,private").toString(), is("/host:/container:rw,private"));
    }

    @Test
    public void toStringDefaultAccessMode() {
        assertThat(Bind.parse("/host:/container").toString(), is("/host:/container:rw"));
    }

    @Test
    public void toStringReadOnlySEL() {
        assertThat(Bind.parse("/host:/container:ro,Z").toString(), is("/host:/container:ro,Z"));
    }

    @Test
    public void toStringReadWriteSEL() {
        assertThat(Bind.parse("/host:/container:rw,z").toString(), is("/host:/container:rw,z"));
    }

    @Test
    public void toStringDefaultSEL() {
        assertThat(Bind.parse("/host:/container:Z").toString(), is("/host:/container:rw,Z"));
    }

}
