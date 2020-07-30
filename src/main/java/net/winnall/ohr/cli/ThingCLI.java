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
package net.winnall.ohr.cli;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import net.winnall.ohr.ThingCommand;
import net.winnall.ohr.configuration.Configuration;

/**
 *
 * @author steve
 */
@Command(
         name = "things",
         description = "create Thing report"
)
public class ThingCLI extends ThingCommand {

    @Option(
             title = "link prefix",
             name = { "-p", "--prefix", "--link-prefix" },
             description = "prefix for links in generated documents"
    )
    private static String linkPrefix = Configuration.getInstance()
            .getDefaultLinkPrefix();

    @Option(
             title = "output folder",
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
             title = "JSON DB folder",
             description = "folder containing the OpenHAB JSON DB Things or Items"
    )
    private static String jsonDBFile = null;

    /**
     *
     */
    public ThingCLI() {
    }

    @Override
    public void run() {
        // set up default options
        // NB configurationFile must be opened before Configuration.getInstance
        // methods are called.
        final Configuration configuration = Configuration.getInstance();
        configuration.setOutputFolderName( outputFolder );
        configuration.setJsonDBFolderName( jsonDBFile );
        configuration.setLinkPrefix( linkPrefix );
        configuration.setZippedOutput( zippedOutput );

        super.run();
    }

}
