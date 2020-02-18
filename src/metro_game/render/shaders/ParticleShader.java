package metro_game.render.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL30;

public class ParticleShader extends ShaderDraw {
	public static int A_PARTICLE_LIFETIME = 1;
	public static int A_PARTICLE_POS = 2;
	
	public ParticleShader() throws IOException {
		super("particle_shader");
		
		GL30.glBindAttribLocation(m_program, A_PARTICLE_LIFETIME, "a_particle_lifetime");
		GL30.glBindAttribLocation(m_program, A_PARTICLE_POS, "a_particle_pos");
	}
}
