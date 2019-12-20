package metro_game.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
	private boolean m_mouseDown;
	private boolean m_needRemove;
	
	public Widget(Context context, float x, float y, float width, float height) {
		m_context = context;
		m_relativeX = x;
		m_relativeY = y;
		m_width = width;
		m_height = height;
		m_mouseInside = false;
		m_mouseDown = false;
		m_needRemove = false;
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
	
	protected boolean isMouseDown() {
		return m_mouseDown;
	}
	
	public void update(double delta) {
		if (m_needRemove) {
			return;
		}
		
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
				boolean up = mouseButtonEvent.isUp();
				boolean click = m_mouseDown ^ !up;
				m_mouseDown = !up;
				if (mouseInside) {
					onHover(true);
					if (click) {
						onClick(up);
					}
				}
				break;
			}
			default: {
				break;
			}
			}
		}
		
		m_mouseInside = mouseInside;
		Stack<Integer> toRemove = new Stack<Integer>();
		for (int i = 0; i < m_children.size(); i++) {
			Widget child = m_children.get(i);
			child.update(delta);
			if (child.isNeedRemove()) {
				toRemove.push(i);
			}
		}
		
		while (!toRemove.isEmpty()) {
			m_children.remove((int) toRemove.pop());
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
	
	public void removeChild(Widget widget) {
		if (m_children.contains(widget)) {
			m_children.remove(widget);
		}
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
	
	public void setNeedRemove(boolean needRemove) {
		m_needRemove = true;
	}
	
	public boolean isNeedRemove() {
		return m_needRemove;
	}
}
