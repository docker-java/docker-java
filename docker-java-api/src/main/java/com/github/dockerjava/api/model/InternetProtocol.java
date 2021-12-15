package com.github.dockerjava.api.model;

/**
 * The IP protocols supported by Docker.
 *
 * @see #TCP
 * @see #UDP
 * @see #SCTP
 */
public enum InternetProtocol {
    /**
     * The <i>Transmission Control Protocol</i>
     */
    TCP,

    /**
     * The <i>User Datagram Protocol</i>
     */
    UDP,

    /**
     * The <i>Stream Control Transmission Protocol</i>
     */
    SCTP;

    /**
     * The default {@link InternetProtocol}: {@link #TCP}
     */
    public static final InternetProtocol DEFAULT = TCP;

    /**
     * Returns a string representation of this {@link InternetProtocol} suitable for inclusion in a JSON message. The output is the
     * lowercased name of the Protocol, e.g. <code>tcp</code>.
     *
     * @return a string representation of this {@link InternetProtocol}
     */
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    /**
     * Parses a string to an {@link InternetProtocol}.
     *
     * @param serialized
     *            the protocol, e.g. <code>tcp</code> or <code>TCP</code>
     * @return an {@link InternetProtocol} described by the string
     * @throws IllegalArgumentException
     *             if the argument cannot be parsed
     */
    public static InternetProtocol parse(String serialized) throws IllegalArgumentException {
        try {
            return valueOf(serialized.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing Protocol '" + serialized + "'");
        }
    }

}
