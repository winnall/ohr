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
package net.winnall.ohr.fx;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.winnall.ohr.ThingCommand;
import net.winnall.ohr.configuration.Configuration;
import net.winnall.ohr.host.Host;

/**
 * FXML Controller class
 *
 * @author Stephen Winnall
 */
public class MainFXController extends VBox implements Initializable {

    private static final String EMPTY_STRING = "";

    @FXML
    private TextField jsonDatabaseFolderName;

    private FileNameHandler jsonDatabaseFolderNameHandler;

    @FXML
    private TextField linkPrefix;

    @FXML
    private TextField outputFolderName;

    private FileNameHandler outputFolderNameHandler;

    private Stage stage;

    @FXML
    private CheckBox zippedOutput;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize( URL url, ResourceBundle rb ) {
        String home = Host.getInstance()
                .getUserHome();
        // outputFolderName
        outputFolderNameHandler = new FileNameHandler(
                outputFolderName,
                home );
        outputFolderNameHandler.setFolder( true );
        // jsonDatabaseFolderName
        jsonDatabaseFolderNameHandler = new FileNameHandler(
                jsonDatabaseFolderName,
                home );
        jsonDatabaseFolderNameHandler.setFolder( true );
        linkPrefix.setText( Configuration.getInstance()
                .getDefaultLinkPrefix() );
        zippedOutput.setSelected( false );
    }

    void setStage( Stage stage ) {
        this.stage = stage;
    }

    @FXML
    private void cancelButton() {
        System.exit( 0 );
    }

    private void cancelButton( ActionEvent event ) {
        cancelButton();
    }

    @FXML
    private void jsonDatabaseFolderNameAction( ActionEvent event ) {
        jsonDatabaseFolderNameHandler.action();
    }

    @FXML
    private void jsonDatabaseFolderNameDragDropped( DragEvent event ) {
        jsonDatabaseFolderNameHandler.dragDropped( event );
    }

    @FXML
    private void jsonDatabaseFolderNameDragOver( DragEvent event ) {
        // FIXME - need to test whether the folder contains appropritae JSON files
        //       - add test as lambda to FileNameHandler.dragOver?
        jsonDatabaseFolderNameHandler.dragOver( event );
    }

    @FXML
    private void jsonDatabaseFolderNameOnClick( MouseEvent event ) {
        jsonDatabaseFolderNameHandler.onClick();
    }

    @FXML
    private void outputFolderNameAction( ActionEvent event ) {
        outputFolderNameHandler.action();
    }

    @FXML
    private void outputFolderNameDragDropped( DragEvent event ) {
        outputFolderNameHandler.dragDropped( event );
    }

    @FXML
    private void outputFolderNameDragOver( DragEvent event ) {
        outputFolderNameHandler.dragOver( event );
    }

    @FXML
    private void outputFolderNameOnClick( MouseEvent event ) {
        outputFolderNameHandler.onClick();
    }

    @FXML
    private void runButton( ActionEvent event ) {
        runButton();
    }

    private void runButton() {
        // set up all the parameters for the app...
        final Configuration configuration = Configuration.getInstance();
        configuration.setOutputFolderName( outputFolderName.getText() );
        configuration.setJsonDBFolderName( jsonDatabaseFolderName.getText() );
        configuration.setLinkPrefix( linkPrefix.getText() );
        configuration.setZippedOutput( zippedOutput.isSelected() );
        // ...and then run it
        new ThingCommand().run();
        // exit when done
        System.exit( 0 );
    }

    private class FileNameHandler {

        private TextField fileNameTextField = null;

        private boolean isFolder = false;

        FileNameHandler( TextField filename, String initialFileName ) {
            this.fileNameTextField = filename;
            initialiseFileNameField( initialFileName );
        }

        void action() {
            fsObjectNameHandler();
        }

        void dragDropped( DragEvent event ) {
            Dragboard dragboard = event.getDragboard();
            if( dragboard.hasFiles() ) {
                final File dropCandidate = dragboard.getFiles()
                        .get( 0 );
                fileNameTextField.setText( dropCandidate
                        .getAbsolutePath() );
                event.setDropCompleted( true );
            } else {
                event.setDropCompleted( false );
            }
            event.consume();
        }

        void dragOver( DragEvent event ) {
            Dragboard dragboard = event.getDragboard();
            if( dragboard.hasFiles() ) {
                final List<File> files = dragboard.getFiles();
                if( files.size() == 1
                    && files.get( 0 )
                                .isDirectory() == isFolder ) {
                    event.acceptTransferModes( TransferMode.COPY );
                }
            }
            event.consume();
        }

        void setFolder( boolean isFolder ) {
            this.isFolder = isFolder;
        }

        void onClick() {
            fsObjectNameHandler();
        }

        private void fsObjectNameHandler() {
            File currentFile = new File( fileNameTextField
                    .getText() );
            File selectedFSObject = null;
            if( isFolder ) {
                DirectoryChooser folderChooser = new DirectoryChooser();
                folderChooser.setInitialDirectory( currentFile
                        .getParentFile() );
                folderChooser.setInitialDirectory( currentFile );
                selectedFSObject = folderChooser.showDialog( stage );
            } else {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory( currentFile
                        .getParentFile() );
                fileChooser.setInitialFileName( currentFile.getName() );
                selectedFSObject = fileChooser.showOpenDialog( stage );
            }
            if( selectedFSObject != null ) {
                fileNameTextField.setText( selectedFSObject.toString() );
            }
        }

        private void initialiseFileNameField( String initialFileName ) {
            fileNameTextField.setText( initialFileName );
            fileNameTextField.textProperty()
                    .addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> {
                        int location = fileNameTextField.getText()
                                .length();
                        Platform.runLater( () -> {
                            fileNameTextField.positionCaret( location );
                        } );
                    } );
        }

    }
}
