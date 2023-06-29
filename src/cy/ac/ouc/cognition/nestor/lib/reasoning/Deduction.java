package cy.ac.ouc.cognition.nestor.lib.reasoning;

import org.json.JSONPropertyIgnore;

import cy.ac.ouc.cognition.nestor.lib.base.NESTORThing;

public abstract class Deduction extends NESTORThing {

	@JSONPropertyIgnore
    public String getMarkedRulesChainText() {
	
		return "";

    }

	
	@JSONPropertyIgnore
    public String getMarkedRulesText() {
    	
		return "";

    }

	

}
