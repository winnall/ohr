/*
 * Copyright 2020 Stephen Winnall.
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
package net.winnall.ohr.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.winnall.ohr.host.Host;

/**
 *
 * @author steve
 */
public class Configuration {

    public final static String APP_DOKU_WIKI = "dokuwiki";

    public static final String DEFAULT_CONF_JSON = "/default.conf.json";

    public static final String FACTORY_CONF_JSON = "/factory.conf.json";

    public static final String JSONPATH_CONF_JSON = "/jsonpath.conf.json";

    private ConfigurationData defaultData;

    private ConfigurationData factoryData;

    private String jsonDBFileName;

    private String jsonDBFolderName;

    private ConfigurationData jsonpathData;

    private String linkPrefix;

    private String outputFolderName;

    private boolean zippedOutput;

    private Configuration() {
        Logger.getLogger( Configuration.class.getName() )
                .log( Level.CONFIG, "intialise configuration" );
        // ensure that JsonPath uses Jackson
        setJsonPathDefaults();
    }

    /**
     *
     * @return
     */
    public static Configuration getInstance() {
        return ConfigurationHolder.INSTANCE;
    }

    public Path getBindingsFolderPath() {
        if( this.outputFolderName == null ) {
            this.outputFolderName = getDefaultParameter( String.class, "bindings-folder" );
        }
        return Paths.get( this.outputFolderName, "bindings" );
    }

    public String getDefaultLinkPrefix() {
        return getDefaultParameter( String.class, "link-prefix" );
    }

    public <T> T getDefaultParameter( Class<T> valueClass, String... keys ) {
        return getDefaultData( valueClass, keys );
    }

    public <T> T getFactoryParameter( Class<T> valueClass, String... keys ) {
        if( factoryData == null ) {
            factoryData = openConfigurationFile( FACTORY_CONF_JSON );
        }
        return valueClass.cast( factoryData );
    }

    public void setJsonDBFolderName( String jsonDBFolder ) {
        this.jsonDBFolderName = jsonDBFolder;
    }

    /**
     *
     * @return
     */
    public Path getJsonDBFolderPath() {
        if( this.jsonDBFolderName == null ) {
            this.jsonDBFolderName = getDefaultParameter( String.class, "json-db-folder" );
        }
        return Paths.get( jsonDBFolderName );
    }

    public Path getJsonDBThingPath() {
        if( this.jsonDBFileName == null ) {
            this.jsonDBFileName = getDefaultParameter( String.class, "json-db-files", "things" );
        }
        return Paths.get( jsonDBFolderName, jsonDBFileName );
    }

    public <T> T getJsonpathParameter( Class<T> valueClass, String... keys ) {
        return getJsonpathData( valueClass, keys );
    }

    /**
     * Get the value of linkPrefix
     *
     * @return the value of linkPrefix
     */
    public String getLinkPrefix() {
        return linkPrefix;
    }

    /**
     * Set the value of linkPrefix
     *
     * @param linkPrefix new value of linkPrefix
     */
    public void setLinkPrefix( String linkPrefix ) {
        this.linkPrefix = linkPrefix;
    }

    public void setOutputFolderName( String outputFolder ) {
        this.outputFolderName = outputFolder;
    }

    public String getTemplateFolderPath() {
//        return Host.getInstance()
//                .getInputSupportFolderName(
//                        getDefaultParameter( String.class, "template-folder" ) );
        return getDefaultParameter( String.class, "template-folder" );
    }

    /**
     * Get the value of zippedOutput
     *
     * @return the value of zippedOutput
     */
    public boolean isZippedOutput() {
        return zippedOutput;
    }

    /**
     * Set the value of zippedOutput
     *
     * @param zippedOutput new value of zippedOutput
     */
    public void setZippedOutput( boolean zippedOutput ) {
        this.zippedOutput = zippedOutput;
    }

    private <T> T getDefaultData( Class<T> valueClass, String... keys ) {
        assert keys.length > 0 : "not enough keys for JSON lookup";
        if( defaultData == null ) {
            defaultData = openConfigurationFile( DEFAULT_CONF_JSON );
        }
        return defaultData
                .lookup( valueClass, keys );
    }

    private <T> T getJsonpathData( Class<T> valueClass, String... keys ) {
        assert keys.length > 0 : "not enough keys for JSON lookup";
        if( jsonpathData == null ) {
            jsonpathData = openConfigurationFile( JSONPATH_CONF_JSON );
        }
        return jsonpathData
                .lookup( valueClass, keys );
    }

    private ConfigurationData openConfigurationFile( String filename ) {
        return openJsonFile( ConfigurationData.class,
                             Host.getInstance()
                                     .getInputSupportFile( filename ) );
    }

    private <J> J openJsonFile( Class<J> jsonClass, final InputStream inputStream ) {
        try {
            return new ObjectMapper().readValue( inputStream, jsonClass );
        } catch( IOException ex ) {
            System.exit( Configuration.Termination.CANNOT_OPEN_JSON_FILE );
        }
        return null;
    }

