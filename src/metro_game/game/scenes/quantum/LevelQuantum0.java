package metro_game.game.scenes.quantum;

import metro_game.Context;
import metro_game.game.Route;
import metro_game.game.entities.ElectronEntity;
import metro_game.game.entities.EnemyEntity;
import metro_game.game.entities.PlayerEntityGolf;

public class LevelQuantum0 extends LevelQuantumBase {
	public LevelQuantum0(Context context) {
		super(context);
	}
	
	private EnemyEntity addAtom(float atomX, float atomY) {
		int electrons = 100;
		float atomRadius = 4.0f;
		float atomRadiusStep = 2.0f;
		EnemyEntity center = addGameEntity(new EnemyEntity(m_context, Route.Mode.LOOP_START_END, atomX, atomY, 2.0f));
		
		int level = 0;
		int index = 0;
		int maxIndex = 2;
		float lifetime = 4.0f;
		float radius = atomRadius;
		float speed = 1.0f;
		for (int i = 0; i < electrons; i++) {
			float ang = (float) (Math.PI  * 2.0f * 1.0f * index / maxIndex);
			float x = atomX + (float) Math.cos(ang) * radius;
			float y = atomY + (float) Math.sin(ang) * radius;
			float currentRadius = radius;
			float currentLevel = level;
			float currentSpeed = speed;
			addGameEntity(new ElectronEntity(m_context, x, y, lifetime) {
				private double m_timer = 0.0;
				
				@Override
				public void getPosition(double delta, float[] x, float[] y) {
					m_timer += delta;
					x[0] = center.getX() + (float) Math.cos(currentSpeed * 0.5f * m_timer * Math.PI + ang) * currentRadius;
					y[0] = center.getY() + (float) Math.sin(currentSpeed * 0.5f * m_timer * Math.PI + ang) * currentRadius;
				}
			});
			index++;
			if (index >= maxIndex) {
				index = 0;
				level++;
				lifetime *= maxIndex * speed;
				maxIndex = Math.min(2 * (level + 1) * (level + 1), electrons - i - 1);
				speed = 1.0f / (currentLevel + 1.0f);
				lifetime /= maxIndex * speed;
				radius += Math.pow(atomRadiusStep, 1) / Math.pow(level, 0.5f);
			}
		}
		
		return center;
	}
	
	@Override
	public void init() {
		float radius = 11.0f;
		for (int i = 0; i < 16; i++) {
			float ang = i * (float) Math.PI * 2 / 16.0f;
			float x = radius * (float) Math.cos(ang);
			float y = radius * (float) Math.sin(ang);
			EnemyEntity center = addAtom(x, y);

			for (int j = 0; j < 64; j++) {
				float waypointAng = (float) Math.PI * 2 * j / 64;
				center.addWaypoint(radius * (float) Math.cos(ang + waypointAng), radius * (float) Math.sin(ang + waypointAng), 2.0f);
			}
		}
		/*for (int i = 0; i < 16; i++) {
			addAtom((i - 8) * 5, -5);
		}*/
		//addAtom(0, -5);
		addGameEntity(new PlayerEntityGolf(m_context, 0.0f, 0.0f));
	}
}
