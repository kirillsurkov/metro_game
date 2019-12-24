package metro_game.render.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL30;

public class FontShader extends Shader {
	public FontShader() throws IOException {
		super("font_shader");
	}
	
	public void setTexture(int texID) {
		int uTexture = GL30.glGetUniformLocation(m_program, "u_texture");
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texID);
		GL30.glUniform1i(uTexture, 0);
	}
}
