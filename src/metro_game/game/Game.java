package metro_game.game;

import java.util.Stack;

import metro_game.Context;
import metro_game.game.events.GameEvent;
import metro_game.game.events.SwitchSceneEvent;
import metro_game.scenes.MainMenu;
import metro_game.scenes.Scene;
import metro_game.ui.events.InputEvent;
import metro_game.ui.events.InputEvent.Type;

public class Game {
	private Context m_context;
	private Stack<Scene> m_scenes;
	
	public Game(Context context) {
		m_context = context;
		m_scenes = new Stack<Scene>();
		m_scenes.push(new MainMenu(m_context));
	}
	
	public Stack<Scene> getScenes() {
		return m_scenes;
	}
	
	public void update(double delta) {
		for (InputEvent event : m_context.getInputEvents().getEvents()) {
			if (event.getType() == Type.BACK) {
				if (!m_scenes.lastElement().onBack()) {
					m_scenes.pop();
				}
			}
		}

		for (GameEvent event : m_context.getGameEvents().getEvents()) {
			switch (event.getType()) {
			case SWITCH_SCENE: {
				SwitchSceneEvent switchSceneEvent = (SwitchSceneEvent) event;
				m_scenes.push(switchSceneEvent.getScene());
				break;
			}
			}
		}
		
		if (m_scenes.size() > 0) {
			Scene scene = m_scenes.lastElement();
			scene.update(delta);
			if (scene.isNeedClose()) {
				m_scenes.pop();
			}
		}
	}
}