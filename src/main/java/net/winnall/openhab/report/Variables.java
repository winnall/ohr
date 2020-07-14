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
package net.winnall.openhab.report;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import net.winnall.openhab.report.configuration.Configuration;
import net.winnall.openhab.report.configuration.Json;
import net.winnall.openhab.report.configuration.NoJsonPatternMatchException;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author Stephen Winnall
 */
public class Variables {

    private static Map<String, String> commonVariablePatterns = null;

    private final SortedMap<String, Object> variableMapping;

    // used for variables that are extracted from JsonDB
    public Variables( Map<String, Object> jsonDB, String binding, String... jsonSubPath ) {
        // set up the variables common to all bindings, models, bridges and things...
        if( commonVariablePatterns == null ) {
            commonVariablePatterns = getVariablePatterns( Json.COMMON_SUBPATH );
        }
        // make sure the common variables are copied to all instances
        this.variableMapping = new TreeMap<>();
        initialiseVariableMapping( jsonDB, commonVariablePatterns );

        // initialise the current set of variables and add to the instance
        Map<String, String> variablePatterns = getVariablePatterns( ArrayUtils
                .addAll( new String[]{ binding },
                         jsonSubPath ) );
        initialiseVariableMapping( jsonDB, variablePatterns );
    }

    // used for Binding variables (not included in JsonDB)
    public Variables( String... jsonSubPath ) {
        this.variableMapping = new TreeMap<>();
    }

    public Map<String, Object> asMap() {
        return variableMapping;
    }

    public Variables putReportables( String key, Set<? extends Reportable> reportables ) {
        Set<String> names = new TreeSet<>();
        for( Reportable reportable : reportables ) {
            names.add( reportable.getName() );
        }
        variableMapping.put( key, names );
        return this;
    }

    public Variables putVariable( String key, Object value ) {
        if( value instanceof String ) {
            value = (String) value;
        }
        variableMapping.put( key, value );
        return this;
    }

    public Variables putVariables( Variables variables ) {
        this.variableMapping.putAll( variables.asMap() );
        return this;
    }

    private Map<String, String> getVariablePatterns( String... subPaths ) {
        Map<String, String> variablePattern = Configuration.getInstance()
                .getJsonpathParameter( Map.class, subPaths );
        return variablePattern;
    }

    private void initialiseVariableMapping( Map<String, Object> jsonDB, Map<String, String> variablePatterns ) {
        for( Map.Entry<String, String> entry : variablePatterns
                .entrySet() ) {
            try {
                final Object variableValue = Json.applyPath( jsonDB,
                                                             entry.getValue() );
                // Give the people what they ask for, even if it isn't a scalar.
                // This means leaving it to the TargetApplication classes to sort out the mess
                variableMapping
                        .put( entry.getKey(), variableValue );
            } catch( NoJsonPatternMatchException njpme ) {
                // no variable pattern matches, so do nothing (not an error)
            }
        }
    }

}
