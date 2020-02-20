package metro_game.render.shaders;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;

public abstract class Shader {
	public enum ShaderType {
		FINAL,
		DEFAULT_GAME,
		FONT,
		GAUSSIAN_BLUR,
		PARTICLE,
		TRANSFORM_PARTICLE
	}

	private ShaderType m_type;
	protected int m_program;
	protected String m_name;
	
	public Shader(ShaderType type, String name) throws IOException {
		m_type = type;
		m_name = name;
		m_program = GL30.glCreateProgram();
	}
	
	public abstract void link();
	
	public void setColor(float r, float g, float b, float a) {
		int uColor = GL30.glGetUniformLocation(m_program, "u_color");
		GL30.glUniform4f(uColor, r, g, b, a);
	}
	
	public void setColor(Vector4f color) {
		setColor(color.x, color.y, color.z, color.w);
	}
	
	public void setMVP(Matrix4f mvp) {
		float[] matrix = new float[16];
		mvp.get(matrix);
		int uMVP = GL30.glGetUniformLocation(m_program, "u_mvp");
		GL30.glUniformMatrix4fv(uMVP, false, matrix);
	}
	
	public void setUseUniforms(boolean use) {
		int uUniforms = GL30.glGetUniformLocation(m_program, "u_uniforms");
		GL30.glUniform1i(uUniforms, use ? 1 : 0);
	}
	
	public void use() {
		GL30.glUseProgram(m_program);
	}
	
	public ShaderType getType() {
		return m_type;
	}
}
