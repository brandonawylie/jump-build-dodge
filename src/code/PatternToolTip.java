package code;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class PatternToolTip {
    	//size of the space between the gray and the actualblocks
	private int TT_SPACING = 3;
	//size from the player to the gray outline of the tooltip
	private int TT_MARGIN = 5;
	//size of the tool tip blocks
	private int TT_SIZE = 5;
	public void draw(Graphics g, String[][] colorArray, float desX, float desY, float shiftX, float shiftY){
	    if(colorArray != null && colorArray[0] != null){
		int rowSize = colorArray.length;
		int colSize = colorArray[0].length;

		//draw the gray outline
		g.setColor(Color.gray);
		g.fillRoundRect(desX - shiftX, desY - shiftY, colSize*TT_SIZE + colSize + TT_SPACING, rowSize*TT_SIZE + rowSize + TT_SPACING, 2);

		//draw the grid lines
		g.setColor(Color.black);
		for(int r = 1; r < colorArray.length; r++){
		    float x1 = desX + TT_SPACING;
		    float y1 = desY + TT_SPACING + r*TT_SIZE + (r-1);
		    float x2 = desX + TT_SPACING + colSize*TT_SIZE + colSize - 1;
		    float y2 = desY + TT_SPACING + r*TT_SIZE + (r-1);
		    g.drawLine(x1 - shiftX, y1 - shiftY, x2 - shiftX, y2 - shiftY);
		}
		g.setColor(Color.black);
		for(int c = 1; c < colorArray[0].length; c++){
		    float x1 = desX + TT_SPACING + c*TT_SIZE + (c-1);
		    float y1 = desY + TT_SPACING;
		    float x2 = desX + TT_SPACING + c*TT_SIZE + (c-1);
		    float y2 = desY + TT_SPACING + rowSize*TT_SIZE + rowSize - 1;
		    g.drawLine(x1 - shiftX, y1 - shiftY, x2 - shiftX, y2 - shiftY);
		}

		//draw the blocks
		for(int r = colorArray.length - 1; r >= 0 ; r--){
			for(int c = 0; c < colorArray[r].length; c++){
				String temp = colorArray[r][c];
				if(temp.equals("b")){
					g.setColor(Color.blue);
				}else if(temp.equals("g")){
					g.setColor(Color.green);
				}else if(temp.equals("r")){
					g.setColor(Color.red);
				}else if(temp.equals("p")){
					g.setColor(Color.pink);
				}else if(temp.equals("e")){
					g.setColor(Color.white);
				}else if(temp.equals("y")){
					g.setColor(Color.yellow);
				}
				float x = desX + TT_SPACING + TT_SIZE*c + c;
				float y = desY + TT_SPACING + TT_SIZE*r + r;
				float w = TT_SIZE;
				float h = TT_SIZE;
				g.fillRect(x - shiftX,y - shiftY,w,h);
			}
		}
	    }
	}


}
