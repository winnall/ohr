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
import java.io.IOException;
import java.io.InputStream;
import static net.winnall.ohr.host.MacOSHost.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Stephen Winnall
 */
public class HostTest {

    private static final String JARLIB_JSON = "/jarlib.json";

    private static final String SYSTEMLIB_JSON = "/systemlib.json";

    private static final String USERLIB_JSON = "/userlib.json";

    private static File systemAppSupportFile;

    private static File systemAppSupportFolder;

    private static File userAppSupportFile;

    private static File userAppSupportFolder;

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

    @org.junit.jupiter.api.Test
    public void testGetInputHostSupportFile() throws Exception {
        System.out.println( "getGetInputHostSupportFile" );
        Host instance = Host.getInstance();
        testGetInputHostSupportFile( instance, USERLIB_JSON );
        testGetInputHostSupportFile( instance, SYSTEMLIB_JSON );
    }

    /**
     * Test of getInputSupportFile method, of class Host.
     *
     * @throws java.lang.Exception
     */
    @org.junit.jupiter.api.Test
    public void testGetInputSupportFile() throws Exception {
        System.out.println( "getInputSupportFile" );
        Host instance = Host.getInstance();
        testGetInputSupportFile( instance, USERLIB_JSON );
        testGetInputSupportFile( instance, SYSTEMLIB_JSON );
        testGetInputSupportFile( instance, JARLIB_JSON );
    }

    /**
     * Test of getInstance method, of class Host.
     */
    @org.junit.jupiter.api.Test
    public void testGetInstance() {
        System.out.println( "getInstance" );
        Host result = Host.getInstance();
        assertNotNull( result );
    }

    private void testGetInputHostSupportFile( Host instance, String filename ) throws IOException {
        InputStream result = instance.getInputHostSupportFile( filename );
        assertNotNull( result );
    }

    private void testGetInputSupportFile( Host instance, String filename ) throws IOException {
        InputStream result = instance.getInputSupportFile( filename );
        assertTrue( result != InputStream.nullInputStream() );
    }

}
