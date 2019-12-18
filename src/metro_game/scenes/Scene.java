package metro_game.scenes;

import metro_game.Context;
import metro_game.ui.AlertWidget;
import metro_game.ui.Widget;

public class Scene {
	protected Context m_context;
	private boolean m_needClose;
	private Widget m_rootUI;
	private AlertWidget m_alert;
	
	public Scene(Context context) {
		m_context = context;
		m_needClose = false;
		m_rootUI = new Widget(context, 0, 0, context.getWidth(), context.getHeight());
		m_alert = null;
	}
	
	protected void addChild() {
		
	}
	
	protected <T extends Widget> T addUIChild(T widget) {
		return m_rootUI.addChild(widget);
	}
	
	public void setNeedClose(boolean needClose) {
		m_needClose = needClose;
	}
	
	public boolean isNeedClose() {
		return m_needClose;
	}
	
	public Widget getRootUI() {
		return m_rootUI;
	}
	
	protected void setAlert(AlertWidget alert) {
		m_alert = addUIChild(alert);
	}
	
	protected void closeAlert() {
		m_alert.setNeedRemove(true);
		m_alert = null;
	}
	
	protected AlertWidget getAlert() {
		return m_alert;
	}
	
	public void update(double delta) {
		if (m_alert == null) {
			m_rootUI.update(delta);
		} else {
			m_alert.update(delta);
		}
	}
	
	public boolean onBack() {
		return false;
	}
}
