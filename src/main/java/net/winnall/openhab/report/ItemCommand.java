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

import com.github.rvesse.airline.annotations.Command;

/**
 *
 * @author steve
 */
@Command( name = "item-command",
          description = "Create Item report" )
public class ItemCommand implements Runnable {

    @Override
    public void run() {
        System.out.println( "generating an Item report" );
    }

}
