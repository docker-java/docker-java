package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.github.dockerjava.api.model.Ports.Binding;

public class BindingTest {

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
    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Binding 'nonsense'", enabled = false)
    public void parseInvalidInput() {
        Binding.parse("nonsense");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Binding 'null'")
    public void parseNull() {
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
