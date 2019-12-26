package metro_game.render.shaders;

import java.io.IOException;

import org.joml.Vector4f;
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
	
	public void setSdfPixel(float sdfPixel) {
		int uSdfPixel = GL30.glGetUniformLocation(m_program, "u_sdf_pixel");
		GL30.glUniform1f(uSdfPixel, sdfPixel);
	}
	
	public void setSdfOnEdge(float sdfOnEdge) {
		int uSdfOnEdge = GL30.glGetUniformLocation(m_program, "u_sdf_onedge");
		GL30.glUniform1f(uSdfOnEdge, sdfOnEdge);
	}
	
	public void setBorder(float border) {
		int uBorder = GL30.glGetUniformLocation(m_program, "u_border");
		GL30.glUniform1f(uBorder, border);
	}
	
	public void setBorderColor(Vector4f borderColor) {
		int uBorder = GL30.glGetUniformLocation(m_program, "u_border_color");
		GL30.glUniform4f(uBorder, borderColor.x, borderColor.y, borderColor.z, borderColor.w);
	}
}
