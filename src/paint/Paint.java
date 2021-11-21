/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;


//IMPORTING ALL THE NECESSARY LIBRARIES

import java.io.IOException;
import java.util.logging.Logger;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

//Defining public class
public class Paint extends Application {
    
    public static Stage myStage;  
    public static TabPane canvasTabs;
    public static CanvasPlus myCanvas;
    public static String filePath;
    public static int autosaveInterval;
    public static Timeline autosave;
    public static Timeline logger;
    final static int LOGGER_INTERVAL = 10;
    final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static boolean AutoSaveOn = false;
    
    @Override                                                                   //Methods in this class will override the same method from the super class
    public void start(Stage stage) throws IOException {
        myStage = stage;
        
        
        
        stage.setTitle("Paint");                                                //Set a title for the stage
        
        canvasTabs = new TabPane();                                             //Creates tabs for each canvas opened in the stage
        StackPane mainStack = new StackPane();                                  //To layer all of it
        BorderPane menuPane = new BorderPane();                                 //To address menu bar and width picker
        VBox betn = new VBox();                                                 //Separates the two panes
        HBox myhbox = new HBox();

        
        Scene scene = new Scene(mainStack, 800, 1000, Color.WHITE);             //Adding new scene and adding mainStack to it

        MenuBar mb = new MenubarPlus();                                         //Creating a menu bar

        ToolsPlus myToolBar = new ToolsPlus();                                  //Creating toolbar from the instance ToolsPlus
        myToolBar.setNoneDefault();                                             //Set "None" as the default tool when starting the application
        TabsPlus myfirstTab = new TabsPlus();                                   //Creating the first blank tab to the screen
        myfirstTab.setText("untitled");                                         //Setting the first tab's name as untitled
        canvasTabs.getTabs().add(myfirstTab);                                   //Adding the first tab created to the pane called canvasTabs
        menuPane.setCenter(canvasTabs);                                         //Adding the pane containing the scrollbar and tabs to the center of the borderpane
        menuPane.setTop(betn);                                                  //adds separator to the top
        betn.getChildren().addAll(mb, myToolBar, myhbox);                       //adds menu bar to top and canvas tools under it
        mainStack.getChildren().add(menuPane);                                  //Adding the boderpane to the main layout
    
        
        stage.setScene(scene);                                                  //Setting the scene for the stage
        stage.setHeight(800);                                                   //Defining the height
        stage.setWidth(1000);                                                   //Defining the width of the window
       
        stage.show();                                                           //Showing everything in the window
        
    
    }
    
    /**
     * Gets the tab that the user is working on and is currently selected
     * @return the active Tab
     */
    public static TabsPlus getActiveTab()                                       //Returns the tab that is currently selected
    {
        return (TabsPlus)canvasTabs.getSelectionModel().getSelectedItem();
    }
    
    /**
     * Closes the active tab
     */
    public static void closeActiveTab()                                         //Closes the tab that is currently selected
    {
        Paint.canvasTabs.getTabs().remove(Paint.getActiveTab());
    }

    /**
     * Just runs the application
     * @param args are arguments
     */
    public static void main(String[] args) {                                    //This is the main function. Launches the 
        launch(args);
    }

}


