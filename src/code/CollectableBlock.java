package code;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import code.infrastructure.Map;

public class CollectableBlock {

	//basic variables to store position, width/height and the x/y ratios of the map
	public float x, y, ratioX, ratioY, width, height;
	public float tileWidth = 1;
	public float tileHeight = 1;
	//colors for the inside and outline of the platform
	private Color outline = Color.white;
	Image image;
	public String color;
	public CollectableBlock(Map m, float x, float y, String path){
		this.x = x;
		this.y = y;
		Texture t = null;
		try {
			t = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
		} catch (IOException e) {}
		image = new Image(t).getScaledCopy((int)(m.mapTileWidth*tileWidth), (int)(m.mapTileHeight*tileHeight));
		width = image.getWidth();
		height = image.getHeight();
		color = path.substring(path.lastIndexOf("_"));
		color = color.substring(1, color.indexOf(".png"));
	}

	//contains most the logic for this collectable block
	public void update(){

	}

	//draws this collectableblock
	public void draw(Graphics g, int shiftX, int shiftY) {
		g.drawImage(image, x - shiftX, y - shiftY);
    }

	//returns the x coordinate of this platform
	public float getX(){
		return x;
	}

	//returns the y coordinate of this platform
	public float getY(){
		return y;
	}
}
