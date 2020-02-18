package metro_game.render.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import metro_game.render.Texture;

public class FinalShader extends ShaderDraw {
	public static int SAMPLES = 4;
	
	public FinalShader() throws IOException {
		super("final_shader");
	}
	
	public void setTexture(Texture texture) {
		int uColor = GL30.glGetUniformLocation(m_program, "u_texture");
		GL32.glActiveTexture(GL32.GL_TEXTURE0);
		GL32.glBindTexture(GL32.GL_TEXTURE_2D, texture.getId());
		GL32.glUniform1i(uColor, 0);
		
		int uTextureSize = GL30.glGetUniformLocation(m_program, "u_textureSize");
		GL32.glUniform2f(uTextureSize, texture.getWidth(), texture.getHeight());
	}
	
	public void setSharpen(boolean sharpen) {
		int uHorizontal = GL30.glGetUniformLocation(m_program, "u_sharpen");
		GL32.glUniform1i(uHorizontal, sharpen ? 1 : 0);
	}
}
