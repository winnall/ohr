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
package net.winnall.ohr.target;

import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.winnall.ohr.BindingReport;
import net.winnall.ohr.BridgeReport;
import net.winnall.ohr.ModelReport;
import net.winnall.ohr.Reportable;
import net.winnall.ohr.ThingReport;
import net.winnall.ohr.configuration.Configuration;
import net.winnall.ohr.factory.MetaFactory;
import net.winnall.ohr.target.freemarker.TemplateEngine;
import net.winnall.ohr.target.freemarker.TemplateEngineFactory;

/**
 *
 * @author Stephen Winnall
 */
public class DokuWikiApplication implements TargetApplication {

    private static final String BINDINGS_VARIABLE = "bindings";

    private final static String BINDING_INDEX_TEMPLATE_NAME = "binding-index.ftl";

    private final static String BINDING_START_TEMPLATE_NAME = "binding-start.ftl";

    private final static String BINDING_TEMPLATE_NAME = "binding.ftl";

    private static final String BINDING_VARIABLE = "binding";

    private final static String BRIDGES_VARIABLE = "bridges";

    private final static String BRIDGE_START_TEMPLATE_NAME = "bridge-start.ftl";

    private final static String BRIDGE_TEMPLATE_NAME = "bridge.ftl";

    private final static String BRIDGE_VARIABLE = "bridge";

    private static final String FILENAME_AUTO_TXT = "auto.txt";

    private static final String FILENAME_START_TXT = "start.txt";

    private static final String FOLDER_PATH_VARIABLE = "folder_path";

    private static final String FREEMARKER_TEMPLATE_ENGINE = "freemarker";

    private final static String LINK_PREFIX_VARIABLE = "link_prefix";

    private final static String MODELS_VARIABLE = "models";

    private final static String MODEL_START_TEMPLATE_NAME = "model-start.ftl";

    private final static String MODEL_TEMPLATE_NAME = "model.ftl";

    private final static String MODEL_VARIABLE = "model";

    private final static String THINGS_VARIABLE = "things";

    private final static String THING_START_TEMPLATE_NAME = "thing-start.ftl";

    private final static String THING_TEMPLATE_NAME = "thing.ftl";

    private final static String THING_VARIABLE = "thing";

    private final TemplateEngineFactory templateEngineFactory;

    public DokuWikiApplication() {
        templateEngineFactory = MetaFactory
                .newFactory( TemplateEngineFactory.class, FREEMARKER_TEMPLATE_ENGINE );
    }

    @Override
    public void generateBindingIndex( Path folderPath, Set<BindingReport> bindingReports ) {
        FileUtilities
                .deleteOldFiles( folderPath, FILENAME_AUTO_TXT );
        FileUtilities.ensurePathExists( folderPath );
        TemplateEngine templateEngine = templateEngineFactory
                .createTemplateEngine()
                .withOutputPath( folderPath
                        .resolve( FILENAME_AUTO_TXT ) )
                .withTemplateFile( BINDING_INDEX_TEMPLATE_NAME )
                .withVariable( LINK_PREFIX_VARIABLE,
                               Configuration.getInstance()
                                       .getLinkPrefix() )
                .withVariable( BINDINGS_VARIABLE, bindingReports );
        templateEngine.run();
    }

    @Override
    public void generateBindingReport( BindingReport bindingReport, Path outputFolderPath ) {
        // {outputFolderPath}/{binding}/
        Path bindingFolderPath = outputFolderPath
                .resolve( bindingReport.getUID()
                        .getBindingName()
                        .toLowerCase() );
        FileUtilities.ensurePathExists( bindingFolderPath );
        bindingReport.getVariables()
                .putVariable( LINK_PREFIX_VARIABLE,
                              Configuration.getInstance()
                                      .getLinkPrefix() )
                .putVariable( FOLDER_PATH_VARIABLE, bindingFolderPath )
                .putVariable( BINDING_VARIABLE,
                              bindingReport.getName() )
                .putVariable( MODELS_VARIABLE, bindingReport.getModelNames() )
                .putVariable( BRIDGES_VARIABLE, bindingReport.getBridgeNames() )
                .putVariable( THINGS_VARIABLE, bindingReport.getThingNames() );
        generateAuto( bindingReport, bindingFolderPath, BINDING_TEMPLATE_NAME );
        generateStart( bindingReport, bindingFolderPath, BINDING_START_TEMPLATE_NAME );
    }

