package metro_game.game.shapes;

import org.joml.Vector2f;

public class Shape {
	public enum Type {
		RECT
	}
	
	private Type m_type;
	private boolean m_visible;
	private Vector2f m_position;
	private float m_rotation;
	
	public Shape(Type type, float x, float y, float rotation) {
		m_type = type;
		m_visible = true;
		m_position = new Vector2f(x, y);
		m_rotation = rotation;
	}
	
	public Type getType() {
		return m_type;
	}
	
	public boolean isVisible() {
		return m_visible;
	}
	
	public void setVisible(boolean visible) {
		m_visible = visible;
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
