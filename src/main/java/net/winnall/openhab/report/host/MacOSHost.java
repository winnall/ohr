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
package net.winnall.openhab.report.host;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import oshi.SystemInfo;

/**
 *
 * @author Stephen Winnall
 */
public class MacOSHost extends Host {

    final static String LIBRARY_APPLICATION_SUPPORT = "/Library/Application Support/"
                                                      + APPLICATION_NAME + "/";

    final static String SYSTEM_APP_SUPPORT_FOLDER = LIBRARY_APPLICATION_SUPPORT;

    private final static String USER_HOME = System.getProperty( "user.home" );

    final static String USER_APP_SUPPORT_FOLDER = USER_HOME + LIBRARY_APPLICATION_SUPPORT;

    private final SystemInfo systemInfo;

    MacOSHost( SystemInfo systemInfo ) {
        this.systemInfo = systemInfo;
    }

    @Override
    public void applyHostConventions() {
        System.setProperty( "apple.laf.useScreenMenuBar", "true" );
    }

    /**
     *
     * @param filename
     * @return
     * @throws IOException
     */
    @Override
    public File getSupportFileReadWrite( String filename ) throws IOException {
        File readWriteFile = new File( USER_APP_SUPPORT_FOLDER + filename );
        if( !readWriteFile.exists() ) {
            File templateFile = getSupportFileReadOnly( filename );
            Files.copy( templateFile.toPath(), readWriteFile.toPath() );
        }
        readWriteFile.setWritable( true );
        return readWriteFile;
    }

    @Override
    protected File getHostFileSystemSupportFile( String filename ) {
        String userAppSupport = USER_APP_SUPPORT_FOLDER + filename;
        File supportFile = new File( userAppSupport );
        if( supportFile.exists() ) {
            return supportFile;
        }
        String systemAppSupport = SYSTEM_APP_SUPPORT_FOLDER + filename;
        supportFile = new File( systemAppSupport );
        if( supportFile.exists() ) {
            return supportFile;
        }
        return null;
    }

}
