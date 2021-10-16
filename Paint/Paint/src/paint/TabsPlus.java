/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

/**
 *
 * @author asedhai
 */
public class TabsPlus extends Tab{    
    public final static String AUTOSAVE_DIR = "H:\\autosaves";
    private Timer autosaveTimer;
    private TimerTask autosave;
    private Image autosaveBackup;
    private int autosaveSecs;
    private final static int MILS_IN_SECS = 1000;
    private static ScrollPane scroller;                                         //To add scroller to every tab generated
    public static CanvasPlus myCanvas;                                          //TO add canvas to every tab
    public Pane canvasPane;
    private File path;                                                          //To determine the path of file to be saved
    private String CanvasTitle;                                                 //To define the title of the canvas appearing on the tab text
    public boolean saveAgain;                                                          //Does the file need to be saved again/ unsaved changes?
    private static FileChooser myFileChooser;                                   //File chooser winddow
    public File loaded_file;
    public Image loaded_image;
    private StackPane canvasStack;
    
    public TabsPlus(File pathname)
    {
        super();                                                                //calls the Tab constructor
        this.saveAgain = false;                                                 //initializing a boolean to check for unsaved changes in the canvas
        this.path = pathname;                                                   //initializing a path name to save the data
        this.CanvasTitle = path.getName();                                      //sets the canvas title from the path name
        scroller = new ScrollPane();                                            //initializing a new scrollpane
        scroller.pannableProperty().set(false);                                 //sets the scrollpane's pannable property to false
        this.myCanvas = new CanvasPlus();                                       //starting a new canvas
        scroller.setContent(myCanvas);                                          //sets the new canvas as the content of the scrollpane
        setContent(scroller);                                                   //sets the scrollpane as the content of the tab
        initTab();                                                              //intitializes the tab
        
        
    }
    
    public TabsPlus(){
        super();                                                                //calls the Tab() constructor
        this.saveAgain = true;                                                  //once a tab is created, there is an unsaved change
        this.path = null;                                                       //sets the path as null
        this.CanvasTitle = "Untitled";                                          //sets the title of the canvas as untitled
        this.myCanvas = new CanvasPlus();                                       //starts a new canvas
        scroller = new ScrollPane();                                            //new scrollpane
        scroller.pannableProperty().set(false);                                 //scrollpane should not be pannable
        scroller.setContent(myCanvas);                                          //sets the canvas as the content of the scrollpane
        setContent(scroller);                                                   //sets the scrollpane as the content of the tab
        initTab();                                                              //initializes a tab
    }
    
