package com.kpelykh.docker.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

class Config {
    URI url;
    String version, username, password, email;

    private Config() {
    }

    static Config createConfig() throws DockerException {
        final Properties p = new Properties();

        try {
            p.load(DockerClient.class.getResourceAsStream("/docker.io.properties"));
        } catch (IOException e) {
            throw new DockerException(e);
        }

        for (String s : new String[]{"url", "version", "username", "password", "email"}) {
            final String key = "docker.io." + s;
            if (System.getProperties().keySet().contains(key)) {
                p.setProperty(key, System.getProperty(key));
            }
        }

        final File file = new File(System.getProperty("user.name"), ".docker.io.properties");
        System.out.println(file);
        if (file.isFile()) {
            try {
                final FileInputStream in = new FileInputStream(file);
                try {
                    p.load(in);
                } finally {
                    in.close();
                }
            } catch (IOException e) {
                throw new DockerException(e);
            }
        }

        final Config c = new Config();

        c.url = URI.create(p.getProperty("docker.io.url"));
        c.version = p.getProperty("docker.io.version");
        c.username = p.getProperty("docker.io.username");
        c.password = p.getProperty("docker.io.password");
        c.email = p.getProperty("docker.io.email");

        return c;
    }
}
