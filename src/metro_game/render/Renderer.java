package metro_game.render;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.opengl.*;

import metro_game.Context;
import metro_game.game.Game;
import metro_game.game.entities.GameEntity;
import metro_game.game.shapes.RectShape;
import metro_game.game.shapes.Shape;
import metro_game.scenes.Scene;
import metro_game.ui.Widget;
import metro_game.ui.primitives.Color;
import metro_game.ui.primitives.Primitive;
import metro_game.ui.primitives.Rect;
import metro_game.ui.primitives.Text;

public class Renderer {
	private Context m_context;
	private Game m_game;
	private FontCache m_fontCache;

	public Renderer(Context context, Game game) {
		m_context = context;
		m_game = game;
		m_fontCache = new FontCache(m_context);
		
		GL.createCapabilities();
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);  
	}
	
	private void drawRect(float width, float height) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(width, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(width, height);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(0, height);
		GL11.glTexCoord2f(0, 0);
		GL11.glEnd();
	}
		
	private void drawShape(Shape shape) {
		Vector2f shapePos = shape.getPosition();
		float shapeRot = shape.getRotation();
		
		GL11.glTranslatef(shapePos.x, shapePos.y, 0);
		GL11.glRotatef(shapeRot, 0.0f, 0.0f, 1.0f);
		
		switch (shape.getType()) {
		case RECT: {
			RectShape rect = (RectShape) shape;
			Vector2f size = rect.getSize();
			GL11.glTranslatef(-size.x / 2.0f, -size.y / 2.0f, 0.0f);
			drawRect(size.x, size.y);
			break;
		}
		}
	}
	
	private void drawPrimitive(Primitive primitive) {
		switch (primitive.getType()) {
		case COLOR: {
			Color color = (Color) primitive;
			GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());
			break;
		}
		case RECT: {
			Rect rect = (Rect) primitive;
			GL11.glTranslatef(rect.getX(), rect.getY(), 0);
			drawRect(rect.getWidth(), rect.getHeight());
			break;
		}
		case TEXT: {
			Text text = (Text) primitive;
			String str = text.getText();
			if (text.isTranslated()) {
				str = m_context.getString(str);
			}
			Font font = m_fontCache.getFont("NotoSerif-Regular.ttf", text.getSize());
			float advance = 0;
			GL11.glTranslatef(text.getX(), text.getY() + font.getAscent(), 0);
			if (text.getAlignmentX() == Text.AlignmentX.CENTER) {
				GL11.glTranslatef(-font.getStringWidth(str) / 2.0f, 0.0f, 0.0f);
			}
			if (text.getAlignmentY() == Text.AlignmentY.CENTER) {
				GL11.glTranslatef(0, -font.getAscent() / 2.0f, 0);
			}
			GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_ADD);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			for (int i = 0; i < str.length(); i++) {
				GL11.glPushMatrix();
				Font.GlyphInfo glyph = font.renderGlyph(str.charAt(i));
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, glyph.getTexID());
				GL11.glTranslatef(advance + glyph.getOffsetX(), glyph.getOffsetY(), 0);
				drawRect(glyph.getWidth(), glyph.getHeight());
				advance += glyph.getAdvanceWidth();
				GL11.glPopMatrix();
			}
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			break;
		}
		}
	}
	
	private void drawGameEntities(List<GameEntity> gameEntities) {
		float aspect = m_context.getAspect();
		float scale = 10;
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-aspect * scale, aspect * scale, scale, -scale, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glColor4f(1, 0, 0, 1);
		for (GameEntity gameEntity : gameEntities) {
			for (Shape shape : gameEntity.getShapes()) {
				GL11.glPushMatrix();
				drawShape(shape);
				GL11.glPopMatrix();
			}
		}
	}
	
	private void drawUI(Widget root) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1, 1, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glColor4f(0, 0, 0, 1);
		List<Widget> toTravel = new ArrayList<Widget>();
		toTravel.add(root);
		while (toTravel.size() > 0) {
			List<Widget> next = new ArrayList<Widget>();
			for (Widget widget : toTravel) {
				GL11.glPushMatrix();
				GL11.glTranslatef(widget.getX(), widget.getY(), 0);
				for (Primitive primitive : widget.getPrimitives()) {
					GL11.glPushMatrix();
					drawPrimitive(primitive);
					GL11.glPopMatrix();
				}
				GL11.glPopMatrix();
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
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		drawGameEntities(scene.getGameEntities());
		drawUI(scene.getRootUI());
	}
}
