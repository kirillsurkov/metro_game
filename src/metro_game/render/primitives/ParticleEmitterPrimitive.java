package metro_game.render.primitives;

import org.joml.Vector2f;

public class ParticleEmitterPrimitive extends Primitive {
	private Vector2f m_position;
	private int m_emitCount;
	
	public ParticleEmitterPrimitive(float x, float y) {
		super(Type.PARTICLE_EMITTER);
		m_position = new Vector2f(x, y);
		m_emitCount = 0;
	}
	
	public void setPosition(float x, float y) {
		m_position.set(x, y);
	}
	
	public float getX() {
		return m_position.x;
	}
	
	public float getY() {
		return m_position.y;
	}
	
	public void setEmitCount(int count) {
		m_emitCount = count;
	}
	
	public int getEmitCount() {
		return m_emitCount;
	}
}
