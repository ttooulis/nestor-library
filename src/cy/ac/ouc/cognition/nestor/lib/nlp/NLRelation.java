package cy.ac.ouc.cognition.nestor.lib.nlp;

public abstract class NLRelation extends NLProcessing {

	protected String	RelationName;
	
	NLRelation(String relationName) {
		super();
		RelationName = new String(relationName);
	}

	

	/**
	 * @return the relationName
	 */
	public  String getRelationName() {
		return RelationName;
	}

	
}
