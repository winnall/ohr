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
package net.winnall.openhab.report.cli;

import net.winnall.openhab.report.configuration.Configuration;
import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.help.Help;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author steve
 */
@com.github.rvesse.airline.annotations.Cli(
         name = "openhab-report",
         description = "OpenHAB Reporter",
         defaultCommand = ThingCLI.class,
         commands = { ThingCLI.class, Help.class }
)
@Command(
         name = "openhab-report",
         description = "create Thing report"
)
public class MainCLI {

    @Option(
             name = { "-p", "--prefix", "--link-prefix" },
             description = "prefix for links in generated documents"
    )
    private static String linkPrefix = Configuration.getInstance()
            .getDefaultLinkPrefix();

    @Option(
             name = { "-o", "--output" },
             description = "report output folder"
    )
    private static String outputFolder = null;

    @Option(
             name = { "-z", "--zip", "--zipped-output" },
             description = "make a .zip of the output folder"
    )
    private static boolean zippedOutput = false;

    @Arguments(
             title = "json",
             description = "file containing the OpenHAB JSON DB Things or Items"
    )
    private static String jsonDBFile = null;

    /**
     *
     * @param arguments
     */
    public static void main( String[] arguments ) {
        Logger.getLogger( MainCLI.class.getName() )
                .log( Level.CONFIG, "starting main()" );
        Cli<Runnable> cli = new Cli<>( MainCLI.class );
        Runnable cmd = cli.parse( arguments );
        // set up default options
        // NB configurationFile must be opened before Configuration.getInstance
        // methods are called.
        final Configuration configuration = Configuration.getInstance();
        configuration.setOutputFolderName( outputFolder );
        configuration.setJsonDBFolderName( jsonDBFile );
        configuration.setLinkPrefix( linkPrefix );
        configuration.setZippedOutput( zippedOutput );
        // run the program
        cmd.run();
    }

}
