package metro_game.render.primitives;

import java.util.ArrayList;
import java.util.List;

import metro_game.render.Particle;

public class ParticleEmitterPrimitive extends Primitive {
	private List<Particle> m_particles;
	private float m_lifetime;
	
	public ParticleEmitterPrimitive(float lifetime) {
		super(Type.PARTICLE_EMITTER);
		m_particles = new ArrayList<Particle>();
		m_lifetime = lifetime;
	}
	
	public void emit(float x, float y, float r, float g, float b) {
		m_particles.add(new Particle(x, y, r, g, b));
	}
	
	public List<Particle> getParticles() {
		return m_particles;
	}
	
	public float getLifetime() {
		return m_lifetime;
	}
}
