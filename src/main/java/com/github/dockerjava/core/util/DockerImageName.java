/*
 * Copyright 2015 jgriffiths.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.dockerjava.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Written to construct and validate docker image names, and their respective parts:
 * [REGISTRY/][NAMESPACE/]REPOSITORY[:TAG]
 * <p>
 * Based on Docker v1.5 source
 *
 * @author jgriffiths 08/04/2015
 */
public class DockerImageName {

    public String imageName;

    private TagNamePart tag = null;
    private RepositoryNamePart repository = null;
    private NamespaceNamePart namespace = null;
    private RegistryNamePart registry = null;

    /**
     * Constructor, de-constructs given image name into relevant objects
     *
     * @param imageName Full image name for docker image
     */
    public DockerImageName(String imageName) throws DockerImageNameException {
        this.imageName = imageName;
        // If we have a string to construct with
        if (imageName != null && !imageName.equals("")) {
            // Split the name into a map and construct the image name parts
            // This action will throw an exception if the name contains too many parts
            Map<String, String> nameMap = splitImageName(imageName);
            if (nameMap.containsKey("registry")) {
                this.registry = new RegistryNamePart(nameMap.get("registry"));
            }
            if (nameMap.containsKey("namespace")) {
                this.namespace = new NamespaceNamePart(nameMap.get("namespace"));
            }
            if (nameMap.containsKey("repository")) {
                this.repository = new RepositoryNamePart(nameMap.get("repository"));
            }
            if (nameMap.containsKey("tag")) {
                this.tag = new TagNamePart(nameMap.get("tag"));
            }
        }
    }

    @Override
    public String toString() {
        return joinParts();
    }

    public String toStringWithoutTag() {
        return joinPartsWithoutTag();
    }

    public TagNamePart getTag() {
        return tag;
    }

    public RepositoryNamePart getRepository() {
        return repository;
    }

    public NamespaceNamePart getNamespace() {
        return namespace;
    }

    public RegistryNamePart getRegistry() {
        return registry;
    }

    private String joinPartsWithoutTag() {

        StringBuilder allParts = new StringBuilder();
        if (registry != null) {
            allParts.append(registry);
            allParts.append("/");
        }
        if (namespace != null) {
            allParts.append(namespace);
            allParts.append("/");
        }
        if (repository != null) {
            allParts.append(repository);
        }

        return allParts.toString();
    }

    private String joinParts() {
        String name = joinPartsWithoutTag();
        if (tag != null) {
            name +=  ":" + tag;
        }
        return name;
    }

    public boolean isValid() {
        return (repository.isValid()) &&
                (tag == null || tag.isValid()) &&
                (registry == null || registry.isValid()) &&
                (namespace == null || namespace.isValid());
    }

    public void makeValid() {
        if (registry != null) {
            registry.makeValid();
        }
        if (namespace != null) {
            namespace.makeValid();
        }
        if (repository != null) {
            repository.makeValid();
        }
        if (tag != null) {
            tag.makeValid();
        }
    }

    /**
     * Splits image name into name parts
     *
     * @param imageName Full docker image name
     * @return Map of image name parts
     * @throws DockerImageException
     */
    private static Map<String, String> splitImageName(String imageName) throws DockerImageNameException {
        String[] nameParts = imageName.split("/");
        // Image names are a maximum of 3 parts (separated by '/')
        if (nameParts.length > 3) {
            throw new DockerImageNameException("Too many name parts in image name");
        }
        if (nameParts.length == 0) {
            throw new DockerImageNameException("Cannot parse empty image name");
        }
        Map<String, String> nameMap = new HashMap<>();

        // Must be only a repository name
        if (nameParts.length == 1) {
            nameMap.put("repository", nameParts[0]);
            // May be namespace/repository or registry/repository
        } else if (nameParts.length == 2) {
            nameMap.put("repository", nameParts[1]);
            // Docker will think this is a registry
            if (nameParts[0].contains(".") || nameParts[0].contains(":") || nameParts[0].equals("localhost")) {
                nameMap.put("registry", nameParts[0]);
                // Must be a namespace
            } else {
                nameMap.put("namespace", nameParts[0]);
            }
            // Full 3-part repository name
        } else if (nameParts.length == 3) {
            nameMap.put("registry", nameParts[0]);
            nameMap.put("namespace", nameParts[1]);
            nameMap.put("repository", nameParts[2]);
        }
        // Split the last tag from the repository if exists
        int lastColonOccurrance = nameMap.get("repository").lastIndexOf(":");
        // If the first char is a ':', treat it as the repository and we'll validate it later
        if (lastColonOccurrance > 0) {
            String repository = nameMap.get("repository").substring(0, lastColonOccurrance);
            String tag = nameMap.get("repository").substring(lastColonOccurrance + 1); // Add one to remove the colon
            nameMap.put("repository", repository);
            nameMap.put("tag", tag);
        }
        return nameMap;
    }

    /**
     * Abstract class for each image name part to extend
     */
    private abstract static class AbstractDockerImageNamePart {

        protected String imageNamePart;
        protected String imageNamePartRegexp; // For matching
        protected String imageNamePartInvertedRegexp; // For replacing

        /**
         * Abstract class constructor for use in subclasses
         *
         * @param namePart
         */
        AbstractDockerImageNamePart(String namePart) {
            if (namePart == null || namePart.isEmpty()) {
                throw new IllegalArgumentException("Illegal use of empty/null name part");
            }
            imageNamePart = namePart;
        }

        @Override
        public String toString() {
            return imageNamePart;
        }

        /**
         * Removes any non-docker-friendly characters from the image part
         */
        public abstract void makeValid();

        /**
         * Checks whether the image name part is valid
         *
         * @return whether image name part is valid
         */
        public abstract boolean isValid();

