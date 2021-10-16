/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;


//IMPORTING ALL THE NECESSARY LIBRARIES
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Slider;
import javafx.scene.shape.*;

//Defining public class
public class Paint extends Application {

    private final double ZOOM_SCALE = 0.1;
    private final double MAX_ZOOM_SCALE = 3.0;
    private final double MIN_ZOOM_SCALE = 0.2;
    
    @Override //Methods in this class will override the same method from the super class
    public void start(Stage stage) {
        
                

        //Set a title for the stage
        stage.setTitle("Paint");
        
        TabPane canvasTabs = new TabPane();//Creates tabs for each canvas opened in the stage
        StackPane mainStack = new StackPane();//To layer all of it
        BorderPane menuPane = new BorderPane();//To address menu bar and width picker
        ScrollPane scroller = new ScrollPane();//Adding the scroller
        VBox betn = new VBox(); //Separates the two panes
        HBox sidebyside = new HBox();
        Tab[] canvases = {new Tab("Canvas", scroller)}; //Creates tab called Canvas
        

       
        //Adding new scene and adding mainStack to it
        Scene scene = new Scene(mainStack, 800, 1000);
        
        //Create a menu bar
        Menu menu1 = new Menu("File"); //First menu
        Menu menu2 = new Menu("Home"); //Second menu and so on
        Menu menu3 = new Menu("View");
    
        //Creating menu items
        MenuItem m1 = new MenuItem("New");
        MenuItem m2 = new MenuItem("Open");
        MenuItem m3 = new MenuItem("Save As");
        MenuItem m4 = new MenuItem("Close");
        MenuItem m5 = new MenuItem("Exit");
   
        //Adding a Menu items to the File menu
        menu1.getItems().addAll(m1, m2, m3, m4, m5);

        //create a menu bar
        MenuBar mb = new MenuBar();

        //Add each menu item to the menu bar
        mb.getMenus().add(menu1);
        mb.getMenus().add(menu2);
        mb.getMenus().add(menu3);
        
        String[] DRAW_TOOLS = {
        "Draw", "Line", "Circle", "Rectangle"};
        
        //Creating a canvas tool bar
        ColorPicker colorPicker = new ColorPicker(); //Adding color picker
        Slider slider = new Slider(0, 50, 0);        //Adding a slider to adjust the thickness of pointer/strokes
        slider.setMajorTickUnit(6.0); 
        slider.setMinorTickCount(3);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        ChoiceBox ToolBox = new ChoiceBox(); //Adding a toolbox to select shapes from different option
        ToolBox = new ChoiceBox(FXCollections.observableArrayList(DRAW_TOOLS)); //Adding items/shapes to the Toolbox
        colorPicker.setValue(Color.BLACK); //Setting the default color to Black
        
      /**************************************FOR FUTURE USE*******************************************************/
//        ToolBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
//            public void changed(ObservableValue ov, Number value, Number new_value){
//                usingTool = new_value.intValue();
//            }});
      /************************************************************************************************************/  
        //Creating a new canvas and defining a graphic context
        Canvas canvas = new Canvas(1080, 790);
        GraphicsContext gc;
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(1);
        
        //Line, Rectangle and Circle shapes to be added to the app later
        Line line = new Line();
        Rectangle rect = new Rectangle();
        Circle circ = new Circle();
       
        
        for(Tab i : canvases)
            canvasTabs.getTabs().add(i);
            
        
        scroller.setContent(canvas);
        menuPane.setCenter(canvasTabs);           //adds the scrolling canvas to the center of the BorderPane for centering things
        menuPane.setTop(betn);               //adds separator to the top
        sidebyside.getChildren().addAll(ToolBox, colorPicker, slider);
        betn.getChildren().addAll(mb, sidebyside);   //adds menu bar to top and canvas tools under it
        mainStack.getChildren().add(menuPane);
    
        //Setting the scene for the stage
        stage.setScene(scene);
        stage.setHeight(800); //Defining the height
        stage.setWidth(1000); //Defining the width of the window
        
        //Set the action events in order to form strokes when mouse is pressed and dragged on the canvas
        canvas.setOnMousePressed(e->{
               // String myChoice == String ToolBox.getValue();
                
                canvas.getGraphicsContext2D().setLineWidth((int) slider.getValue());
                gc.setStroke(colorPicker.getValue());
                gc.beginPath();
                gc.lineTo(e.getX(), e.getY());
            });
        
        canvas.setOnMouseDragged(e->{
                
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
        });
        
        //Action event for New button, clears the screen
        m1.setOnAction((ActionEvent e) -> gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()));
        
        //Creating an action event for Open button in the File menu
        m2.setOnAction((e)->{
            //Creating a file chooser
            FileChooser openFile = new FileChooser();
            openFile.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"));
            openFile.setTitle("Open File");
            File file = openFile.showOpenDialog(stage);
            //If file is not empty
            if (file != null) {
                try {
                    InputStream io = new FileInputStream(file);
                    Image img = new Image(io);
                    canvas.setHeight(img.getHeight()); //Setting the canvas height
                    canvas.setWidth(img.getWidth());  //Setting the canvas width
                    gc.drawImage(img, 0, 0);    //Drawing the image in the canvas
                } catch (IOException ex) { //If the file is empty
                    System.out.println("Error!");
                }
            }
        });

           //Setting the action commmand for Save as button
        m3.setOnAction((e)->{
            FileChooser savefile = new FileChooser();
            savefile.setTitle("Save As");
            savefile.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"), new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("JPEG", "*.jpg"));

            File file = savefile.showSaveDialog(stage);
            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage(1080, 790);
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    System.out.println("Error!");
                }
            }

        });
        
        zoomIn.setOnAction(e -> {

   canvas.getTransforms().remove(zoomScale);
   currentZoom += .05;
   zoomScale = new Scale(currentZoom, currentZoom, 0, 0);
   canvas.getTransforms().add(zoomScale);
   zoom.setText(Math.round(currentZoom * 100) + "%");
   zoomOut.setDisable(false);
});

zoomOut.setOnAction(e -> {
   canvas.getTransforms().remove(zoomScale);
   currentZoom -= .05;
   zoomScale = new Scale(currentZoom, currentZoom, 0, 0);
   canvas.getTransforms().add(zoomScale);
   zoom.setText(Math.round(currentZoom * 100) + "%");
   if (currentZoom < .06) { //if we're all the way zoomed out, disable zoom out
       zoomOut.setDisable(true);
   }
});


        //Creating an action event for Close button and New Button in the File menu
        m4.setOnAction((ActionEvent e) -> gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()));
        
        //Creating an action event for Exit button in the File menu
        m5.setOnAction((ActionEvent e) -> {
            Platform.exit();
            System.exit(0);
        });
        

        //Creating shortcut key combinations for the Menu items in the File menu
        m1.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN)); //Shortcut for New = Ctrl + N
        m2.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)); //Shortcut for Open = Ctrl + O
        m3.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)); //Shortcut for Save = Ctrl + S
        m4.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)); //Shortcut for Close = Ctrl + C
        m5.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN)); //Shortcut for Exit = Ctrl + E
        
        //Showing everything in the window
        stage.show();
        
        

    }
    
    
    

    public static void main(String[] args) {
        launch(args);
    }
}


