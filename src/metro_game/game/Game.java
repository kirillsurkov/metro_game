package metro_game.game;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import metro_game.Context;
import metro_game.game.entities.GameEntity;
import metro_game.game.events.GameEvent;
import metro_game.game.events.NewBodyEvent;
import metro_game.game.events.SwitchSceneEvent;
import metro_game.game.physics.Physics;
import metro_game.game.physics.bodies.Body;
import metro_game.scenes.MainMenu;
import metro_game.scenes.Scene;
import metro_game.ui.events.InputEvent;
import metro_game.ui.events.InputEvent.Type;

public class Game {
	private Context m_context;
	private Physics m_physics;
	private Stack<Scene> m_scenes;
	
	public Game(Context context, Physics physics) {
		m_context = context;
		m_physics = physics;
		m_scenes = new Stack<Scene>();
		m_context.getGameEvents().pushEvent(new SwitchSceneEvent(new MainMenu(context)));
	}
	
	public Stack<Scene> getScenes() {
		return m_scenes;
	}
	
	public void update(double delta) {
		for (InputEvent event : m_context.getInputEvents().getEvents()) {
			if (event.getType() == Type.BACK) {
				if (!m_scenes.lastElement().onBack()) {
					m_scenes.pop();
					if (m_scenes.size() > 0) {
						m_scenes.lastElement().setReady(true);
					}
				}
			}
		}

		for (GameEvent event : m_context.getGameEvents().getEvents()) {
			switch (event.getType()) {
			case SWITCH_SCENE: {
				SwitchSceneEvent switchSceneEvent = (SwitchSceneEvent) event;
				Scene scene = switchSceneEvent.getScene();
				if (m_scenes.size() > 0) {
					m_scenes.lastElement().setReady(false);
				}
				m_scenes.push(scene);
				m_physics.switchScene(scene);
				scene.setReady(true);
				scene.init();
				break;
			}
			case NEW_BODY: {
				NewBodyEvent newBodyEvent = (NewBodyEvent) event;
				m_physics.addBody(newBodyEvent.getOwner(), newBodyEvent.getBody());
				break;
			}
			}
		}
		
		for (Map.Entry<GameEntity, List<Body>> entry : m_physics.getBodies().entrySet()) {
			GameEntity owner = entry.getKey();
			for (Body body : entry.getValue()) {
				owner.onPhysicsUpdate(body);
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