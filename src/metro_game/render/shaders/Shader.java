package metro_game.render.shaders;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import metro_game.Utils;

public class Shader {
	protected int m_program;
	private int m_vao;
	
	public Shader(String name) throws IOException {
		int vs = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
		ByteBuffer vsBB = Utils.readFile("res/shaders/" + name + "/vs.glsl");
		byte[] vsB = new byte[vsBB.capacity()];
		vsBB.get(vsB);
		GL30.glShaderSource(vs, new String(vsB));
		GL30.glCompileShader(vs);
		System.out.println(GL30.glGetShaderInfoLog(vs));
		
		int fs = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
		ByteBuffer fsBB = Utils.readFile("res/shaders/" + name + "/fs.glsl");
		byte[] fsB = new byte[fsBB.capacity()];
		fsBB.get(fsB);
		GL30.glShaderSource(fs, new String(fsB));
		GL30.glCompileShader(fs);
		System.out.println(GL30.glGetShaderInfoLog(fs));
		
		m_program = GL30.glCreateProgram();
		GL30.glAttachShader(m_program, vs);
		GL30.glAttachShader(m_program, fs);
		GL30.glLinkProgram(m_program);
		System.out.println(GL30.glGetProgramInfoLog(m_program));
		
		GL30.glBindFragDataLocation(m_program, 0, "outColor");
		
		m_vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(m_vao);
		
		int aPosition = GL30.glGetAttribLocation(m_program, "a_position");
		GL30.glVertexAttribPointer(aPosition, 2, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(aPosition);
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
		GL30.glBindVertexArray(m_vao);
	}
}
