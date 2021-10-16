/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

////////////////////******IMPORTING ALL THE NECESSARY LIBRARIES**********////////////////
import java.util.Stack;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.shape.*;
import javafx.scene.transform.Scale;
import static javafx.scene.paint.Color.*;



/**
 *
 * @author asedhai
 */
public class CanvasPlus extends Acrylic{
    
    //+++++++++++++++++++++++++++++++++++++++++++++++//
    //+++++++++DECLARING ALL THE VARIABLE++++++++++++//
    //++++++++++++++++++HERE+++++++++++++++++++++++++//
    //+++++++++++++++++++++++++++++++++++++++++++++++//
    public String selectedTool;
    public Paint p;
    private Stack<Image> undoHistory;                                           //Image stack to be used for undo                                
    private Stack<Image> redoHistory;                                           //Image stack to be used for redo       
    private static final double width = 1600;                                   //To define the width of a canvas
    private static final double height = 800;                                   //To define the height of a canvas
    public static GraphicsContext gc;                                           //To enable canvas' ability to be drawn on
    private double x, y;                                                        //To keep track of the location of mouse pointer on the canvas
    private Scale zoomScale;                                                    //To define the scale of canvas for zoom in and zoom out purposes
    private static double currentZoom = 1;                                      //To set the default zooming scale as 1
    public Circle circ;                                                         //To draw a circle on the canvas whenver needed
    public Line line;                                                           //To draw a line 
    public Rectangle rect;                                                      //To draw rectangle
    public Ellipse elps;                                                        //To draw ellipse
    private static Label zoom;                                                  //Label for zoom tool
    private Image clipboard;                                                    //Clipboard to store the cut item
    
    
    public CanvasPlus()
    {
        super();                                                                //Calls for the Canvas() constructor
        
        this.undoHistory = new Stack<>();                                       //Defines new stack of blank images for the canvas for undo feature
        this.redoHistory = new Stack<>();                                       //Defines new stack of blank images for the canvas for redo feature
        
        gc = this.getGraphicsContext2D();                                       //Associates graphics context to the canvas
        
        gc.setLineWidth(1);                                                     //Sets the current line width to 1
        gc.setLineCap(StrokeLineCap.ROUND);                                     //Sets the stroke cap as round
        this.setWidth(width);                                                   //Sets the width as provided
        this.setHeight(height);                                                 //Sets the height as provided
        line = new Line();                                                      //Initializing a new line
        rect = new Rectangle();                                                 //Initializing a new rectangle
        elps = new Ellipse();                                                   //Initializing a new Ellipse
        zoom = new Label();                                                     //Initializing a new label
        
        this.undoHistory.push(this.getRegion(0,0, this.getWidth(),              //Pushes blank image to the top of the stack
                this.getHeight())); 

        this.setOnMousePressed(e -> {                                           //Action event for every time the mouse button is clicked
            x = e.getX();                                                       //Stores current position of mouse in x direction
            y = e.getY();                                                       //Stores current position of mouse in y direction
            this.setLineColor(ToolsPlus.getStrokeColor());                      //Gets the current stroke color from the color picker and sets it as the stroke color for the graphic context 
            this.setFillColor(ToolsPlus.getFillColor());                        //Does the same thing (like Stroke color) but for the fill color
            this.setLineWidth(ToolsPlus.getStrokeSize());                       //Gets the current value of slider and sets the stroke size to that value
            this.setFillShape(true);                                            //Allows figures to be filled
            
            switch(ToolsPlus.getCurrentTool()){                                 //Gets the current tool and checks; switches cases based on the tool selected 
                case "Zoom In":                                                 //If the tool: "Zoom In" is selected:       
                    this.getTransforms().remove(zoomScale);                     //      - removes the current scaling
                    currentZoom += .05;                                         //      - sets the current zoom at 0.05 and increases it by the same number every time the zoom in tool is used
                    zoomScale = new Scale(currentZoom, currentZoom, 0, 0);      //      - sets current zoomScale based on the current zoom
                    this.getTransforms().add(zoomScale);                        //      - changes the canvas as the zoom button is pressed
                    zoom.setText(Math.round(currentZoom * 100) + "%");          //      - Zoom label describing the current zoom level
                    ToolsPlus.ZoomIn.setDisable(false);                         //      - If maximum zoom in has reached, sets the Zoom In to false 
                    selectedTool = "Zoom In selected";
                    
                    break;                                                      // breaks
                case "Zoom Out":                                                //If the tool: "Zoom Out" is selected:
                    this.getTransforms().remove(zoomScale);                     //      - removes the current scaling
                    currentZoom -= .05;                                         //      - sets the current zoom at 0.05 and decreases it by the same number every time the zoom in tool is used
                    zoomScale = new Scale(currentZoom, currentZoom, 0, 0);      //      - sets current zoomScale based on the current zoom
                    this.getTransforms().add(zoomScale);                        //      - changes the canvas as the zoom button is pressed
                    zoom.setText(Math.round(currentZoom * 100) + "%");          //      - Zoom label describing the current zoom level
                    if (currentZoom < .06) {                                    //      - If maximum zoom in has reached, sets the Zoom In to false 
                        ToolsPlus.ZoomOut.setDisable(true);
                    }
                    break;
                case "None":                                                    //If none is selected: Do not use any tool. This is the default tool when the app starts
                    break;
                case("Line"):                                                   //If line is selected, draw a line based on how the mouse is dragged.
                    this.drawLine(x, y, x, y);
                    break;                                                          
                case("Rectangle"):                                              //If rectangle tool is selected, draw a rectangle.
                    this.drawRect(x,y,x,y);
                    break;
                case("Round Rectangle"):                                        //If the rounded rectangle tool is selected, draw that
                    this.drawRoundRect(x,y,x,y);
                    break;
                case("Square"):                                                 //If square tool is selected
                    this.drawSquare(x, y, x, y);
                    break;
                case("Ellipse"):                                                //If Ellipse tool is selected
                    this.drawEllipse(x,y,x,y);
                    break;
                case("Circle"):                                                 //If Circle is selected
                    this.drawNgon(x, y, x, y, 314);
                    break;
                case("Regular Polygon"):                                        //If polygon tool is selected, draw polygon
                    this.drawNgon(x, y, x, y, 5);   
                    break;
                case("Cut"):                                                    //If Cut tool is selected
                    break;
                case("Paste"):                                                  //If paste tool is selected
                    break;
                case("Text"):                                                   //if Text tool is selected
                    this.drawText(ToolsPlus.getmyText(), e.getX(), e.getY());
                    break;
                case("Color Grabber"):                                          //If the "Color Grabber" tool is selected
                    ToolsPlus.setFillColor(this.getColorAtPixel(x,y));  
                    ToolsPlus.setStrokeColor(this.getColorAtPixel(x,y));
                    break;
            }
        });
        this.setOnMouseDragged(e -> {
            switch(ToolsPlus.getCurrentTool()){
                case("Pencil"):                                                 //Pencil tool
                    this.drawLine(x, y, e.getX(), e.getY());
                    x = e.getX();
                    y = e.getY();
                    break;
                    
                case("Eraser"):                                                 //Eraser tool
                    this.setLineColor(WHITE);
                    this.drawLine(x, y, e.getX(), e.getY());
                    x = e.getX();
                    y = e.getY();
                    break;
                case("Text"):                                                   //Text tool
                    this.undo();
                    this.drawText(ToolsPlus.getmyText(), e.getX(), e.getY());
                    this.updateStacks(this.getRegion(0,0,this.getWidth(),
                            this.getHeight()));
                    break;
                case("Line"):                                                   //Line tool
                    this.undo();
                    this.drawLine(x, y, e.getX(), e.getY());
                    this.updateStacks(this.getRegion(0,0,this.getWidth(),
                            this.getHeight()));
                    break;
                case("Rectangle"):                                              //Rectangle tool
                    this.undo();
                    this.drawRect(x, y, e.getX(), e.getY());
                    this.updateStacks(this.getRegion(0,0,this.getWidth(),
                            this.getHeight()));
                    break;
                case("Round Rectangle"):                                        //Round rectangle tool
                    this.undo();
                    this.drawRoundRect(x,y,e.getX(),e.getY());
                    this.updateStacks(this.getRegion(0,0,this.getWidth(),
                            this.getHeight()));
                    break;
                case("Square"):                                                 //Square tool
                    this.undo();
                    this.drawSquare(x, y, e.getX(), e.getY());
                    this.updateStacks(this.getRegion(0,0,this.getWidth(),
                            this.getHeight()));
                    break;
                case("Ellipse"):                                                //Ellipse tool
                    this.undo();
                    this.drawEllipse(x,y,e.getX(),e.getY());
                    this.updateStacks(this.getRegion(0,0,this.getWidth(),
                            this.getHeight()));
                    break;
                case("Circle"):                                                 //Circle tool
                    this.undo();
                    this.drawNgon(x, y, e.getX(), e.getY(), 314);
                    this.updateStacks(this.getRegion(0,0,this.getWidth(),
                            this.getHeight()));
                    break;
                case("Regular Polygon"):                                        //Regular polygon tool
                    this.undo();
                    this.drawNgon(x, y, e.getX(), e.getY(),
                            ToolsPlus.getNumSides());
                    this.updateStacks(this.getRegion(0,0,this.getWidth(),
                            this.getHeight()));
                    break;  
                case("Cut"):                                                    //Cut tool
                    break;
                case("Paste"):                                                  //Paste tool
                    this.undo();
                    this.drawImageAt(clipboard, e.getX(), e.getY());
                    this.updateStacks(this.getRegion(0,0,this.getWidth(),
                            this.getHeight()));
                    break;
                case("Color Grabber"):                                          //Color Grabber tool
                    ToolsPlus.setFillColor(
                            this.getColorAtPixel(e.getX(),e.getY()));
                    ToolsPlus.setStrokeColor(
                            this.getColorAtPixel(e.getX(),e.getY()));
                    break;
            }
        });
        this.setOnMouseReleased(e -> {
            switch(ToolsPlus.getCurrentTool()){
                case("Pencil"):                                                 //Pencil Tool
                    this.drawLine(x, y, e.getX(), e.getY());
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
            
                    
                case("Eraser"):                                                 //Eraser tool
                    this.setLineColor(WHITE);
                    this.drawLine(x, y, e.getX(), e.getY());
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
                case("Line"):
                    this.drawLine(x, y, e.getX(), e.getY());
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
                case("Rectangle"):
                    this.drawRect(x, y, e.getX(), e.getY());
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
                case("Round Rectangle"):
                    this.drawRoundRect(x,y,e.getX(),e.getY());
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
                case("Square"):
                    this.drawSquare(x, y, e.getX(), e.getY());
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
                case("Ellipse"):
                    this.drawEllipse(x, y, e.getX(), e.getY());
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
                case("Circle"):
                    this.drawNgon(x, y, e.getX(), e.getY(), 314);
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
                case("Regular Polygon"):
                    this.drawNgon(x, y, e.getX(), e.getY(), ToolsPlus.getNumSides());
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
                case("Text"):
                    this.drawText(ToolsPlus.getmyText(), e.getX(), e.getY());
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
                case("Color Grabber"):
                    ToolsPlus.setFillColor(this.getColorAtPixel(e.getX(),e.getY()));
                    ToolsPlus.setStrokeColor(this.getColorAtPixel(e.getX(),e.getY()));
                    break;
                case("Cut"):
                    this.clipboard = this.getRegion(x, y, e.getX(), e.getY());
                    //draws white rectangle where we cut
                    this.setFillShape(true);
                    this.setLineWidth(1);
                    this.setFillColor(WHITE);
                    this.setLineColor(WHITE);
                    this.drawRect(x, y, e.getX(), e.getY());
                    this.updateStacks(this.getRegion(0,0, this.getWidth(), this.getHeight()));
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
                case("Paste"):
                    if(this.clipboard != null){
                        this.drawImageAt(this.clipboard, e.getX(), e.getY());
                    }
                    Paint.getActiveTab().setUnsavedChanges(true);
                    break;
            }
            Paint.getActiveTab().updateTabTitle();
            this.updateStacks(this.getRegion(0,0, this.getWidth(), this.getHeight()));
            x = y = 0;  //resets the x and y coordinates at the end to be safe
        });
    }
    
