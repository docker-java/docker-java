package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@ToString
@Getter
public class NetworkAttachment implements Serializable {
    @Getter(AccessLevel.PRIVATE)
    public static final long serialVersionUID = 1L;

    @JsonProperty("Network")
    private Network network;

    @JsonProperty("Addresses")
    private List<String> addresses;


    @EqualsAndHashCode
    @ToString
    @Getter
    public static class Network implements Serializable {
        @Getter(AccessLevel.PRIVATE)
        private static final long serialVersionUID = 1L;

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Version")
        private Version version;

        @JsonProperty("CreatedAt")
        private Date createdAt;

        @JsonProperty("UpdatedAt")
        private Date updatedAt;

        @JsonProperty("Spec")
        private Spec spec;

        @JsonProperty("DriverState")
        private DriverState driverState;

        @JsonProperty("IPAMOptions")
        private IPAMOptions ipamOptions;


        @EqualsAndHashCode
        @ToString
        @Getter
        public static class Version implements Serializable {
            @Getter(AccessLevel.PRIVATE)
            private static final long serialVersionUID = 1L;

            @JsonProperty("Index")
            private int index;

        }


        @EqualsAndHashCode
        @ToString
        @Getter
        public static class Spec implements Serializable {
            @Getter(AccessLevel.PRIVATE)
            private static final long serialVersionUID = 1L;

            @JsonProperty("Name")
            private String name;

            @JsonProperty("Labels")
            private Map<String, String> labels;

            @JsonProperty("Ingress")
            private Boolean ingress;

            @JsonProperty("IPAMOptions")
            private IPAMOptions ipamOptions;

            @JsonProperty("Scope")
            private String scope;

        }


        @EqualsAndHashCode
        @ToString
        @Getter
        public static class DriverState implements Serializable {
            @Getter(AccessLevel.PRIVATE)
            private static final long serialVersionUID = 1L;

            @JsonProperty("Name")
            private String name;

            @JsonProperty("Options")
            private Map<String, String> options;

        }


        @EqualsAndHashCode
        @ToString
        @Getter
        public static class IPAMOptions implements Serializable {
            @Getter(AccessLevel.PRIVATE)
            private static final long serialVersionUID = 1L;

            @JsonProperty("Driver")
            private Driver driver;

            @JsonProperty("Configs")
            private List<Config> configs;


            @EqualsAndHashCode
            @ToString
            @Getter
            public static class Driver implements Serializable {
                @Getter(AccessLevel.PRIVATE)
                private static final long serialVersionUID = 1L;

                @JsonProperty("Name")
                private String name;

            }


            @EqualsAndHashCode
            @ToString
            @Getter
            public static class Config implements Serializable {
                @Getter(AccessLevel.PRIVATE)
                private static final long serialVersionUID = 1L;

                @JsonProperty("Subnet")
                private String subnet;

                @JsonProperty("Gateway")
                private String gateway;

            }
        }

    }

}
