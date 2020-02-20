package metro_game.render;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Particle {
	private Vector2f m_position;
	private Vector3f m_color;
	
	public Particle(float x, float y, float r, float g, float b) {
		m_position = new Vector2f(x, y);
		m_color = new Vector3f(r, g, b);
	}
	
	public float getPositionX() {
		return m_position.x;
	}
	
	public float getPositionY() {
		return m_position.y;
	}
	
	public float getColorR() {
		return m_color.x;
	}
	
	public float getColorG() {
		return m_color.y;
	}
	
	public float getColorB() {
		return m_color.z;
	}
}
