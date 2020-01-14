package metro_game.render.primitives;

import org.joml.Vector2f;

public class CirclePrimitive extends Primitive {
	private Vector2f m_position;
	private float m_radius;
	private float m_rotation;
	
	public CirclePrimitive(float x, float y, float radius, float rotation) {
		super(Type.CIRCLE);
		m_position = new Vector2f(x, y);
		m_radius = radius;
		m_rotation = rotation;
	}
	
	public Vector2f getPosition() {
		return m_position;
	}
	
	public void setRadius(float radius) {
		m_radius = radius;
	}
	
	public float getRadius() {
		return m_radius;
	}
	
	public void setRotation(float rotation) {
		m_rotation = rotation;
	}
	
	public float getRotation() {
		return m_rotation;
	}
}
