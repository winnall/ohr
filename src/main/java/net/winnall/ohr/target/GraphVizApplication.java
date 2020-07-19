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

import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import java.nio.file.Path;
import java.util.Set;
import net.winnall.ohr.BindingReport;
import net.winnall.ohr.BridgeReport;
import net.winnall.ohr.ModelReport;
import net.winnall.ohr.ThingReport;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.Node;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.winnall.ohr.DeviceReport;
import net.winnall.ohr.configuration.Configuration;

/**
 *
 * @author Stephen Winnall
 */
public class GraphVizApplication implements TargetApplication {

    private static final String BINDING_CLASS = "binding";

    private static final String BRIDGE_CLASS = "bridge";

    private static final String BRIDGE_NODE = "bridges";

    private static final String CLASS_ATTRIBUTE = "class";

    private static final String EDGES_LAYER = "edges";

    private static final String FILENAME_AUTO_SVG = "auto-svg.txt";

    private static final String GRAPHVIZ_ENGINE = Configuration
            .getInstance()
            .getDefaultParameter( String.class, "graphviz-engine" );

    private static final String HOST_PATH_GRAPHVIZ_APP = Configuration
            .getInstance()
            .getDefaultParameter(
                    String.class,
                    "graphviz-app"
            );

    private static final String LAYERS_ATTRIBUTE = "layers";

    private static final String LAYER_ATTRIBUTE = "layer";

    private static final String MODEL_CLASS = "model";

    private static final String MODEL_NODE = "models";

    private static final String NODES_LAYER = "nodes";

    private static final String OPENHAB_NODE = "openHAB";

    private static final String THING_CLASS = "thing";

    private static final String THING_NODE = "devices";

    private static final String URL_ATTRIBUTE = "URL";

    public GraphVizApplication() {
        final GraphvizCmdLineEngine graphvizCmdLineEngine = new GraphvizCmdLineEngine( HOST_PATH_GRAPHVIZ_APP );
        Graphviz.useEngine( graphvizCmdLineEngine );
    }

    @Override
    public void generateBindingIndex( Path outputFolderPath, Set<BindingReport> bindingReports ) {
        FileUtilities
                .deleteOldFiles( outputFolderPath, FILENAME_AUTO_SVG );
        FileUtilities.ensurePathExists( outputFolderPath );

        // generate the graph
        Graph graphRoot = Factory.graph( OPENHAB_NODE )
                .graphAttr()
                .with( LAYERS_ATTRIBUTE,
                       String.join( ":", EDGES_LAYER, NODES_LAYER ) )
                .with( Factory.node( OPENHAB_NODE )
                        .with( URL_ATTRIBUTE,
                               FileUtilities.getWebLink() )
                        .with( LAYER_ATTRIBUTE, NODES_LAYER )
                );
        for( BindingReport bindingReport : bindingReports ) {
            graphRoot = graphRoot
                    .nodeAttr()
                    .with( LAYER_ATTRIBUTE, NODES_LAYER )
                    .linkAttr()
                    .with( LAYER_ATTRIBUTE, EDGES_LAYER )
                    .with( Factory.node( bindingReport.getName() )
                            .with( URL_ATTRIBUTE,
                                   FileUtilities.getWebLink( bindingReport ) )
                            .with( CLASS_ATTRIBUTE, BINDING_CLASS )
                            .link( OPENHAB_NODE )
                    );
        }

        GraphVizApplication.this
                .generateGraph( outputFolderPath, graphRoot, GRAPHVIZ_ENGINE );
    }

    @Override
    public void generateBindingReport( BindingReport bindingReport, Path outputFolderPath ) {
        Path bindingFolderPath = outputFolderPath
                .resolve( bindingReport.getUID()
                        .getBindingName()
                        .toLowerCase() );
        FileUtilities.ensurePathExists( bindingFolderPath );
        FileUtilities
                .deleteOldFiles( bindingFolderPath, FILENAME_AUTO_SVG );

        final String bindingReportName = bindingReport.getName();
        // generate the graph
        Graph bindingGraph = Factory.graph( bindingReportName )
                .graphAttr()
                .with( LAYERS_ATTRIBUTE,
                       String.join( ":", EDGES_LAYER, NODES_LAYER ) )
                .with( Factory.node( bindingReportName )
                        .with( URL_ATTRIBUTE, FileUtilities
                               .getWebLink( bindingReport ) )
                        .with( CLASS_ATTRIBUTE, BINDING_CLASS )
                );

        // models
        bindingGraph = addToGraph( bindingGraph,
                                   bindingReport.getModels(),
                                   bindingReportName,
                                   MODEL_NODE,
                                   MODEL_CLASS );

        // bridges
        bindingGraph = addToGraph( bindingGraph,
                                   bindingReport.getBridges(),
                                   bindingReportName,
                                   BRIDGE_NODE,
                                   BRIDGE_CLASS );

        // devices
        bindingGraph = addToGraph( bindingGraph,
                                   bindingReport.getThings(),
                                   bindingReportName,
                                   THING_NODE,
                                   THING_CLASS );

        GraphVizApplication.this
                .generateGraph( bindingFolderPath, bindingGraph, GRAPHVIZ_ENGINE );
    }

