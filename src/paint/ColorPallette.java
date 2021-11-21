package paint;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/*All the imports are going to be here*/


public class ColorPallette extends GridPane{
    private final Button[] colorButton;
    private final int n_row;
    private final int n_column;
    
    public ColorPallette(){
        colorButton = new Button[20];
        n_row = 2;
        n_column = 10;
        int btnCnt = 20;
        
        this.setPrefSize(310, 70);
        for(int i = 0; i < btnCnt; i++)
        {
            colorButton[i].setPrefSize(30, 30);
        }
        
        int buttonIndex = 0;
        for(int i = 0; i < n_row; i++)
        {
            for(int j = 0; j < n_column; j++)
            {
                this.add(colorButton[buttonIndex], j, i);
            }
        }

}

}