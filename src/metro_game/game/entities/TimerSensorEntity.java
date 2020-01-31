package metro_game.game.entities;

import metro_game.Context;

public class TimerSensorEntity extends SensorEntity {
	private float m_timeout;
	private float m_timer;
	
	public TimerSensorEntity(Context context, float x, float y, float width, float height, float timeout) {
		super(context, x, y, width, height);

		m_timeout = timeout;
		m_timer = 0;
	}
	
	@Override
	public void onCollideStart(PhysicsEntity physicsEntity) {
		if (physicsEntity instanceof PlayerEntityGolf) {
			m_active = true;
			m_timer = 0.0f;
			m_color.set(0.5f, 0.0f, 0.0f, 1.0f);
		}
	}
	
	@Override
	public void onCollideEnd(PhysicsEntity physicsEntity) {
		super.onCollideEnd(physicsEntity);
	}
	
	@Override
	public void update(double delta) {
		if (m_active) {
			m_timer += delta;
		}
		
		if (m_timer >= m_timeout) {
			m_active = false;
			m_timer = 0.0f;
			onActivated();
		}
	}
}
