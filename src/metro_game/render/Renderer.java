package metro_game.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
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
import metro_game.render.primitives.TextPrimitive;
import metro_game.render.shaders.DefaultGameShader;
import metro_game.render.shaders.FinalShader;
import metro_game.render.shaders.FontShader;
import metro_game.render.shaders.GaussianBlurShader;
import metro_game.render.shaders.ParticleShader;
import metro_game.render.shaders.Shader;
import metro_game.render.shaders.Shader.ShaderType;
import metro_game.render.shaders.ShaderDraw;
import metro_game.render.shaders.TransformParticleShader;
import metro_game.ui.widgets.Widget;

public class Renderer {
	private Context m_context;
	private Game m_game;
	private FontCache m_fontCache;
	private Map<ShaderType, Shader> m_shaderCache;
	private Shader m_currentShader;
	private Vector4f m_currentColor;
	private ParticleSystem m_particleSystem;
	private Framebuffer m_fboMultisample;
	private Texture m_fboMultisampleTextureColor;
	private Texture m_fboMultisampleTextureGlow;
	private Framebuffer m_fboRasterize;
	private Texture m_fboRasterizeTextureColor;
	private int m_vfxDownsampleFactor;
	private Framebuffer m_fboVFX;
	private Texture m_fboVFXTextureTmp;
	private Texture m_fboVFXTextureColor;
	private Texture m_fboVFXTextureGlow;
	private int m_quadVAO;
	private int m_quadVBO;
	private int m_circleVAO;
	private int m_circleSegments;
	private int m_circleShapeVBO;
	private int m_circlesCount;
	private int m_circlesDataVBO;
	private int m_circlesBufferSize;
	private float[] m_circlesDataBuffer;

