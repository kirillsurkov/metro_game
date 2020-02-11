package metro_game.render.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class FinalShader extends Shader {
	public static int SAMPLES = 4;
	
	public FinalShader() throws IOException {
		super("final_shader");
	}
	
	public void setSamples(int samples) {
		int uSamples = GL30.glGetUniformLocation(m_program, "u_samples");
		GL32.glUniform1i(uSamples, samples);
	}
	
	public void setTextureSize(int width, int height) {
		int uTextureSize = GL30.glGetUniformLocation(m_program, "u_textureSize");
		GL32.glUniform2f(uTextureSize, width, height);
	}
	
	public void setColorTexture(int texture) {
		int uColor = GL30.glGetUniformLocation(m_program, "u_colorTexture");
		GL32.glActiveTexture(GL32.GL_TEXTURE0);
		GL32.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, texture);
		GL32.glUniform1i(uColor, 0);
	}
	
	public void setGlowTexture(int texture) {
		int uGlow = GL30.glGetUniformLocation(m_program, "u_glowTexture");
		GL32.glActiveTexture(GL32.GL_TEXTURE1);
		GL32.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, texture);
		GL32.glUniform1i(uGlow, 1);
	}
}
