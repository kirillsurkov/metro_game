package metro_game.render.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import metro_game.render.Texture;

public class GaussianBlurShader extends ShaderDraw {
	public GaussianBlurShader() throws IOException {
		super(ShaderType.GAUSSIAN_BLUR, "gaussian_blur_shader");
	}
	
	public void setTexture(Texture texture) {
		int uTexture = GL30.glGetUniformLocation(m_program, "u_texture");
		GL32.glActiveTexture(GL32.GL_TEXTURE0);
		GL32.glBindTexture(texture.getTarget(), texture.getId());
		GL32.glUniform1i(uTexture, 0);
		
		int uTextureSize = GL30.glGetUniformLocation(m_program, "u_textureSize");
		GL32.glUniform2f(uTextureSize, texture.getWidth(), texture.getHeight());
	}
	
	public void setHorizontal(boolean horizontal) {
		int uHorizontal = GL30.glGetUniformLocation(m_program, "u_horizontal");
		GL32.glUniform1i(uHorizontal, horizontal ? 1 : 0);
	}
}
