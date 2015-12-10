package com.github.dockerjava.core.dockerfile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.github.dockerjava.api.exception.DockerClientException;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * A statement present in a dockerfile.
 */
public abstract class DockerfileStatement<T extends DockerfileStatement<?>> {

    private DockerfileStatement() {
    }

    public T transform(Map<String, String> env) {
        return (T) this;
    }

    protected String filterForEnvironmentVars(Map<String, String> environmentMap, String extractedResource) {

        if (environmentMap.size() > 0) {

            String currentResourceContent = extractedResource;

            for (Map.Entry<String, String> entry : environmentMap.entrySet()) {

                String variable = entry.getKey();

                String replacementValue = entry.getValue();

                // handle: $VARIABLE case
                currentResourceContent = currentResourceContent.replaceAll("\\$" + variable,
                        Matcher.quoteReplacement(replacementValue));

                // handle ${VARIABLE} case
                currentResourceContent = currentResourceContent.replaceAll("\\$\\{" + variable + "\\}",
                        Matcher.quoteReplacement(replacementValue));

            }

            return currentResourceContent;
        } else {
            return extractedResource;
        }
    }

    /**
     * A statement that we don't particularly care about.
     */
    public static class OtherLine extends DockerfileStatement {

        public final String statement;

        public OtherLine(String statement) {
            this.statement = statement;
        }

        @Override
        public String toString() {
            return statement;
        }
    }

    /**
     * An ADD or a COPY
     */
    public static class Add extends DockerfileStatement<Add> {

        private static final Pattern ARGUMENT_TOKENIZER = Pattern.compile("(?:\"[^\"]+\")|(\\S+)");

        public final Collection<String> sources;

        public final String destination;

        private Add(Collection<String> sources, String destination) {
            this.sources = sources;
            this.destination = destination;
        }

        @Override
        public Add transform(final Map<String, String> env) {
            Collection<String> resources = Collections2.transform(sources, new Function<String, String>() {
                @Override
                public String apply(String source) {
                    return filterForEnvironmentVars(env, source).trim();
                }
            });
            return new Add(resources, destination);
        }

        public Iterable<String> getFileResources() {
            return Collections2.filter(sources, new Predicate<String>() {

                @Override
                public boolean apply(String source) {
                    URI uri;
                    try {
                        uri = new URI(source);
                    } catch (URISyntaxException e) {
                        return false;
                    }
                    return uri.getScheme() == null || "file".equals(uri.getScheme());
                }
            });
        }

        /**
         * Createa an Add if it matches, or missing if not.
         *
         * @param statement
         *            statement that may be an ADD or a COPY
         * @return optional typed item.
         */
        public static Optional<Add> create(String statement) {
            Matcher argumentMatcher = ARGUMENT_TOKENIZER.matcher(statement.trim());

            if (!argumentMatcher.find()) {
                return Optional.absent();
            }

            String commandName = argumentMatcher.group();
            if (!(StringUtils.equals(commandName, "ADD") || StringUtils.equals(commandName, "COPY"))) {
                return Optional.absent();
            }

            String lastToken = null;
            Collection<String> sources = new ArrayList<>();

            while (argumentMatcher.find()) {
                if (lastToken != null) {
                    sources.add(lastToken);
                }
                lastToken = argumentMatcher.group().replaceAll("(^\")|(\"$)", "");
            }

            if (sources.isEmpty()) {
                throw new DockerClientException("Wrong ADD or COPY format");
            }

            return Optional.of(new Add(sources, lastToken));
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("sources", sources).add("destination", destination).toString();
        }
    }

    public static class Env extends DockerfileStatement<Env> {

        private static final Pattern ENV_PATTERN = Pattern.compile("^ENV\\s+(.*)\\s+(.*)$");

        public final String variable;

        public final String value;

        private Env(String variable, String value) {
            this.variable = variable;
            this.value = value;
        }

        private Env(Matcher envMatcher) {
            this.variable = envMatcher.group(1).trim();
            this.value = envMatcher.group(2).trim();
        }

        public static Optional<Env> create(String statement) {
            Matcher matcher = ENV_PATTERN.matcher(statement.trim());
            if (!matcher.find()) {
                return Optional.absent();
            }

            if (matcher.groupCount() != 2) {
                throw new DockerClientException("Wrong ENV format");
            }

            return Optional.of(new Env(matcher));
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("variable", variable).add("value", value).toString();
        }
    }

    /**
     * Return a dockerfile statement
     */
    public static Optional<? extends DockerfileStatement> createFromLine(String cmd) {
        if (cmd.trim().isEmpty() || cmd.startsWith("#")) {
            return Optional.absent();
        }

        Optional<? extends DockerfileStatement> line;

        line = Add.create(cmd);

        if (line.isPresent()) {
            return line;
        }

        line = Env.create(cmd);

        if (line.isPresent()) {
            return line;
        }

        return Optional.of(new OtherLine(cmd));

    }

}
