
package com.github.dockerjava.api.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.NullNode;

@JsonSerialize(using = Links.Serializer.class)
@JsonDeserialize(using = Links.Deserializer.class)
public class Links
{

	private final Link[] links;

	public Links(final Link... links)
	{
		this.links = links;
	}

	public Link[] getLinks()
	{
		return links;
	}

	public static class Serializer extends JsonSerializer<Links>
	{

		@Override
		public void serialize(final Links links, final JsonGenerator jsonGen, final SerializerProvider serProvider) throws IOException, JsonProcessingException
		{
			jsonGen.writeStartArray();
			for (final Link link : links.getLinks()) {
				jsonGen.writeString(link.toString());
			}
			jsonGen.writeEndArray();
		}

	}

	public static class Deserializer extends JsonDeserializer<Links>
	{

		@Override
		public Links deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException
		{
			final List<Link> binds = new ArrayList<Link>();
			final ObjectCodec oc = jsonParser.getCodec();
			final JsonNode node = oc.readTree(jsonParser);
			for (final Iterator<JsonNode> it = node.elements(); it.hasNext();) {

				final JsonNode element = it.next();
				if (!element.equals(NullNode.getInstance())) {
					binds.add(Link.parse(element.asText()));
				}
			}
			return new Links(binds.toArray(new Link[0]));
		}
	}

}
