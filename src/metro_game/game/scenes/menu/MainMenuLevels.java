package metro_game.game.scenes.menu;

import metro_game.Context;
import metro_game.game.events.SwitchSceneEvent;
import metro_game.game.scenes.Scene;
import metro_game.game.scenes.carriage.LevelCarriage0;
import metro_game.game.scenes.quantum.LevelQuantum0;
import metro_game.ui.widgets.LevelWidget;

public class MainMenuLevels extends Scene {
	public MainMenuLevels(Context context) {
		super(context);
		
		addUIChild(new LevelWidget(context, 0.1f, 0.2f) {
			@Override
			public void onClick(boolean up) {
				super.onClick(up);
				if (up) {
					context.getGameEvents().pushEvent(new SwitchSceneEvent(new LevelCarriage0(context)));
				}
			}
		});
		addUIChild(new LevelWidget(context, 0.27f, 0.2f) {
			@Override
			public void onClick(boolean up) {
				super.onClick(up);
				if (up) {
					context.getGameEvents().pushEvent(new SwitchSceneEvent(new LevelQuantum0(context)));
				}
			}
		});
		addUIChild(new LevelWidget(context, 0.44f, 0.2f));
		addUIChild(new LevelWidget(context, 0.61f, 0.2f));
		addUIChild(new LevelWidget(context, 0.78f, 0.2f));
	}
}
