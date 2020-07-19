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

import java.util.Map;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 *
 * @author steve
 */
public class Json {

    // following paths all relative to absolute root of JSON DB file
    /**
     *
     */
    public static final String BINDING_PATH = "$..value.uid.segments[0]";

    public static final String BRIDGE_SUBPATH = "bridge";

    public static final String BRIDGE_UID_SEGMENTS_PATH = "$.bridgeUID.segments";

    /**
     *
     */
    public static final String BRIDGE_VALUE_PATH = "$..[?(@.class =~ /.*\\.BridgeImpl$/ && @.value.uid.segments[0] == '%s')].value";

    public static final String COMMON_SUBPATH = "[common]";

    public static final String MODEL_SUBPATH = "model";

    public static final String THING_SUBPATH = "thing";

    public static final String THING_TYPE_UID_SEGMENTS_PATH = "$.thingTypeUID.segments";

    /**
     *
     */
    public static final String THING_VALUE_PATH = "$..[?(@.class =~ /.*\\.ThingImpl$/ && @.value.uid.segments[0] == '%s')].value";

    public static final String UID_SEGMENTS_PATH = "$.uid.segments";

    private static final String BINDING_SUBPATH = "binding";

    /**
     *
     * @param <T>
     * @param returnClass
     * @param jsonValue
     * @param jsonPath
     * @return
     * @throws
     * net.winnall.ohr.configuration.NoJsonPatternMatchException
     */
    public static <T> T applyPath( Class<T> returnClass, Map<String, Object> jsonValue, String jsonPath ) throws NoJsonPatternMatchException {
        try {
            return returnClass.cast( JsonPath.compile( jsonPath )
                    .read( jsonValue ) );
        } catch( PathNotFoundException pnfe ) {
            throw new NoJsonPatternMatchException( pnfe );
        }
    }

    public static Object applyPath( Map<String, Object> jsonValue, String jsonPath ) throws NoJsonPatternMatchException {
        return applyPath( Object.class, jsonValue, jsonPath );
    }

}
