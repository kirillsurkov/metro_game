package metro_game.game.physics.bodies;

import org.joml.Vector2f;

public class Body {
	public enum Type {
		BOX,
		CIRCLE
	}
	
	private Type m_type;
	private boolean m_dynamic;
	private boolean m_sensor;
	private Vector2f m_position;
	private Vector2f m_linearVelocity;
	private float m_rotation;
	private float m_angularVelocity;
	
	public Body(Type type, boolean dynamic, float x, float y, float xVel, float yVel, float rotation, float angVel) {
		m_type = type;
		m_dynamic = dynamic;
		m_sensor = false;
		m_position = new Vector2f(x, y);
		m_linearVelocity = new Vector2f(xVel, yVel);
		m_rotation = rotation;
		m_angularVelocity = angVel;
	}
	
	public boolean isDynamic() {
		return m_dynamic;
	}
	
	public void setSensor(boolean sensor) {
		m_sensor = sensor;
	}
	
	public boolean isSensor() {
		return m_sensor;
	}
	
	public Vector2f getPosition() {
		return m_position;
	}
	
	public Vector2f getLinearVelocity() {
		return m_linearVelocity;
	}

	public void setRotation(float rotation) {
		m_rotation = rotation;
	}
	
	public float getRotation() {
		return m_rotation;
	}
	
	public void setAngularVelocity(float angularVelocity) {
		m_angularVelocity = angularVelocity;
	}
	
	public float getAngularVelocity() {
		return m_angularVelocity;
	}
	
	public Type getType() {
		return m_type;
	}
}
