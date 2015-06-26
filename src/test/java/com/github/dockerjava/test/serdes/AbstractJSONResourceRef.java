/*
 * Copyright 2015 CloudBees Inc., Oleg Nenashev.
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
 * Default implementation of the Resource reference.
 * 
 * @author Oleg Nenashev
 */
public abstract class AbstractJSONResourceRef implements JSONResourceRef {
    /**
     * Gets a class which stores resources.
     * 
     * @return Reference class by default.
     */
    @Override
    public Class<?> getResourceClass() {
        return this.getClass();
    }
}
