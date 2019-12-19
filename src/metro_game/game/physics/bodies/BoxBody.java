package metro_game.game.physics.bodies;

public class BoxBody extends Body {
	private float m_width;
	private float m_height;
	
	public BoxBody(boolean dynamic, float x, float y, float width, float height) {
		super(Type.BOX, dynamic, x, y, 0.0f, -10.0f, 0.0f, 0.0f);
		m_width = width;
		m_height = height;
	}
	
	public float getWidth() {
		return m_width;
	}
	
	public float getHeight() {
		return m_height;
	}
}
