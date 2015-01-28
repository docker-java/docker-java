package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class VolumeFrom {

	private String container;

	private AccessMode accessMode;

	public VolumeFrom(String container) {
		this(container, AccessMode.DEFAULT);
	}

	public VolumeFrom(String container, AccessMode accessMode) {
		this.container = container;
		this.accessMode = accessMode;
	}
	
	public String getContainer() {
		return container;
	}
	
	public AccessMode getAccessMode() {
		return accessMode;
	}


	/**
	 * Parses a volume from specification to a {@link VolumeFrom}.
	 * 
	 * @param serialized the specification, e.g. <code>container:ro</code>
	 * @return a {@link VolumeFrom} matching the specification
	 * @throws IllegalArgumentException if the specification cannot be parsed
	 */
	public static VolumeFrom parse(String serialized) {
		try {
			String[] parts = serialized.split(":");
			switch (parts.length) {
			case 1: {
				return new VolumeFrom(parts[0]);
			}
			case 2: {
				return new VolumeFrom(parts[0], AccessMode.valueOf(parts[1]));
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
		if (obj instanceof VolumeFrom) {
			VolumeFrom other = (VolumeFrom) obj;
			return new EqualsBuilder().append(container, other.getContainer())
					.append(accessMode, other.getAccessMode()).isEquals();
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(container)
				.append(accessMode).toHashCode();
	}

	/**
	 * Returns a string representation of this {@link VolumeFrom} suitable
	 * for inclusion in a JSON message.
	 * The format is <code>&lt;container&gt;:&lt;access mode&gt;</code>,
	 * like the argument in {@link #parse(String)}.
	 * 
	 * @return a string representation of this {@link VolumeFrom}
	 */
	@Override
	public String toString() {
		return container + ":" + accessMode.toString();
	}

}
