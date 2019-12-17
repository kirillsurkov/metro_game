package metro_game;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.*;
import org.lwjgl.stb.*;

import metro_game.scenes.Scene;
import metro_game.ui.Widget;
import metro_game.ui.primitives.Color;
import metro_game.ui.primitives.Primitive;
import metro_game.ui.primitives.Rect;

public class Renderer {
	private Context m_context;
	private Window m_window;
	private Game m_game;
	
	public Renderer(Context context, Window window, Game game) {
		m_context = context;
		m_window = window;
		m_game = game;
		
		try {
			ByteBuffer ttf = Utils.ioResourceToByteBuffer("NotoSerif-Regular.ttf", 512 * 1024);
		} catch (IOException e) {
			System.out.println("Font not found");
		}
		
		GL.createCapabilities();
		GL11.glClearColor(0, 0, 0, 1);
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
				GL11.glBegin(GL11.GL_QUADS);
			    GL11.glVertex2f(x + rect.getX(), y + rect.getY());
			    GL11.glVertex2f(x + rect.getX() + rect.getWidth(), y + rect.getY());
			    GL11.glVertex2f(x + rect.getX() + rect.getWidth(), y + rect.getY() + rect.getHeight());
			    GL11.glVertex2f(x + rect.getX(), y + rect.getY() + rect.getHeight());
			    GL11.glEnd();
				break;
			}
			case TEXT: {
				
				break;
			}
			}
		}
	}
}
