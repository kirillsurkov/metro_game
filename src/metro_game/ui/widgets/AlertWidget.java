package metro_game.ui.widgets;

import metro_game.Context;
import metro_game.render.primitives.Color;
import metro_game.render.primitives.Rect;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.Text;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;

public class AlertWidget extends Widget {
	public AlertWidget(Context context, String text, String[] buttons) {
		super(context, 0, 0, 1, 1);
		
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		addPrimitive(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		addPrimitive(new Rect(0, 0, getWidth(), getHeight(), 0.0f));
		addPrimitive(new Color(0.75f, 0.0f, 0.0f, 0.9f));
		addPrimitive(new Rect(0.2f, 0.2f, 0.6f, 0.65f, 0.0f));
		addPrimitive(new ShaderPrimitive(ShaderType.FONT));
		addPrimitive(new Color(1.0f, 1.0f, 1.0f, 1.0f));
		addPrimitive(new Text(text, false, 48, 0.5f, 0.3f, Text.AlignmentX.CENTER, Text.AlignmentY.TOP));
		
		float buttonGap = 0.05f;
		float buttonWidth = 0.45f / buttons.length - buttonGap;
		for (int i = 0; i < buttons.length; i++) {
			final int index = i;
			addChild(new ButtonWidget(context, buttons[i], 0.3f + i * (buttonWidth + buttonGap), 0.6f, buttonWidth, 0.1f) {
				@Override
				public void onClick(boolean up) {
					super.onClick(up);
					if (up) {
						onButton(index);
					}
				}
			});
		}
	}
	
	public void onButton(int index) {
	}
}
