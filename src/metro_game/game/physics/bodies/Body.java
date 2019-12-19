package metro_game.game.physics.bodies;

import org.joml.Vector2f;

public class Body {
	private Vector2f m_position;
	private Vector2f m_linearVelocity;
	private float m_rotation;
	private float m_angularVelocity;
	
	public Body(float x, float y, float xVel, float yVel, float rotation, float angVel) {
		m_position = new Vector2f(x, y);
		m_linearVelocity = new Vector2f(xVel, yVel);
		m_rotation = rotation;
		m_angularVelocity = angVel;
	}
	
	public Vector2f getPosition() {
		return m_position;
	}
	
	public float getRotation() {
		return m_rotation;
	}
	
	public void setRotation(float rotation) {
		m_rotation = rotation;
	}
}
