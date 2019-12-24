package metro_game.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.*;

import metro_game.Context;
import metro_game.Utils;
import metro_game.game.Camera;
import metro_game.game.Game;
import metro_game.game.entities.GameEntity;
import metro_game.game.scenes.Scene;
import metro_game.render.shapes.RectShape;
import metro_game.render.shapes.Shape;
import metro_game.ui.primitives.Color;
import metro_game.ui.primitives.Primitive;
import metro_game.ui.primitives.Rect;
import metro_game.ui.primitives.Text;
import metro_game.ui.widgets.Widget;

public class Renderer {
	private Context m_context;
	private Game m_game;
	private FontCache m_fontCache;
	private int m_program;
	private int m_quadVBO;
	private int m_quadDefaultShaderVAO;

	public Renderer(Context context, Game game) {
		m_context = context;
		m_game = game;
		m_fontCache = new FontCache(m_context);
		
		GL.createCapabilities();
		GL30.glClearColor(0, 0, 0, 1);
		GL30.glEnable(GL30.GL_BLEND);
		GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		
		float[] quadVertices = new float[] {0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};
		
		m_quadVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, m_quadVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, quadVertices, GL30.GL_STATIC_DRAW);
		
		try {
			int vs = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
			GL30.glShaderSource(vs, new String(Utils.readFile("vs.glsl")));
			GL30.glCompileShader(vs);
			System.out.println(GL30.glGetShaderInfoLog(vs));
			int fs = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
			GL30.glShaderSource(fs, new String(Utils.readFile("fs.glsl")));
			GL30.glCompileShader(fs);
			System.out.println(GL30.glGetShaderInfoLog(fs));
			m_program = GL30.glCreateProgram();
			GL30.glAttachShader(m_program, vs);
			GL30.glAttachShader(m_program, fs);
			GL30.glLinkProgram(m_program);
			System.out.println(GL30.glGetProgramInfoLog(m_program));
			
			GL30.glBindFragDataLocation(m_program, 0, "outColor");
			
			m_quadDefaultShaderVAO = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(m_quadDefaultShaderVAO);
			
			int aPosition = GL30.glGetAttribLocation(m_program, "a_position");
			GL30.glVertexAttribPointer(aPosition, 2, GL30.GL_FLOAT, false, 0, 0);
			GL30.glEnableVertexAttribArray(aPosition);
		} catch (IOException e) {
			System.out.println("Shaders not found");
		}
	}
	
	private void drawRect(float width, float height, Matrix4f modelViewProjection) {
		float[] mvp = new float[16];
		new Matrix4f(modelViewProjection).scale(width, height, 0.0f).get(mvp);
		int uMVP = GL30.glGetUniformLocation(m_program, "u_mvp");
		GL30.glUniformMatrix4fv(uMVP, false, mvp);
		
		GL30.glBindVertexArray(m_quadDefaultShaderVAO);
		GL30.glDrawArrays(GL30.GL_QUADS, 0, 4);
	}
		
	private void drawShape(Shape shape, Matrix4f viewProjection) {
		GL30.glUseProgram(m_program);
		int uTextured = GL30.glGetUniformLocation(m_program, "u_textured");
		GL30.glUniform1i(uTextured, 0);
		
		Vector2f shapePos = shape.getPosition();
		float shapeRot = shape.getRotation();
		
		Matrix4f model = new Matrix4f();
		model.translate(shapePos.x, shapePos.y, 0.0f);
		model.rotate((float) Math.toRadians(shapeRot), 0.0f, 0.0f, 1.0f);
		
		switch (shape.getType()) {
		case RECT: {
			RectShape rect = (RectShape) shape;
			Vector2f size = rect.getSize();
			model.translate(-size.x / 2.0f, -size.y / 2.0f, 0.0f);
			drawRect(size.x, size.y, new Matrix4f(viewProjection).mul(model));
			break;
		}
		}
	}
	
	private void drawPrimitive(Primitive primitive, Matrix4f viewProjection) {
		GL30.glUseProgram(m_program);
		int uTextured = GL30.glGetUniformLocation(m_program, "u_textured");
		GL30.glUniform1i(uTextured, 0);
		Matrix4f model = new Matrix4f();
		switch (primitive.getType()) {
		case COLOR: {
			Color color = (Color) primitive;
			int uColor = GL30.glGetUniformLocation(m_program, "u_color");
			GL30.glUniform4f(uColor, color.getR(), color.getG(), color.getB(), color.getA());
			break;
		}
		case RECT: {
			Rect rect = (Rect) primitive;
			model.translate(rect.getX(), rect.getY(), 0.0f);
			drawRect(rect.getWidth(), rect.getHeight(), new Matrix4f(viewProjection).mul(model));
			break;
		}
		case TEXT: {
			GL30.glUniform1i(uTextured, 1);
			Text text = (Text) primitive;
			String str = text.getText();
			if (text.isTranslated()) {
				str = m_context.getString(str);
			}
			Font font = m_fontCache.getFont("NotoSerif-Regular.ttf", text.getSize());
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
				int uTexture = GL30.glGetUniformLocation(m_program, "u_texture");
				GL30.glActiveTexture(GL30.GL_TEXTURE0);
				GL30.glBindTexture(GL30.GL_TEXTURE_2D, glyph.getTexID());
				GL30.glUniform1i(uTexture, 0);
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
			for (Shape shape : gameEntity.getShapes()) {
				if (!shape.isVisible()) {
					continue;
				}
				drawShape(shape, viewProjection);
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
					drawPrimitive(primitive, viewProjection);
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
