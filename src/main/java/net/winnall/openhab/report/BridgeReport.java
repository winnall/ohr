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
package net.winnall.openhab.report;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.winnall.openhab.report.target.TargetApplication;

/**
 *
 * @author steve
 */
public class BridgeReport extends ThingReport {

    private static final Map<UID, BridgeReport> catalogue = new HashMap<>();

    private Set<ThingReport> things = new LinkedHashSet<>();

    protected BridgeReport( BindingReport bindingReport, UID uid ) {
        super( bindingReport, uid );
    }

    public static BridgeReport getUniqueBridgeInstance( BindingReport bindingReport, UID uid ) {
        BridgeReport report = catalogue.get( uid );
        if( report == null ) {
            report = new BridgeReport( bindingReport, uid );
            catalogue.put( uid, report );
        }
        return report;
    }

    public void addThing( ThingReport thingReport ) {
        this.things.add( thingReport );
    }

    @Override
    public String getName() {
        return getBridgeId();
    }

    public Set<ThingReport> getThings() {
        return things;
    }

    @Override
    public void prepareReport( TargetApplication targetApplication, Path outputFolderPath ) {
        targetApplication.prepareBridgeReport( this, outputFolderPath );
    }

}
