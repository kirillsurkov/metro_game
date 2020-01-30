package metro_game.game.physics.bodies;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import metro_game.game.physics.bodies.modifiers.BodyModifier;

public class Body {
	public enum Type {
		BOX,
		CIRCLE
	}
	
	private Type m_type;
	private List<BodyModifier> m_modifiers;
	private boolean m_dynamic;
	private boolean m_sensor;
	private Vector2f m_position;
	private Vector2f m_linearVelocity;
	private float m_rotation;
	private float m_angularVelocity;
	
	public Body(Type type, boolean dynamic, float x, float y, float xVel, float yVel, float rotation, float angVel) {
		m_type = type;
		m_modifiers = new ArrayList<BodyModifier>();
		m_dynamic = dynamic;
		m_sensor = false;
		m_position = new Vector2f(x, y);
		m_linearVelocity = new Vector2f(xVel, yVel);
		m_rotation = rotation;
		m_angularVelocity = angVel;
	}

	public Type getType() {
		return m_type;
	}
	
	public <T extends BodyModifier> void pushModifier(T modifier) {
		m_modifiers.add(modifier);
	}
	
	public List<BodyModifier> getModifiers() {
		return m_modifiers;
	}
	
	public void clearModifiers() {
		m_modifiers.clear();
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
}