	public Renderer(Context context, Game game) {
		m_context = context;
		m_game = game;
		
		GLCapabilities capabilities = GL.createCapabilities();
		
		m_fontCache = new FontCache(m_context);
		m_shaderCache = new HashMap<ShaderType, Shader>();
		m_currentShader = null;
		m_currentColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
		m_particleSystem = new ParticleSystem();
		
		if (!capabilities.OpenGL30) {
			throw new IllegalStateException("OpenGL 3.0 is not supported");
		}
		
		if (!capabilities.GL_ARB_texture_multisample) {
			throw new IllegalStateException("Extension ARB_texture_multisample is not available");
		}
		
		if (!capabilities.GL_EXT_framebuffer_multisample_blit_scaled) {
			throw new IllegalStateException("Extension GL_EXT_framebuffer_multisample_blit_scaled is not available");
		}
		
		if (!capabilities.GL_ARB_instanced_arrays) {
			throw new IllegalStateException("Extension GL_ARB_instanced_arrays is not available");
		}
		
		if (!capabilities.GL_ARB_draw_instanced) {
			throw new IllegalStateException("Extension GL_ARB_draw_instanced is not available");			
		}
		
		if (!capabilities.GL_ARB_transform_feedback2) {
			throw new IllegalStateException("Extension GL_ARB_transform_feedback2 is not available");
		}
		
		GL30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		m_fboMultisample = Framebuffer.create(context.getWidth(), context.getHeight(), FinalShader.SAMPLES);
		m_fboMultisampleTextureColor = m_fboMultisample.attachTexture();
		m_fboMultisampleTextureGlow = m_fboMultisample.attachTexture();
		System.out.println(GL32.glCheckFramebufferStatus(GL32.GL_FRAMEBUFFER));
		
		m_fboRasterize = Framebuffer.create(context.getWidth(), context.getHeight());
		m_fboRasterizeTextureColor = m_fboRasterize.attachTexture();
		System.out.println(GL32.glCheckFramebufferStatus(GL32.GL_FRAMEBUFFER));
		
		m_vfxDownsampleFactor = 2;
		
		m_fboVFX = Framebuffer.create(context.getWidth() / m_vfxDownsampleFactor, context.getHeight() / m_vfxDownsampleFactor);
		m_fboVFXTextureTmp = m_fboVFX.attachTexture();
		m_fboVFXTextureColor = m_fboVFX.attachTexture();
		m_fboVFXTextureGlow = m_fboVFX.attachTexture();
		System.out.println(GL32.glCheckFramebufferStatus(GL32.GL_FRAMEBUFFER));
		
		float[] quadVertices = new float[] {1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};

		m_quadVAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(m_quadVAO);
		GL30.glEnableVertexAttribArray(ShaderDraw.A_POSITION);
		m_quadVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_quadVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, quadVertices, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(ShaderDraw.A_POSITION, 2, GL30.GL_FLOAT, false, 0, 0);
		
		m_circleSegments = 16;
		float[] circleVertices = new float[2 * m_circleSegments];
		for (int i = 0; i < m_circleSegments; i++) {
			double angle = 2.0f * Math.PI * i / m_circleSegments;
			circleVertices[i * 2 + 0] = (float) Math.cos(angle);
			circleVertices[i * 2 + 1] = (float) Math.sin(angle);
		}
		
		m_circlesCount = 0;
		m_circlesBufferSize = 0;

		m_circleVAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(m_circleVAO);
		GL30.glEnableVertexAttribArray(ShaderDraw.A_POSITION);
		GL30.glEnableVertexAttribArray(ShaderDraw.A_SCALE);
		GL30.glEnableVertexAttribArray(ShaderDraw.A_OFFSET);
		GL30.glEnableVertexAttribArray(ShaderDraw.A_COLOR);
		GL33.glVertexAttribDivisor(ShaderDraw.A_SCALE, 1);
		GL33.glVertexAttribDivisor(ShaderDraw.A_OFFSET, 1);
		GL33.glVertexAttribDivisor(ShaderDraw.A_COLOR, 1);
		m_circleShapeVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_circleShapeVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, circleVertices, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(ShaderDraw.A_POSITION, 2, GL30.GL_FLOAT, false, 0, 0);
		m_circlesDataVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_circlesDataVBO);
		GL30.glVertexAttribPointer(ShaderDraw.A_SCALE, 1, GL30.GL_FLOAT, false, 4 * 7, 4 * 0);
		GL30.glVertexAttribPointer(ShaderDraw.A_OFFSET, 2, GL30.GL_FLOAT, false, 4 * 7, 4 * 1);
		GL30.glVertexAttribPointer(ShaderDraw.A_COLOR, 4, GL30.GL_FLOAT, false, 4 * 7, 4 * 3);
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
			case TRANSFORM_PARTICLE: {
				if (!m_shaderCache.containsKey(shaderType)) {
					TransformParticleShader shader = new TransformParticleShader();
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
	
	private void drawEmpty(int count) {
		GL30.glDrawArrays(GL30.GL_TRIANGLE_STRIP, 0, 4);
	}
	
	private void drawRect(float width, float height, Matrix4f modelViewProjection) {
		m_currentShader.setMVP(new Matrix4f(modelViewProjection).scale(width, height, 1.0f));
		GL30.glBindVertexArray(m_quadVAO);
		GL30.glDrawArrays(GL30.GL_TRIANGLE_STRIP, 0, 4);
	}
	
	private void drawPrimitive(Primitive primitive, Matrix4f viewProjection, float viewWidth, float viewHeight) {
		if (!primitive.isVisible()) {
			return;
		}
		
		Matrix4f model = new Matrix4f();
		switch (primitive.getType()) {
		case COLOR: {
			ColorPrimitive color = (ColorPrimitive) primitive;
			float alpha = color.getA();
			m_currentColor.set(color.getR() * alpha, color.getG() * alpha, color.getB() * alpha, alpha);
			break;
		}
		case RECT: {
			useShader(getShader(ShaderType.DEFAULT_GAME));
			m_currentShader.setUseUniforms(true);
			m_currentShader.setColor(m_currentColor);
			
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
			
			if (m_circlesBufferSize == 0) {
				m_circlesBufferSize = 1;
				m_circlesDataBuffer = new float[7];
			} else if (m_circlesBufferSize == m_circlesCount) {
				m_circlesBufferSize = (int) Math.ceil(m_circlesBufferSize * 1.5f);
				m_circlesDataBuffer = Arrays.copyOf(m_circlesDataBuffer, m_circlesBufferSize * 7);
			}
			
			m_circlesDataBuffer[m_circlesCount * 7 + 0] = circle.getRadius();
			m_circlesDataBuffer[m_circlesCount * 7 + 1] = circle.getPositionX();
			m_circlesDataBuffer[m_circlesCount * 7 + 2] = circle.getPositionY();
			m_circlesDataBuffer[m_circlesCount * 7 + 3] = m_currentColor.x;
			m_circlesDataBuffer[m_circlesCount * 7 + 4] = m_currentColor.y;
			m_circlesDataBuffer[m_circlesCount * 7 + 5] = m_currentColor.z;
			m_circlesDataBuffer[m_circlesCount * 7 + 6] = m_currentColor.w;
			
			m_circlesCount++;
			break;
		}
		case TEXT: {
			FontShader shader = (FontShader) getShader(ShaderType.FONT);
			useShader(shader);
			m_currentShader.setUseUniforms(true);
			m_currentShader.setColor(m_currentColor);
			
			TextPrimitive text = (TextPrimitive) primitive;
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
		case PARTICLE_EMITTER: {
			ParticleEmitterPrimitive emitter = (ParticleEmitterPrimitive) primitive;
			m_particleSystem.emit(emitter.getParticles(), emitter.getLifetime(), m_context.getTimer());
			emitter.getParticles().clear();
			break;
		}
		}
	}
	
	private void finishRender(Matrix4f viewProjection) {
		useShader(getShader(ShaderType.DEFAULT_GAME));
		
		m_currentShader.setMVP(viewProjection);
		m_currentShader.setUseUniforms(false);
		
		if (m_circlesCount > 0) {
			GL30.glEnable(GL30.GL_BLEND);
			GL30.glEnable(GL30.GL_MULTISAMPLE);
			GL30.glBindVertexArray(m_circleVAO);
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_circlesDataVBO);
			GL30.glBufferData(GL30.GL_ARRAY_BUFFER, m_circlesDataBuffer, GL30.GL_STATIC_DRAW);
			GL31.glDrawArraysInstanced(GL30.GL_TRIANGLE_FAN, 0, m_circleSegments, m_circlesCount);
			GL30.glDisable(GL30.GL_MULTISAMPLE);
			GL30.glDisable(GL30.GL_BLEND);
			m_circlesCount = 0;
		}
	}
	
	private Matrix4f renderGameEntities(List<GameEntity> gameEntities) {
		Camera camera = m_game.getCamera();
		Vector2f cameraPosition = camera.getPosition();
		float aspect = m_context.getAspect();
		float scale = 10;
		
		float width = aspect * scale * 2.0f;
		float height = scale * 2.0f;
		
		GL30.glBlendFunc(GL30.GL_ONE, GL30.GL_ONE_MINUS_SRC_ALPHA);
		
		Matrix4f projection = new Matrix4f().ortho(-width / 2.0f, width / 2.0f, height / 2.0f, -height / 2.0f, 1.0f, -1.0f);
		Matrix4f view = new Matrix4f().translate(-cameraPosition.x, -cameraPosition.y, 0.0f);
		Matrix4f viewProjection = new Matrix4f(projection).mul(view);
		for (GameEntity gameEntity : gameEntities) {
			for (Primitive primitive : gameEntity.getPrimitives()) {
				drawPrimitive(primitive, viewProjection, width, height);
			}
		}
		
		GL30.glBlendFunc(GL30.GL_ONE_MINUS_DST_ALPHA, GL30.GL_ONE);
		
		finishRender(viewProjection);
		
		GL30.glBlendFunc(GL30.GL_ONE, GL30.GL_ONE_MINUS_SRC_ALPHA);
		
		return viewProjection;
	}
	
	private void renderUI(Widget root) {
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
	
	public void renderVFX(Matrix4f viewProjection) {
		GL30.glEnable(GL30.GL_MULTISAMPLE);
		ParticleShader shader = (ParticleShader) getShader(ShaderType.PARTICLE);
		useShader(shader);
		shader.setMVP(new Matrix4f(viewProjection));
		m_particleSystem.renderInstanced();
		GL30.glDisable(GL30.GL_MULTISAMPLE);
	}
	
	private void blur(Texture vfxTexture) {
		GaussianBlurShader gaussianBlurShader = (GaussianBlurShader) getShader(ShaderType.GAUSSIAN_BLUR);
		useShader(gaussianBlurShader);
		
		gaussianBlurShader.setTexture(vfxTexture);
		gaussianBlurShader.setHorizontal(true);
		GL32.glDrawBuffer(m_fboVFXTextureTmp.getAttachmentId());
		GL32.glClear(GL30.GL_COLOR_BUFFER_BIT);
		drawRect(2.0f, 2.0f, new Matrix4f().translate(-1.0f, -1.0f, 0.0f));
		
		gaussianBlurShader.setTexture(m_fboVFXTextureTmp);
		gaussianBlurShader.setHorizontal(false);
		GL32.glDrawBuffers(vfxTexture.getAttachmentId());
		GL32.glClear(GL30.GL_COLOR_BUFFER_BIT);
		drawRect(2.0f, 2.0f, new Matrix4f().translate(-1.0f, -1.0f, 0.0f));
	}
	
	public void draw(double delta) {
		Scene scene = m_game.getScenes().lastElement();
		if (scene == null) {
			return;
		}
		
		GL30.glDisable(GL30.GL_BLEND);
		
		TransformParticleShader transformParticleShader = (TransformParticleShader) getShader(ShaderType.TRANSFORM_PARTICLE);
		useShader(transformParticleShader);
		transformParticleShader.setDelta(delta);
		m_particleSystem.update(m_context.getTimer());
		
		GL32.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, m_fboMultisample.getId());
		GL32.glDrawBuffers(new int[] {m_fboMultisampleTextureColor.getAttachmentId(), m_fboMultisampleTextureGlow.getAttachmentId()});
		GL32.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		Matrix4f worldMatrix = renderGameEntities(scene.getGameEntities());
		renderUI(scene.getRootUI());
		
		GL32.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, m_fboVFX.getId());
		GL32.glDrawBuffer(m_fboVFXTextureColor.getAttachmentId());
		GL30.glViewport(0, 0, m_fboVFXTextureColor.getWidth(), m_fboVFXTextureColor.getHeight());
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT);
		renderVFX(worldMatrix);
		
		GL32.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, m_fboMultisample.getId());
		
