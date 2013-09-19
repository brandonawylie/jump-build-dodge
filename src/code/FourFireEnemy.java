package code;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

public class FourFireEnemy extends ShootingEnemy{
    Polygon shape;
//    x, y
//    x, y
//    x, y
    List<float[]> firePoints = new ArrayList<>();
    public FourFireEnemy(float x, float y, int width, int height) {
	super(x, y, width, height);


	shape = new Polygon();
	float pointX = x + width/2;
	float pointY = y;

	shape.addPoint(pointX, pointY);
	pointX = x  + 2*width/3;
	pointY = y + height/3;
	shape.addPoint(pointX, pointY);
	pointX = x  + width;
	pointY = y + height/2;
	shape.addPoint(pointX, pointY);
	pointX = x  + 2*width/3;
	pointY = y + 2*height/3;
	shape.addPoint(pointX, pointY);
	pointX = x  + width/2;
	pointY = y + height;

	shape.addPoint(pointX, pointY);
	pointX = x  + width/3;
	pointY = y + 2*height/3;
	shape.addPoint(pointX, pointY);
	pointX = x;
	pointY = y + height/2;

	shape.addPoint(pointX, pointY);
	pointX = x  + width/3;
	pointY = y + height/3;
	shape.addPoint(pointX, pointY);
    }

    public void draw(Graphics g, int shiftX, int shiftY){
	super.draw(g, shiftX, shiftY);

	//shift the shape w/ shiftX & shiftY
	shape.setX(x - shiftX);
	shape.setY(y - shiftY);
	g.setColor(Color.black);
	g.fill(shape);

	//reset the shape
	shape.setX(x);
	shape.setY(y);
    }

    public void update(int delta, Player player){
	for(int i = 0; i < shape.getPointCount(); i++){
	    float[] point = shape.getPoint(i);
	    if(i%2 == 0 && firePoints.get(i) == null){
		firePoints.add(point);
	    }else
		firePoints.set(i, point);
	}
	super.update(delta, player);
    }

    public void fire(float targetX, float targetY){
	int i = 0;
	for(float[] point : firePoints){
	    float[] dir = new float[2];
	    if(i == 0){
		dir[0] = 0.0f;
		dir[1] = -1.0f;
	    }else if(i == 1){
		dir[0] = 1.0f;
		dir[1] = 0.0f;
	    }else if(i == 2){
		dir[0] = 0.0f;
		dir[1] = 1.0f;
	    }else{
		dir[0] = -1.0f;
		dir[1] = 0.0f;
	    }

	    projectiles.add(new Projectile(point[0], point[1], dir));
	    i++;
	}
    }




}
