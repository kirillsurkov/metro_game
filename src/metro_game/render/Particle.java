package metro_game.render;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Particle {
	public Vector2f position;
	public Vector3f color;
	
	public Particle(float x, float y, float r, float g, float b) {
		position = new Vector2f(x, y);
		color = new Vector3f(r, g, b);
	}
}
