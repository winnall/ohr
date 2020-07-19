package net.winnall.ohr.target.freemarker;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleCollection;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.winnall.ohr.Reportable;
import net.winnall.ohr.Variables;

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
/**
 *
 * @author Stephen Winnall
 */
public class Freemarker implements TemplateEngine {

    private Configuration freemarkerConfiguration;

    private Template freemarkerTemplate;

    private Path outputPath;

    private final Map<String, Object> variables = new HashMap<>();

    public Freemarker() {
        freemarkerConfiguration = new Configuration( Configuration.VERSION_2_3_30 );
        freemarkerConfiguration
                .setClassForTemplateLoading(
                        Freemarker.class,
                        net.winnall.ohr.configuration.Configuration
                                .getInstance()
                                .getTemplateFolderPath()
                );
        // Recommended settings for new projects:
        freemarkerConfiguration.setDefaultEncoding( "UTF-8" );
        freemarkerConfiguration
                .setTemplateExceptionHandler( TemplateExceptionHandler.RETHROW_HANDLER );
        freemarkerConfiguration.setLogTemplateExceptions( false );
        freemarkerConfiguration.setWrapUncheckedExceptions( true );
        freemarkerConfiguration.setFallbackOnNullLoopVariable( false );
        freemarkerConfiguration.setBooleanFormat( "c" );
    }

    @Override
    public void run() {
        FileWriter fileWriter = null;
        try {
            final File outputFile = this.outputPath.toFile();
            fileWriter = new FileWriter( outputFile );
            this.freemarkerTemplate
                    .process( variables, fileWriter );
        } catch( TemplateException
                | IOException ex ) {
            Logger.getLogger( Freemarker.class.getName() )
                    .log( Level.SEVERE, this.outputPath.toString(), ex );
        } finally {
            try {
                if( fileWriter != null ) {
                    fileWriter.close();
                }
            } catch( IOException ex ) {
                Logger.getLogger( Freemarker.class.getName() )
                        .log( Level.SEVERE, null, ex );
            }
        }
    }

    @Override
    public TemplateEngine withOutputPath( Path path ) {
        this.outputPath = path;
        return this;
    }

    @Override
    public TemplateEngine withTemplateFile( String templateFileName ) {
        try {
            this.freemarkerTemplate = this.freemarkerConfiguration
                    .getTemplate( templateFileName );
        } catch( IOException ex ) {
            Logger.getLogger( Freemarker.class.getName() )
                    .log( Level.SEVERE, null, ex );
        }
        return this;
    }

//    @Override
//    public TemplateEngine withVariable( String key, Object value ) {
//        this.variables.put( key, value );
//        return this;
//    }
    @Override
    public TemplateEngine withVariable( String key, Set<? extends Reportable> reports ) {
        Set<String> names = new TreeSet<>();
        for( Reportable report : reports ) {
            names.add( report.getName() );
        }
        this.variables.put( key,
                            new SimpleCollection( names,
                                                  new DefaultObjectWrapper( Configuration.VERSION_2_3_30 )
                            )
        );
        return this;
    }

    @Override
    public TemplateEngine withVariable( String key, String value ) {
        this.variables.put( key, value );
        return this;
    }

    @Override
    public TemplateEngine withVariables( Variables variables ) {
        this.variables.putAll( variables.asMap() );
        return this;
    }

}
