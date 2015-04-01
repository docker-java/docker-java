package com.github.dockerjava.api.model;

/**
 * The access mode of a file system or file: <code>read-write</code>
 * or <code>read-only</code>.
 */
public enum AccessMode {
	/** read-write */
	rw,
	
	/** read-only */
	ro;
	
	/**
	 * The default {@link AccessMode}: {@link #rw}
	 */
	public static final AccessMode DEFAULT = rw;

	
}
