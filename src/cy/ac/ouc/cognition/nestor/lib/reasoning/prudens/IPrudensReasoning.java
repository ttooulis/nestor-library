package cy.ac.ouc.cognition.nestor.lib.reasoning.prudens;

import cy.ac.ouc.cognition.nestor.lib.reasoning.IReasoning;

public interface IPrudensReasoning extends IReasoning {
	
	static enum Interface {
		WEB,
		LOCAL,
		UNDEFINED;
	}


	static enum Importance {
		ASCENDING,
		DESCENDING,
		UNDEFINED;
	}


}
