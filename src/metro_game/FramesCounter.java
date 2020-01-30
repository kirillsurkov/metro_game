package metro_game;

public abstract class FramesCounter {
	private int m_frames;
	private double m_timer;
	private double m_timeout;
	
	public FramesCounter(double timeout) {
		m_frames = 0;
		m_timer = 0;
		m_timeout = timeout;
	}
	
	public abstract void onFPS(int frames, double timer);
	
	public void step(double delta) {
		m_frames++;
		m_timer += delta;
		if (m_timer >= m_timeout) {
			onFPS(m_frames, m_timer);
			m_frames = 0;
			m_timer -= m_timeout;
		}
	}
}
