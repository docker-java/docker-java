package com.github.dockerjava.api.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author magnayn
 */
@EqualsAndHashCode
@ToString
public class Identifier implements Serializable {
    private static final long serialVersionUID = 1L;

    public final Repository repository;

    public final Optional<String> tag;

    public Identifier(Repository repository, String tag) {
        this.repository = repository;

        this.tag = Optional.ofNullable(tag);
    }

    /**
     * Return an identifier that correctly splits up the repository and tag. There can be &gt; 1 ":" fred/jim --&gt; fred/jim, []
     * fred/jim:123 --&gt; fred/jim, 123 fred:123/jim:123 --&gt; fred:123/jim, 123
     *
     *
     * @param identifier
     *            as a string
     * @return parsed identifier.
     */
    public static Identifier fromCompoundString(String identifier) {
        String[] parts = identifier.split("/");
        if (parts.length != 2) {
            String[] rhs = identifier.split(":");
            if (rhs.length != 2) {
                return new Identifier(new Repository(identifier), null);
            } else {
                return new Identifier(new Repository(rhs[0]), rhs[1]);
            }
        }

        String[] rhs = parts[1].split(":");
        if (rhs.length != 2) {
            return new Identifier(new Repository(identifier), null);
        }

        return new Identifier(new Repository(parts[0] + "/" + rhs[0]), rhs[1]);
    }
}
