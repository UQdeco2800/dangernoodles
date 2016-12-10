package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.components.StatusBarComponent;
import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.ecs.ComponentMap;
import uq.deco2800.dangernoodles.ecs.System;
import uq.deco2800.dangernoodles.ecs.World;

public class HealthSystem implements System {

	@Override
	public void run(World world, double t, double dt) {
        for (ComponentMap cm : world.getIterator(
                HealthComponent.class,
                StatusBarComponent.class
            )) {
            StatusBarComponent s = cm.get(StatusBarComponent.class);
            HealthComponent h = cm.get(HealthComponent.class);
            s.setValue(h.getHealth());
        }
	}
}
