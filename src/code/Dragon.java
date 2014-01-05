package code;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Dragon {
    int x, y;
    float tileWidth = 1;
    float tileHeight = 1;
    int width = 200;
    int height = 236;
    int flyingRow = 0;

    org.newdawn.slick.Animation flyingRight, flyingLeft;

    boolean facingRight;
    public Dragon(String path, int x, int y){
	this.x = x; this.y = y;
	SpriteSheet spriteSheet = null;
	try{
	    spriteSheet = new SpriteSheet(path, width, height);
	}catch(Exception e){ }

	Image[] flyRight = {spriteSheet.getSprite(0, flyingRow).getFlippedCopy(true, false).getScaledCopy(.5f), spriteSheet.getSprite(1, flyingRow).getFlippedCopy(true, false).getScaledCopy(.5f),
		spriteSheet.getSprite(2, flyingRow).getFlippedCopy(true, false).getScaledCopy(.5f), spriteSheet.getSprite(3, flyingRow).getFlippedCopy(true, false).getScaledCopy(.5f),
		spriteSheet.getSprite(4, flyingRow).getFlippedCopy(true, false).getScaledCopy(.5f), spriteSheet.getSprite(5, flyingRow).getFlippedCopy(true, false).getScaledCopy(.5f),
		spriteSheet.getSprite(6, flyingRow).getFlippedCopy(true, false).getScaledCopy(.5f), spriteSheet.getSprite(7, flyingRow).getFlippedCopy(true, false).getScaledCopy(.5f),
		spriteSheet.getSprite(8, flyingRow).getFlippedCopy(true, false).getScaledCopy(.5f), spriteSheet.getSprite(9, flyingRow).getFlippedCopy(true, false).getScaledCopy(.5f)};
	int[] dur = {200,200,200,200,200,200,200,200,200,200};
	flyingRight = new org.newdawn.slick.Animation(flyRight, dur);

	Image[] flyLeft = {spriteSheet.getSprite(0, flyingRow).getFlippedCopy(true, false).getScaledCopy(.5f), spriteSheet.getSprite(1, flyingRow).getFlippedCopy(false, false).getScaledCopy(.5f),
		spriteSheet.getSprite(2, flyingRow).getFlippedCopy(false, false).getScaledCopy(.5f), spriteSheet.getSprite(3, flyingRow).getFlippedCopy(false, false).getScaledCopy(.5f),
		spriteSheet.getSprite(4, flyingRow).getFlippedCopy(false, false).getScaledCopy(.5f), spriteSheet.getSprite(5, flyingRow).getFlippedCopy(false, false).getScaledCopy(.5f),
		spriteSheet.getSprite(6, flyingRow).getFlippedCopy(false, false).getScaledCopy(.5f), spriteSheet.getSprite(7, flyingRow).getFlippedCopy(false, false).getScaledCopy(.5f),
		spriteSheet.getSprite(8, flyingRow).getFlippedCopy(false, false).getScaledCopy(.5f), spriteSheet.getSprite(9, flyingRow).getFlippedCopy(false, false).getScaledCopy(.5f)};
	flyingLeft = new org.newdawn.slick.Animation(flyLeft, dur);
    }

    public void draw(Graphics g, float shiftX, float shiftY){
	if(facingRight){
	    flyingRight.draw(x-shiftX, y-shiftY);
	}else{
	    flyingLeft.draw(x-shiftX, y-shiftY);
	}
    }

    public void update(Player player){
	facingRight = player.isFacingRight();
	this.x = (int) (player.getX() - (2 + this.width));
	this.y = (int) player.getY();
    }
}
