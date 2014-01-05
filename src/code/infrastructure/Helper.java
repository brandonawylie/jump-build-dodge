package code.infrastructure;

import org.newdawn.slick.geom.Vector2f;

public class Helper {
	public static final float GRAVITY = .4f;

	public static Vector2f pointsToDirectionVector(float ax, float ay, float bx, float by) {
		float dx = ax - bx;
		float dy = ay - by;
		Vector2f direction = new Vector2f(dx, dy);
		direction.normalise();
		return direction;
	}
}
