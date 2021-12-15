package com.github.dockerjava.api.model;

import com.github.dockerjava.api.model.Ports.Binding;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;


public class BindingTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void parseIpAndPort() {
        assertEquals(Binding.parse("127.0.0.1:80"), Binding.bindIpAndPort("127.0.0.1", 80));
    }

    @Test
    public void parsePortOnly() {
        assertEquals(Binding.parse("80"), Binding.bindPort(80));
    }

    @Test
    public void parseIPOnly() {
        assertEquals(Binding.parse("127.0.0.1"), Binding.bindIp("127.0.0.1"));
    }

    @Test
    public void parseEmptyString() {
        assertEquals(Binding.parse(""), Binding.empty());
    }

    // Strings can be used since it can be a range. Let the docker daemon do the validation.
    @Test
    @Ignore
    public void parseInvalidInput() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing Binding 'nonsense'");

        Binding.parse("nonsense");
    }

    @Test
    public void parseNull() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing Binding 'null'");

        Binding.parse(null);
    }

    @Test
    public void toStringIpAndHost() {
        assertEquals(Binding.parse("127.0.0.1:80").toString(), "127.0.0.1:80");
    }

    @Test
    public void toStringPortOnly() {
        assertEquals(Binding.parse("80").toString(), "80");
    }

    @Test
    public void toStringIpOnly() {
        assertEquals(Binding.parse("127.0.0.1").toString(), "127.0.0.1");
    }

}
