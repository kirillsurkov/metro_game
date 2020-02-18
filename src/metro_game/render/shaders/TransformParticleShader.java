package metro_game.render.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class TransformParticleShader extends ShaderTransform {
	public static int A_LIFETIME = 1;
	
	public static int O_LIFETIME = 0;
	
	public TransformParticleShader() throws IOException {
		super("transform_particle_shader");
		
		GL30.glBindAttribLocation(m_program, A_LIFETIME, "a_lifetime");
		GL30.glTransformFeedbackVaryings(m_program, "o_lifetime", GL30.GL_INTERLEAVED_ATTRIBS);
	}
	
	public void setDelta(double delta) {
		int uDelta = GL30.glGetUniformLocation(m_program, "u_delta");
		GL32.glUniform1f(uDelta, (float) delta);
	}
}
