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
package net.winnall.openhab.report.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import net.winnall.openhab.report.configuration.Configuration;

/**
 *
 * @author Stephen Winnall
 * @param <P>
 */
public abstract class MetaFactory<P> {

    protected final Map<String, Map> factoryJason;

    protected MetaFactory() {
        factoryJason = Configuration.getInstance()
                .getFactoryParameter( Map.class );
    }

    /**
     *
     * @param <F>
     * @param factoryType
     * @param implementation
     * @return
     */
    public static <F extends MetaFactory> F newFactory( Class<F> factoryType, String implementation ) {
        F result = null;
        try {
            result = factoryType.getDeclaredConstructor( String.class )
                    .newInstance( implementation );
        } catch( IllegalAccessException
                | IllegalArgumentException
                | InstantiationException
                | NoSuchMethodException
                | SecurityException
                | InvocationTargetException ex ) {
            assert result != null : "unable to create a factory for " + factoryType
                    .getName();
        }
        return result;
    }

    public abstract P createTemplateEngine();

}