        /**
         * Validates the image name part and throws DockerImageNameException, with reason, if invalid
         */
        public abstract void validate() throws DockerImageNameException;

        public String spacesToHyphens(String str) {
            return str.replaceAll("[\\s]+", "-");
        }
    }

    public static class RegistryNamePart extends AbstractDockerImageNamePart {

        /**
         * Calls constructor on abstract to set namePart
         *
         * @param namePart Part of image name
         */
        public RegistryNamePart(String namePart) {
            super(namePart);
        }

        @Override
        public void validate() {
            if (imageNamePart.contains("://")) {
                throw new DockerImageNameException("Invalid registry: Registry must not contain a schema");
            }
        }

        @Override
        public boolean isValid() {
            return !imageNamePart.contains("://");
        }

        @Override
        public void makeValid() {
            if (!isValid()) {
                removeSchema();
            }
        }

        private void removeSchema() {
            int lastSchemaInstance = imageNamePart.lastIndexOf("://");
            if (lastSchemaInstance > -1) {
                imageNamePart = imageNamePart.substring(lastSchemaInstance, 3);  // Remove the 3 schema chars too.
            }
        }
    }

    public static class NamespaceNamePart extends AbstractDockerImageNamePart {

        /**
         * Calls constructor on abstract to set namePart
         *
         * @param namePart Part of image name
         */
        public NamespaceNamePart(String namePart) {
            super(namePart);
            this.imageNamePartRegexp = "^([a-z0-9_-]*)$";
            this.imageNamePartInvertedRegexp = "[^a-z0-9_-]";
        }

        @Override
        public void validate() {
            if (imageNamePart.length() < 2 || imageNamePart.length() > 255) {
                throw new DockerImageNameException("Invalid namespace: Namespace must be between 2 and 255 characters");
            }
            if (imageNamePart.startsWith("-") || imageNamePart.endsWith("-")) {
                throw new DockerImageNameException("Invalid namespace: Namespace must not begin or end with a hyphon");
            }
            if (imageNamePart.contains("--")) {
                throw new DockerImageNameException("Invalid namespace: Namespace must not contain consecutive hyphons");
            }
            if (!imageNamePart.matches(imageNamePartRegexp)) {
                throw new DockerImageNameException("Invalid namespace: Namespace must match [a-z0-9_-]");
            }
        }

        @Override
        public boolean isValid() {
            return imageNamePart.matches(imageNamePartRegexp) && !imageNamePart.contains("--")
                    && !imageNamePart.startsWith("-") && !imageNamePart.endsWith("-") && imageNamePart.length() < 255
                    && imageNamePart.length() > 2;
        }

        @Override
        public void makeValid() {
            if (imageNamePart.length() > 255) {
                imageNamePart = imageNamePart.substring(0, 255);
            }
            if (!imageNamePart.matches(imageNamePartRegexp)) {
                imageNamePart = spacesToHyphens(imageNamePart)
                        .toLowerCase()
                        .replaceAll(imageNamePartInvertedRegexp, "");
            }
            while (imageNamePart.contains("--")) {
                imageNamePart = imageNamePart.replaceAll("--", "-");
            }
            imageNamePart = imageNamePart.replaceAll("^-", "").replaceAll("-$", "");
            while (imageNamePart.length() < 2) {
                imageNamePart += "0";
            }
        }
    }

    public static class RepositoryNamePart extends AbstractDockerImageNamePart {

        /**
         * Calls constructor on abstract to set namePart
         *
         * @param namePart Part of image name
         */
        public RepositoryNamePart(String namePart) {
            super(namePart);
            this.imageNamePartRegexp = "^([a-z0-9_.-]*)$";
            this.imageNamePartInvertedRegexp = "[^a-z0-9_.-]";
        }

        @Override
        public void validate() {
            if (!imageNamePart.matches(imageNamePartRegexp)) {
                throw new DockerImageNameException("Invalid registry: Registry must match [a-z0-9_.-]");
            }
        }

        @Override
        public boolean isValid() {
            return imageNamePart.matches(imageNamePartRegexp);
        }

        @Override
        public void makeValid() {
            if (!isValid()) {
                imageNamePart = spacesToHyphens(imageNamePart)
                        .toLowerCase()
                        .replaceAll(imageNamePartInvertedRegexp, "");
            }
        }
    }

    public static class TagNamePart extends AbstractDockerImageNamePart {

        /**
         * Calls constructor on abstract to set namePart
         *
         * @param namePart Part of image name
         */
        public TagNamePart(String namePart) {
            super(namePart);
            this.imageNamePartRegexp = "^[a-zA-Z0-9][a-zA-Z0-9_.-]*$";
            this.imageNamePartInvertedRegexp = "(?:^[^a-zA-Z0-9])(?:[^a-zA-Z0-9_.-])*";
        }

        @Override
        public void validate() {
            if (!imageNamePart.matches(imageNamePartRegexp)) {
                throw new DockerImageNameException("Invalid tag: Tag must match [a-zA-Z0-9][a-zA-Z0-9_.-]*");
            }
            if (imageNamePart.length() > 128) {
                throw new DockerImageNameException("Invalid tag: Tag must be between 1 and 128 characters");
            }
        }

        @Override
        public boolean isValid() {
            return imageNamePart.matches(imageNamePartRegexp) || imageNamePart.length() < 128;
        }

        @Override
        public void makeValid() {
            if (imageNamePart.length() > 128) {
                imageNamePart = imageNamePart.substring(0, 128);
            }
            if (!imageNamePart.matches(imageNamePartRegexp)) {
                imageNamePart = spacesToHyphens(imageNamePart)
                        .replaceAll(imageNamePartInvertedRegexp, "");
            }
        }
    }
}
