package metro_game.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.*;

import metro_game.Context;
import metro_game.game.Camera;
import metro_game.game.Game;
import metro_game.game.entities.GameEntity;
import metro_game.game.scenes.Scene;
import metro_game.render.primitives.CirclePrimitive;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.ParticleEmitterPrimitive;
import metro_game.render.primitives.Primitive;
import metro_game.render.primitives.RectPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.TextPrimitive;
import metro_game.render.primitives.TrailPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;
import metro_game.render.shaders.DefaultGameShader;
import metro_game.render.shaders.FinalShader;
import metro_game.render.shaders.FontShader;
import metro_game.render.shaders.GaussianBlurShader;
import metro_game.render.shaders.ParticleShader;
import metro_game.render.shaders.Shader;
import metro_game.render.shaders.TrailShader;
import metro_game.ui.widgets.Widget;

public class Renderer {
	private Context m_context;
	private Game m_game;
	private FontCache m_fontCache;
	private Map<ShaderType, Shader> m_shaderCache;
	private Shader m_currentShader;
	private int m_downsampleFactor;
	private Framebuffer m_fboMultisample;
	private Texture m_fboMultisampleTextureColor;
	private Texture m_fboMultisampleTextureGlow;
	private Framebuffer m_fboRasterize;
	private Texture m_fboRasterizeTextureColor;
	private Texture m_fboRasterizeTextureGlow;
	private Framebuffer m_fboDownsample;
	private Texture m_fboDownsampleTextureTmp;
	private Texture m_fboDownsampleTextureGlow;
	private int m_quadVBO;
	private int m_quadVAO;
	private int m_circleSegments;
	private int m_circleVBO;
	private int m_circleVAO;
	private int m_lineStaticVBO;
	private int m_lineDynamicVBO;
	private int m_lineVAO;
	private int m_particlesVBO;
	private int m_particlesVAO;
	private int m_particlesMax;
	private int m_particlesCount;
	private float[] m_particles;

	public Renderer(Context context, Game game) {
		m_context = context;
		m_game = game;
		m_fontCache = new FontCache(m_context);
		m_shaderCache = new HashMap<ShaderType, Shader>();
		
		GLCapabilities capabilities = GL.createCapabilities();
		
		if (!capabilities.OpenGL30) {
			throw new IllegalStateException("OpenGL 3.0 is not supported");
		}
		
		if (!capabilities.GL_ARB_texture_multisample) {
			throw new IllegalStateException("Extension ARB_texture_multisample is not available");
		}
		
		GL30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL30.glBlendFunc(GL30.GL_ONE, GL30.GL_ONE_MINUS_SRC_ALPHA);
		
		m_downsampleFactor = 2;
		
		m_fboMultisample = Framebuffer.create(context.getWidth(), context.getHeight(), FinalShader.SAMPLES);
		m_fboMultisampleTextureColor = m_fboMultisample.attachTexture();
		m_fboMultisampleTextureGlow = m_fboMultisample.attachTexture();
		System.out.println(GL32.glCheckFramebufferStatus(GL32.GL_FRAMEBUFFER));
		
		m_fboRasterize = Framebuffer.create(context.getWidth(), context.getHeight(), 1);
		m_fboRasterizeTextureColor = m_fboRasterize.attachTexture();
		m_fboRasterizeTextureGlow = m_fboRasterize.attachTexture();
		System.out.println(GL32.glCheckFramebufferStatus(GL32.GL_FRAMEBUFFER));
		
		m_fboDownsample = Framebuffer.create(context.getWidth() / m_downsampleFactor, context.getHeight() / m_downsampleFactor, 1);
		m_fboDownsampleTextureTmp = m_fboDownsample.attachTexture();
		m_fboDownsampleTextureGlow = m_fboDownsample.attachTexture();
		System.out.println(GL32.glCheckFramebufferStatus(GL32.GL_FRAMEBUFFER));
		
		float[] quadVertices = new float[] {0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};

		m_quadVBO = GL30.glGenBuffers();
		m_quadVAO = GL30.glGenVertexArrays();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_quadVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, quadVertices, GL30.GL_STATIC_DRAW);
		GL30.glBindVertexArray(m_quadVAO);
		GL30.glVertexAttribPointer(Shader.A_POSITION, 2, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(Shader.A_POSITION);
		
		m_circleSegments = 16;
		float[] circleVertices = new float[2 * m_circleSegments];
		for (int i = 0; i < m_circleSegments; i++) {
			double angle = 2.0f * Math.PI * i / m_circleSegments;
			circleVertices[i*2] = (float) Math.cos(angle);
			circleVertices[i*2 + 1] = (float) Math.sin(angle);
		}
		
		m_circleVBO = GL30.glGenBuffers();
		m_circleVAO = GL30.glGenVertexArrays();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_circleVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, circleVertices, GL30.GL_STATIC_DRAW);
		GL30.glBindVertexArray(m_circleVAO);
		GL30.glVertexAttribPointer(Shader.A_POSITION, 2, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(Shader.A_POSITION);
		
		float[] lineVertices = new float[TrailPrimitive.MAX_POINTS * 2];
		for (int i = 0; i < lineVertices.length; i++) {
			lineVertices[i] = i;
		}
		
		m_lineVAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(m_lineVAO);
		m_lineDynamicVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_lineDynamicVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, new float[TrailPrimitive.MAX_POINTS * 4], GL30.GL_DYNAMIC_DRAW);
		GL30.glVertexAttribPointer(Shader.A_POSITION, 2, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(Shader.A_POSITION);
		m_lineStaticVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_lineStaticVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, lineVertices, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(TrailShader.A_NUMBER, 1, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(TrailShader.A_NUMBER);
		
		m_particlesMax = 10000;
		m_particlesCount = 0;
		
		m_particles = new float[m_particlesMax * 2];
		
		m_particlesVAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(m_particlesVAO);
		m_particlesVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_particlesVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, m_particles, GL30.GL_DYNAMIC_DRAW);
		GL30.glVertexAttribPointer(ParticleShader.A_PARTICLE_POS, 2, GL30.GL_FLOAT, false, 0, 0);
		GL33.glVertexAttribDivisor(ParticleShader.A_PARTICLE_POS, 1);
		GL30.glEnableVertexAttribArray(ParticleShader.A_PARTICLE_POS);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_circleVBO);
		GL30.glVertexAttribPointer(Shader.A_POSITION, 2, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(Shader.A_POSITION);
		
		m_currentShader = null;
	}
	
