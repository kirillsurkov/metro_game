package metro_game.render.primitives;

public class TrailPrimitive extends Primitive {
	public static int MAX_POINTS = 20;
	
	private int m_count;
	private float[] m_buffer;
	private int m_frames;
	
	public TrailPrimitive() {
		super(Type.TRAIL);
		m_count = 0;
		m_buffer = new float[MAX_POINTS * 2];
		m_frames = 0;
	}
	
	private void setCurrentPoint(float x, float y) {
		if (m_count > 0) {
			m_buffer[(m_count - 1) * 2 + 0] = x;
			m_buffer[(m_count - 1) * 2 + 1] = y;
		}
	}
	
	private void addPoint(float x, float y) {
		if (m_count >= MAX_POINTS) {
			m_count--;
			float[] buffer = new float[m_buffer.length];
			System.arraycopy(m_buffer, 2, buffer, 0, m_buffer.length - 2);
			m_buffer = buffer;
		}
		m_buffer[m_count * 2 + 0] = x;
		m_buffer[m_count * 2 + 1] = y;
		m_count++;
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
