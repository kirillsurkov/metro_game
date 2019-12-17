package metro_game;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.stb.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import metro_game.scenes.Scene;
import metro_game.ui.Widget;
import metro_game.ui.primitives.Color;
import metro_game.ui.primitives.Primitive;
import metro_game.ui.primitives.Rect;

public class Renderer {
	private Context m_context;
	private Window m_window;
	private Game m_game;
	
	private int m_fontTexID;
	
	private final STBTTFontinfo m_info;
	
	public Renderer(Context context, Window window, Game game) {
		m_context = context;
		m_window = window;
		m_game = game;
		
		GL.createCapabilities();
		GL11.glClearColor(0, 0, 0, 1);
		
		ByteBuffer ttf = null;
		try {
			ttf = Utils.ioResourceToByteBuffer("NotoSerif-Regular.ttf", 512 * 1024);
		} catch (IOException e) {
			System.out.println("Font not found");
		}
		
		m_info = STBTTFontinfo.create();
		if (!STBTruetype.stbtt_InitFont(m_info, ttf)) {
			System.out.println("InitFont failed");
		}
		
		int fw = 320;
		int fh = 64;
		
		STBTTBakedChar.Buffer cdata = STBTTBakedChar.malloc(96*2);
		ByteBuffer bitmap = BufferUtils.createByteBuffer(fw * fh);
		STBTruetype.stbtt_BakeFontBitmap(ttf, 18, bitmap, fw, fh, 48, cdata);
		
		int[] w = new int[1];
		int[] h = new int[1];
		int[] xoff = new int[1];
		int[] yoff = new int[1];
		ByteBuffer glyph = STBTruetype.stbtt_GetGlyphBitmap(m_info, 10, 10, STBTruetype.stbtt_FindGlyphIndex(m_info, '9'), w, h, xoff, yoff);
		fw = w[0];
		fh = h[0];
		System.out.println(fw);
		System.out.println(fh);
		System.out.println(xoff[0]);
		System.out.println(yoff[0]);
		
		/*ByteBuffer img = null;
		try {
			img = Utils.ioResourceToByteBuffer("img.png", 512 * 1024);
		} catch (IOException e) {
		}
		
		int[] x = new int[1];
		int[] y = new int[1];
		int[] comp = new int[1];
		ByteBuffer m_img = STBImage.stbi_load_from_memory(img, x, y, comp, 0);
		System.out.println(x[0]);
		System.out.println(y[0]);
		System.out.println(comp[0]);*/
		
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		m_fontTexID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_fontTexID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, fw, fh, 0, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, glyph);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	}
	
	public void draw() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		drawScene(m_game.getScenes().lastElement());
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
		
		float x = widget.getX();
		float y = widget.getY();
		
		for (Primitive primitive : widget.getPrimitives()) {
			switch (primitive.getType()) {
			case COLOR: {
				Color color = (Color) primitive;
				GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());
				break;
			}
			case RECT: {
				Rect rect = (Rect) primitive;
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_fontTexID);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0, 0);
			    GL11.glVertex2f(x + rect.getX(), y + rect.getY());
			    GL11.glTexCoord2f(1, 0);
			    GL11.glVertex2f(x + rect.getX() + rect.getWidth(), y + rect.getY());
			    GL11.glTexCoord2f(1, 1);
			    GL11.glVertex2f(x + rect.getX() + rect.getWidth(), y + rect.getY() + rect.getHeight());
			    GL11.glTexCoord2f(0, 1);
			    GL11.glVertex2f(x + rect.getX(), y + rect.getY() + rect.getHeight());
			    GL11.glEnd();
			    GL11.glDisable(GL11.GL_TEXTURE_2D);
				break;
			}
			case TEXT: {
				
				break;
			}
			}
		}
	}
}
