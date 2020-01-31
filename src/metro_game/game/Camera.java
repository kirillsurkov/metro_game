package metro_game.game;

import org.joml.Vector2f;

public class Camera {
	private Vector2f m_position;
	private Vector2f m_newPosition;
	private boolean m_posImmediately;
	
	public Camera(float x, float y) {
		m_position = new Vector2f(x, y);
		m_newPosition = new Vector2f(x, y);
	}
	
	public void move(float x, float y) {
		m_newPosition.set(x, y);
	}
	
	public Vector2f getPosition() {
		return m_position;
	}
	
	public void setPosImmediately(boolean immediately) {
		m_posImmediately = immediately;
	}
	
	public void update(double delta) {
		if (m_posImmediately) {
			m_posImmediately = false;
			m_position.set(m_newPosition);
		} else {
			m_position.add(new Vector2f(m_newPosition).sub(m_position).mul((float) delta * 10));
		}
	}
}
