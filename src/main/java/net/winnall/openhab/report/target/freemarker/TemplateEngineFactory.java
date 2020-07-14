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
package net.winnall.openhab.report.target.freemarker;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.winnall.openhab.report.factory.MetaFactory;

/**
 *
 * @author Stephen Winnall
 */
public class TemplateEngineFactory extends MetaFactory<TemplateEngine> {

    private Class<? extends TemplateEngine> templateClass;

    private final Map<String, String> templateEngines;

    public TemplateEngineFactory( String implementation ) {
        super();
        this.templateEngines = super.factoryJason.get( "template-engine" );
        assert templateEngines.keySet()
                .contains( implementation ) : "unknown TemplateEngine: " + implementation;
        String templateClassName = templateEngines.get( implementation );
        try {
            this.templateClass = (Class<? extends TemplateEngine>) Class
                    .forName( templateClassName );
        } catch( ClassNotFoundException ex ) {
            Logger.getLogger( TemplateEngineFactory.class.getName() )
                    .log( Level.SEVERE, null, ex );
            this.templateClass = null;
        }
    }

    @Override
    public TemplateEngine createTemplateEngine() {
        try {
            return templateClass.getDeclaredConstructor()
                    .newInstance();
        } catch( IllegalAccessException
                | IllegalArgumentException
                | InstantiationException
                | NoSuchMethodException
                | SecurityException
                | InvocationTargetException ex ) {
            Logger.getLogger( getClass()
                    .getName() )
                    .log( Level.SEVERE, "factory" + templateClass.getName() + "cannot generate product", ex );
        }
        return null;
    }

}
