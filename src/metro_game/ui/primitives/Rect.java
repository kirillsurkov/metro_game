package metro_game.ui.primitives;

public class Rect extends Primitive {
	private float m_x;
	private float m_y;
	private float m_width;
	private float m_height;
	
	public Rect(float x, float y, float width, float height) {
		super(Type.RECT);
		m_x = x;
		m_y = y;
		m_width = width;
		m_height = height;
	}
	
	public float getX() {
		return m_x;
	}
	
	public float getY() {
		return m_y;
	}
	
	public float getWidth() {
		return m_width;
	}
	
	public float getHeight() {
		return m_height;
	}
}
