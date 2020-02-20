package metro_game.render.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL30;

public class ParticleShader extends ShaderDraw {
	public static int A_PARTICLE_LIFETIME_ORIG = ATTRIBS + 0;
	public static int A_PARTICLE_LIFETIME = ATTRIBS + 1;
	public static int A_PARTICLE_POS = ATTRIBS + 2;
	public static int A_PARTICLE_COLOR = ATTRIBS + 3;
	
	public ParticleShader() throws IOException {
		super(ShaderType.PARTICLE, "particle_shader");
		
		GL30.glBindAttribLocation(m_program, A_PARTICLE_LIFETIME_ORIG, "a_particle_lifetime_orig");
		GL30.glBindAttribLocation(m_program, A_PARTICLE_LIFETIME, "a_particle_lifetime");
		GL30.glBindAttribLocation(m_program, A_PARTICLE_POS, "a_particle_pos");
		GL30.glBindAttribLocation(m_program, A_PARTICLE_COLOR, "a_particle_color");
	}
}
