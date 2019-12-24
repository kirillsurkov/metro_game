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
import metro_game.render.primitives.Color;
import metro_game.render.primitives.Primitive;
import metro_game.render.primitives.Rect;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.Text;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;
import metro_game.render.shaders.DefaultGameShader;
import metro_game.render.shaders.FontShader;
import metro_game.render.shaders.Shader;
import metro_game.ui.widgets.Widget;

public class Renderer {
	private Context m_context;
	private Game m_game;
	private FontCache m_fontCache;
	private Map<ShaderType, Shader> m_shaderCache;
	private Shader m_currentShader;
	private int m_quadVBO;

	public Renderer(Context context, Game game) {
		m_context = context;
		m_game = game;
		m_fontCache = new FontCache(m_context);
		m_shaderCache = new HashMap<ShaderType, Shader>();
		
		GL.createCapabilities();
		GL30.glClearColor(0, 0, 0, 1);
		GL30.glEnable(GL30.GL_BLEND);
		GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		
		float[] quadVertices = new float[] {0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};
		
		m_quadVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_quadVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, quadVertices, GL30.GL_STATIC_DRAW);
		
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
			case DEFAULT_GAME: {
				if (!m_shaderCache.containsKey(shaderType)) {
					m_shaderCache.put(shaderType, new DefaultGameShader());
				}
				return m_shaderCache.get(shaderType);
			}
			case FONT: {
				if (!m_shaderCache.containsKey(shaderType)) {
					m_shaderCache.put(shaderType, new FontShader());
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
		m_currentShader.setMVP(new Matrix4f(modelViewProjection).scale(width, height, 0.0f));
		GL30.glDrawArrays(GL30.GL_QUADS, 0, 4);
	}
		
	private void drawPrimitive(Primitive primitive, Matrix4f viewProjection, boolean center) {
		if (!primitive.isVisible()) {
			return;
		}
		
		Matrix4f model = new Matrix4f();
		switch (primitive.getType()) {
		case SHADER: {
			ShaderPrimitive shaderPrimitive = (ShaderPrimitive) primitive;
			useShader(getShader(shaderPrimitive.getShaderType()));
			break;
		}
		case COLOR: {
			Color color = (Color) primitive;
			m_currentShader.setColor(color.getR(), color.getG(), color.getB(), color.getA());
			break;
		}
		case RECT: {
			Rect rect = (Rect) primitive;
			Vector2f position = rect.getPosition();
			Vector2f size = rect.getSize();
			model.translate(position.x, position.y, 0.0f);
			model.rotate((float) Math.toRadians(rect.getRotation()), 0.0f, 0.0f, 1.0f);
			if (center) {
				model.translate(-size.x / 2.0f, -size.y / 2.0f, 0.0f);
			}
			drawRect(size.x, size.y, new Matrix4f(viewProjection).mul(model));
			break;
		}
		case TEXT: {
			Text text = (Text) primitive;
			String str = text.getText();
			if (text.isTranslated()) {
				str = m_context.getString(str);
			}
			Font font = m_fontCache.getFont(text.getFont(), text.getSize());
			float advance = 0;
			model.translate(text.getX(), text.getY() + font.getAscent(), 0.0f);
			if (text.getAlignmentX() == Text.AlignmentX.CENTER) {
				model.translate(-font.getStringWidth(str) / 2.0f, 0.0f, 0.0f);
			}
			if (text.getAlignmentY() == Text.AlignmentY.CENTER) {
				model.translate(0.0f, -font.getAscent() / 2.0f, 0.0f);
			}
			Matrix4f modelViewProjection = new Matrix4f(viewProjection).mul(model);
			GL30.glEnable(GL30.GL_TEXTURE_2D);
			for (int i = 0; i < str.length(); i++) {
				Font.GlyphInfo glyph = font.renderGlyph(str.charAt(i));
				if (!(m_currentShader instanceof FontShader)) {
					System.out.println("Wrong shader bound for text");
					return;
				}
				((FontShader) m_currentShader).setTexture(glyph.getTexID());
				drawRect(glyph.getWidth(), glyph.getHeight(), new Matrix4f(modelViewProjection).translate(advance + glyph.getOffsetX(), glyph.getOffsetY(), 0.0f));
				advance += glyph.getAdvanceWidth();
			}
			GL30.glDisable(GL30.GL_TEXTURE_2D);
			break;
		}
		}
	}
	
	private void drawGameEntities(List<GameEntity> gameEntities) {
		Camera camera = m_game.getCamera();
		Vector2f cameraPosition = camera.getPosition();
		float aspect = m_context.getAspect();
		float scale = 10;
		
		Matrix4f projection = new Matrix4f().ortho(-aspect * scale, aspect * scale, scale, -scale, 1.0f, -1.0f);
		Matrix4f view = new Matrix4f().translate(-cameraPosition.x, -cameraPosition.y, 0.0f);
		Matrix4f viewProjection = new Matrix4f(projection).mul(view);
		for (GameEntity gameEntity : gameEntities) {
			for (Primitive primitive : gameEntity.getPrimitives()) {
				drawPrimitive(primitive, viewProjection, true);
			}
		}
	}
	
	private void drawUI(Widget root) {
		Matrix4f projection = new Matrix4f().ortho(0.0f, 1.0f, 1.0f, 0.0f, -1.0f, 1.0f);
		List<Widget> toTravel = new ArrayList<Widget>();
		toTravel.add(root);
		while (toTravel.size() > 0) {
			List<Widget> next = new ArrayList<Widget>();
			for (Widget widget : toTravel) {
				Matrix4f view = new Matrix4f().translate(widget.getX(), widget.getY(), 0.0f);
				Matrix4f viewProjection = new Matrix4f(projection).mul(view);
				for (Primitive primitive : widget.getPrimitives()) {
					drawPrimitive(primitive, viewProjection, false);
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
		
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		
		drawGameEntities(scene.getGameEntities());
		drawUI(scene.getRootUI());
	}
}
