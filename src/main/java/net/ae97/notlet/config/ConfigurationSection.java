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

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface ConfigurationSection extends Serializable {

    public String getString(String path);

    public String getString(String path, String def);

    public int getInt(String path);

    public int getInt(String path, int def);

    public List<String> getStringList(String path);

    public List<String> getStringList(String path, List<String> def);

    public boolean getBoolean(String path);

    public boolean getBoolean(String path, boolean def);

    public Object get(String path);

    public Object get(String path, Object def);

    public ConfigurationSection getSection(String path);

    public ConfigurationSection getSection(String path, ConfigurationSection def);

    public Set<String> getKeys();

}