    private void initTab(){                                                     
        myFileChooser = new FileChooser();                                      //starting a new file saver
        myFileChooser.getExtensionFilters().addAll(                             //Adds all different extensions to the file chooser
                new FileChooser.ExtensionFilter("PNG", "*.png"), 
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
                new FileChooser.ExtensionFilter("BITMAP", "*.bmp"));
        this.autosaveBackup = null;
        
        if(saveAgain){                                                          //if there are unsaved changes
                    this.setText(this.CanvasTitle + "*");                       //      -add an asterisk to the end of the name
        }
        this.setOnCloseRequest(e->{                                             //If the x button on canvas tab is pressed
            e.consume();                                                        //Consumes the event
            if(this.saveAgain)                                                  //if there are unsaved changes           
                DialogBox.needSavingAlert();                                    //      -popup up the alert dialog box for unsaved changes
            else
                Paint.closeActiveTab();                                         //else just close the tab
        });
        
        this.autosaveSecs = 30;
        this.autosaveTimer = new Timer();
        this.autosave = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(new Runnable(){
                      public void run(){
                          autosaveImage();
                          autosaveTimer.schedule(autosave, 0, autosaveSecs*MILS_IN_SECS);
                      }
                });
            }
        };
        this.autosaveTimer.schedule(this.autosave,30000, this.autosaveSecs*MILS_IN_SECS);
        
    }
    
    public void autosaveImage(){
        if(this.saveAgain && this.path != null) {
            System.out.println("The path is not null");
            this.autosaveBackup = this.myCanvas.getRegion(0, 0, this.myCanvas.getWidth(), this.myCanvas.getHeight()); //snapshot of the current canvas
            File backupFile = new File(AUTOSAVE_DIR + LocalDate.now() + Instant.now().toEpochMilli() + ".png");
            try {                           //backup path is just date + time in secs since epoch and then the .png file type
                backupFile.createNewFile();
                ImageIO.write(SwingFXUtils.fromFXImage(this.autosaveBackup, null),
                        "png",
                        new FileOutputStream(backupFile));
                System.out.println("Autosaved!");
            } catch (IOException ex) {
                Logger.getLogger(TabsPlus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void updateAutosaveTimer(){
        this.autosaveSecs = ToolsPlus.getAutosaveTime();
        this.autosave.cancel();
        this.autosaveTimer.purge();
        this.autosave = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(new Runnable(){
                    public void run(){
                        autosaveImage();
                        autosaveTimer.schedule(autosave, 0, autosaveSecs*MILS_IN_SECS);
                    }
                });
            }
        };
        this.autosaveTimer.schedule(this.autosave, 0, this.autosaveSecs*MILS_IN_SECS);
    }
    
   
    
    
    
    public boolean getUnsavedChanges()                                          //returns the state of the variable saveAgain to find if unsaved changes exist
{
    return saveAgain;
}
    
        public  void saveImage(){
        WritableImage im = myCanvas.snapshot(new SnapshotParameters(), null);   //Takes the image from the canvas
        try{
            if(this.path != null){
                ImageIO.write(SwingFXUtils.fromFXImage(im, null),               //By default saves the image as a png image at the path
                        "png", this.path);                      
                this.setUnsavedChanges(false);                                  //After saving, changes the status of unsaved changes to false, detecting that there are no more unsaved changes
                this.setTitle(this.getFilePath().getName());
            }
        }catch(IOException ex){ 
            System.out.println(ex.toString());                                  //If the file/path is empty
        }
        this.setUnsavedChanges(false);
        this.updateTabTitle();                                                  //updates the title of the tab at the end to remove *
    }
        
        public static void saveAsFile(Window stage) {
        // add ', File f' to the args?
        Image out_img;
        out_img = Paint.getActiveTab().myCanvas.getImage();
        if (out_img == null) { //Check to make sure there is a file to save
            System.out.println("Warning. No image in Canvas. Failed to save.");
            return; // should raise an error here (like a pop-up box)
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.gif"),
            new FileChooser.ExtensionFilter("All Files", "*.*"));
        File out_file = fileChooser.showSaveDialog(stage);
        //need to have a catcher for if the save dialog is cancelled

        saveImage2(out_img, out_file);
        Paint.getActiveTab().loaded_file = out_file; // update the name of the tab too
        Paint.getActiveTab().setText(out_file.getName());
    }

        
        
        public static void saveFile(){
        Image out_img;
        out_img = Paint.getActiveTab().myCanvas.getImage();

        if  (out_img == null) {
            System.out.println("Warning. No image in Canvas. Failed to save.");
            return;
        }
        if (Paint.getActiveTab().loaded_file == null) {
            saveAsFile(Paint.myStage);
            return;
        }
        saveImage2(out_img, Paint.getActiveTab().loaded_file);
        Paint.getActiveTab().saveAgain = false;
    }
        
        private static String getFileExtension(File f) {
        String fn = f.getName(); //get the file name (not full path)
        int pos = fn.lastIndexOf("."); //get the pos of the last period
        if (pos > 0) {
            return fn.substring(pos + 1).toLowerCase();
            //return the substring that is one greater than the last period
        }
        return "";  //might want to change this to be a sensible default
    }
        
        private static void saveImage2(Image out_img, File opened_file) {
        // Get buffered image:
        BufferedImage image = SwingFXUtils.fromFXImage(out_img, null);
        BufferedImage imageRGB;
        //If the file extensions are different, show a popup warning
        if (Paint.getActiveTab().loaded_file != null) {
            if (getFileExtension(Paint.getActiveTab().loaded_file).equals(
                        getFileExtension(opened_file))) {
                System.out.println("Image Formats are the same.");
            } else {
                DialogBox.saveWarning();
            }
        }

        if (getFileExtension(opened_file).equals("jpg")) {
            // Remove alpha-channel from buffered image:
            imageRGB = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.OPAQUE
            );
        } else {
            imageRGB = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TRANSLUCENT
            );
        }
        }
    /**
     * Saves an image at a path that isn't the path the canvas is already at
     */
    public void saveImageAs(){
        File path = myFileChooser.showSaveDialog(Paint.myStage);                //Shows the Save As dialog box
        this.setFilePath(path);                                                 //sets the file path as choosen by the user
        this.saveImage();                                                       //saves image
    }
    /**
     * Saves an image at a given path
     * @param path The given path to save the image to
     */
    public void saveImageAs(File path){                                         //save as if the path is provided
        this.setFilePath(path);                                                 //sets the path as provided from the parameter
        this.saveImage();                                                       //saves the image
    }

    public void setTitle(String title)                                          //to set the canvas title
    {
        this.CanvasTitle = title;                                               //title given by user
        this.updateTabTitle();                                                  //updates the title from the user
    }
    
    public void setFilePath(File path)                                          
    {
        this.path = path;
    }

    public String getTitle(){return this.CanvasTitle;}                          //returns the title of the canvas
    
    public File getFilePath(){return this.path;}                                //returns the file path
    
    public void updateTabTitle(){                                               //updates the title of the tab
        if(this.path != null)
            this.CanvasTitle = this.path.getName();                             //stores the current name of the title tab
        if(this.saveAgain)
            this.setText(this.CanvasTitle + "*");                               //adds an asterisk if there is an unsaved changes
        else
            this.setText(this.CanvasTitle);                                     //else just sets the title of the tab as it is
    }
    
    public void undo(){
        TabsPlus.myCanvas.undo();                                                   //undos the previous action
        this.setUnsavedChanges(true);                                           //sets unsaved changes to true
    }
    
    public void redo(){
        TabsPlus.myCanvas.redo();                                                   //redos the previous action
        this.setUnsavedChanges(true);                                           //sets unsaved changes to true
    }
    
    public void setUnsavedChanges(boolean unsavedChanges){
        this.saveAgain = unsavedChanges;                                        
    }
    
    public static void openImage(File path){
        TabsPlus temp;
        if(path == null)
            temp = new TabsPlus();
        else
            temp = new TabsPlus(path);
        temp.updateTabTitle();                                                  //Updates the title of the tab
        TabsPlus.myCanvas.drawImage(path);                                      //Takes the image in the defined path
        Paint.canvasTabs.getTabs().add(temp);                                   //Adds the image to the tab
        Paint.canvasTabs.getSelectionModel().select(temp);                      //For active tab
    }


    
    public static void openBlankImage(){
        TabsPlus temp = new TabsPlus();                                         //Creating a new tab
        Paint.canvasTabs.getTabs().add(temp);                                   //Adds a tab that has blank canvas
        Paint.canvasTabs.getSelectionModel().select(temp);                      //For active tab
    }
    
    
    
    
    
}
