package cy.ac.ouc.cognition.nestor.lib.base;

import org.json.JSONPropertyIgnore;

public abstract class NESTORThing {
	
	protected transient boolean		Complete;


	public NESTORThing() {
		Complete = false;
	}

	

	/**
	 * @return the complete
	 */
	@JSONPropertyIgnore
	public  boolean isComplete() {
		return Complete;
	}
	

	/**
	 * @@param complete the complete to set
	 */
	@JSONPropertyIgnore
	public void setComplete(boolean complete) {
		Complete = complete;
	}

}
