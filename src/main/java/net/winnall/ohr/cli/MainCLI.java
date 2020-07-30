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

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.help.Help;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.winnall.ohr.fx.MainUI;

/**
 *
 * @author steve
 */
@com.github.rvesse.airline.annotations.Cli(
         name = "ohr",
         description = "OpenHAB Reporter",
         defaultCommand = MainUI.class,
         commands = { ThingCLI.class, MainUI.class, Help.class }
)
@Command(
         name = "ohr",
         description = "create Thing report"
)
public class MainCLI {

    /**
     *
     * @param arguments
     */
    public static void main( String[] arguments ) {
        Logger.getLogger( MainCLI.class.getName() )
                .log( Level.CONFIG, "starting main()" );
        Cli<Runnable> cli = new Cli<>( MainCLI.class );
        Runnable cmd = cli.parse( arguments );
        // run the program
        cmd.run();
    }

}
