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
import net.winnall.openhab.report.target.TargetApplication;

/**
 *
 * @author Stephen Winnall
 */
public interface Reportable {

    String getName();

    UID getUID();

    Variables getVariables();

    /**
     *
     * @param application
     * @param outputFolderPath
     */
    void prepareReport( TargetApplication application, Path outputFolderPath );

}
