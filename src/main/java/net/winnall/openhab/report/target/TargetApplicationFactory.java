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
package net.winnall.openhab.report.target;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.winnall.openhab.report.factory.MetaFactory;

/**
 *
 * @author Stephen Winnall
 */
public class TargetApplicationFactory extends MetaFactory<TargetApplication> {

    private Class<? extends TargetApplication> targetApplicationClass;

    private final Map<String, String> targetApplications;

    public TargetApplicationFactory( String implementation ) {
        super();
        this.targetApplications = super.factoryJason.get( "target-application" );
        assert targetApplications.keySet()
                .contains( implementation ) : "unknown TargetApplication: " + implementation;
        String targetApplicationClassName = targetApplications
                .get( implementation );
        try {
            this.targetApplicationClass = (Class<? extends TargetApplication>) Class
                    .forName( targetApplicationClassName );
        } catch( ClassNotFoundException ex ) {
            Logger.getLogger( TargetApplicationFactory.class.getName() )
                    .log( Level.SEVERE, null, ex );
            this.targetApplicationClass = null;
        }
    }

    @Override
    public TargetApplication createTemplateEngine() {
        try {
            return targetApplicationClass.getDeclaredConstructor()
                    .newInstance();
        } catch( IllegalAccessException
                | IllegalArgumentException
                | InstantiationException
                | NoSuchMethodException
                | SecurityException
                | InvocationTargetException ex ) {
            Logger.getLogger( getClass()
                    .getName() )
                    .log( Level.SEVERE, "factory" + targetApplicationClass
                          .getName() + "cannot generate product", ex );
        }
        return null;
    }

}
