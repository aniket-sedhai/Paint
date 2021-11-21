/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
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
    
    private Timer autosaveTimer;
    private Timer loggerTimer;
    private TimerTask autosave;
    private TimerTask logger;
    private Image autosaveImage;
    private int autosaveTime;
    private final static int time_count = 1000;
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
    
    public final static String autosaveHere = "H:\\Backup folder\\Paint\\Paint\\src\\autosaves";
    public final static String LOGGER_TXT = "Mylogs.txt";
    
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
    
    
    
    public void autosaveImage(){
        if(this.saveAgain && this.path != null) {
            System.out.println("The path is not null");
            this.autosaveImage = this.myCanvas.getRegion(0, 0, this.myCanvas.getWidth(), this.myCanvas.getHeight()); //snapshot of the current canvas
            File backupFile = new File(autosaveHere + LocalDate.now() + Instant.now().toEpochMilli() + ".png");
            try {                           //backup path is just date + time in secs since epoch and then the .png file type
                backupFile.createNewFile();
                ImageIO.write(SwingFXUtils.fromFXImage(this.autosaveImage, null),
                        "png",
                        new FileOutputStream(backupFile));
                System.out.println("Autosaved!");
            } catch (IOException ex) {
                Logger.getLogger(TabsPlus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void updateAutosaveTimer(){
        this.autosaveTime = ToolsPlus.getAutosaveTime();
        this.autosave.cancel();
        this.autosaveTimer.purge();
        this.autosave = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(new Runnable(){
                    public void run(){
                        autosaveImage();
                        autosaveTimer.schedule(autosave, 0, autosaveTime*time_count);
                    }
                });
            }
        };
        this.autosaveTimer.schedule(this.autosave, 0, this.autosaveTime*time_count);
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
        Image out_img;
        out_img = Paint.getActiveTab().myCanvas.getImage();
        if (out_img == null) { 
            System.out.println("Warning. No image in Canvas. Failed to save.");
            return;     
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.bmp", "*.gif"),
            new FileChooser.ExtensionFilter("All Files", "*.*"));
        File out_file = fileChooser.showSaveDialog(stage);
        

        saveImage2(out_img, out_file);
        Paint.getActiveTab().loaded_file = out_file; 
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
        String fn = f.getName(); 
        int pos = fn.lastIndexOf("."); 
        if (pos > 0) {
            return fn.substring(pos + 1).toLowerCase();
        }
        return "";
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
                //DialogBox.saveWarning();
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
     * Saves at location and name preferred by the user and 
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
    
    /**
     * Updates the stack on canvas every time a change is made
     * Sets the changes made indicator to true
     */
    public void updateStacks(){
        this.myCanvas.updateStacks();
        this.setUnsavedChanges(true);
    }

    /**
     * Sets the title of the Canvas tab
     * @param title, a string which can be set as the title of the canvas
     */
    public void setTitle(String title)                                          //to set the canvas title
    {
        this.CanvasTitle = title;                                               //title given by user
        this.updateTabTitle();                                                  //updates the title from the user
    }
    
    /**
     * Sets the path of the file/canvas in the active tab
     * @param path The file path preferred by the user 
     */
    public void setFilePath(File path)                                          
    {
        this.path = path;
    }

    /**
     * 
     * Gets the title of the active canvas
     * @return the string 
     */
    public String getTitle(){return this.CanvasTitle;}                          //returns the title of the canvas
    
    
    /**
     * Gets the path of the file associated to the canvas
     * @return returns the path obtained (in String)
     */
    public File getFilePath(){return this.path;}                                //returns the file path
    
    public void updateTabTitle(){                                               //updates the title of the tab
        if(this.path != null)
            this.CanvasTitle = this.path.getName();                             //stores the current name of the title tab
        if(this.saveAgain)
            this.setText(this.CanvasTitle + "*");                               //adds an asterisk if there is an unsaved changes
        else
            this.setText(this.CanvasTitle);                                     //else just sets the title of the tab as it is
    }
    
    /**
     * 
     * 
     * Gets the previous undone action in the canvas
     */
    public void undo(){
        TabsPlus.myCanvas.undo();                                                   //undos the previous action
        this.setUnsavedChanges(true);                                           //sets unsaved changes to true
    }
    
    
    /**
     * 
     * Redoes the previous undone action in the canvas
     * 
     */
    public void redo(){
        TabsPlus.myCanvas.redo();                                                   //redos the previous action
        this.setUnsavedChanges(true);                                           //sets unsaved changes to true
    }
    
    
    /**
     * 
     * Sets the saveAgain variable to true to indicate that changes have been made to the file
     * 
     */
    public void setUnsavedChanges(boolean unsavedChanges){
        this.saveAgain = unsavedChanges;                                        
    }
    
    /**
     * Opens a file chooser for user to open images from; calls
     * 
     *
     * @param path the path where the image exists
     */
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
/**
 * Opens a blank image into the canvas
 * 
 * 
 */ 
    public static void openBlankImage(){
        TabsPlus temp = new TabsPlus();                                         //Creating a new tab
        Paint.canvasTabs.getTabs().add(temp);                                   //Adds a tab that has blank canvas
        Paint.canvasTabs.getSelectionModel().select(temp);                      //For active tab
    }
    
    private void initTab(){                                                     
        myFileChooser = new FileChooser();                                      //starting a new file saver
        myFileChooser.getExtensionFilters().addAll(                             //Adds all different extensions to the file chooser
                new FileChooser.ExtensionFilter("PNG", "*.png"), 
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
                new FileChooser.ExtensionFilter("BITMAP", "*.bmp"));
        this.autosaveImage = null;                                              //The image to be autosave is null at the setup
        
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
        
        this.autosaveTime = 30;
        this.autosaveTimer = new Timer();
        this.autosave = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(new Runnable(){
                      public void run(){
                          autosaveImage();
                          autosaveTimer.schedule(autosave, 0, autosaveTime*time_count);
                      }
                });
            }
        };
        this.autosaveTimer.schedule(this.autosave,30000, this.autosaveTime*time_count);
        
        this.loggerTimer = new Timer();
        this.logger = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        File loggerFile = new File(LOGGER_TXT);
                            try{
                                loggerFile.createNewFile();
                            }
                            catch(Exception ex){
                                System.out.println(ex);
                            }
                            try{
                                FileWriter writer = new FileWriter(LOGGER_TXT, true);
                                BufferedWriter writer_buffer = new BufferedWriter(writer);
                                writer_buffer.write(ToolsPlus.getCurrentTool() + " used on " + LocalDate.now() + " at " + LocalTime.now());
                                writer_buffer.newLine();
                                writer_buffer.close();
                            }
                            catch(Exception ex){
                                System.out.println(ex);
                            }
                    }
                });
            }
        };
        this.loggerTimer.scheduleAtFixedRate(this.logger,5000, 30000);
    }
    
    
    
}