	private void useShader(Shader shader) {
		if (m_currentShader != shader) {
			m_currentShader = shader;
			m_currentShader.use();
		}
	}
	
	private Shader getShader(ShaderType shaderType) {
		try {
			switch (shaderType) {
			case FINAL: {
				if (!m_shaderCache.containsKey(shaderType)) {
					FinalShader shader = new FinalShader();
					shader.link();
					m_shaderCache.put(shaderType, shader);
				}
				return m_shaderCache.get(shaderType);
			}
			case DEFAULT_GAME: {
				if (!m_shaderCache.containsKey(shaderType)) {
					DefaultGameShader shader = new DefaultGameShader();
					shader.link();
					m_shaderCache.put(shaderType, shader);
				}
				return m_shaderCache.get(shaderType);
			}
			case FONT: {
				if (!m_shaderCache.containsKey(shaderType)) {
					FontShader shader = new FontShader();
					shader.link();
					shader.use();
					shader.setSdfPixel((Font.sdfOnEdge / 255.0f) / Font.sdfPadding);
					shader.setSdfOnEdge(Font.sdfOnEdge / 255.0f);
					m_shaderCache.put(shaderType, shader);
				}
				return m_shaderCache.get(shaderType);
			}
			case TRAIL: {
				if (!m_shaderCache.containsKey(shaderType)) {
					TrailShader shader = new TrailShader();
					shader.link();
					m_shaderCache.put(shaderType, shader);
				}
				return m_shaderCache.get(shaderType);
			}
			case GAUSSIAN_BLUR: {
				if (!m_shaderCache.containsKey(shaderType)) {
					GaussianBlurShader shader = new GaussianBlurShader();
					shader.link();
					m_shaderCache.put(shaderType, shader);
				}
				return m_shaderCache.get(shaderType);
			}
			case PARTICLE: {
				if (!m_shaderCache.containsKey(shaderType)) {
					ParticleShader shader = new ParticleShader();
					shader.link();
					m_shaderCache.put(shaderType, shader);
				}
				return m_shaderCache.get(shaderType);
			}
			}
		} catch (IOException e) {
			System.out.println("Shader " + shaderType + " not found");
		}
		return null;
	}
	
	private void drawRect(float width, float height, Matrix4f modelViewProjection) {
		m_currentShader.setMVP(new Matrix4f(modelViewProjection).scale(width, height, 1.0f));
		GL30.glBindVertexArray(m_quadVAO);
		GL30.glDrawArrays(GL30.GL_QUADS, 0, 4);
	}
	
