package metro_game.render.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL30;

public class ParticleShader extends ShaderDraw {
	public static int A_PARTICLE_LIFETIME_ORIG = 1;
	public static int A_PARTICLE_LIFETIME = 2;
	public static int A_PARTICLE_POS = 3;
	public static int A_PARTICLE_COLOR = 4;
	
	public ParticleShader() throws IOException {
		super("particle_shader");
		
		GL30.glBindAttribLocation(m_program, A_PARTICLE_LIFETIME_ORIG, "a_particle_lifetime_orig");
		GL30.glBindAttribLocation(m_program, A_PARTICLE_LIFETIME, "a_particle_lifetime");
		GL30.glBindAttribLocation(m_program, A_PARTICLE_POS, "a_particle_pos");
		GL30.glBindAttribLocation(m_program, A_PARTICLE_COLOR, "a_particle_color");
	}
}
