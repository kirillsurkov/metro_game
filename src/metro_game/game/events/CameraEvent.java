package metro_game.game.events;

import org.joml.Vector2f;

public class CameraEvent extends GameEvent {
	private Vector2f m_position;
	
	public CameraEvent(float x, float y) {
		super(Type.CAMERA);
		m_position = new Vector2f(x, y);
	}
	
	public float getPositionX() {
		return m_position.x;
	}
	
	public float getPositionY() {
		return m_position.y;
	}
}
