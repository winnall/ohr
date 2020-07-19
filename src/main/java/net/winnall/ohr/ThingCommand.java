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
package net.winnall.ohr;

import net.winnall.ohr.configuration.OpenHAB;
import net.winnall.ohr.configuration.Json;
import net.winnall.ohr.configuration.Configuration;
import com.jayway.jsonpath.JsonPath;
import java.nio.file.Path;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Map;
import net.winnall.ohr.factory.MetaFactory;
import net.winnall.ohr.target.TargetApplication;
import net.winnall.ohr.target.TargetApplicationFactory;

/**
 *
 * @author steve
 */
public class ThingCommand implements Runnable {

    private final Set<BindingReport> bindingReports = new LinkedHashSet<>();

    /**
     *
     */
    public ThingCommand() {
    }

    @Override
    public void run() {
        // use the Thing database
        final Map<String, Object> jsonDB = OpenHAB.getInstance()
                .getJsonThingDB();
        // get the distinct bindings
        final Set<String> bindings = new LinkedHashSet<>( JsonPath
                .read( jsonDB, Json.BINDING_PATH ) );
        // process the bindings
        for( String binding : bindings ) {
            BindingReport bindingReport = new BindingReport( binding );
            rememberBindingReport( bindingReport );
            bindingReport.addBridges();
            bindingReport.addThings();
        }
        runTarget( "dokuwiki" );
        runTarget( "graphviz" );
    }

    private void runTarget( String targetApplicationName ) {
        TargetApplicationFactory targetApplicationFactory = MetaFactory
                .newFactory( TargetApplicationFactory.class, targetApplicationName );
        TargetApplication targetApplication = targetApplicationFactory
                .createTemplateEngine();
        generateAllReports( targetApplication,
                            Configuration.getInstance()
                                    .getBindingsFolderPath() );
    }

    private void generateAllReports( TargetApplication targetApplication, Path outputFolderPath ) {
        for( BindingReport bindingReport : bindingReports ) {
            bindingReport.prepareReport( targetApplication, outputFolderPath );
        }
        targetApplication
                .generateBindingIndex( outputFolderPath, bindingReports );
    }

    private void rememberBindingReport( BindingReport bindingReport ) {
        bindingReports.add( bindingReport );
    }

}