    private void setJsonPathDefaults() {
        com.jayway.jsonpath.Configuration
                .setDefaults( new com.jayway.jsonpath.Configuration.Defaults() {

                    private final JsonProvider jsonProvider = new JacksonJsonProvider();

                    private final MappingProvider mappingProvider = new JacksonMappingProvider();

                    @Override
                    public JsonProvider jsonProvider() {
                        return jsonProvider;
                    }

                    @Override
                    public MappingProvider mappingProvider() {
                        return mappingProvider;
                    }

                    @Override
                    public Set<Option> options() {
                        return EnumSet.noneOf( Option.class );
                    }

                } );
    }

    public static class Termination {

        public static int NO_JSON_DB_FOLDER = 1;

        public static int CANNOT_DELETE_EXISTING_FOLDER = 2;

        public static int CANNOT_READ_BINDINGS = 3;

        public static int CANNOT_OPEN_JSON_DB = 4;

        public static int CANNOT_OPEN_JSON_FILE = 5;

        public static int CANCEL_BUTTON_OPERATED = 6;

        public static int NO_JSON_THING_DB = 7;

    }

    private static class ConfigurationData implements Map<String, Object> {

        private final Map<String, Object> data;

        public ConfigurationData() {
            this.data = new HashMap<>();
        }

        ConfigurationData( Map<String, Object> map ) {
            this.data = map;
        }

        ConfigurationData( Object object ) {
            this.data = (Map) object;
        }

        @Override
        public void clear() {
            data.clear();
        }

        @Override
        public Object compute( String key, BiFunction<? super String, ? super Object, ? extends Object> remappingFunction ) {
            return data.compute( key, remappingFunction );
        }

        @Override
        public Object computeIfAbsent( String key, Function<? super String, ? extends Object> mappingFunction ) {
            return data.computeIfAbsent( key, mappingFunction );
        }

        @Override
        public Object computeIfPresent( String key, BiFunction<? super String, ? super Object, ? extends Object> remappingFunction ) {
            return data.computeIfPresent( key, remappingFunction );
        }

        @Override
        public boolean containsKey( Object key ) {
            return data.containsKey( key );
        }

        @Override
        public boolean containsValue( Object value ) {
            return data.containsValue( value );
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            return data.entrySet();
        }

        @Override
        public boolean equals( Object o ) {
            return data.equals( o );
        }

        @Override
        public void forEach( BiConsumer<? super String, ? super Object> action ) {
            data.forEach( action );
        }

        @Override
        public Object get( Object key ) {
            return data.get( key );
        }

        @Override
        public Object getOrDefault( Object key, Object defaultValue ) {
            return data.getOrDefault( key, defaultValue );
        }

        @Override
        public int hashCode() {
            return data.hashCode();
        }

        @Override
        public boolean isEmpty() {
            return data.isEmpty();
        }

        @Override
        public Set<String> keySet() {
            return data.keySet();
        }

        @Override
        public Object merge( String key, Object value, BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction ) {
            return data.merge( key, value, remappingFunction );
        }

        @Override
        public Object put( String key, Object value ) {
            return data.put( key, value );
        }

        @Override
        public void putAll( Map<? extends String, ? extends Object> m ) {
            data.putAll( m );
        }

        @Override
        public Object putIfAbsent( String key, Object value ) {
            return data.putIfAbsent( key, value );
        }

        @Override
        public Object remove( Object key ) {
            return data.remove( key );
        }

        @Override
        public boolean remove( Object key, Object value ) {
            return data.remove( key, value );
        }

        @Override
        public boolean replace( String key, Object oldValue, Object newValue ) {
            return data.replace( key, oldValue, newValue );
        }

        @Override
        public Object replace( String key, Object value ) {
            return data.replace( key, value );
        }

        @Override
        public void replaceAll( BiFunction<? super String, ? super Object, ? extends Object> function ) {
            data.replaceAll( function );
        }

        @Override
        public int size() {
            return data.size();
        }

        @Override
        public Collection<Object> values() {
            return data.values();
        }

        private <T> T lookup( Class<T> valueClass, String... keys ) {
            if( keys.length == 1 ) {
                return valueClass.cast( get( keys[ 0 ] ) );
            }
            final String key = keys[ 0 ];
            if( !containsKey( key ) ) {
                Logger.getLogger( Configuration.class.getName() )
                        .log( Level.SEVERE, "unknown key: {0}", key );
                return null;
            }
            ConfigurationData configurationData = new ConfigurationData( get( key ) );
            return configurationData
                    .lookup( valueClass,
                             Arrays.copyOfRange( keys, 1, keys.length )
                    );
        }

    }

    private static class ConfigurationHolder {

        private static final Configuration INSTANCE = new Configuration();

    }

}
