package metro_game.render;

import java.util.List;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;

import metro_game.render.shaders.ParticleShader;
import metro_game.render.shaders.TransformParticleShader;

public class ParticleSystem {
	private int m_particlesMax;
	private int m_particleIndex;
	private boolean m_useFirstVBO;
	private int m_vao;
	private int m_lifetimesVBO_1;
	private int m_lifetimesVBO_2;
	private int m_lifetimesOrigVBO;
	private int m_positionsVBO;
	private int m_colorsVBO;

	public ParticleSystem() {
		m_particlesMax = 500000;
		m_particleIndex = 0;
		m_useFirstVBO = true;
		
		m_vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(m_vao);
		GL30.glEnableVertexAttribArray(ParticleShader.A_PARTICLE_LIFETIME_ORIG);
		GL30.glEnableVertexAttribArray(ParticleShader.A_PARTICLE_LIFETIME);
		GL30.glEnableVertexAttribArray(ParticleShader.A_PARTICLE_POS);
		GL30.glEnableVertexAttribArray(ParticleShader.A_PARTICLE_COLOR);
		GL33.glVertexAttribDivisor(ParticleShader.A_PARTICLE_LIFETIME_ORIG, 1);
		GL33.glVertexAttribDivisor(ParticleShader.A_PARTICLE_POS, 1);
		GL33.glVertexAttribDivisor(ParticleShader.A_PARTICLE_COLOR, 1);
		
		m_lifetimesVBO_1 = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_lifetimesVBO_1);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, new float[m_particlesMax], GL30.GL_STATIC_DRAW);
		
		m_lifetimesVBO_2 = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_lifetimesVBO_2);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, new float[m_particlesMax], GL30.GL_STATIC_DRAW);
		
		m_lifetimesOrigVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_lifetimesOrigVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, new float[m_particlesMax], GL30.GL_DYNAMIC_DRAW);
		GL30.glVertexAttribPointer(ParticleShader.A_PARTICLE_LIFETIME_ORIG, 1, GL30.GL_FLOAT, false, 0, 0);
		
		m_positionsVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_positionsVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, new float[m_particlesMax * 2], GL30.GL_DYNAMIC_DRAW);
		GL30.glVertexAttribPointer(ParticleShader.A_PARTICLE_POS, 2, GL30.GL_FLOAT, false, 0, 0);
		
		m_colorsVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_colorsVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, new float[m_particlesMax * 3], GL30.GL_DYNAMIC_DRAW);
		GL30.glVertexAttribPointer(ParticleShader.A_PARTICLE_COLOR, 3, GL30.GL_FLOAT, false, 0, 0);
	}
	
	private int getCurrentLifetimesVBOInput() {
		return m_useFirstVBO ? m_lifetimesVBO_1 : m_lifetimesVBO_2;
	}
	
	private int getCurrentLifetimesVBOOutput() {
		return m_useFirstVBO ? m_lifetimesVBO_2 : m_lifetimesVBO_1;
	}
	
	public void emit(List<Particle> particles, float lifetime, float timer) {
		int offset = 0;
		
		while (true) {
			int count = Math.min(particles.size(), m_particlesMax - m_particleIndex);
			
			float[] lifetimesBuffer = new float[count];
			float[] positionsBuffer = new float[count * 2];
			float[] colorsBuffer = new float[count * 3];
			
			for (int i = 0; i < count; i++) {
				Particle particle = particles.get(offset + i);
				lifetimesBuffer[i] = lifetime;
				positionsBuffer[i * 2 + 0] = particle.getPositionX();
				positionsBuffer[i * 2 + 1] = particle.getPositionY();
				colorsBuffer[i * 3 + 0] = particle.getColorR();
				colorsBuffer[i * 3 + 1] = particle.getColorG();
				colorsBuffer[i * 3 + 2] = particle.getColorB();
			}
			
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, getCurrentLifetimesVBOInput());
			GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 4 * m_particleIndex, lifetimesBuffer);
			
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_lifetimesOrigVBO);
			GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 4 * m_particleIndex, lifetimesBuffer);
			
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_positionsVBO);
			GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 8 * m_particleIndex, positionsBuffer);
			
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_colorsVBO);
			GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 12 * m_particleIndex, colorsBuffer);
			
			m_particleIndex = (m_particleIndex + count) % m_particlesMax;
			
			if (offset + count == particles.size()) {
				break;
			}
		}
	}
	
	public void renderInstanced() {
		GL30.glBindVertexArray(m_vao);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, getCurrentLifetimesVBOInput());
		GL30.glVertexAttribPointer(ParticleShader.A_PARTICLE_LIFETIME, 1, GL30.GL_FLOAT, false, 0, 0);
		GL33.glVertexAttribDivisor(ParticleShader.A_PARTICLE_LIFETIME, 1);
		GL31.glDrawArraysInstanced(GL30.GL_TRIANGLE_STRIP, 0, 4, m_particlesMax);
	}
	
	public void update(float timer) {
		GL30.glBindVertexArray(m_vao);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, getCurrentLifetimesVBOInput());
		GL30.glVertexAttribPointer(ParticleShader.A_PARTICLE_LIFETIME, 1, GL30.GL_FLOAT, false, 0, 0);
		GL33.glVertexAttribDivisor(TransformParticleShader.A_LIFETIME, 0);
		GL30.glBindBufferBase(GL30.GL_TRANSFORM_FEEDBACK_BUFFER, TransformParticleShader.O_LIFETIME, getCurrentLifetimesVBOOutput());
		GL30.glEnable(GL30.GL_RASTERIZER_DISCARD);
		GL30.glBeginTransformFeedback(GL30.GL_POINTS);
		GL30.glDrawArrays(GL30.GL_POINTS, 0, m_particlesMax);
		GL30.glEndTransformFeedback();
		GL30.glDisable(GL30.GL_RASTERIZER_DISCARD);
		
		m_useFirstVBO = !m_useFirstVBO;
	}
}
