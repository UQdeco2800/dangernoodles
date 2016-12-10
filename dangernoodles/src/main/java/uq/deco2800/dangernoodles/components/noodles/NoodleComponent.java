package uq.deco2800.dangernoodles.components.noodles;

import uq.deco2800.dangernoodles.ecs.Component;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;


/**
 * 
 * @author Paul Haley
 * 
 * The NoodleComponent stores all noodle specific information.
 */
public class NoodleComponent extends Component {
	private final NoodleEnum noodleType;
	private String currentSprite;

	/**
	 * Constructor for NoodleComponent
	 * @require noodle != null && noodle is of class NoodleEnum
	 * @ensure valid NoodleComponent
	 * 
	 * @param noodle The NoodleEnum (noodle character class) to be used for the creation of the noodle component.
	 */
	public NoodleComponent(NoodleEnum noodle) {
		this.noodleType = noodle;
	}

	/**
	 * @return The string of the noodle class character. See NoodleEnum.java for
	 *         possible classes.
	 */
	public String getNoodleClassName() {
		return this.noodleType.getName();
	}
	
	/**
	 * @return NoodleEnum of the NoodleComponent
	 */
	public NoodleEnum getNoodleClass() {
		return this.noodleType;
	}

	/**
	 * @ensure The current filepath to the noodleComponent's sprite is returned.
	 * @return the currentSprite filepath.
	 */
	public String getCurrentSprite() {
		return currentSprite;
	}

	/**
	 * @require valid filepath is given of the form 
	 * 		"/characters/[I-]noodleClass[_idle][_*team].gif"
	 * 		Where I- when the noodle is moving right
	 * 		noodle class is Civilian, Juggernaught or Warrior
	 * 		_idle is for if the noodle is not moving, place nothing here otherwise
	 * 		_*team is the team the noodle is on B for blue, R for red, Y for Yellow, 
	 * 			nothing in the _*team place if green.
	 * @ensure The given filepath is set.
	 * @param currentSprite the currentSprite to set for the noodleComponent.
	 */
	public void setCurrentSprite(String currentSprite) {
		this.currentSprite = currentSprite;
	}

}
