package uq.deco2800.dangernoodles.components.weapons;

import uq.deco2800.dangernoodles.ecs.Component;

public class ExplosionComponent extends Component {
    double damage;
    int blastRadius;
    double lifeLeft;
    boolean firstRun;

    public ExplosionComponent(int blastRadius, double damage) {
        this.blastRadius = blastRadius;
        this.damage = damage;
        this.lifeLeft = 0.18; // This looks right... probably should do testing
        this.firstRun = true;
    }
    
    public int getBlastRadius() {
        return this.blastRadius;
    }
    
    public double getDamage() {
        return this.damage;
    }
    
    public boolean isAlive() {
        return this.lifeLeft > 0;
    }
    
    public double lifeLeft() {
        return this.lifeLeft;
    }
    
    public void decrementLife(double dt) {
        this.lifeLeft -= dt;
    }
    
    public boolean isFirstRun() {
        return this.firstRun;
    }
    
    public void firstRunHappened() {
        this.firstRun = false;
    }
}
