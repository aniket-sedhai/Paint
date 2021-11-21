package paint;


import java.io.File;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

/**
 * @author Aniket Sedhai
 *Contains helper function for the CanvasPlus to use
 */
public class Acrylic extends Canvas{
    private boolean fillShape;
    private GraphicsContext gc;
    
    public Acrylic(){
        super();
        this.fillShape = false;
        this.gc = this.getGraphicsContext2D();
        this.gc.setLineCap(StrokeLineCap.ROUND);
    }
    /**
     * Draws a rectangle from (x1,y1) to (x2,y2)
     * @param x1 Initial x
     * @param y1 Initial y
     * @param x2 Ending x
     * @param y2 Ending y
     */
    public void drawRect(double x1, double y1, double x2, double y2){
        double x = (x1 < x2 ? x1 : x2); 
        double y = (y1 < y2 ? y1 : y2); 
        double w = Math.abs(x1 - x2);   
        double h = Math.abs(y1 - y2);
        if(this.getFillShape())
            this.gc.fillRect(x,y,w,h);
        this.gc.strokeRect(x,y,w,h);
    }
    /**
     * Draws a rounded rectangle from (x1,y1) to (x2,y2)
     * @param x1 Initial x
     * @param y1 Initial y
     * @param x2 Ending x
     * @param y2 Ending y
     */
    public void drawRoundRect(double x1, double y1, double x2, double y2){
        double x = (x1 < x2 ? x1 : x2); 
        double y = (y1 < y2 ? y1 : y2); 
        double w = Math.abs(x1 - x2);
        double h = Math.abs(y1 - y2);
        if(this.getFillShape())
            this.gc.fillRoundRect(x,y,w,h,10,10);
        this.gc.strokeRoundRect(x,y,w,h,10,10);
    }
    
    /**
     * Draws an ellipse from (x1,y1) to (x2,y2)
     * @param x1 Initial x
     * @param y1 Initial y
     * @param x2 Ending x
     * @param y2 Ending y
     */
    public void drawEllipse(double x1, double y1, double x2, double y2){
        double x = (x1 < x2 ? x1 : x2);
        double y = (y1 < y2 ? y1 : y2);
        double w = Math.abs(x1 - x2);
        double h = Math.abs(y1 - y2);
        if(this.getFillShape())
            this.gc.fillOval(x,y,w,h);
        this.gc.strokeOval(x,y,w,h);
    }
    /**
     * Draws an n-sided polygon given the center (x1, y1) and point on the radius (x2, y2)
     * @param x1 The x coordinate for a center of a circumscribed circle
     * @param y1 The y coordinate for a center of a circumscribed circle
     * @param x2 An x coordinate that represents a tangent point on a circle
     * @param y2 An y coordinate that represents a tangent point on a circle
     * @param n The number of sides/vertices
     */
    public void drawNgon(double x1, double y1, double x2, double y2, int n){
        double[] xPoints = new double[n];
        double[] yPoints = new double[n];
        double radius = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
        double startAngle = Math.atan2(y2 - y1, x2 - x1);
        
        for(int i = 0; i < n; i++){
            xPoints[i] = x1 + (radius * Math.cos(((2*Math.PI*i)/n) + startAngle));
            yPoints[i] = y1 + (radius * Math.sin(((2*Math.PI*i)/n) + startAngle));
        }
        if(this.getFillShape())
            this.gc.fillPolygon(xPoints, yPoints, n);
        this.gc.strokePolygon(xPoints, yPoints, n);
    }
    /**
     * Draws a line from (x1,y1) to (x2,y2)
     * @param x1 Initial x
     * @param y1 Initial y
     * @param x2 Ending x
     * @param y2 Ending y
     */
    public void drawLine(double x1, double y1, double x2, double y2){gc.strokeLine(x1, y1, x2, y2);}
    /**
     * Draws the specified text at the initial point (x, y)
     * @param text The text to draw
     * @param x The bottom left x coordinate
     * @param y The bottom left y coordinate
     */
    public void drawText(String text, double x, double y){
        double temp = this.gc.getLineWidth();   
        this.gc.setLineWidth(1);
        if(this.getFillColor() == null){
            this.setFillColor(this.getLineColor());
            gc.fillText(text, x, y);
            this.setFillColor(this.getFillColor());
        }
        else{
            gc.fillText(text, x, y);
        }
        gc.strokeText(text, x, y);
        this.gc.setLineWidth(temp);
    }
    
    /**
     * Draws a square from (x1,y1) to (x2,y2)
     * @param x1 Initial x
     * @param y1 Initial y
     * @param x2 Ending x
     * @param y2 Ending y
     */
    public void drawSquare(double x1, double y1, double x2, double y2){
        final double ANGLE_45 = Math.PI/4.0;    
        final int SIDES = 4;
        double[] xPoints = new double[SIDES];   
        double[] yPoints = new double[SIDES];
        double radius = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
       
        for(int i = 0; i < SIDES; i++){
            xPoints[i] = x1 + (radius * Math.cos(((2*Math.PI*i)/4) + ANGLE_45));   
            yPoints[i] = y1 + (radius * Math.sin(((2*Math.PI*i)/4) + ANGLE_45));
        }
        if(this.getFillShape())
            this.gc.fillPolygon(xPoints, yPoints, SIDES);
        this.gc.strokePolygon(xPoints, yPoints, SIDES);
    }
    
