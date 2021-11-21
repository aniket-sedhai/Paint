/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

/////////////IMPORTING ALL THE NECESSARY LIBRARIES///////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * This class manages the dialog boxes that popup due to various reasons
 * @author asedhai
 */
public class DialogBox{
    
    public static void generateDialogBox(File path, String title,                           
            int width, int height) throws FileNotFoundException{
        Stage dialogBox = new Stage();                                          //Creates new stage/ window called dialogBox
        dialogBox.initOwner(Paint.myStage);                                     //This dialogBox is owned by the myStage of paint class
        ScrollPane scrollIt = new ScrollPane();                                 //Creates new ScrollPane
        BorderPane brp = new BorderPane();                                      //Creates new borderPane
        HBox hbox = new HBox();                                                 //Creates new HBox
        Scene myscene = new Scene(brp, width, height);                          //Creates a new scene with the borderpane, and defined width and height
        Scanner myScanner = new Scanner(path);                                  //Scanner to read and verify status of saved/unsaved file
        String myText = "";                                                     //Placeholder text
        while(myScanner.hasNextLine())                                          //As long as the file has a next line
            myText += myScanner.nextLine() + "\n";                              //takes the file and reads all the text
        
        Text text = new Text(myText);                                           //Stores the text content
        Button choice = new Button("Yes");                                      //Text label on the button
        choice.setOnAction((ActionEvent e) -> {                                 //If the user presses "Yes" button, hide the dialog Box
            
            dialogBox.hide();
        });
        //makes the window look all nice
        hbox.getChildren().addAll(choice);                                      //Adds the choice button to the hbox
        hbox.setAlignment(Pos.CENTER);                                          //sets the button to center
        scrollIt.setContent(text);                                              //Sets the text in the scrollpane's content zone
        brp.setPadding(new Insets(10));                                         //Provide padding to avoid crowding
        brp.setBottom(hbox);                                                    //sets the horizontal box at the bottom that contains the button
        brp.setCenter(scrollIt);                                                //sets the scroll pane at the center 
        dialogBox.setScene(myscene);                                            //sets the scene
        dialogBox.setTitle(title);                                              //sets the title of the window
        dialogBox.show();                                                       //shows the popup box
    }
    /**
     * Creates an alert that asks if the person is sure that they want to exit
     * the program
     * @param stage The stage to safely close
     */
    public static void createExitAlert(Stage stage){
        Alert sureAlert = new Alert(AlertType.CONFIRMATION,                     //Type of alert: Confirmation
                "Are you sure you want to exit?",                               //Ask this question
                ButtonType.YES, ButtonType.NO);                                 //Two types of button
        sureAlert.setTitle("Exit");                                             //Sets the title of the exit dialog box
        sureAlert.initOwner(Paint.myStage);                                     //myStage from paint owns this new dialog box, cannot avoid answering
        if(sureAlert.showAndWait().get() == ButtonType.YES){                    //if the anwer is yes, exit
            stage.close();                                                      //close the stage
            Paint.autosave.stop();
            Platform.exit();                                                    //Exit from the pltform
            System.exit(0);                                                     //Allows exit
        }
    }
    /**
     * Creates an alert that asks if the user wants to save before 
     * leaving
     */
    public static void needSavingAlert(){
        ButtonType[] buttons = {new ButtonType("Save"), 
                                new ButtonType("Save As"), 
                                new ButtonType("Cancel"), 
                                new ButtonType("Close")};
        Alert unsavedChanges = new Alert(AlertType.WARNING,
                "You have unsaved changes. Are you sure you want to close?",    //Warning message
                buttons[0], buttons[1], buttons[2], buttons[3]);                //4 types of buttons
        unsavedChanges.setTitle("Unsaved Changes");                             //Title of the dialog box is "Unsaved Changes"
        ButtonType nextStep = unsavedChanges.showAndWait().get();               //if there are unsaved changes in the current canvas
        if(nextStep == buttons[0]){                                            
            Paint.getActiveTab().saveImage();                                   //Saves image from the active tab
        }
        else if(nextStep == buttons[1]){                                        
            Paint.getActiveTab().saveImageAs();                                 //Save as for the image in the active tab
        }
        else if(nextStep == buttons[3]){                                        //Closes the active tab
            Paint.closeActiveTab();
        }
    }
    
           
    }




