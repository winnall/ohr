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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.winnall.ohr.target.TargetApplication;

/**
 *
 * @author steve
 */
public class ThingReport implements DeviceReport {

    private static final Map<UID, ThingReport> catalogue = new HashMap<>();

    private String binding = null;

    private BindingReport bindingReport = null;

    private String bridgeId = null;

    private BridgeReport bridgeReport;

    private UID bridgeUID = null;

    private Map<String, Object> common = null;

    private String deviceId = null;

    private String modelId = null;

    private ModelReport modelReport;

    private UID modelUID;

    private final UID ownUID;

    private Variables variables = null;

    /**
     *
     * @param bindingReport
     * @param uid
     */
    protected ThingReport( BindingReport bindingReport, UID uid ) {
        this.bindingReport = bindingReport;
        this.ownUID = uid;
        // copy the uid
        int index = 0;
        for( String segment : uid ) {
            switch( index ) {
                case 3:
                    setDeviceId( segment );
                    break;
                case 2:
                    setBridgeId( segment );
                    break;
                case 1:
                    setModelId( segment );
                    break;
                case 0:
                    assert segment.equals( bindingReport.getName() ) : String
                            .format( "binding names go not match: %s, %s",
                                     segment,
                                     bindingReport.getName() );
                    setBinding( segment );
                    break;
                default:
                    throw new ReportUIDException( "UID with strange size" );
            }
            index++;
        }
        setBridgeUID( new UID( Arrays
                .asList( getBinding(), getModelId(), getBridgeId() ) ) );
        BridgeThingLinkCatalogue.getInstance()
                .newLink( bridgeUID, uid );
    }

    public static ThingReport getUniqueThingInstance( BindingReport bindingReport, UID uid ) {
        ThingReport report = catalogue.get( uid );
        if( report == null ) {
            report = new ThingReport( bindingReport, uid );
            catalogue.put( uid, report );
        }
        return report;
    }

    /**
     *
     * @return
     */
    @Override
    public String getBinding() {
        return binding;
    }

    /**
     *
     * @param binding
     */
    private void setBinding( String binding ) {
        this.binding = binding;
    }

    /**
     *
     * @return
     */
    @Override
    public BindingReport getBindingReport() {
        return bindingReport;
    }

    public BridgeReport getBridge() {
        return bridgeReport;
    }

    public void setBridge( BridgeReport bridgeReport ) {
        this.bridgeReport = bridgeReport;
    }

    /**
     *
     * @return
     */
    @Override
    public final String getBridgeId() {
        return bridgeId;
    }

    /**
     *
     * @param bridgeId
     */
    private void setBridgeId( String bridgeId ) {
        this.bridgeId = bridgeId;
    }

    public UID getBridgeUID() {
        return bridgeUID;
    }

    public final void setBridgeUID( UID bridgeUID ) {
        this.bridgeUID = bridgeUID;
    }

    /**
     *
     * @return
     */
    @Override
    public Map<String, Object> getCommon() {
        return common;
    }

    /**
     *
     * @param common
     */
    @Override
    public void setCommon( Map<String, Object> common ) {
        this.common = common;
    }

    /**
     *
     * @return
     */
    @Override
    public final String getDeviceId() {
        if( deviceId == null ) {
            return bridgeId;
        }
        return deviceId;
    }

    /**
     *
     * @param deviceId
     */
    private void setDeviceId( String deviceId ) {
        this.deviceId = deviceId;
    }

    public ModelReport getModel() {
        return modelReport;
    }

    public void setModel( ModelReport modelReport ) {
        this.modelReport = modelReport;
    }

    /**
     *
     * @return
     */
    @Override
    final public String getModelId() {
        return modelId;
    }

    /**
     *
     * @param modelId
     */
    private void setModelId( String modelId ) {
        this.modelId = modelId;
    }

    public UID getModelUID() {
        return modelUID;
    }

    public void setModelUID( UID modelUID ) {
        this.modelUID = modelUID;
    }

    @Override
    public String getName() {
        return getDeviceId();
    }

    /**
     *
     * @return
     */
    @Override
    public UID getUID() {
        return ownUID;
    }

    @Override
    public Variables getVariables() {
        return variables;
    }

    /**
     *
     * @param variables
     */
    @Override
    public void setVariables( Variables variables ) {
        this.variables = variables;
    }

    /**
     *
     * @param targetApplication
     * @param outputFolderPath
     */
    @Override
    public void prepareReport( TargetApplication targetApplication, Path outputFolderPath ) {
        targetApplication.prepareThingReport( this, outputFolderPath );
    }

}
