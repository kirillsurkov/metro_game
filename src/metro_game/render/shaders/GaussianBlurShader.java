package metro_game.render.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class GaussianBlurShader extends Shader {
	public GaussianBlurShader() throws IOException {
		super("gaussian_blur_shader");
	}
	
	public void setSamples(int samples) {
		int uSamples = GL30.glGetUniformLocation(m_program, "u_samples");
		GL32.glUniform1i(uSamples, samples);
	}
	
	public void setTextureSize(int width, int height) {
		int uTextureSize = GL30.glGetUniformLocation(m_program, "u_textureSize");
		GL32.glUniform2f(uTextureSize, width, height);
	}
	
	public void setTexture(int texture) {
		int uTexture = GL30.glGetUniformLocation(m_program, "u_texture");
		GL32.glActiveTexture(GL32.GL_TEXTURE0);
		GL32.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, texture);
		GL32.glUniform1i(uTexture, 0);
	}
	
	public void setHorizontal(boolean horizontal) {
		int uHorizontal = GL30.glGetUniformLocation(m_program, "u_horizontal");
		GL32.glUniform1i(uHorizontal, horizontal ? 1 : 0);
	}
}
