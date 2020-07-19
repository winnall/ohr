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
package net.winnall.ohr.target;

import java.io.File;
import java.nio.file.Path;
import net.winnall.ohr.BindingReport;
import net.winnall.ohr.BridgeReport;
import net.winnall.ohr.DeviceReport;
import net.winnall.ohr.ModelReport;
import net.winnall.ohr.ThingReport;
import net.winnall.ohr.UID;
import net.winnall.ohr.configuration.Configuration;

/**
 *
 * @author Stephen Winnall
 */
class FileUtilities {

    private static final String BRIDGE_SUBDIR = "bridges";

    private static final String MODEL_SUBDIR = "models";

    private static final String THING_SUBDIR = "things";

    private static final String WEB_ROOT_TEMPLATE = "doku.php?id=%s:start";

    private static final String WEB_BINDING_TEMPLATE = "doku.php?id=%s:bindings:%s:start";

    private static final String WEB_DEVICE_TEMPLATE = "doku.php?id=%s:bindings:%s:%s:%s:start";

    static void deleteOldFiles( Path folderPath, String filename ) {
        final File autoFile = folderPath.resolve( filename )
                .toFile();
        if( autoFile.exists() ) {
            autoFile.delete();
        }
    }

    static void ensurePathExists( Path path ) {
        final File pathFolder = path.toFile();
        if( !pathFolder.exists() ) {
            final boolean pathCreated = pathFolder.mkdirs();
            assert pathCreated : "could not create a folder called " + path
                    .toString();
        }
    }

    static String getWebLink() {
        return String.format( WEB_ROOT_TEMPLATE,
                              Configuration.getInstance()
                                      .getLinkPrefix()
        );
    }

    static String getWebLink( BindingReport bindingReport ) {
        return String.format( WEB_BINDING_TEMPLATE,
                              Configuration.getInstance()
                                      .getLinkPrefix(),
                              bindingReport.getName()
        );
    }

    static String getWebLink( DeviceReport deviceReport ) {
        String subdir;
        String id;
        if( deviceReport instanceof BridgeReport ) {
            subdir = BRIDGE_SUBDIR;
            id = deviceReport.getDeviceId();
        } else if( deviceReport instanceof ModelReport ) {
            subdir = MODEL_SUBDIR;
            id = deviceReport.getModelId();
        } else {
            // assume it's a thing
            subdir = THING_SUBDIR;
            id = deviceReport.getDeviceId();
        }
        return String.format( WEB_DEVICE_TEMPLATE,
                              Configuration.getInstance()
                                      .getLinkPrefix(),
                              deviceReport.getBindingReport()
                                      .getName(),
                              subdir,
                              id.toLowerCase()
        );
    }

    static Path uidToBridgePath( UID uid, Path outputFolderPath ) {
        return outputFolderPath
                .resolve( uid.getBindingName() )
                .resolve( BRIDGE_SUBDIR )
                .resolve( uid.getDeviceId()
                        .toLowerCase() );
    }

    static Path uidToModelPath( UID uid, Path outputFolderPath ) {
        return outputFolderPath
                .resolve( uid.getBindingName() )
                .resolve( MODEL_SUBDIR )
                .resolve( uid.getModelId()
                        .toLowerCase() );
    }

    static Path uidToThingPath( UID uid, Path outputFolderPath ) {
        return outputFolderPath
                .resolve( uid.getBindingName() )
                .resolve( THING_SUBDIR )
                .resolve( uid.getDeviceId()
                        .toLowerCase() );
    }

}