    /**
     * Draws an image to the canvas starting at the origin
     * @param im The image object to draw
     */
    public void drawImage(Image im){
        clearCanvas();
        this.setWidth(im.getWidth());
        this.setHeight(im.getHeight());
        this.gc.drawImage(im, 0, 0);
    }
    /**
     * Draws an image to the canvas starting at the origin
     * @param file The file to get the image from
     */
    public void drawImage(File file){
        if(file != null){
            Image im = new Image(file.toURI().toString());
            this.drawImage(im);
        }
            
    }
    
    public Image getImage() {
        SnapshotParameters params = new SnapshotParameters();
        //params.setFill(Color.TRANSPARENT);
        WritableImage wi = this.snapshot(params, null);
        ImageView iv = new ImageView(wi);
        return iv.getImage();
    }
    /**
     * Draws an image to the canvas starting at the point x, y
     * @param im The image object to draw
     * @param x The x coordinate to draw the top left point of the image
     * @param y The y coordinate to draw the top left point of the image
     */
    public void drawImageAt(Image im, double x, double y){this.gc.drawImage(im, x, y);}
    /**
     * Gets the color at the coordinate (x,y)
     * @param x The x value to get the color at
     * @param y The y value to get the color at
     * @return The Color at that pixel
     */
    public Color getColorAtPixel(double x, double y){
        return this.getRegion(x, y, x+1, y+1).getPixelReader().getColor(0, 0);
    }
    /**
     * Gets the image using the bounds (x1,y1) and (x2,y2)
     * @param x1 Starting x value (top left)
     * @param y1 Starting y value (top left)
     * @param x2 Ending x value (bottom right)
     * @param y2 Ending y value (bottom right)
     * @return The image in that region
     */
    public Image getRegion(double x1, double y1, double x2, double y2){
        SnapshotParameters sp = new SnapshotParameters();
        WritableImage wi = new WritableImage((int)Math.abs(x1 - x2),(int)Math.abs(y1 - y2));
                    
        sp.setViewport(new Rectangle2D(
            (x1 < x2 ? x1 : x2),
            (y1 < y2 ? y1 : y2),
            Math.abs(x1 - x2),
            Math.abs(y1 - y2)));
        
        this.snapshot(sp, wi);
        return wi;
    }
    public Image getRegionAsImage(Point2D ic, double cx, double cy) {
        PixelReader r = this.getImage().getPixelReader();
        double ix = (double) ic.getX();
        double iy = (double) ic.getY();
        WritableImage wi = new WritableImage(
            r,
            roundDouble(ix),
            roundDouble(iy),
            roundDouble(cx - ix),
            roundDouble(cy - iy)
        );
        return (Image) wi;
    }
    
    public void applyEffectToRegion(Point2D ic, double cx, double cy, Effect e) {
        Image wi = getRegionAsImage(ic, cx, cy);
        //2
        CanvasPlus t = new CanvasPlus();
        t.updateDimensions(wi); 
        t.gc.setEffect(e);
        t.gc.drawImage(wi, 0, 0);
        this.gc.drawImage(
            t.getImage(),
            ic.getX(),
            ic.getY()
        );
    }
    
    /**
     * Clears everything on the canvas
     */
    public void clearCanvas(){
        this.gc.clearRect(0,0, this.getWidth(), this.getHeight());
    }
    
    /**
     * Sets the fillShape attribute to the boolean value given
     * @param fillShape Boolean value to tell us whether to fill the shapes or not
     */
    public void setFillShape(boolean fillShape){this.fillShape = fillShape;}
    
    /**
     * Gives us the fillShape attribute boolean value
     * @return Whether or not to fill the shape or not
     */
    public boolean getFillShape(){return this.fillShape;}
    
    /**
     * Sets all lines and colors to the specified color
     * @param color The color to set the lines/borders
     */
    public void setLineColor(Color color){gc.setStroke(color);}
    
    /**
     * Gets the color that the lines and borders are set to
     * @return The Color object that represents line/border color
     */
    public Color getLineColor(){return (Color)gc.getStroke();}
    
    /**
     * Sets the fills of the shapes to the specified color
     * @param color The color to set the fill of shapes
     */
    public void setFillColor(Color color){gc.setFill(color);}
    
    /**
     * Gets the color that the fill is set to
     * @return The Color object that represents the fill color
     */
    public Color getFillColor(){return (Color)gc.getFill();}
    
    /**
     * Sets the width of the lines/borders
     * @param width The value to set line width to
     */
    public void setLineWidth(double width){this.gc.setLineWidth(width);}
    
    /**
     * Gets the width of the lines/borders
     * @return The value representing the line/border width
     */
    public double getLineWidth(){return this.gc.getLineWidth();}
    
    /**
     * 
     * @param d is the number to be rounded
     * @return the integer that has been rounded 
     */
    private int roundDouble(double d) {
        return (int) Math.round(d);
    }
}