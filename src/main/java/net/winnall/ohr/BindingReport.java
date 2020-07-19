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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.winnall.ohr.configuration.Json;
import net.winnall.ohr.configuration.NoJsonPatternMatchException;
import net.winnall.ohr.configuration.OpenHAB;
import net.winnall.ohr.target.TargetApplication;

public class BindingReport implements Reportable {

    private final String binding;

    private final Set<BridgeReport> bridges = new LinkedHashSet<>();

    private final Set<ModelReport> models = new LinkedHashSet<>();

    private final UID ownUID;

    private boolean reportOK = true;

    private final Set<ThingReport> things = new LinkedHashSet<>();

    private final Variables variables;

    public BindingReport( String binding ) {
        this.binding = binding;
        this.ownUID = new UID( binding );
        this.variables = new Variables();
    }

    public void addBridges() {
        try {
            List<Map<String, Object>> bridgeJson = getJsonList( Json.BRIDGE_VALUE_PATH );
            for( Map<String, Object> jsonValue : bridgeJson ) {
                // record the bridge
                recordBridge( jsonValue );
                // record the model
                recordModel( jsonValue );
            }
        } catch( NoJsonPatternMatchException njpme ) {
            // no variable pattern matches, so do nothing (not an error)
        }
    }

    public void addThings() {
        try {
            List<Map<String, Object>> thingJson = getJsonList( Json.THING_VALUE_PATH );
            for( Map<String, Object> jsonValue : thingJson ) {
                // record the thing
                recordThing( jsonValue );
                // record the model
                recordModel( jsonValue );
            }
        } catch( NoJsonPatternMatchException njpme ) {
            // no variable pattern matches, so do nothing (not an error)
        }
    }

    public Set<String> getBridgeNames() {
        Set<String> result = new TreeSet<>();
        bridges.forEach( ( bridge ) -> {
            result.add( bridge.getBridgeId() );
        } );
        return result;
    }

    public Set<BridgeReport> getBridges() {
        return bridges;
    }

    public Set<String> getModelNames() {
        Set<String> result = new TreeSet<>();
        models.forEach( ( model ) -> {
            result.add( model.getModelId() );
        } );
        return result;
    }

    public Set<ModelReport> getModels() {
        return models;
    }

    @Override
    public String getName() {
        return getUID()
                .getBindingName();
    }

    public Set<String> getThingNames() {
        Set<String> result = new TreeSet<>();
        things.forEach( ( thing ) -> {
            result.add( thing.getDeviceId() );
        } );
        return result;
    }

    public Set<ThingReport> getThings() {
        return things;
    }

    @Override
    public UID getUID() {
        return ownUID;
    }

    @Override
    public Variables getVariables() {
        return variables;
    }

    @Override
    public void prepareReport( TargetApplication targetApplication, Path outputFolderPath ) {
        assert reportOK : "errors during report generation - no report created";
        targetApplication
                .generateBindingReport( this, outputFolderPath );
        for( ModelReport modelReport : models ) {
            modelReport.prepareReport( targetApplication, outputFolderPath );
        }
        for( BridgeReport bridgeReport : bridges ) {
            bridgeReport.prepareReport( targetApplication, outputFolderPath );
        }
        for( ThingReport thingReport : things ) {
            thingReport.prepareReport( targetApplication, outputFolderPath );
        }
    }

    private List<Map<String, Object>> getJsonList( String jsonPath ) throws NoJsonPatternMatchException {
        return (List) Json
                .applyPath( List.class, OpenHAB.getInstance()
                            .getJsonThingDB(), String
                                    .format( jsonPath, binding ) );
    }

    private UID readBridgeUID( Map<String, Object> jsonValue ) throws NoJsonPatternMatchException {
        return new UID( Json
                .applyPath( List.class, jsonValue, Json.BRIDGE_UID_SEGMENTS_PATH ) );
    }

    private UID readThingTypeUID( Map<String, Object> jsonValue ) throws NoJsonPatternMatchException {
        return new UID( Json
                .applyPath( List.class, jsonValue, Json.THING_TYPE_UID_SEGMENTS_PATH ) );
    }

    private UID readUID( Map<String, Object> jsonValue ) throws NoJsonPatternMatchException {
        return new UID( Json
                .applyPath( List.class, jsonValue, Json.UID_SEGMENTS_PATH ) );
    }

    private void recordBridge( Map<String, Object> jsonValue ) {
        try {
            BridgeReport bridgeReport = BridgeReport
                    .getUniqueBridgeInstance( this, readUID( jsonValue ) );
            bridges.add( bridgeReport );
            bridgeReport
                    .setVariables( new Variables( jsonValue, binding, Json.BRIDGE_SUBPATH ) );
            // add model information
            UID modelUID = readThingTypeUID( jsonValue );
            ModelReport modelReport = ModelReport
                    .getUniqueModelInstance( this, modelUID );
            bridgeReport.setModel( modelReport );
            modelReport.addBridge( bridgeReport );
        } catch( NoJsonPatternMatchException njpme ) {
            // no variable pattern matches, so do nothing (not an error)
        }
    }

    private void recordModel( Map<String, Object> jsonValue ) throws NoJsonPatternMatchException {
        ModelReport modelReport = ModelReport
                .getUniqueModelInstance( this, readThingTypeUID( jsonValue ) );
        models.add( modelReport );
        modelReport
                .setVariables( new Variables( jsonValue, binding, Json.MODEL_SUBPATH ) );
    }

    private void recordThing( Map<String, Object> jsonValue ) {
        try {
            ThingReport thingReport = ThingReport
                    .getUniqueThingInstance( this, readUID( jsonValue ) );
            things.add( thingReport );
            thingReport
                    .setVariables( new Variables( jsonValue, binding, Json.THING_SUBPATH ) );
            // add model information
            UID modelUID = readThingTypeUID( jsonValue );
            ModelReport modelReport = ModelReport
                    .getUniqueModelInstance( this, modelUID );
            thingReport.setModel( modelReport );
            modelReport.addThing( thingReport );
            // add bridge information
            UID bridgeUID = readBridgeUID( jsonValue );
            BridgeReport bridgeReport = BridgeReport
                    .getUniqueBridgeInstance( this, bridgeUID );
            thingReport.setBridge( bridgeReport );
            bridgeReport.addThing( thingReport );
        } catch( NoJsonPatternMatchException njpme ) {
            // no variable pattern matches, so do nothing (not an error)
        }
    }

}
