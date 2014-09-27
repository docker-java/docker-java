package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
					throw new RuntimeException("Error parsing Bind '"
							+ serialized + "'");
			}
			default: {
				throw new RuntimeException("Error parsing Bind '" + serialized
						+ "'");
			}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error parsing Bind '" + serialized
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
}
