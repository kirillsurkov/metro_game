package metro_game.game.events;

import org.joml.Vector2f;

public class CameraEvent extends GameEvent {
	private Vector2f m_position;
	
	public CameraEvent() {
		super(Type.CAMERA);
		m_position = new Vector2f();
	}
	
	public Vector2f getPosition() {
		return m_position;
	}
}
