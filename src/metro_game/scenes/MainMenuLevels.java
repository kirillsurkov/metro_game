package metro_game.scenes;

import metro_game.Context;
import metro_game.ui.LevelWidget;

public class MainMenuLevels extends Scene {
	public MainMenuLevels(Context context) {
		super(context);
		
		addUIChild(new LevelWidget(context, 0.1f, 0.2f));
		addUIChild(new LevelWidget(context, 0.27f, 0.2f));
		addUIChild(new LevelWidget(context, 0.44f, 0.2f));
		addUIChild(new LevelWidget(context, 0.61f, 0.2f));
		addUIChild(new LevelWidget(context, 0.78f, 0.2f));
	}
}
