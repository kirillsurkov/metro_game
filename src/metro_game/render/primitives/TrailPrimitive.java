package metro_game.render.primitives;

import org.joml.Vector2f;

public class TrailPrimitive extends Primitive {
	public static int MAX_POINTS = 15;
	
	private int m_count;
	private float[] m_buffer;
	private int m_frames;
	
	public TrailPrimitive() {
		super(Type.TRAIL);
		m_count = 0;
		m_buffer = new float[MAX_POINTS * 4];
		m_frames = 0;
	}
	
	private void setCurrentPoint(float x, float y) {
		float width = 0.1f;
		Vector2f offset = new Vector2f(1.0f, 0.0f);
		if (m_count > 1) {
			float px = (m_buffer[(m_count - 2) * 4 + 0] + m_buffer[(m_count - 2) * 4 + 2]) / 2.0f;
			float py = (m_buffer[(m_count - 2) * 4 + 1] + m_buffer[(m_count - 2) * 4 + 3]) / 2.0f;
			offset = new Vector2f(px - x, py - y).perpendicular().normalize();
		}
		if (m_count > 0) {
			m_buffer[(m_count - 1) * 4 + 0] = x + offset.x * width;
			m_buffer[(m_count - 1) * 4 + 1] = y + offset.y * width;
			m_buffer[(m_count - 1) * 4 + 2] = x - offset.x * width;
			m_buffer[(m_count - 1) * 4 + 3] = y - offset.y * width;
		}
	}
	
	private void addPoint(float x, float y) {
		if (m_count >= MAX_POINTS) {
			m_count--;
			float[] buffer = new float[m_buffer.length];
			System.arraycopy(m_buffer, 4, buffer, 0, m_buffer.length - 4);
			m_buffer = buffer;
		}
		m_count++;
		setCurrentPoint(x, y);
	}
	
	public void update(double delta, float x, float y) {
		m_frames++;
		if (m_frames >= 5) {
			m_frames -= 5;
			addPoint(x, y);
		} else {
			setCurrentPoint(x, y);
		}
	}
	
	public float[] getVertices() {
		return m_buffer;
	}
	
	public int getVerticesCount() {
		return m_count;
	}
}
