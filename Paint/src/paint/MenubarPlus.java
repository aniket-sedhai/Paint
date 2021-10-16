/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;


/**
 *
 * @author asedhai
 */
public class MenubarPlus extends MenuBar {
    
    private int canvasIndex = 1;
    
    private static Menu FileMenu, HomeMenu, ViewMenu, EditMenu, AboutMenu;      //Declares all the different menu options in the menubar
    
                                                                                //Declares all the menu items to be included in the File menu.
    static MenuItem Create, New_FM, Open_FM, Save, Save_As, Exit;
    
    public MenubarPlus()
    {
        super();
        FileMenu = new Menu("File");
        HomeMenu = new Menu("Home");
        ViewMenu = new Menu("View"); 
        EditMenu = new Menu("Edit");
        AboutMenu = new Menu("About");
      
        Create = new MenuItem("Create New");
        New_FM = new MenuItem("New");
        Open_FM = new MenuItem("Open");
        Save = new MenuItem("Save");
        Save_As = new MenuItem("Save As");
        Exit = new MenuItem("Exit");
     
        FileMenu.getItems().addAll(Create, New_FM, Open_FM, Save, Save_As, Exit);
      
        
        this.getMenus().addAll(FileMenu, HomeMenu, ViewMenu, EditMenu, AboutMenu);
        
        Create.setOnAction((ActionEvent e)-> {
        TabsPlus.openBlankImage();
    });
        
        New_FM.setOnAction((ActionEvent e) -> {
            TabsPlus newTab = new TabsPlus();
            newTab.setText("Tab "+ canvasIndex);
            Paint.canvasTabs.getTabs().add(newTab);
            canvasIndex++;
        });

        Open_FM.setOnAction((e)->{
                                                                                
            FileChooser openFile = new FileChooser();                           //Creating a file chooser
            openFile.getExtensionFilters().addAll(                              //Adds all different extension filters
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
                new FileChooser.ExtensionFilter("Bitmap", "*.bmp"));
            openFile.setTitle("Open File");                                     //File opener dialog box title
            File file = openFile.showOpenDialog(Paint.myStage);                 //Adding the dialog box to paint's myStage
            //If file is not empty
            if (file != null) {                                                 //If the file is not empty
                try {
                    InputStream io = new FileInputStream(file);                 //Takes input from the file 
                    Image img = new Image(io);                                  //Creates a variable of image type
                    TabsPlus.myCanvas.setHeight(img.getHeight());               //Setting the canvas height
                    TabsPlus.myCanvas.setWidth(img.getWidth());                 //Setting the canvas width
                    CanvasPlus.gc.drawImage(img, 0, 0);                         //Drawing the image in the canvas
                    CanvasPlus.gc.drawImage(img, 0, 0);
                } catch (IOException ex) {                                      //If the file is empty
                    System.out.println("Error!");                               //Print an error if exception occured
                }
            }
        });
        
        Save.setOnAction((e)->{                                                 //Action event for the save button
            if(Paint.getActiveTab().getFilePath() == null)
                Paint.getActiveTab().saveImageAs();
        });
        
        Save_As.setOnAction((e)->{                                              //Action event for the Save as button
            FileChooser savefile = new FileChooser();
            savefile.setTitle("Save As");
            savefile.getExtensionFilters().addAll                               //Adds all different kind of extensions to the file saver
               (new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PNG", "*.png"), 
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"), 
                new FileChooser.ExtensionFilter("Bitmap", "*.bmp"));

            File file = savefile.showSaveDialog(Paint.myStage);                 //Declares save dialog box
            if (file != null) {                                                 //if the path is not wrong
                try {
                    WritableImage writableImage = new WritableImage(1080, 790); //Creates a variable to store image from the canvas
                    TabsPlus.myCanvas.snapshot(null, writableImage);            //Takes the snapshot from the canvas and stores that image in writableimage
                    RenderedImage renderedImage = 
                            SwingFXUtils.fromFXImage(writableImage, null);      //Renders image
                    ImageIO.write(renderedImage, "png", file);                  //Rendered image is in default made to be a png file
                } catch (IOException ex) {
                    System.out.println("Error!");                               //Exception has occured
                }
            }

        });
        
        Exit.setOnAction((ActionEvent e) -> {                                   //Exit dialog box
            DialogBox.createExitAlert(Paint.myStage);                           //Creates exit alert derived from the class DialogBox()
        });
        
        //Creating shortcut key combinations for the Menu items in the File menu
        New_FM.setAccelerator
        (new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));       //Shortcut for New = Ctrl + N
        Open_FM.setAccelerator
        (new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));       //Shortcut for Open = Ctrl + O
        Save_As.setAccelerator
        (new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));       //Shortcut for Save = Ctrl + S
        Exit.setAccelerator
        (new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));       //Shortcut for Exit = Ctrl + E
        
        
    }
 
    
}
