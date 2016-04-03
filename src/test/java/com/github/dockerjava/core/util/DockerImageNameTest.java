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

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 *
 * @author jgriffiths
 */
public class DockerImageNameTest {

    public String[] validNames = {
            "pool.docker/ubuntu",
            "pooldocker:8080/jgriffiths/ubuntu",
            "jgriffiths/ubuntu",
            "jgriffiths/ubuntu:100",
            "pool.docker/ubuntu:latest"
    };


    @Test
    public void fullNameValidation() {
        for( String validName : validNames ) {
            DockerImageName image = new DockerImageName(validName);
            assertTrue(image.isValid());
            assertTrue(image.getRepository() != null);
            assertTrue(image.getRepository().isValid());
        }
    }

    public String[] validRegistryNames = {
            "pool.docker/ubuntu",
            "pooldocker:8080/jgriffiths/ubuntu",
            "j.griffiths/ubuntu",
            "&:jgriffiths/ubuntu/repo:100",
            "pool.docker/ubuntu:latest"
    };

    @Test
    public void registryValidation() {
        for( String validName : validRegistryNames ) {
            DockerImageName image = new DockerImageName(validName);
            assertTrue(image.isValid());
            assertTrue(image.getRepository() != null);
            assertTrue(image.getRepository().isValid());
            assertTrue(image.getRegistry() != null);
            assertTrue(image.getRegistry().isValid());
        }
    }

    public String[] validNamespaces = {
            "pool.docker/namespace/ubuntu",
            "pooldocker:8080/name-sp_ace/ubuntu:latest",
            "jgriffiths/ubuntu",
            "jgrif_fiths/repo:100",
            "pool.docker/ubuntu/ubun2:latest"
    };

    @Test
    public void namespaceValidation() {
        for( String validName : validNamespaces ) {
            DockerImageName image = new DockerImageName(validName);
            assertTrue(image.isValid());
            assertTrue(image.getRepository() != null);
            assertTrue(image.getRepository().isValid());
            assertTrue(image.getNamespace() != null);
            assertTrue(image.getNamespace().isValid());
        }
    }

    public String[] validTags = {
            "pool.docker/namespace/ubuntu:t__-.345",
            "pooldocker:8080/name-sp_ace/ubuntu:latest",
            "jgriffiths/ubuntu:dl8__.-",
            "jgrif_fiths/repo:l333cKA-",
            "pool.docker/ubuntu/ubun2:t444g."
    };

    @Test
    public void tagValidation() {
        for( String validName : validTags ) {
            DockerImageName image = new DockerImageName(validName);
            assertTrue(image.isValid());
            assertTrue(image.getRepository() != null);
            assertTrue(image.getRepository().isValid());
            assertTrue(image.getTag() != null);
            assertTrue(image.getTag().isValid());
        }
    }

    public String[] badNames = {
            "pool.docker/name£$%^&PACE/ubuntu:_--500",
            "JgRif£$%^&s/ubuntu:dl8__.-",
            "-jgrif_f--iths-/rePO:l333cKA-",
            "pool.docker/ubUNtu/ubuN2:t444g."
    };

    @Test
    public void cleaningNames() {
        for( String name : badNames ) {
            DockerImageName imageName = new DockerImageName(name);
            assertFalse(imageName.isValid());
            imageName.makeValid();
            assertTrue(imageName.isValid());
        }
    }

    public String[] tooManySlashes = {
            "sdfasd/asdfasdf/asdfasdfas/asdfasdfas:8789",
            "pool.docker/name£/$%^&PACE/ubuntu:_--500",
            "JgRif/£$%^&s/ubunt/u:dl8__.-",
            "-jgrif_f/--iths-/rePO:l/333cKA-",
            "pool./docker/ubUNtu/ubuN2:t444g."
    };

    @Test
    public void testTooManySlashesException() {
        for( String name : tooManySlashes ) {
            try {
                DockerImageName imageName = new DockerImageName(name);
            } catch (DockerImageNameException ex) {
                assertEquals(ex.getMessage().toLowerCase(), "too many name parts in image name");
            }
        }
    }

    @Test
    public void testOne() {
        String tag = "MySuperTag";
        DockerImageName.TagNamePart ix = new DockerImageName.TagNamePart(tag);
        assertTrue(ix.isValid());
        ix.makeValid();
        assertEquals(ix.toString(), tag);
    }
}
