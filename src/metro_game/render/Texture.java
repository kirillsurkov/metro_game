package metro_game.render;

import org.lwjgl.opengl.GL32;

public class Texture {
	private int m_id;
	private int m_target;
	private int m_width;
	private int m_height;
	private int m_attachmentId;
	
	public Texture(int id, int target, int width, int height) {
		m_id = id;
		m_target = target;
		m_width = width;
		m_height = height;
		m_attachmentId = -1;
	}
	
	public static Texture create(int internalFormat, int format, int width, int height) {
		int texture = GL32.glGenTextures();
		GL32.glBindTexture(GL32.GL_TEXTURE_2D, texture);
		GL32.glTexImage2D(GL32.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL32.GL_FLOAT, (float[]) null);
		GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_MIN_FILTER, GL32.GL_LINEAR);
		GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_MAG_FILTER, GL32.GL_LINEAR);
		return new Texture(texture, GL32.GL_TEXTURE_2D, width, height);
	}
	
	public static Texture createMultisample(int format, int samples, int width, int height) {
		int texture = GL32.glGenTextures();
		GL32.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, texture);
		GL32.glTexImage2DMultisample(GL32.GL_TEXTURE_2D_MULTISAMPLE, samples, format, width, height, true);
		return new Texture(texture, GL32.GL_TEXTURE_2D_MULTISAMPLE, width, height);
	}
	
	
	public int getId() {
		return m_id;
	}
	
	public int getTarget() {
		return m_target;
	}
	
	public int getWidth() {
		return m_width;
	}
	
	public int getHeight() {
		return m_height;
	}

	public void setAttachmentId(int attachmentId) {
		m_attachmentId = attachmentId;
	}
	
	public int getAttachmentId() {
		return m_attachmentId;
	}
}
