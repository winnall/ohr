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
package net.winnall.openhab.report.configuration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.winnall.openhab.report.host.Host;

/**
 *
 * @author Stephen Winnall
 */
public class Defaults {

    private Defaults() {
    }

    public static Defaults getInstance() {
        return DefaultsHolder.INSTANCE;
    }

    public String getConfigurationFolder() {
        try {
            return Host.getInstance()
                    .getSupportFileReadOnly( "default.conf.json" )
                    .toString();
        } catch( IOException ex ) {
            Logger.getLogger( Defaults.class.getName() )
                    .log( Level.SEVERE, null, ex );
        }
        return null;
    }

    private static class DefaultsHolder {

        private static final Defaults INSTANCE = new Defaults();

    }
}
