package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents a host path being bind mounted as a {@link Volume}
 * in a Docker container.
 * The Bind can be in read only or read write access mode.
 */
public class Bind {

	private String path;

	private Volume volume;

	private boolean readOnly = false;

	public Bind(String path, Volume volume) {
		this(path, volume, false);
	}

	public Bind(String path, Volume volume, boolean readOnly) {
		this.path = path;
		this.volume = volume;
		this.readOnly = readOnly;
	}

	public String getPath() {
		return path;
	}

	public Volume getVolume() {
		return volume;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Parses a bind mount specification to a {@link Bind}.
	 * 
	 * @param serialized the specification, e.g. <code>/host:/container:ro</code>
	 * @return a {@link Bind} matching the specification
	 * @throws IllegalArgumentException if the specification cannot be parsed
	 */
	public static Bind parse(String serialized) {
		try {
			String[] parts = serialized.split(":");
			switch (parts.length) {
			case 2: {
				return new Bind(parts[0], Volume.parse(parts[1]));
			}
			case 3: {
				if ("rw".equals(parts[2].toLowerCase()))
					return new Bind(parts[0], Volume.parse(parts[1]), false);
				else if ("ro".equals(parts[2].toLowerCase()))
					return new Bind(parts[0], Volume.parse(parts[1]), true);
				else
					throw new IllegalArgumentException();
			}
			default: {
				throw new IllegalArgumentException();
			}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Error parsing Bind '" + serialized
					+ "'");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Bind) {
			Bind other = (Bind) obj;
			return new EqualsBuilder().append(path, other.getPath())
					.append(volume, other.getVolume())
					.append(readOnly, other.isReadOnly()).isEquals();
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(path).append(volume)
				.append(readOnly).toHashCode();
	}

	/**
	 * Returns a string representation of this {@link Bind} suitable
	 * for inclusion in a JSON message.
	 * The format is <code>&lt;host path&gt;:&lt;container path&gt;:&lt;access mode&gt;</code>,
	 * like the argument in {@link #parse(String)}.
	 * 
	 * @return a string representation of this {@link Bind}
	 */
	@Override
	public String toString() {
		return path + ":" + volume.toString() + (readOnly ? ":ro" : ":rw");
	}

}