    public void updateStacks(Image myImage)                                     
    {
        undoHistory.push(myImage);                                              //updates stacks in case of undo
        redoHistory.clear();
    }

    public void undo(){
        Image myImage = undoHistory.pop();                                      //stores the image by poping the top stacks
        if(!undoHistory.empty()){
            redoHistory.push(myImage);                                          //pushes the image to the redo histry stack
            this.drawImage(undoHistory.peek());                                 //draws image from undoHistory
        }
        else{
            this.drawImage(myImage);                                            //draws image from the stack on the canvas
            undoHistory.push(myImage);                                          //pushes the image from undoHistory image
        }
    }
    
    public void redo(){
        if(!redoHistory.empty()){
            Image myImage = redoHistory.pop();                                  //pops the first image from the redo history
            undoHistory.push(myImage);                                          //pushes the new stack into the old
            this.drawImage(myImage);    
        }
        
    }
    
     public void updateDimensions(Image i) {
        setDimensions((int) i.getWidth(), (int) i.getHeight());
    }
    
    public void updateDimensions(boolean inc_zoom) {
        if (inc_zoom) {
            this.setWidth(this.getWidth() * 2);
            this.setHeight(this.getHeight() * 2);
        } else {
            this.setWidth(this.getWidth() / 2);
            this.setHeight(this.getHeight() / 2);
        }
    }   
    
    public void updateDimensions() {
        //Also potential thought, I may have the image be a proportion of the current window size,
        //so that when the main window is resized, the image resizes with it.
        if (Paint.getActiveTab().loaded_image != null) {
            //setDimensions(Paint.getCurrentTab().opened_image.getWidth(), Paint.getCurrentTab().opened_image.getHeight());
            this.setHeight(Paint.getActiveTab().loaded_image.getHeight());
            this.setWidth(Paint.getActiveTab().loaded_image.getWidth());
        } else {
            this.setHeight(0);
            this.setWidth(0);
        }
    }
    
    public void setDimensions(int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
    }
    
    
}
