package metro_game.render.primitives;

import org.joml.Vector2f;

public class Rect extends Primitive {
	private Vector2f m_position;
	private Vector2f m_size;
	private float m_rotation;
	
	public Rect(float x, float y, float width, float height, float rotation) {
		super(Type.RECT);
		m_position = new Vector2f(x, y);
		m_size = new Vector2f(width, height);
		m_rotation = rotation;
	}
	
	public Vector2f getPosition() {
		return m_position;
	}
	
	public Vector2f getSize() {
		return m_size;
	}
	
	public void setRotation(float rotation) {
		m_rotation = rotation;
	}
	
	public float getRotation() {
		return m_rotation;
	}
}
