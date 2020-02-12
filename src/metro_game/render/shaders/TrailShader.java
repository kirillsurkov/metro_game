package metro_game.render.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL30;

public class TrailShader extends Shader {
	public static int A_NUMBER = 1;
	
	public TrailShader() throws IOException {
		super("trail_shader");
		
		GL30.glBindAttribLocation(m_program, A_NUMBER, "a_number");
	}
	
	public void setGlow(boolean glow) {
		int uGlow = GL30.glGetUniformLocation(m_program, "u_glow");
		GL30.glUniform1i(uGlow, glow ? 1 : 0);
	}
	
	public void setCount(int count) {
		int uCount = GL30.glGetUniformLocation(m_program, "u_count");
		GL30.glUniform1f(uCount, count);
	}
}
