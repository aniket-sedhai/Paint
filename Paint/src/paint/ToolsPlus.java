/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

//IMPORTING ALL THE NEEDED LIBRARIES
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

/**
 *
 * @author anikeT
 */
                                                                                //CREATING THE CLASS
public class ToolsPlus extends ToolBar{
   
    private static int usingTime;                                                                         //DECLARING ALL THE ELEMENTS GLOBALLY SO THAT THE MAIN CAN ACCESS IT TOO
    static ToggleButton ZoomIn;                                                 //ZOOM IN BUTTON
    static ToggleButton ZoomOut;                                                //ZOOM OUT BUTTO
    private static ColorPicker FillColor;                                       // FILL COLOR CHOOSER
    private static ColorPicker StrokeColor;                                     //STROKE COLOR CHOOSER
    private static Slider StrokeSizer;                                          //THIS CONTROLS THE STROKE SIZE
    public static double StrokeSize;                                            //THIS STORES THE STROKE SIZE
    public static ToggleButton colorGrabber;                                    //THIS IS THE COLOR GRABBER
    private static ChoiceBox toolSelector;                                      //THIS IS CONTAINS ALL THE TOOLS
    public static ToggleButton TforText;                                        //THIS ADDS THE TEXT
    private static CheckBox shapeFiller;                                        //THIS CHECKS IF FILLING IS NECESSARY
    private static int currentTool;                                             //THIS KEEPS THE TRACK OF WHAT TOOL IS BEING USED
    private static TextArea writeHere;                                          //Text field
    public static Button undo, redo;
    private static int n_polygon;
    private final int BTN_SIZE = 30, BTN_SIZE2 = 60, BTN_SIZE3 = 80;
    private static TextArea polygon_sides;
    private static TextField autosaveTime;
    
                                                                                //DEFINING ALL THE TOOLS THAT I WILL HAVE IN MY PAINT
    public final static String[] MY_TOOLS = {"None",
        "Pencil", "Eraser", "Line", "Circle", "Rectangle","Round Rectangle",
        "Square", "Ellipse", "Regular Polygon", "Cut", "Paste"};
    
