package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

import static com.github.dockerjava.api.model.EventType.CONTAINER;
import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Kanstantsin Shautsou
 */
public class EventsTest {

    @Test
    public void serderDocs1() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final JavaType type = mapper.getTypeFactory().uncheckedSimpleType(Event.class);

        final Event event = testRoundTrip(RemoteApiVersion.VERSION_1_24,
                "/events/docs1.json",
                type
        );

        assertThat(event, notNullValue());
        assertThat(event.getType(), is(CONTAINER));
        assertThat(event.getAction(), is("create"));
        assertThat(event.getId(), is("ede54ee1afda366ab42f824e8a5ffd195155d853ceaec74a927f249ea270c743"));
        assertThat(event.getFrom(), is("alpine"));
        assertThat(event.getTime(), is(1461943101L));
        assertThat(event.getNode(), nullValue());
        assertThat(event.getTimeNano(), is(1461943101381709551L));

        final HashMap<String, String> attributes = new HashMap<>();
        attributes.put("com.example.some-label", "some-label-value");
        attributes.put("image", "alpine");
        attributes.put("name", "my-container");

        final EventActor actor = new EventActor()
                .withId("ede54ee1afda366ab42f824e8a5ffd195155d853ceaec74a927f249ea270c743")
                .withAttributes(attributes);

        final Event event1 = new Event()
                .withType(CONTAINER)
                .withStatus("create")
                .withId("ede54ee1afda366ab42f824e8a5ffd195155d853ceaec74a927f249ea270c743")
                .withFrom("alpine")
                .withTime(1461943101L)
                .withTimenano(1461943101381709551L)
                .withAction("create")
                .withEventActor(actor);

        assertThat(event1, equalTo(event));
    }
}
