package cy.ac.ouc.cognition.nestor.lib.logic;

import cy.ac.ouc.cognition.nestor.lib.utility.ParameterLib;

public class Conflict extends LogicExpressionWithHead {
	
	public Conflict() {
		this("");
	}
	
	
	public Conflict(String name) {
		super(name);
		TypeOfExpression = ExpressionType.CONFLICT;
		MultiExpression = false;
		HasDocumentScope = false;
	}
	


	protected String buildTextRepresentation() {

 		String textRepresentation = "";
 		
		if (!Body.getTextRepresentation().isEmpty())
			textRepresentation +=	Body.getTextRepresentation() + " " +
 									ParameterLib.TargetPolicy_ConflictNeckSymbol + " ";
		
		if (!Head.getTextRepresentation().isEmpty())
			textRepresentation += Head.getTextRepresentation() + ";";
	        
		if (TitleIncluded)
			textRepresentation = Title + " " + ParameterLib.TargetPolicy_NameSeparator + " " + textRepresentation;
	        
	    return textRepresentation;

	}

}
