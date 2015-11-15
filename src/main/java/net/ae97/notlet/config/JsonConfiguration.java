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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonConfiguration implements Configuration {

    JsonConfigurationSection root = new JsonConfigurationSection();

    @Override
    public void save(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(saveToString());
            writer.newLine();
        }
    }

    public String saveToString() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(JsonConfigurationSection.class, new JsonConfigurationSectionAdapter())
                .create();
        return gson.toJson(root);
    }

    @Override
    public void load(File file) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(JsonConfigurationSection.class, new JsonConfigurationSectionAdapter())
                .create();
        root = gson.fromJson(new FileReader(file), JsonConfigurationSection.class);
    }

    @Override
    public String getString(String path) {
        return root.getString(path);
    }

    @Override
    public String getString(String path, String def) {
        return root.getString(path, def);
    }

    @Override
    public int getInt(String path) {
        return root.getInt(path);
    }

    @Override
    public int getInt(String path, int def) {
        return root.getInt(path, def);
    }

    @Override
    public boolean getBoolean(String path) {
        return root.getBoolean(path);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return root.getBoolean(path, def);
    }

    @Override
    public List<String> getStringList(String path) {
        return root.getStringList(path);
    }

    @Override
    public List<String> getStringList(String path, List<String> def) {
        return root.getStringList(path, def);
    }

    @Override
    public Object get(String path) {
        return root.get(path);
    }

    @Override
    public Object get(String path, Object def) {
        return root.get(path, def);
    }

    @Override
    public ConfigurationSection getSection(String path) {
        return root.getSection(path);
    }

    @Override
    public ConfigurationSection getSection(String path, ConfigurationSection def) {
        return root.getSection(path, def);
    }

    @Override
    public Set<String> getKeys() {
        return root.getKeys();
    }

    private class JsonConfigurationSection implements ConfigurationSection {

        final Map<String, Object> _internal = new TreeMap<>();

        @Override
        public String getString(String path) {
            return getString(path, null);
        }

        @Override
        public String getString(String path, String def) {
            return get(path, def, String.class);
        }

        @Override
        public int getInt(String path) {
            return getInt(path, 0);
        }

        @Override
        public int getInt(String path, int def) {
            return get(path, def, Integer.class);
        }

        @Override
        public List<String> getStringList(String path) {
            return getStringList(path, new ArrayList<>(0));
        }

        @Override
        public List<String> getStringList(String path, List<String> def) {
            return get(path, def, List.class);
        }

        @Override
        public ConfigurationSection getSection(String path) {
            return getSection(path, null);
        }

        @Override
        public ConfigurationSection getSection(String path, ConfigurationSection def) {
            return get(path, def, ConfigurationSection.class);
        }

        @Override
        public boolean getBoolean(String path) {
            return getBoolean(path, false);
        }

        @Override
        public boolean getBoolean(String path, boolean def) {
            return get(
                    path,
                    def,
                    Boolean.class
            );
        }

        @Override
        public Object get(String path) {
            return get(path, null);
        }

        @Override
        public Object get(String path, Object def) {
            return get(path, def, Object.class);
        }

        @Override
        public Set<String> getKeys() {
            return _internal.keySet();
        }

        private <T> T get(String path, T def, Class<T> cls) {
            if (path.contains(".")) {
                String root = path.split("\\.")[0];
                String remaining = path.split("\\.", 2)[1];
                Object obj = _internal.get(root);
                if (obj == null || !(obj instanceof JsonConfigurationSection)) {
                    return def;
                } else {
                    return ((JsonConfigurationSection) obj).get(remaining, def, cls);
                }
            } else {
                Object obj = _internal.get(path);
                return obj == null ? def : cls.cast(obj);
            }
        }

    }

    private class JsonConfigurationSectionAdapter extends TypeAdapter<JsonConfigurationSection> {

        @Override
        public void write(JsonWriter out, JsonConfigurationSection value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            for (Map.Entry<String, Object> val : value._internal.entrySet()) {
                out.name(val.getKey());
                Object obj = val.getValue();
                if (obj instanceof Number) {
                    out.value((Number) obj);
                } else if (obj instanceof String) {
                    out.value((String) obj);
                } else if (obj instanceof Boolean) {
                    out.value((boolean) obj);
                } else if (obj instanceof JsonConfigurationSection) {
                    write(out, (JsonConfigurationSection) obj);
                } else {
                    Logger.getLogger(JsonConfigurationSection.class.getName()).log(Level.WARNING, "Unknown data found: {0}", obj.getClass().getName());
                }

            }
            out.endObject();
        }

        @Override
        public JsonConfigurationSection read(JsonReader in) throws IOException {
            return readObject(in);
        }

        private JsonConfigurationSection readObject(JsonReader in) throws IOException {
            JsonConfigurationSection section = new JsonConfigurationSection();
            in.beginObject();
            while (in.peek() != JsonToken.END_OBJECT) {
                String name = in.nextName();
                switch (in.peek()) {
                    case BEGIN_OBJECT: {
                        section._internal.put(name, readObject(in));
                    }
                    break;
                    case BEGIN_ARRAY: {
                        in.beginArray();
                        List<String> array = new LinkedList<>();
                        while (in.peek() != JsonToken.END_ARRAY) {
                            array.add(in.nextString());
                        }
                        in.endArray();
                        section._internal.put(name, array);
                    }
                    break;
                    case BOOLEAN: {
                        boolean next = in.nextBoolean();
                        section._internal.put(name, next);
                    }
                    break;
                    case NUMBER: {
                        String line = in.nextString();
                        BigDecimal decimal = new BigDecimal(line);
                        try {
                            section._internal.put(name, decimal.intValueExact());
                        } catch (ArithmeticException e) {
                            try {
                                section._internal.put(name, decimal.longValueExact());
                            } catch (ArithmeticException ex) {
                                section._internal.put(name, decimal.doubleValue());
                            }
                        }
                    }
                    break;
                    case STRING: {
                        String next = in.nextString();
                        section._internal.put(name, next);
                    }
                    break;
                }
            }
            in.endObject();
            return section;
        }
    }
}
