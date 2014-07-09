
package com.github.dockerjava.client.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Link
{

	private final String name;

	private final String alias;

	public Link(final String name, final String alias)
	{
		this.name = name;
		this.alias = alias;
	}

	public String getName()
	{
		return name;
	}

	public String getAlias()
	{
		return alias;
	}

	public static Link parse(final String serialized)
	{
		try {
			final String[] parts = serialized.split(":");
			switch (parts.length) {
			case 2: {
				return new Link(parts[0], parts[1]);
			}
			default: {
				throw new RuntimeException("Error parsing Link '" + serialized + "'");
			}
			}
		} catch (final Exception e) {
			throw new RuntimeException("Error parsing Link '" + serialized + "'");
		}
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj instanceof Link) {
			final Link other = (Link) obj;
			return new EqualsBuilder().append(name, other.getName()).append(alias, other.getAlias()).isEquals();
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(name).append(alias).toHashCode();
	}

}