		GL32.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, m_fboRasterize.getId());
		GL32.glReadBuffer(m_fboMultisampleTextureColor.getAttachmentId());
		GL32.glDrawBuffer(m_fboRasterizeTextureColor.getAttachmentId());
		GL32.glBlitFramebuffer(0, 0, m_fboMultisample.getWidth(), m_fboMultisample.getHeight(), 0, 0, m_fboRasterize.getWidth(), m_fboRasterize.getHeight(), GL32.GL_COLOR_BUFFER_BIT, GL32.GL_LINEAR);
		
		GL32.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, m_fboVFX.getId());
		GL32.glReadBuffer(m_fboMultisampleTextureGlow.getAttachmentId());
		GL32.glDrawBuffer(m_fboVFXTextureGlow.getAttachmentId());
		GL32.glBlitFramebuffer(0, 0, m_fboMultisample.getWidth(), m_fboMultisample.getHeight(), 0, 0, m_fboVFX.getWidth(), m_fboVFX.getHeight(), GL32.GL_COLOR_BUFFER_BIT, EXTFramebufferMultisampleBlitScaled.GL_SCALED_RESOLVE_NICEST_EXT);
		
		GL32.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, m_fboVFX.getId());
		
		GL30.glViewport(0, 0, m_fboVFX.getWidth(), m_fboVFX.getHeight());
		
		blur(m_fboVFXTextureColor);
		blur(m_fboVFXTextureGlow);
		
		GL32.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL30.glViewport(0, 0, m_fboRasterize.getWidth(), m_fboRasterize.getHeight());
		GL32.glClear(GL30.GL_COLOR_BUFFER_BIT);
		GL30.glEnable(GL30.GL_BLEND);
		
		FinalShader finalShader = (FinalShader) getShader(ShaderType.FINAL);
		useShader(finalShader);
		finalShader.setTexture(m_fboVFXTextureGlow);
		finalShader.setSharpen(false);
		drawEmpty(4);
		finalShader.setTexture(m_fboVFXTextureColor);
		finalShader.setSharpen(true);
		drawEmpty(4);
		finalShader.setTexture(m_fboRasterizeTextureColor);
		finalShader.setSharpen(false);
		drawEmpty(4);
	}
}