                                                                                //CONSTRUCTOR
    public  ToolsPlus(){
        super();                                                                //Calls the "ToolsBar()" constructor
        
        usingTime = 30;
        autosaveTime = new TextField(Integer.toString(usingTime));
        int index = 0;
        n_polygon = 5;
        polygon_sides = new TextArea("5");
        //autosaveTime = new TextField(Integer.toString(usingTime));
        polygon_sides.setPrefHeight(30);
        polygon_sides.setPrefWidth(50);
        polygon_sides.setPrefWidth(50);
        
        undo = new Button();
        undo.setPrefSize(BTN_SIZE, BTN_SIZE);
        
//        try{
//            Image img = new Image("H:/Sprint/Paint/Paint/images/undo.png", 55, 55, false, false);
//            ImageView icon = new ImageView(img);
//            undo.setGraphic(icon);
//        }
//        catch(NullPointerException e)
//                {System.out.println(e + "happened");
//                }
        redo = new Button();
        redo.setPrefSize(BTN_SIZE, BTN_SIZE);
        
        
        
                                                                                //DEFINING AND INITIALIZING ALL THE BUTTONS DECLARED ABOVE
        ZoomIn = new ToggleButton("Zoom In");
        ZoomOut = new ToggleButton("Zoom Out");
        toolSelector = 
                new ChoiceBox(FXCollections.observableArrayList(MY_TOOLS));
        writeHere = new TextArea();                                             //Text area for the text tool
        writeHere.setPrefHeight(30);                                            //Height of the text area
        writeHere.setPrefWidth(120);                                            //Height of the text area
        TforText = new ToggleButton("Text");                                    //Toggle button for text tool
        colorGrabber = new ToggleButton("Color Grabber");                       //Color grabber toggle button
        FillColor = new ColorPicker(Color.TRANSPARENT);
        FillColor.setPrefWidth(80);
        StrokeColor = new ColorPicker(Color.BLACK);                             //Setting the default color as black for stroke
        StrokeColor.setPrefWidth(80);
        shapeFiller = new CheckBox("");
        StrokeSizer = new Slider(0, 50, 0);                                     //Slider for stroke size adjustment
        StrokeSizer.setMajorTickUnit(6.0); 
        StrokeSizer.setMinorTickCount(3);
        StrokeSizer.setSnapToTicks(true);
        StrokeSizer.setShowTickMarks(true);
        Tooltip sliderTip = new Tooltip("Adjust stroke size");                  //Adding tool tip
        StrokeSizer.setTooltip(sliderTip);
        Tooltip FillerTip = 
                new Tooltip("Check the box to fill color in shapes");           //New Tool tip
        shapeFiller.setTooltip(FillerTip);
        autosaveTime.setPrefWidth(55);
        
        
                                                                                //ADDING ALL THE MEMBERS TO THE TOOOLBAR
        getItems().addAll(new Label(" Tools: "), toolSelector, TforText, 
                new Label(" Text: "), writeHere, new Label("Polygon Sides: "),
                polygon_sides,  new Label("Stroke Color: "), StrokeColor,
                new Label(" Fill Color: "),  FillColor, colorGrabber,ZoomIn,
                ZoomOut, StrokeSizer, undo, redo,new Label("Autosave every "), 
                autosaveTime, new Label(" seconds") );
        
                                                                                //THIS FUNCTION KEEPS TRACK OF ANY CHANGE IN THE SLIDER VALUE
        StrokeSizer.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_value, Number new_val) -> {
            
            
            StrokeSize = new_val.doubleValue();
        });  
        
                                                                                //THIS FUNCTION KEEPS THE CHANGE OF TOOLS SELECTED IN TRACK
        toolSelector.getSelectionModel().selectedIndexProperty().addListener
        (new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue ov, Number value,
                    Number new_value){currentTool = new_value.intValue();}
        });
        
        autosaveTime.textProperty().addListener((observable, value, newValue) -> {
            usingTime = Integer.parseInt(newValue);
            Paint.getActiveTab().updateAutosaveTimer();
        }); 
        
        polygon_sides.textProperty().addListener                                //Reads from the text area for the text tool
        ((observable, value, newValue)->{
            n_polygon = Integer.parseInt(newValue);});
        
        undo.setOnAction((ActionEvent e)-> {                                    //Action event for undo button
            Paint.getActiveTab().undo();});
        
        redo.setOnAction((ActionEvent e)-> {                                    //Action event for redo button
            Paint.getActiveTab().redo();});
        
               
    }
    
                                                                                //TO GET THE TEXT FROM THE TEXT FIELD
    public static String getmyText(){
        return writeHere.getText();
    }
    
                                                                                //TO GET THE STROKE COLOR
    public static void setStrokeColor(Color myColor){
        StrokeColor.setValue(myColor);
    }
    
                                                                                //TO SET THE STROKE COLOR
    public static void setFillColor(Color myColor){
        FillColor.setValue(myColor);
    }
    
                                                                                //TO GET THE STROKE COLOR'S VALUE
    public static Color getStrokeColor(){
        return StrokeColor.getValue();
    }
      
                                                                                //TO GET THE FILL COLOR'S VALUE
    public static Color getFillColor(){
        return FillColor.getValue();
    }
    
                                                                                //GETTING THE STROKE SIZE
    public static double getStrokeSize(){
        return StrokeSize;
    }
    
    public static int getNumSides(){                                            //Returns the number of sides the user wants the polygon to have
        return n_polygon;
    }
    
    public void setNoneDefault(){
        toolSelector.setValue("None");
    }
    
                                                                                //FINDING THE CURRENT TOOL
    public static String getCurrentTool(){
        if(ZoomIn.isSelected())
            return "Zoom In";           
        if(ZoomOut.isSelected())
            return "Zoom Out";
        if(TforText.isSelected())
            return "Text";
        else if(colorGrabber.isSelected())
            return "Color Grabber";
        else
            return MY_TOOLS[currentTool];
    }
    
     public static int getAutosaveTime(){return usingTime;}
    
    }
    
    
    