	private void drawCircle(float radius, Matrix4f modelViewProjection) {
		m_currentShader.setMVP(new Matrix4f(modelViewProjection).scale(radius, radius, 1.0f));
		GL30.glBindVertexArray(m_circleVAO);
		GL30.glDrawArrays(GL30.GL_TRIANGLE_FAN, 0, m_circleSegments);
	}
	
	private void drawLine(float[] vertices, int count, Matrix4f modelViewProjection) {
		m_currentShader.setMVP(modelViewProjection);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_lineDynamicVBO);
		GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, vertices);
		GL30.glBindVertexArray(m_lineVAO);
		GL30.glDrawArrays(GL30.GL_QUAD_STRIP, 0, count * 2);
	}
	
	private void drawPrimitive(Primitive primitive, Matrix4f viewProjection, float viewWidth, float viewHeight) {
		if (!primitive.isVisible()) {
			return;
		}
		
		Matrix4f model = new Matrix4f();
		switch (primitive.getType()) {
		case SHADER: {
			ShaderPrimitive shader = (ShaderPrimitive) primitive;
			useShader(getShader(shader.getShaderType()));
			break;
		}
		case COLOR: {
			ColorPrimitive color = (ColorPrimitive) primitive;
			float alpha = color.getA();
			m_currentShader.setColor(color.getR() * alpha, color.getG() * alpha, color.getB() * alpha, color.getA());
			break;
		}
		case RECT: {
			RectPrimitive rect = (RectPrimitive) primitive;
			float sizeX = rect.getSizeX();
			float sizeY = rect.getSizeY();
			model.translate(rect.getPositionX(), rect.getPositionY(), 0.0f);
			model.rotate((float) Math.toRadians(rect.getRotation()), 0.0f, 0.0f, 1.0f);
			if (rect.isCentered()) {
				model.translate(-sizeX / 2.0f, -sizeY / 2.0f, 0.0f);
			}
			GL30.glEnable(GL30.GL_BLEND);
			GL30.glEnable(GL30.GL_MULTISAMPLE);
			drawRect(sizeX, sizeY, new Matrix4f(viewProjection).mul(model));
			GL30.glDisable(GL30.GL_MULTISAMPLE);
			GL30.glDisable(GL30.GL_BLEND);
			break;
		}
		case CIRCLE: {
			CirclePrimitive circle = (CirclePrimitive) primitive;
			float radius = circle.getRadius();
			model.translate(circle.getPositionX(), circle.getPositionY(), 0.0f);
			model.rotate((float) Math.toRadians(circle.getRotation()), 0.0f, 0.0f, 1.0f);
			GL30.glEnable(GL30.GL_BLEND);
			GL30.glEnable(GL30.GL_MULTISAMPLE);
			drawCircle(radius, new Matrix4f(viewProjection).mul(model));
			GL30.glDisable(GL30.GL_MULTISAMPLE);
			GL30.glDisable(GL30.GL_BLEND);
			break;
		}
		case TEXT: {
			if (!(m_currentShader instanceof FontShader)) {
				System.out.println("Wrong shader bound for text");
				break;
			}
			TextPrimitive text = (TextPrimitive) primitive;
			FontShader shader = (FontShader) m_currentShader;
			shader.setBorder(text.getBorder());
			shader.setBorderColor(text.getBorderColor());
			String str = text.getText();
			if (text.isTranslated()) {
				str = m_context.getString(str);
			}
			model.translate(text.getPositionX(), text.getPositionY(), 0.0f);
			model.rotate((float) Math.toRadians(text.getRotation()), 0.0f, 0.0f, 1.0f);
			model.scale(viewWidth, viewHeight, 1.0f);
			Font font = m_fontCache.getFont(text.getFont(), text.getFontSize());
			model.translate(0.0f, font.getAscent(), 0.0f);
			if (text.getAlignmentX() == TextPrimitive.AlignmentX.CENTER) {
				model.translate(-font.getStringWidth(str) / 2.0f, 0.0f, 0.0f);
			}
			if (text.getAlignmentY() == TextPrimitive.AlignmentY.CENTER) {
				model.translate(0.0f, (font.getDescent() - font.getAscent()) / 2.0f, 0.0f);
			}
			GL30.glEnable(GL30.GL_BLEND);
			GL30.glEnable(GL30.GL_MULTISAMPLE);
			GL30.glEnable(GL30.GL_TEXTURE_2D);
			for (int i = 0; i < str.length(); i++) {
				Font.GlyphInfo glyph = font.renderGlyph(str.charAt(i));
				shader.setTexture(glyph.getTexID());
				drawRect(glyph.getWidth(), glyph.getHeight(), new Matrix4f(viewProjection).mul(model).translate(glyph.getOffsetX(), glyph.getOffsetY(), 0.0f));
				model.translate(glyph.getAdvanceWidth(), 0.0f, 0.0f);
			}
			GL30.glDisable(GL30.GL_TEXTURE_2D);
			GL30.glDisable(GL30.GL_MULTISAMPLE);
			GL30.glDisable(GL30.GL_BLEND);
			break;
		}
		case TRAIL: {
			if (!(m_currentShader instanceof TrailShader)) {
				System.out.println("Wrong shader bound for trail");
				break;
			}
			TrailPrimitive trail = (TrailPrimitive) primitive;
			TrailShader shader = (TrailShader) m_currentShader;
			int count = trail.getVerticesCount();
			shader.setCount(count);
			GL30.glEnable(GL30.GL_BLEND);
			GL30.glEnable(GL30.GL_MULTISAMPLE);
			GL30.glEnable(GL30.GL_LINE_SMOOTH);
			drawLine(trail.getVertices(), count, new Matrix4f(viewProjection).mul(model));
			GL30.glDisable(GL30.GL_LINE_SMOOTH);
			GL30.glDisable(GL30.GL_MULTISAMPLE);
			GL30.glDisable(GL30.GL_BLEND);
			break;
		}
		case PARTICLE_EMITTER: {
			if (!(m_currentShader instanceof ParticleShader)) {
				System.out.println("Wrong shader bound for particles");
				break;
			}
			ParticleEmitterPrimitive emitter = (ParticleEmitterPrimitive) primitive;
			float x = emitter.getX();
			float y = emitter.getY();
			int count = emitter.getEmitCount();
			if (count > 0 && count + m_particlesCount < m_particlesMax) {
				for (int i = 0; i < count; i++) {
					m_particles[(m_particlesCount + i) * 2 + 0] = x;
					m_particles[(m_particlesCount + i) * 2 + 1] = y;
				}
				GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_particlesVBO);
				GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, m_particles);
				m_particlesCount += count;
			}
			break;
		}
		}
	}
	
	public void postDraw(Matrix4f viewProjection) {
		if (m_particlesCount > 0) {
			ParticleShader shader = (ParticleShader) getShader(ShaderType.PARTICLE);
			useShader(shader);
			shader.setMVP(new Matrix4f(viewProjection));
			GL30.glBindVertexArray(m_particlesVAO);
			GL31.glDrawArraysInstanced(GL30.GL_TRIANGLE_FAN, 0, m_circleSegments, m_particlesCount);
		}
	}
	
	private void drawGameEntities(List<GameEntity> gameEntities) {
		Camera camera = m_game.getCamera();
		Vector2f cameraPosition = camera.getPosition();
		float aspect = m_context.getAspect();
		float scale = 10;
		
		float width = aspect * scale * 2.0f;
		float height = scale * 2.0f;
		
		Matrix4f projection = new Matrix4f().ortho(-width / 2.0f, width / 2.0f, height / 2.0f, -height / 2.0f, 1.0f, -1.0f);
		Matrix4f view = new Matrix4f().translate(-cameraPosition.x, -cameraPosition.y, 0.0f);
		Matrix4f viewProjection = new Matrix4f(projection).mul(view);
		for (GameEntity gameEntity : gameEntities) {
			for (Primitive primitive : gameEntity.getPrimitives()) {
				drawPrimitive(primitive, viewProjection, width, height);
			}
		}
		
		postDraw(viewProjection);
	}
	
	private void drawUI(Widget root) {
		float width = 1.0f;
		float height = 1.0f;
		
		Matrix4f projection = new Matrix4f().ortho(0.0f, width, height, 0.0f, -1.0f, 1.0f);
		List<Widget> toTravel = new ArrayList<Widget>();
		toTravel.add(root);
		while (toTravel.size() > 0) {
			List<Widget> next = new ArrayList<Widget>();
			for (Widget widget : toTravel) {
				Matrix4f view = new Matrix4f().translate(widget.getX(), widget.getY(), 0.0f);
				Matrix4f viewProjection = new Matrix4f(projection).mul(view);
				for (Primitive primitive : widget.getPrimitives()) {
					drawPrimitive(primitive, viewProjection, width, height);
				}
				next.addAll(widget.getChildren());
			}
			toTravel = next;
		}
	}
	
	public void draw() {
		Scene scene = m_game.getScenes().lastElement();
		if (scene == null) {
			return;
		}

		GL32.glBindFramebuffer(GL30.GL_FRAMEBUFFER, m_fboMultisample.getId());
		GL32.glDrawBuffers(new int[] {m_fboMultisampleTextureColor.getAttachmentId(), m_fboMultisampleTextureGlow.getAttachmentId()});
		GL32.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		drawGameEntities(scene.getGameEntities());
		drawUI(scene.getRootUI());
		
		GL32.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, m_fboRasterize.getId());
		
		GL32.glReadBuffer(m_fboMultisampleTextureColor.getAttachmentId());
		GL32.glDrawBuffer(m_fboRasterizeTextureColor.getAttachmentId());
		GL32.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL32.glClear(GL32.GL_COLOR_BUFFER_BIT);
		GL32.glBlitFramebuffer(0, 0, m_fboMultisample.getWidth(), m_fboMultisample.getHeight(), 0, 0, m_fboRasterize.getWidth(), m_fboRasterize.getHeight(), GL32.GL_COLOR_BUFFER_BIT, GL32.GL_LINEAR);
		
		GL32.glReadBuffer(m_fboMultisampleTextureGlow.getAttachmentId());
		GL32.glDrawBuffer(m_fboRasterizeTextureGlow.getAttachmentId());
		GL32.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL32.glClear(GL32.GL_COLOR_BUFFER_BIT);
		GL32.glBlitFramebuffer(0, 0, m_fboMultisample.getWidth(), m_fboMultisample.getHeight(), 0, 0, m_fboRasterize.getWidth(), m_fboRasterize.getHeight(), GL32.GL_COLOR_BUFFER_BIT, GL32.GL_LINEAR);
		
		GL32.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, m_fboRasterize.getId());
		GL32.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, m_fboDownsample.getId());
		
		GL32.glReadBuffer(m_fboRasterizeTextureGlow.getAttachmentId());
		GL32.glDrawBuffer(m_fboDownsampleTextureGlow.getAttachmentId());
		GL32.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL32.glClear(GL32.GL_COLOR_BUFFER_BIT);
		GL32.glBlitFramebuffer(0, 0, m_fboRasterize.getWidth(), m_fboRasterize.getHeight(), 0, 0, m_fboDownsample.getWidth(), m_fboDownsample.getHeight(), GL32.GL_COLOR_BUFFER_BIT, GL32.GL_LINEAR);
		
		GL32.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, m_fboDownsample.getId());
		
		GL30.glViewport(0, 0, m_fboDownsample.getWidth(), m_fboDownsample.getHeight());
		
		GaussianBlurShader gaussianBlurShader = (GaussianBlurShader) getShader(ShaderType.GAUSSIAN_BLUR);
		useShader(gaussianBlurShader);
		
		for (int i = 0; i < 1; i++) {
			gaussianBlurShader.setTexture(m_fboDownsampleTextureGlow);
			gaussianBlurShader.setHorizontal(true);
			GL32.glDrawBuffers(new int[] {m_fboDownsampleTextureTmp.getAttachmentId()});
			drawRect(2.0f, 2.0f, new Matrix4f().translate(-1.0f, -1.0f, 0.0f));
			
			gaussianBlurShader.setTexture(m_fboDownsampleTextureTmp);
			gaussianBlurShader.setHorizontal(false);
			GL32.glDrawBuffers(new int[] {m_fboDownsampleTextureGlow.getAttachmentId()});
			drawRect(2.0f, 2.0f, new Matrix4f().translate(-1.0f, -1.0f, 0.0f));
		}
		
		GL30.glViewport(0, 0, m_fboMultisample.getWidth(), m_fboMultisample.getHeight());
		
		GL32.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL32.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

		GL30.glEnable(GL30.GL_BLEND);
		FinalShader finalShader = (FinalShader) getShader(ShaderType.FINAL);
		useShader(finalShader);
		finalShader.setTexture(m_fboDownsampleTextureGlow);
		drawRect(2.0f, 2.0f, new Matrix4f().translate(-1.0f, -1.0f, 0.0f));
		finalShader.setTexture(m_fboRasterizeTextureColor);
		drawRect(2.0f, 2.0f, new Matrix4f().translate(-1.0f, -1.0f, 0.0f));
		GL30.glDisable(GL30.GL_BLEND);
	}
}
