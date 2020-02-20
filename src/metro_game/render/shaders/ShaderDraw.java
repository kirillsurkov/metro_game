package metro_game.render.shaders;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL30;

import metro_game.Utils;

public class ShaderDraw extends Shader {
	public static int A_POSITION = 0;
	public static int A_SCALE = 1;
	public static int A_OFFSET = 2;
	public static int A_COLOR = 3;
	public static int ATTRIBS = 4;
	
	private int m_vs;
	private int m_fs;
	
	public ShaderDraw(ShaderType type, String name) throws IOException {
		super(type, name);
		
		m_vs = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
		ByteBuffer vsBB = Utils.readFile("res/shaders/" + name + "/vs.glsl");
		byte[] vsB = new byte[vsBB.capacity()];
		vsBB.get(vsB);
		GL30.glShaderSource(m_vs, new String(vsB));
		GL30.glCompileShader(m_vs);
		if (GL30.glGetShaderi(m_vs, GL30.GL_COMPILE_STATUS) != GL30.GL_TRUE) {
			System.out.println("Vertex shader '" + name + "' failed:\n" + GL30.glGetShaderInfoLog(m_vs));
		}
		
		m_fs = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
		ByteBuffer fsBB = Utils.readFile("res/shaders/" + name + "/fs.glsl");
		byte[] fsB = new byte[fsBB.capacity()];
		fsBB.get(fsB);
		GL30.glShaderSource(m_fs, new String(fsB));
		GL30.glCompileShader(m_fs);
		if (GL30.glGetShaderi(m_fs, GL30.GL_COMPILE_STATUS) != GL30.GL_TRUE) {
			System.out.println("Fragment shader '" + name + "' failed:\n" + GL30.glGetShaderInfoLog(m_fs));
		}
		
		GL30.glBindAttribLocation(m_program, A_POSITION, "a_position");
		GL30.glBindAttribLocation(m_program, A_SCALE, "a_scale");
		GL30.glBindAttribLocation(m_program, A_OFFSET, "a_offset");
		GL30.glBindAttribLocation(m_program, A_COLOR, "a_color");
	}
	
	@Override
	public void link() {
		GL30.glAttachShader(m_program, m_vs);
		GL30.glAttachShader(m_program, m_fs);
		
		GL30.glLinkProgram(m_program);
		if (GL30.glGetProgrami(m_program, GL30.GL_LINK_STATUS) != GL30.GL_TRUE) {
			System.out.println("Program '" + m_name + "' failed:\n" + GL30.glGetProgramInfoLog(m_program));
		}
		
		GL30.glBindFragDataLocation(m_program, 0, "outColor");
		GL30.glBindFragDataLocation(m_program, 1, "outGlow");
	}
}
