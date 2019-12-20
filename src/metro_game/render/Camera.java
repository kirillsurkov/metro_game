package metro_game.render;

import org.joml.Vector2f;

public class Camera {
	private Vector2f m_position;
	private Vector2f m_newPosition;
	
	public Camera(float x, float y) {
		m_position = new Vector2f(x, y);
		m_newPosition = new Vector2f(x, y);
	}
	
	public void move(Vector2f newPosition) {
		m_newPosition.set(newPosition);
	}
	
	public Vector2f getPosition() {
		return m_position;
	}
	
	public void update(double delta) {
		m_position.add(new Vector2f(m_newPosition).sub(m_position).mul((float) delta * 20));
	}
}
