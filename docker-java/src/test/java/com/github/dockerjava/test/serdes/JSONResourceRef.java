/*
 * Copyright 2015 Oleg Nenashev.
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
package com.github.dockerjava.test.serdes;

/**
 * References JSON resources, which
 * 
 * @author Oleg Nenashev
 */
public interface JSONResourceRef {

    /**
     * Gets the resource file name under the class.
     * 
     * @return File name, which is stored under the resource class
     */
    String getFileName();

    /**
     * Gets a class which stores resources.
     * 
     * @return Class to be used as a resource source
     */
    Class<?> getResourceClass();
}
