package cy.ac.ouc.cognition.nestor.lib.logic;

import cy.ac.ouc.cognition.nestor.lib.utility.ParameterLib;

public class Implication extends LogicExpressionWithHead {
	
	public Implication() {
		this("");
	}
	
	
	public Implication(String title) {
		super(title);
		TypeOfExpression = ExpressionType.IMPLICATION;
		MultiExpression = false;
		HasDocumentScope = false;
	}
	


	protected String buildTextRepresentation() {

 		String textRepresentation = "";
 		
		if (!Body.getTextRepresentation().isEmpty())
			textRepresentation +=	Body.getTextRepresentation() + " " +
 									ParameterLib.TargetPolicy_NeckSymbol + " ";
		
		if (!Head.getTextRepresentation().isEmpty())
			textRepresentation += Head.getTextRepresentation() + ";";
	        
		if (TitleIncluded)
			textRepresentation = Title + " " + ParameterLib.TargetPolicy_NameSeparator + " " + textRepresentation;

	    return textRepresentation;

	}

}
