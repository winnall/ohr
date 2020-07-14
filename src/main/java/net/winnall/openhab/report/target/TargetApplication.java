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
package net.winnall.openhab.report.target;

import java.nio.file.Path;
import java.util.Set;
import net.winnall.openhab.report.BindingReport;
import net.winnall.openhab.report.BridgeReport;
import net.winnall.openhab.report.ModelReport;
import net.winnall.openhab.report.ThingReport;

/**
 *
 * @author Stephen Winnall
 */
public interface TargetApplication {

    void generateBindingIndex( Path outputFolderPath, Set<BindingReport> bindingReports );

    /**
     *
     * @param bindingReport
     * @param outputFolderPath
     */
    void generateBindingReport( BindingReport bindingReport, Path outputFolderPath );

    /**
     *
     * @param bridgeReport
     * @param outputFolderPath
     */
    void prepareBridgeReport( BridgeReport bridgeReport, Path outputFolderPath );

    void prepareModelReport( ModelReport modelReport, Path outputFolderPath );

    /**
     *
     * @param thingReport
     * @param outputFolderPath
     */
    void prepareThingReport( ThingReport thingReport, Path outputFolderPath );

}
