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
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * This class implements everything that is going into the toolbar
 * @author aniket
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
    private final int BTN_SIZE = 25, BTN_SIZE2 = 60, BTN_SIZE3 = 80;
    private static TextArea polygon_sides;
    private static TextField autosaveTime2;
    private static Label polyLabel;
    private static Label toolLabel;
    private static Label strokeColorL;
    private static Label fillColorL;
    private static Label autosaveTimer;
    //private static ColorPallette myColorPallette;
    
                                                                                //DEFINING ALL THE TOOLS THAT I WILL HAVE IN MY PAINT
    public final static String[] MY_TOOLS = {"None",
        "Pencil", "Eraser", "Line", "Circle", "Rectangle","Round Rectangle",
        "Square", "Ellipse", "Regular Polygon", "Blur", "Cut", "Copy", "Paste","Crop","Select/Move", "Tool"};
    
                                                                                //CONSTRUCTOR
    public  ToolsPlus(){
        super();                                                                //Calls the "ToolsBar()" constructor
        
        //myColorPallette = new ColorPallette();
        usingTime = 30;
        autosaveTime2 = new TextField(Integer.toString(usingTime));
        int index = 0;
        n_polygon = 5;
        polygon_sides = new TextArea("5");
        polygon_sides.setPrefHeight(30);
        polygon_sides.setPrefWidth(50);
        polygon_sides.setPrefWidth(50);
        
        undo = new Button();
        undo.setPrefSize(BTN_SIZE, BTN_SIZE);
        
            
        redo = new Button();
        redo.setPrefSize(BTN_SIZE, BTN_SIZE);
        
        
        undo.setGraphic(new ImageView(new Image
        ("file:src/Images/undo.png", 25, 25, false, false)));
        Tooltip undoTip = new Tooltip
        ("Undo Last Action");
        undo.setTooltip(undoTip); 
        
        redo.setGraphic(new ImageView(new Image
        ("file:src/Images/redo.png", 25, 25, false, false)));
        Tooltip redoTip = new Tooltip
        ("Redo Last Undone Action");
        redo.setTooltip(redoTip);  
        
        
                                                                                //DEFINING AND INITIALIZING ALL THE BUTTONS DECLARED ABOVE
        ZoomIn = new ToggleButton("");
        ZoomOut = new ToggleButton("");
        
        ZoomIn.setPrefSize(BTN_SIZE, BTN_SIZE);
        ZoomOut.setPrefSize(BTN_SIZE, BTN_SIZE);
        
        ZoomIn.setGraphic(new ImageView(new Image
        ("file:src/Images/ZoomInIcon.png", 25, 25, false, false)));
        Tooltip ZinTip = new Tooltip
        ("Zoom In");
        ZoomIn.setTooltip(ZinTip); 
        
        ZoomOut.setGraphic(new ImageView(new Image
        ("file:src/Images/ZoomOutIcon.png", 25, 25, false, false)));
        Tooltip ZoutTip = new Tooltip
        ("Zoom Out");
        ZoomOut.setTooltip(ZoutTip);
        
        toolLabel = new Label("");
        toolSelector = 
                new ChoiceBox(FXCollections.observableArrayList(MY_TOOLS));
        writeHere = new TextArea();                                             //Text area for the text tool
        writeHere.setPrefHeight(30);                                            //Height of the text area
        writeHere.setPrefWidth(120);                                            //Height of the text area
        TforText = new ToggleButton("");                                        //Toggle button for text tool
        colorGrabber = new ToggleButton("");                                    //Color grabber toggle button
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
        autosaveTime2.setPrefWidth(55);
        
        TforText.setPrefSize(BTN_SIZE, BTN_SIZE);
        TforText.setGraphic(new ImageView
        (new Image("file:src/Images/text.png", 25, 25, false, false)));
        Tooltip TextTip = new Tooltip
        ("Text Tool. Fill the desired text inside the box.");
        TforText.setTooltip(TextTip);
        
        strokeColorL = new Label("");
        strokeColorL.setGraphic(new ImageView
        (new Image("file:src/Images/stroke.png", 35, 35, false, false)));
        Tooltip strokeLabel = new Tooltip
        ("Choose Stroke Color");
        strokeColorL.setTooltip(strokeLabel);
        
        fillColorL = new Label("");
        fillColorL.setGraphic(new ImageView
        (new Image("file:src/Images/fill.png", 25, 25, false, false)));
        Tooltip fillLabel = new Tooltip
        ("Choose Fill Color");
        fillColorL.setTooltip(fillLabel);  
        
        autosaveTimer = new Label("");
        autosaveTimer.setGraphic(new ImageView
        (new Image("file:src/Images/saveTimer.png", 35, 35, false, false)));
        Tooltip autosaverT = new Tooltip
        ("Autosave Timer.");
        autosaveTimer.setTooltip(autosaverT);        
        
        colorGrabber.setPrefSize(BTN_SIZE, BTN_SIZE);
        colorGrabber.setGraphic(new ImageView
        (new Image("file:src/Images/dropper.png", 25, 25, false, false)));
        Tooltip grabberTip = new Tooltip
        ("Color Grabber");
        colorGrabber.setTooltip(grabberTip); 
        
        polyLabel = new Label("");
        polyLabel.setGraphic(new ImageView
        (new Image("file:src/Images/poly.png", 35, 35, false, false)));
        Tooltip PolyTip = new Tooltip
        ("Choose the desired number of sides for polygon.");
        polyLabel.setTooltip(PolyTip);
        
        toolLabel = new Label("");
        toolLabel.setGraphic(new ImageView
        (new Image("file:src/Images/tools.png", 25, 25, false, false)));
        Tooltip toolLabelTip = new Tooltip("Select tool from dropdown menu");
        toolLabel.setTooltip(toolLabelTip);
        
        
       
                                                                                //ADDING ALL THE MEMBERS TO THE TOOOLBAR
        getItems().addAll(toolLabel, toolSelector, new Separator(), TforText, 
               writeHere, new Separator(), polyLabel,
                polygon_sides, new Separator(), strokeColorL, StrokeColor, 
                new Separator(),
                fillColorL,  FillColor, new Separator(), colorGrabber,
                new Separator(),ZoomIn,
                ZoomOut, new Separator(), StrokeSizer, new Separator(),
                undo, redo, new Separator(), autosaveTimer, 
                autosaveTime2, new Label(" seconds") );
        
                                                                                //THIS FUNCTION KEEPS TRACK OF ANY CHANGE IN THE SLIDER VALUE
        StrokeSizer.valueProperty().addListener((ObservableValue<?
                extends Number> ov, Number old_value, Number new_val) -> {
            
            
            StrokeSize = new_val.doubleValue();
        });  
        
                                                                                //THIS FUNCTION KEEPS THE CHANGE OF TOOLS SELECTED IN TRACK
        toolSelector.getSelectionModel().selectedIndexProperty().addListener
        (new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue ov, Number value,
                    Number new_value){currentTool = new_value.intValue();}
        });
        
        autosaveTime2.textProperty().addListener((observable, value, newValue) -> {
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
    /**
     * Extracts text from the text box if the user has written anything
     * @return text from the text box
     */
    public static String getmyText(){
        return writeHere.getText();
    }
    
                                                                                //TO GET THE STROKE COLOR
    /**
     * Sets the stroke color according the color from the color picker
     * @param myColor color from the color chooser
     */
    public static void setStrokeColor(Color myColor){
        StrokeColor.setValue(myColor);
    }
    
                                                                                //TO SET THE STROKE COLOR
    /**
     * Sets fill color based on the color from the color chooser
     * @param myColor color from the color chooser
     */
    public static void setFillColor(Color myColor){
        FillColor.setValue(myColor);
    }
    
                                                                                //TO GET THE STROKE COLOR'S VALUE
    /**
     * Gets stroke color from the color picker
     * @return returns the color obtained from the chooser
     */
    public static Color getStrokeColor(){
        return StrokeColor.getValue();
    }
      
                                                                                //TO GET THE FILL COLOR'S VALUE
    /**
     * Gets the fill color from the color chooser and
     * @return color obtained from the color chooser
     */
    public static Color getFillColor(){
        return FillColor.getValue();
    }
    
                                                                                //GETTING THE STROKE SIZE
    /**
     * Gets stroke size from the slider
     * @return the stroke size from the slider value
     */
    public static double getStrokeSize(){
        return StrokeSize;
    }
    
    /**
     * Gets the number of sides of the polygon from the box
     * @return the number of sides for the polygon
     */
    public static int getNumSides(){                                            //Returns the number of sides the user wants the polygon to have
        return n_polygon;
    }
    
    /**
     * Sets the active tool as None by default
     */
    public void setNoneDefault(){
        toolSelector.setValue("None");
    }
    
                                                                                //FINDING THE CURRENT TOOL
    /**
     * Gets the current tool from the tool selector
     * @return 
     */
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
    
    /**
     * Returns the time being used for autosave
     * @return Time for autosave
     */
     public static int getAutosaveTime(){return usingTime;}
    
    }
    
    
    