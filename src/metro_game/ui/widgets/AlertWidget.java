package metro_game.ui.widgets;

import metro_game.Context;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.TextPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;

public class AlertWidget extends Widget {
	public AlertWidget(Context context, String text, String[] buttons) {
		super(context, 0, 0, 1, 1);
		
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		addPrimitive(new ColorPrimitive(0.0f, 0.0f, 0.0f, 0.5f));
		addPrimitive(new RectPrimitive(0, 0, getWidth(), getHeight(), 0.0f));
		addPrimitive(new ColorPrimitive(0.75f, 0.0f, 0.0f, 0.9f));
		addPrimitive(new RectPrimitive(0.2f, 0.2f, 0.6f, 0.65f, 0.0f));
		addPrimitive(new ShaderPrimitive(ShaderType.FONT));
		addPrimitive(new ColorPrimitive(1.0f, 1.0f, 1.0f, 1.0f));
		addPrimitive(new TextPrimitive(text, false, 48, 0.5f, 0.3f, TextPrimitive.AlignmentX.CENTER, TextPrimitive.AlignmentY.TOP));
		
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
