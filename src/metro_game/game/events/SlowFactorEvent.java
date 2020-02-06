package metro_game.game.events;

public class SlowFactorEvent extends GameEvent {
	private float m_slowFactor;
	
	public SlowFactorEvent(float slowFactor) {
		super(Type.SLOW_FACTOR);
		m_slowFactor = slowFactor;
	}
	
	public float getSlowFactor() {
		return m_slowFactor;
	}
}
