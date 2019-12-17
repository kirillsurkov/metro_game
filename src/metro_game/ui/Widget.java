package metro_game.ui;

import java.util.ArrayList;
import java.util.List;

import metro_game.Context;
import metro_game.Utils;
import metro_game.ui.events.InputEvent;
import metro_game.ui.events.MouseButtonEvent;
import metro_game.ui.primitives.Primitive;

public class Widget {
	private Context m_context;
	private List<Primitive> m_primitives;
	private List<Widget> m_children;
	private Widget m_parent;
	private float m_relativeX;
	private float m_relativeY;
	private float m_width;
	private float m_height;
	private boolean m_mouseInside;
	
	public Widget(Context context, float x, float y, float width, float height) {
		m_context = context;
		m_relativeX = x;
		m_relativeY = y;
		m_width = width;
		m_height = height;
		m_primitives = new ArrayList<Primitive>();
		m_children = new ArrayList<Widget>();
		m_parent = null;
	}
	
	protected void onHover(boolean mouseInside) {
	}
	
	protected void onClick(boolean up) {
	}
	
	protected void addPrimitive(Primitive primitive) {
		m_primitives.add(primitive);
	}
	
	protected Context getContext() {
		return m_context;
	}
	
	public void update(double delta) {
		float mouseX = (float) m_context.getMouseX() / m_context.getWidth();
		float mouseY = (float) m_context.getMouseY() / m_context.getHeight();
		boolean mouseInside = Utils.pointInside(mouseX, mouseY, getX(), getY(), m_width, m_height);
		if (!m_mouseInside && mouseInside) {
			onHover(true);
		}
		if (m_mouseInside && !mouseInside) {
			onHover(false);
		}
		for (InputEvent event : m_context.getInputEvents().getEvents()) {
			switch (event.getType()) {
			case MOUSE_BUTTON: {
				MouseButtonEvent mouseButtonEvent = (MouseButtonEvent) event;
				if (mouseInside) {
					onClick(mouseButtonEvent.isUp());
				}
				break;
			}
			default: {
				break;
			}
			}
		}
		m_mouseInside = mouseInside;
		for (Widget child : m_children) {
			child.update(delta);
		}
	}
	
	public <T extends Widget> T addChild(T widget) {
		if (widget.getParent() != null) {
			throw new IllegalStateException("Widget already has parent");
		}
		widget.setParent(this);
		m_children.add(widget);
		return widget;
	}
	
	public List<Primitive> getPrimitives() {
		return m_primitives;
	}
	
	public List<Widget> getChildren() {
		return m_children;
	}
	
	public Widget getParent() {
		return m_parent;
	}
	
	public float getX() {
		float x = m_relativeX;
		if (m_parent != null) {
			x += m_parent.getX();
		}
		return x;
	}
	
	public float getY() {
		float y = m_relativeY;
		if (m_parent != null) {
			y += m_parent.getY();
		}
		return y;
	}
	
	public float getWidth() {
		return m_width;
	}
	
	public float getHeight() {
		return m_height;
	}
	
	public float getRelativeX() {
		return m_relativeX;
	}
	
	public float getRelativeY() {
		return m_relativeY;
	}
	
	public boolean isOnScreen() {
		if (m_parent == null) {
			return true;
		}
		return Utils.intersects(getX(), getY(), m_width, m_height, m_parent.getX(), m_parent.getY(), m_parent.getWidth(), m_parent.getHeight()) && m_parent.isOnScreen();
	}
	
	public void setParent(Widget parent) {
		m_parent = parent;
	}
	
	public void setRelativeX(int x) {
		m_relativeX = x;
	}
	
	public void setRelativeY(int y) {
		m_relativeY = y;
	}
}
