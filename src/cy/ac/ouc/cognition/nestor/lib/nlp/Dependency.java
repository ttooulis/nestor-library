package cy.ac.ouc.cognition.nestor.lib.nlp;

import org.json.JSONPropertyIgnore;

public class Dependency extends NLRelation {
	
	private	NLToken	Governor;
	private NLToken	Dependent;

	
		
	public Dependency(String dependencyName) {
		super(dependencyName);
	}

	public Dependency(String dependencyName, NLToken governor, NLToken dependent) {
		
		this(dependencyName);
		
		Governor = governor;
		Dependent = dependent;
		
		Complete = true;
	}
	


	/**
	 * @return the governor
	 */
	@JSONPropertyIgnore
	public NLToken getGovernor() {
		return Governor;
	}
	
	/**
	 * @param governor the governor to set
	 */
	@JSONPropertyIgnore
	public void setGovernor(NLToken governor) {
		Governor = governor;
		Complete = false;
	}
	
	
	
	/**
	 * @return the dependent
	 */
	public NLToken getDependent() {
		return Dependent;
	}
	/**
	 * @param dependent the dependent to set
	 */
	public void setDependent(NLToken dependent) {
		Dependent = dependent;
	}

}
