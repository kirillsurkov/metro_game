package metro_game.scenes;

import metro_game.Context;
import metro_game.ui.Widget;

public class Scene {
	private Widget m_rootUI;
	
	public Scene(Context context) {
		m_rootUI = new Widget(context, 0, 0, context.getWidth(), context.getHeight());
	}
	
	protected void addChild() {
		
	}
	
	protected <T extends Widget> T addUIChild(T widget) {
		return m_rootUI.addChild(widget);
	}
	
	public Widget getRootUI() {
		return m_rootUI;
	}
	
	public void update(double delta) {
		m_rootUI.update(delta);
	}
}
