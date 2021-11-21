package paint;

import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestToolsPlus {
    
    public TestToolsPlus() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Testing setFillColor() Method
     */
    @Test
    public void testsetStrokeColor(){
        System.out.println("testing setStrokeColor");
        Color myColor = Color.WHITE;
        ToolsPlus instance = new ToolsPlus();
        instance.setStrokeColor(myColor);
        assertEquals(myColor,instance.getStrokeColor()); //if getStrokeColor returns WHITE, it works
    }

    /**
     * Testing setFillColor() Method
     */
    @Test
    public void testsetFillColor(){
        System.out.println("testing setFillColor");
        Color myColor = Color.RED;
        ToolsPlus instance = new ToolsPlus();
        instance.setFillColor(myColor);
        assertEquals(myColor,instance.getFillColor()); //if getFillColor returns Red, it works
    }

    /**
     * Testing getmyText() Method
     */
    public void testgetmyText(){
        System.out.println("testing getmyText()");
        String myText = "Text";
        ToolsPlus instance = new ToolsPlus();
        assertEquals(myText, instance.getmyText()); //if text returned from getmyText equals myText, it works
        
    }
}