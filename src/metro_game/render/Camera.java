package metro_game.render;

import org.joml.Vector2f;

public class Camera {
	private Vector2f m_position;
	
	public Camera(float x, float y) {
		m_position = new Vector2f(x, y);
	}
	
	public Vector2f getPosition() {
		return m_position;
	}
}
