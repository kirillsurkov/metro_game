package metro_game.game.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import metro_game.Context;
import metro_game.game.entities.GameEntity;
import metro_game.game.events.DestroyEntityEvent;
import metro_game.ui.widgets.AlertWidget;
import metro_game.ui.widgets.Widget;

public class Scene {
	protected Context m_context;
	private boolean m_ready;
	private boolean m_paused;
	private boolean m_needClose;
	private float m_slowFactor;
	private List<GameEntity> m_gameEntities;
	private Widget m_rootUI;
	private AlertWidget m_alert;
	
	public Scene(Context context) {
		m_context = context;
		m_ready = false;
		m_paused = false;
		m_needClose = false;
		m_slowFactor = 1.0f;
		m_gameEntities = new ArrayList<GameEntity>();
		m_rootUI = new Widget(context, 0, 0, context.getWidth(), context.getHeight());
		m_alert = null;
	}
	
	public void init() {
	}
	
	protected <T extends GameEntity> T addGameEntity(T gameEntity) {
		if (!m_ready) {
			throw new IllegalStateException("Scene not ready");
		}
		m_gameEntities.add(gameEntity);
		return gameEntity;
	}
	
	protected <T extends Widget> T addUIChild(T widget) {
		return m_rootUI.addChild(widget);
	}
	
	public void setReady(boolean ready) {
		m_ready = ready;
	}

	public boolean isPaused() {
		return m_paused;
	}
	
	public void setNeedClose(boolean needClose) {
		m_needClose = needClose;
	}
	
	public boolean isNeedClose() {
		return m_needClose;
	}
	
	public void setSlowFactor(float slowFactor) {
		m_slowFactor = slowFactor;
	}
	
	public float getSlowFactor() {
		return m_slowFactor;
	}
	
	public List<GameEntity> getGameEntities() {
		return m_gameEntities;
	}
	
	public Widget getRootUI() {
		return m_rootUI;
	}
	
	protected void setAlert(AlertWidget alert) {
		m_alert = addUIChild(alert);
		if (m_alert != null) {
			m_paused = true;
		}
	}
	
	protected void closeAlert() {
		m_alert.setNeedRemove(true);
		m_alert = null;
		m_paused = false;
	}
	
	protected AlertWidget getAlert() {
		return m_alert;
	}
	
	public void update(double delta) {
		if (m_alert != null) {
			m_alert.update(delta);
		}
		
		if (!m_paused) {
			Stack<Integer> toRemove = new Stack<Integer>();
			for (int i = 0; i < m_gameEntities.size(); i++) {
				GameEntity gameEntity = m_gameEntities.get(i);
				gameEntity.update(delta);
				if (gameEntity.isNeedRemove()) {
					m_context.getGameEvents().pushEvent(new DestroyEntityEvent(gameEntity));
					toRemove.push(i);
				}
			}
			while (toRemove.size() > 0) {
				m_gameEntities.remove((int) toRemove.pop());
			}
			m_rootUI.update(delta);
		}
	}
	
	public boolean onBack() {
		return false;
	}
}
