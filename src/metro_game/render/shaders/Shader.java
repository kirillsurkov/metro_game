package metro_game.render.shaders;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import metro_game.Utils;

public class Shader {
	public static int A_POSITION = 0;
	
	protected int m_program;
	private int m_vs;
	private int m_fs;
	
	public Shader(String name) throws IOException {
		m_vs = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
		ByteBuffer vsBB = Utils.readFile("res/shaders/" + name + "/vs.glsl");
		byte[] vsB = new byte[vsBB.capacity()];
		vsBB.get(vsB);
		GL30.glShaderSource(m_vs, new String(vsB));
		GL30.glCompileShader(m_vs);
		System.out.println(GL30.glGetShaderInfoLog(m_vs));
		
		m_fs = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
		ByteBuffer fsBB = Utils.readFile("res/shaders/" + name + "/fs.glsl");
		byte[] fsB = new byte[fsBB.capacity()];
		fsBB.get(fsB);
		GL30.glShaderSource(m_fs, new String(fsB));
		GL30.glCompileShader(m_fs);
		System.out.println(GL30.glGetShaderInfoLog(m_fs));
		
		m_program = GL30.glCreateProgram();
		GL30.glBindAttribLocation(m_program, 0, "a_position");
	}
	
	public void link() {
		GL30.glAttachShader(m_program, m_vs);
		GL30.glAttachShader(m_program, m_fs);
		
		GL30.glLinkProgram(m_program);
		System.out.println(GL30.glGetProgramInfoLog(m_program));
		
		GL30.glBindFragDataLocation(m_program, 0, "outColor");
	}
	
	public void setColor(float r, float g, float b, float a) {
		int uColor = GL30.glGetUniformLocation(m_program, "u_color");
		GL30.glUniform4f(uColor, r, g, b, a);
	}
	
	public void setMVP(Matrix4f mvp) {
		float[] matrix = new float[16];
		mvp.get(matrix);
		int uMVP = GL30.glGetUniformLocation(m_program, "u_mvp");
		GL30.glUniformMatrix4fv(uMVP, false, matrix);
	}
	
	public void use() {
		GL30.glUseProgram(m_program);
	}
}
