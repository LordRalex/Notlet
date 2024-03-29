/*
 * Copyright 2015 Lord_Ralex
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
package net.ae97.notlet.config;

import java.io.File;
import java.io.IOException;

public interface Configuration extends ConfigurationSection {

    /**
     * Saves this configuration to a file
     *
     * @param file File to save to
     *
     * @throws IOException Error
     */
    public void save(File file) throws IOException;

    /**
     * Loads configuration from file
     *
     * @param file File to load from
     *
     * @throws IOException Error
     */
    public void load(File file) throws IOException;

}
