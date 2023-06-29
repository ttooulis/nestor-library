package cy.ac.ouc.cognition.nestor.lib.logic;

import cy.ac.ouc.cognition.nestor.lib.utility.ParameterLib;

public class Constant extends LogicExpression {

	public Constant() {
		this("");
	}
	
	
	public Constant(String name) {
		super(name);
		TypeOfExpression = ExpressionType.CONSTANT;
		MultiExpression = true;
		HasDocumentScope = true;
		HasHead = false;
	}
	


	protected String buildTextRepresentation() {

 		String textRepresentation = "";
 		
 		Body.setDelimiter(" ");
 		
		if (!Body.getTextRepresentation().isEmpty())
			textRepresentation += Body.getTextRepresentation() + ".";      
        
		if (TitleIncluded)
			textRepresentation = Title + " " + ParameterLib.TargetPolicy_NameSeparator + " " + textRepresentation;

	    return textRepresentation;

	}

}
