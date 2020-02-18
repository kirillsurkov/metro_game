package metro_game.render.shaders;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL30;

import metro_game.Utils;

public class ShaderTransform extends Shader {
	private int m_vs;
	
	public ShaderTransform(String name) throws IOException {
		super(name);
		
		m_vs = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
		ByteBuffer vsBB = Utils.readFile("res/shaders/" + name + "/vs.glsl");
		byte[] vsB = new byte[vsBB.capacity()];
		vsBB.get(vsB);
		GL30.glShaderSource(m_vs, new String(vsB));
		GL30.glCompileShader(m_vs);
		if (GL30.glGetShaderi(m_vs, GL30.GL_COMPILE_STATUS) != GL30.GL_TRUE) {
			System.out.println("Vertex shader '" + name + "' failed:\n" + GL30.glGetShaderInfoLog(m_vs));
		}
	}
	
	@Override
	public void link() {
		GL30.glAttachShader(m_program, m_vs);
		
		GL30.glLinkProgram(m_program);
		if (GL30.glGetProgrami(m_program, GL30.GL_LINK_STATUS) != GL30.GL_TRUE) {
			System.out.println("Program '" + m_name + "' failed:\n" + GL30.glGetProgramInfoLog(m_program));
		}
	}
}
