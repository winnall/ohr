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
import static net.winnall.openhab.report.host.MacOSHost.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Stephen Winnall
 */
public class HostTest {

    private static final String SYSTEMLIB_JSON = "systemlib.json";

    private static final String USERLIB_JSON = "userlib.json";

    private static File systemAppSupportFolder;

    private static File userAppSupportFolder;

    private static File systemAppSupportFile;

    private static File userAppSupportFile;

    public HostTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
        systemAppSupportFolder = new File( SYSTEM_APP_SUPPORT_FOLDER );
        if( !systemAppSupportFolder.exists() ) {
            systemAppSupportFolder.mkdirs();
        }
        systemAppSupportFile = new File( SYSTEM_APP_SUPPORT_FOLDER, SYSTEMLIB_JSON );
        userAppSupportFolder = new File( USER_APP_SUPPORT_FOLDER );
        if( !userAppSupportFolder.exists() ) {
            userAppSupportFolder.mkdirs();
        }
        userAppSupportFile = new File( USER_APP_SUPPORT_FOLDER, USERLIB_JSON );
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
        System.out.println( "-> setUp()" );
        systemAppSupportFile.createNewFile();
        userAppSupportFile.createNewFile();
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
        System.out.println( "-> tearDown()" );
        systemAppSupportFile.delete();
        userAppSupportFile.delete();
    }

    /**
     * Test of applyHostConventions method, of class Host.
     */
    @org.junit.jupiter.api.Test
    public void testApplyHostConventions() {
        System.out.println( "applyHostConventions" );
        Host instance = Host.getInstance();
        instance.applyHostConventions();
    }

    /**
     * Test of getHostFileSystemSupportFile method, of class Host.
     */
    @org.junit.jupiter.api.Test
    public void testGetHostFileSystemSupportFile() throws Exception {
        System.out.println( "getHostFileSystemSupportFile" );
        Host instance = Host.getInstance();
        testHostFileSystemSupportFile( instance, USERLIB_JSON );
        testHostFileSystemSupportFile( instance, SYSTEMLIB_JSON );
    }

    /**
     * Test of getInstance method, of class Host.
     */
    @org.junit.jupiter.api.Test
    public void testGetInstance() {
        System.out.println( "getInstance" );
        Host expResult = null;
        Host result = Host.getInstance();
        assertNotNull( result );
    }

    /**
     * Test of getSupportFileReadOnly method, of class Host.
     *
     * @throws java.lang.Exception
     */
    @org.junit.jupiter.api.Test
    public void testGetSupportFileReadOnly() throws Exception {
        System.out.println( "getSupportFile" );
        Host instance = Host.getInstance();
        testSupportFile( instance, USERLIB_JSON );
        testSupportFile( instance, SYSTEMLIB_JSON );
        testSupportFile( instance, "jarlib.json" );
    }

    private void testHostFileSystemSupportFile( Host instance, String filename ) throws IOException {
        File result = instance.getHostFileSystemSupportFile( filename );
        assertNotNull( result );
        if( result != null ) {
            assertTrue( result.exists() );
        } else {
            fail( filename + " not found" );
        }
    }

    private void testSupportFile( Host instance, String filename ) throws IOException {
        File result = instance.getSupportFileReadOnly( filename );
        assertNotNull( result );
        if( result != null ) {
            assertTrue( result.exists() );
        } else {
            fail( filename + " not found" );
        }
    }

    public class HostImpl extends Host {

        @Override
        public void applyHostConventions() {
        }

        @Override
        public File getHostFileSystemSupportFile( String filename ) {
            return null;
        }

        @Override
        public File getSupportFileReadWrite( String filename ) throws IOException {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }

    }

}
