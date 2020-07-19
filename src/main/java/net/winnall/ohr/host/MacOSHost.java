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
package net.winnall.ohr.host;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import oshi.SystemInfo;

/**
 *
 * @author Stephen Winnall
 */
public class MacOSHost extends Host {

    final static String LIBRARY_APPLICATION_SUPPORT = "/Library/Application Support/"
                                                      + APPLICATION_NAME;

    final static String SYSTEM_APP_SUPPORT_FOLDER = LIBRARY_APPLICATION_SUPPORT;

    private final static String USER_HOME = System.getProperty( "user.home" );

    final static String USER_APP_SUPPORT_FOLDER = USER_HOME + LIBRARY_APPLICATION_SUPPORT;

    private final SystemInfo systemInfo;

    MacOSHost( SystemInfo systemInfo ) {
        this.systemInfo = systemInfo;
    }

    @Override
    public void applyHostConventions() {
    }

    /**
     *
     * @param filename
     * @return
     * @throws IOException
     */
    @Override
    public OutputStream getOutputSupportFile( String filename ) throws IOException {
        return new FileOutputStream( USER_APP_SUPPORT_FOLDER + filename );
    }

    @Override
    protected InputStream getInputHostSupportFile( String filename ) {
        String userAppSupport = USER_APP_SUPPORT_FOLDER + filename;
        try {
            return new FileInputStream( userAppSupport );
        } catch( FileNotFoundException ex ) {
            // ignore - try SYSTEM_APP_SUPPORT_FOLDER
        }
        String systemAppSupport = SYSTEM_APP_SUPPORT_FOLDER + filename;
        try {
            return new FileInputStream( systemAppSupport );
        } catch( FileNotFoundException ex ) {
            // we signal this exception by returning null
        }
        return null;
    }

    @Override
    protected String getInputHostSupportFolderName( String folderName ) {
        String userAppSupport = USER_APP_SUPPORT_FOLDER + folderName;
        if( new File( userAppSupport ).exists() ) {
            return userAppSupport;
        }

        String systemAppSupport = SYSTEM_APP_SUPPORT_FOLDER + folderName;
        if( new File( systemAppSupport ).exists() ) {
            return systemAppSupport;
        }

        return null;
    }

}
