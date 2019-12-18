package metro_game.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.*;

import metro_game.Context;
import metro_game.game.Game;
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
	
	private void drawScene(Scene scene) {
		List<Widget> toTravel = new ArrayList<Widget>();
		toTravel.add(scene.getRootUI());
		while (true) {
			List<Widget> next = new ArrayList<Widget>();
			for (Widget widget : toTravel) {
				drawWidget(widget);
				next.addAll(widget.getChildren());
			}
			toTravel = next;
			if (toTravel.size() == 0) {
				break;
			}
		}
	}
	
	private void drawWidget(Widget widget) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1, 1, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glColor4f(0, 0, 0, 1);
		
		float widgetX = widget.getX();
		float widgetY = widget.getY();
		
		for (Primitive primitive : widget.getPrimitives()) {
			switch (primitive.getType()) {
			case COLOR: {
				Color color = (Color) primitive;
				GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());
				break;
			}
			case RECT: {
				Rect rect = (Rect) primitive;
				float x = rect.getX();
				float y = rect.getY();
				float w = rect.getWidth();
				float h = rect.getHeight();
				GL11.glBegin(GL11.GL_QUADS);
			    GL11.glVertex2f(widgetX + x, widgetY + y);
			    GL11.glVertex2f(widgetX + x + w, widgetY + y);
			    GL11.glVertex2f(widgetX + x + w, widgetY + y + h);
			    GL11.glVertex2f(widgetX + x, widgetY + y + h);
			    GL11.glEnd();
				break;
			}
			case TEXT: {
				Text text = (Text) primitive;
				float textX = text.getX();
				float textY = text.getY();
				String str = text.getText();
				Font font = m_fontCache.getFont("NotoSerif-Regular.ttf", text.getSize());
				float strWidth = font.getStringWidth(str);
				float advance = 0;
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				for (int i = 0; i < str.length(); i++) {
					Font.GlyphInfo glyph = font.renderGlyph(str.charAt(i));
					float w = glyph.getWidth();
					float h = glyph.getHeight();
					float offX = widgetX + textX + advance + glyph.getOffsetX();
					float offY = widgetY + textY + glyph.getOffsetY() + font.getAscent();
					if (text.getAlignmentX() == Text.AlignmentX.CENTER) {
						offX -= strWidth / 2.0f;
					}
					if (text.getAlignmentY() == Text.AlignmentY.CENTER) {
						offY -= font.getAscent() / 2.0f;
					}
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, glyph.getTexID());
					GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_ADD);
					GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex2f(offX, offY);
					GL11.glTexCoord2f(1, 0);
				    GL11.glVertex2f(offX + w, offY);
				    GL11.glTexCoord2f(1, 1);
				    GL11.glVertex2f(offX + w, offY + h);
				    GL11.glTexCoord2f(0, 1);
				    GL11.glVertex2f(offX, offY + h);
					GL11.glTexCoord2f(0, 0);
				    GL11.glEnd();
				    advance += glyph.getAdvanceWidth();
				}
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				break;
			}
			}
		}
	}
	
	public void draw() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		drawScene(m_game.getScenes().lastElement());
	}
}