    @Override
    public void prepareBridgeReport( BridgeReport bridgeReport, Path outputFolderPath ) {
        // {outputFolderPath}/{binding}/bridge/{modelId}/{deviceId}
        final Path bridgeFolderPath = FileUtilities
                .uidToBridgePath( bridgeReport.getUID(), outputFolderPath );
        FileUtilities.ensurePathExists( bridgeFolderPath );
        bridgeReport.getVariables()
                .putVariable( LINK_PREFIX_VARIABLE,
                              Configuration.getInstance()
                                      .getLinkPrefix() )
                .putVariable( BINDING_VARIABLE, bridgeReport.getBinding() )
                .putVariable( FOLDER_PATH_VARIABLE, bridgeFolderPath )
                .putVariable( BRIDGE_VARIABLE, bridgeReport.getName() )
                .putVariable( MODEL_VARIABLE, bridgeReport.getModelId() )
                .putReportables( THINGS_VARIABLE, bridgeReport.getThings() );
        generateAuto( bridgeReport, bridgeFolderPath, BRIDGE_TEMPLATE_NAME );
        generateStart( bridgeReport, bridgeFolderPath, BRIDGE_START_TEMPLATE_NAME );
    }

    @Override
    public void prepareModelReport( ModelReport modelReport, Path outputFolderPath ) {
        // {outputFolderPath}/{binding}/thing/{modelId}
        final Path modelFolderPath = FileUtilities.uidToModelPath( modelReport
                .getUID(), outputFolderPath );
        FileUtilities.ensurePathExists( modelFolderPath );
        modelReport.getVariables()
                .putVariable( LINK_PREFIX_VARIABLE,
                              Configuration.getInstance()
                                      .getLinkPrefix() )
                .putVariable( BINDING_VARIABLE, modelReport.getBinding() )
                .putVariable( FOLDER_PATH_VARIABLE, modelFolderPath )
                .putVariable( MODEL_VARIABLE, modelReport.getName() )
                .putReportables( BRIDGES_VARIABLE, modelReport.getBridges() )
                .putReportables( THINGS_VARIABLE, modelReport.getThings() );
        generateAuto( modelReport, modelFolderPath, MODEL_TEMPLATE_NAME );
        generateStart( modelReport, modelFolderPath, MODEL_START_TEMPLATE_NAME );
    }

    @Override
    public void prepareThingReport( ThingReport thingReport, Path outputFolderPath ) {
        // {outputFolderPath}/{binding}/thing/{modelId}[/{deviceId}]
        final Path thingFolderPath = FileUtilities.uidToThingPath( thingReport
                .getUID(), outputFolderPath );
        FileUtilities.ensurePathExists( thingFolderPath );
        thingReport.getVariables()
                .putVariable( LINK_PREFIX_VARIABLE,
                              Configuration.getInstance()
                                      .getLinkPrefix() )
                .putVariable( BINDING_VARIABLE, thingReport.getBinding() )
                .putVariable( FOLDER_PATH_VARIABLE, thingFolderPath )
                .putVariable( THING_VARIABLE, thingReport.getName() )
                .putVariable( BRIDGE_VARIABLE, thingReport.getBridgeId() )
                .putVariable( MODEL_VARIABLE, thingReport.getModelId() );
        generateAuto( thingReport, thingFolderPath, THING_TEMPLATE_NAME );
        generateStart( thingReport, thingFolderPath, THING_START_TEMPLATE_NAME );
    }

    private void generateAuto( Reportable reportable, Path folderPath, String templateName ) {
        FileUtilities
                .deleteOldFiles( folderPath, FILENAME_AUTO_TXT );
        runTemplate( reportable, folderPath, templateName, FILENAME_AUTO_TXT );
    }

    private void generateStart( Reportable reportable, Path folderPath, String templateName ) {
        // do NOT overwrite an existing FILENAME_START_TXT!
        if( !startFileExists( folderPath, FILENAME_START_TXT ) ) {
            runTemplate( reportable, folderPath, templateName, FILENAME_START_TXT );
        }
    }

    private void runTemplate( Reportable reportable, Path folderPath, String templateName, String outputFileName ) {
        TemplateEngine templateEngine = templateEngineFactory
                .createTemplateEngine()
                .withOutputPath( folderPath.resolve( outputFileName ) )
                .withTemplateFile( templateName )
                .withVariables( reportable.getVariables() );
        templateEngine.run();
    }

    private boolean startFileExists( Path folderPath, String filename ) {
        boolean fileExists = folderPath.resolve( filename )
                .toFile()
                .exists();
        if( fileExists ) {
            Logger.getLogger(
                    getClass()
                            .getName() )
                    .log( Level.INFO,
                          String
                                  .format( "not overwriting %s in %s", filename, folderPath ) );
            return true;
        }
        return false;
    }

}
