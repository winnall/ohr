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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author steve
 */
public class OpenHAB {

    private JsonDB jsonThingDB = null;

    private OpenHAB() {
    }

    /**
     *
     * @return
     */
    public static OpenHAB getInstance() {
        return OpenHABHolder.INSTANCE;
    }

    public JsonDB getJsonThingDB() {
        if( jsonThingDB == null ) {
            File jsonDBThingFile = Configuration.getInstance()
                    .getJsonDBThingPath()
                    .toFile();
            if( jsonDBThingFile == null ) {
                System.exit( Configuration.Termination.NO_JSON_THING_DB );
            }
            try {
                jsonThingDB = new ObjectMapper()
                        .readValue( jsonDBThingFile, JsonDB.class );
            } catch( IOException ex ) {
                Logger.getLogger( Configuration.class.getName() )
                        .log( Level.SEVERE, null, ex );
                System.exit( Configuration.Termination.CANNOT_OPEN_JSON_DB );
            }
        }
        return jsonThingDB;
    }

    private static class OpenHABHolder {

        private static final OpenHAB INSTANCE = new OpenHAB();

    }

    public static class JsonDB extends HashMap<String, Object> {

        public JsonDB() {
            super();
        }

    }
}
