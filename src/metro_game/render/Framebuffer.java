package metro_game.render;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class Framebuffer {
	private int m_id;
	private int m_attachments;
	
	public Framebuffer(int id) {
		m_id = id;
		m_attachments = 0;
	}
	
	public static Framebuffer create() {
		int id = GL32.glGenFramebuffers();
		GL32.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
		GL30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL30.glEnable(GL30.GL_BLEND);
		GL30.glBlendFunc(GL30.GL_ONE, GL30.GL_ONE_MINUS_SRC_ALPHA);
		return new Framebuffer(id);
	}
	
	public void attachTexture(Texture texture) {
		texture.setAttachmentId(GL32.GL_COLOR_ATTACHMENT0 + m_attachments++);
		GL32.glFramebufferTexture2D(GL32.GL_FRAMEBUFFER, texture.getAttachmentId(), texture.getTarget(), texture.getId(), 0);
	}
	
	public int getId() {
		return m_id;
	}
}