    @Override
    public void prepareBridgeReport( BridgeReport bridgeReport, Path outputFolderPath ) {
        final Path bridgeFolderPath = FileUtilities
                .uidToBridgePath( bridgeReport.getUID(), outputFolderPath );
        FileUtilities.ensurePathExists( bridgeFolderPath );
    }

    @Override
    public void prepareModelReport( ModelReport modelReport, Path outputFolderPath ) {
        final Path modelFolderPath = FileUtilities.uidToModelPath( modelReport
                .getUID(), outputFolderPath );
        FileUtilities.ensurePathExists( modelFolderPath );
    }

    @Override
    public void prepareThingReport( ThingReport thingReport, Path outputFolderPath ) {
        final Path thingFolderPath = FileUtilities.uidToThingPath( thingReport
                .getUID(), outputFolderPath );
        FileUtilities.ensurePathExists( thingFolderPath );
    }

    private <D extends DeviceReport> Graph addToGraph( Graph bindingGraph, Set<D> bindingThings, String bindingReportName, String deviceType, String deviceClass ) {
        if( !bindingThings.isEmpty() ) {
            final Node node = Factory.node( deviceType );
            bindingGraph = bindingGraph
                    .nodeAttr()
                    .with( LAYER_ATTRIBUTE, NODES_LAYER )
                    .linkAttr()
                    .with( LAYER_ATTRIBUTE, EDGES_LAYER )
                    .with( node.link( bindingReportName )
                    );
            for( D deviceReport : bindingThings ) {
                bindingGraph = bindingGraph
                        .with( Factory.node( deviceReport.getName() )
                                .with( Shape.RECTANGLE )
                                .with( URL_ATTRIBUTE, FileUtilities
                                       .getWebLink( deviceReport ) )
                                .with( CLASS_ATTRIBUTE, deviceClass )
                                .link( deviceType )
                        );
            }
        }
        return bindingGraph;
    }

    private void generateGraph( Path outputFolderPath, Graph graphRoot, String engineName ) {
        final File graphvizFile = outputFolderPath.resolve( FILENAME_AUTO_SVG )
                .toFile();
        try( PrivatePrintStream printStream = new PrivatePrintStream( graphvizFile ) ) {
            printStream.println( "<html>" );
            printStream.println( "<div class=\"svg-scrollable\">" );
            printStream.canClose( false );
            Graphviz.fromGraph( graphRoot )
                    .engine( translateEngine( engineName ) )
                    .render( Format.SVG )
                    .toOutputStream( printStream );
            printStream.canClose( true );
            printStream.print( "</div>" );
            printStream.println( "</html>" );
        } catch( IOException ex ) {
            Logger.getLogger( GraphVizApplication.class.getName() )
                    .log( Level.SEVERE, null, ex );
        }
    }

    private void linkNodes( String parent, String reportable ) {
        final MutableNode mutNode = Factory.mutNode( parent );
        Factory.mutNode( reportable )
                .addLink( mutNode );
    }

    private Engine translateEngine( String engineName ) {
        switch( engineName.toUpperCase() ) {
            case "CIRCO":
                return Engine.CIRCO;
            case "DOT":
                return Engine.DOT;
            case "NEATO":
                return Engine.NEATO;
            case "OSAGE":
                return Engine.OSAGE;
            case "TWOPI":
                return Engine.TWOPI;
            case "FDP":
                return Engine.FDP;
        }
        return Engine.FDP;
    }

    // PrivatePrintStream is needed because Graphviz.toOutputStream closes the
    // underlying file after it has finished, so nothing else can be appended to
    // it. This class makes the close method switchable...
    private class PrivatePrintStream extends PrintStream {

        private boolean closeable = true;

        public PrivatePrintStream( File file ) throws FileNotFoundException {
            super( file );
        }

        private void canClose( boolean closeable ) {
            this.closeable = closeable;
        }

        @Override
        public void close() {
            if( closeable ) {
                super.close();
            }
        }

    }
}
