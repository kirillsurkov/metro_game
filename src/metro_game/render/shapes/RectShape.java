package metro_game.render.shapes;

import org.joml.Vector2f;

public class RectShape extends Shape {
	private Vector2f m_size;
	
	public RectShape(float x, float y, float width, float height, float rotation) {
		super(Type.RECT, x, y, rotation);
		m_size = new Vector2f(width, height);
	}
	
	public Vector2f getSize() {
		return m_size;
	}
}
