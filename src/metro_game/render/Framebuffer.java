package metro_game.render;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class Framebuffer {
	private int m_id;
	private int m_width;
	private int m_height;
	private int m_samples;
	private int m_attachments;
	
	public Framebuffer(int id, int width, int height, int samples) {
		m_id = id;
		m_width = width;
		m_height = height;
		m_samples = samples;
		m_attachments = 0;
	}
	
	public static Framebuffer create(int width, int height, int samples) {
		int id = GL32.glGenFramebuffers();
		GL32.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
		GL30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL30.glBlendFunc(GL30.GL_ONE, GL30.GL_ONE_MINUS_SRC_ALPHA);
		return new Framebuffer(id, width, height, samples);
	}
	
	public static Framebuffer create(int width, int height) {
		return create(width, height, 0);
	}
	
	public Texture attachTexture() {
		Texture texture;
		if (m_samples > 0) {
			texture = Texture.createMultisample(GL32.GL_RGBA, m_samples, m_width, m_height);
		} else {
			texture = Texture.create(GL32.GL_RGBA, GL32.GL_RGBA, m_width, m_height);
		}
		texture.setAttachmentId(GL32.GL_COLOR_ATTACHMENT0 + m_attachments++);
		GL32.glFramebufferTexture2D(GL32.GL_FRAMEBUFFER, texture.getAttachmentId(), texture.getTarget(), texture.getId(), 0);
		return texture;
	}
	
	public int getId() {
		return m_id;
	}
	
	public int getWidth() {
		return m_width;
	}
	
	public int getHeight() {
		return m_height;
	}
}
