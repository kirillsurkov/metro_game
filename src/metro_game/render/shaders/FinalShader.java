package metro_game.render.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class FinalShader extends Shader {
	public FinalShader() throws IOException {
		super("final_shader");
	}
	
	public void setColorTexture(int texture) {
		int uColor = GL30.glGetUniformLocation(m_program, "u_colorTexture");
		GL32.glActiveTexture(GL32.GL_TEXTURE0);
		GL32.glBindTexture(GL32.GL_TEXTURE_2D, texture);
		GL32.glUniform1f(uColor, 0);
	}
}
