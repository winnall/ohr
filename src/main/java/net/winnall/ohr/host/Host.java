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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

/**
 * This abstract class provides access to features of the host platform that are
 * needed by the application. The class is implemented as a <em>singleton</em>
 * (<code>getInstance()</code>) which delivers an implementation appropriate to
 * the current host environment, determined by using
 * <code>oshi.SystemInfo</code>. The host platforms currently known are macOS,
 * Linux, Unix, Windows and iOS.
 *
 * As of this writing, the only available implementation of <code>Host</code> is
 * <code>MacOS</code>. Stubs are provided for the other platforms mentioned
 * above.
 *
 * @author steve
 */
public abstract class Host {

    /**
     * This is the name of the application as displayed to the end user on the
     * desktop.
     */
    public final static String APPLICATION_NAME = "OpenHAB Reporter";

    /**
     * This method always returns the instantiated instance of the
     * <code>Host</code> singleton. On its first call, it performs the
     * instantiation. The version here is not thread-safe.
     *
     * @return
     */
    public static Host getInstance() {
        return HostHolder.INSTANCE;
    }

    /**
     * This method is provided as a hook to set up any specific constructs that
     * enable the application to adhere to the host platform's look and feel
     * conventions.
     */
    public abstract void applyHostConventions();

    /**
     * This method fetches the named support file and opens it for reading. Its
     * behaviour differs from <code>applyHostConventions</code> only in that the
     * opened file is read-only and no copy is made in application-specific user
     * space.
     *
     * @param filename Name of the required file
     * @return A readable <code>File</code> object (may be in user space, system
     * space or the application JAR file)
     */
    public InputStream getInputSupportFile( String filename ) {
        // check the default locations on the current host
        InputStream inputStream = getInputHostSupportFile( filename );
        // otherwise look in JAR file
        if( inputStream == null ) {
            inputStream = getInputJarSupportFile( filename );
        }
        return inputStream;
    }

    public String getInputSupportFolderName( String folderName ) {
        // check the default locations on the current host
        String supportFolderName = getInputHostSupportFolderName( folderName );
        // otherwise look in JAR file
        if( supportFolderName == null ) {
            supportFolderName = getInputJarSupportFolderName( folderName );
        }
        return supportFolderName;
    }

    /**
     * This method fetches the named support file and opens it for reading and
     * writing. The exact behaviour is specified by the host platform. In
     * general, the file will be looked for in an application-specific location
     * in the following order:
     * <ol>
     * <li>user space</li>
     * <li>system space</li>
     * <li>the application JAR file</li>
     * </ol>
     *
     * The writable version of the file is created or updated in the
     * application-specific location (as above) in user space. This method is
     * required to conform to the conventions of the host platform.
     *
     * @param filename Name of the required file
     * @return An writable <code>File</code> object in user space
     * @throws IOException Anything that might result from a <code>File</code>
     * operation.
     */
    public abstract OutputStream getOutputSupportFile( String filename ) throws IOException;

    /**
     * This method should return the name of the user's home folder on the given
     * platform. In many cases, <code>System.getProperty("user.home")</code> is
     * adequate, but apparently it does not always work satisfactorily for
     * Windows, so should be overridden.
     *
     * @return the user's home folder
     */
    public String getUserHome() {
        return System.getProperty( "user.home" );
    }

    /**
     * This method fetches the named support file and opens it for reading. Its
     * behaviour differs from <code>getInputSupportFile</code> only in that the
     * opened file is in an application-specific location in user space or
     * system space (i.e. it is not in the JAR file).
     *
     * @param filename
     * @return
     */
    protected abstract InputStream getInputHostSupportFile( String filename );

    protected InputStream getInputJarSupportFile( String filename ) {
        final URL filenameResource = getClass()
                .getResource( filename );
        // The following will work from the IDE:
        try {
            return filenameResource.openStream();
        } catch( IllegalArgumentException
                | IOException ex ) {
            // ignore - try the following method instead
        }
        // The IDE version hasn't worked, so try the following:
        try {
            return new FileInputStream( filenameResource.toExternalForm() );
        } catch( FileNotFoundException ex ) {
            Logger.getLogger( Host.class.getName() )
                    .log( Level.WARNING, null, ex );
        }
        // failure all along the line
        return InputStream.nullInputStream();
    }

    protected abstract String getInputHostSupportFolderName( String folderName );

    private String getInputJarSupportFolderName( String folderName ) {
        final URL folderResource = getClass()
                .getResource( folderName );
        // The following will work from the IDE:
        try {
            return folderResource.toURI()
                    .toString();
        } catch( IllegalArgumentException
                | URISyntaxException ex ) {
            // ignore - try the following method instead
        }
        // The IDE version hasn't worked, so try the following:
        return folderResource.toExternalForm();
    }

    private static class HostHolder {

        private static final Host INSTANCE = hostFactory();

        private static Host hostFactory() {
            SystemInfo systemInfo = new SystemInfo();
            OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
            switch( operatingSystem.getFamily() ) {
                case "macOS":
                    return new MacOSHost( systemInfo );
                case "linux":
                    return new LinuxHost( systemInfo );
                case "unix":
                    return new UnixHost( systemInfo );
                case "windows":
                    return new WindowsHost( systemInfo );
                case "iOS":
                    return new IOSHost( systemInfo );
                default:
            }
            return null;
        }

    }
}
