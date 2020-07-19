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

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.winnall.ohr.target.TargetApplication;

/**
 *
 * @author Stephen Winnall
 */
public class ModelReport extends ThingReport {

    private static final Map<UID, ModelReport> catalogue = new HashMap<>();

    private final Set<BridgeReport> bridges = new LinkedHashSet<>();

    private final Set<ThingReport> things = new LinkedHashSet<>();

    public ModelReport( BindingReport bindingReport, UID uid ) {
        super( bindingReport, uid );
    }

    public static ModelReport getUniqueModelInstance( BindingReport bindingReport, UID uid ) {
        ModelReport report = catalogue.get( uid );
        if( report == null ) {
            report = new ModelReport( bindingReport, uid );
            catalogue.put( uid, report );
        }
        return report;
    }

    public Set<BridgeReport> getBridges() {
        return bridges;
    }

    @Override
    public String getName() {
        return getModelId();
    }

    public Set<ThingReport> getThings() {
        return things;
    }

    @Override
    public void prepareReport( TargetApplication targetApplication, Path outputFolderPath ) {
        targetApplication.prepareModelReport( this, outputFolderPath );
    }

    void addBridge( BridgeReport bridgeReport ) {
        this.bridges.add( bridgeReport );
    }

    void addThing( ThingReport thingReport ) {
        this.things.add( thingReport );
    }

}
