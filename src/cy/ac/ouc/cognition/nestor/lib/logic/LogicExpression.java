package cy.ac.ouc.cognition.nestor.lib.logic;

import org.json.JSONPropertyIgnore;

import cy.ac.ouc.cognition.nestor.lib.utility.ParameterLib;
import cy.ac.ouc.cognition.nestor.lib.reasoning.Deduction;


public abstract class LogicExpression extends LogicElement {

	protected ExpressionType	TypeOfExpression;
	protected boolean			MultiExpression;
	protected boolean			HasDocumentScope;
	protected String			Title;
	protected boolean			TitleIncluded;
	protected PredicateSet		Body;
	protected boolean			HasHead;

	private Deduction			RuleDeduction;


	LogicExpression() {
		this("");
	}
	
	
	LogicExpression(String title) {
		super();
		Title = new String(title);
		TitleIncluded = (ParameterLib.TargetPolicy_GenerateName == 0) ? false : true;
		Body = new PredicateSet(PartType.BODY);
	}
	


	protected String buildTextRepresentation() {

 		String textRepresentation = Body.getTextRepresentation();
	        
	    return textRepresentation;

	}


	public boolean arePredicatesAdded() {

		return Body.arePredicatesAdded();

	}


	
	public boolean sameAs(LogicElement logicElement, boolean ignoreNegation) {

		return this.Body.sameAs(((LogicExpression) logicElement).Body, ignoreNegation);
	
	}
	



	/**
	 * @return the typeOfExpression
	 */
	public ExpressionType getTypeOfExpression() {
		return TypeOfExpression;
	}


	/**
	 * @return the multiExpression
	 */
	public boolean isMultiExpression() {
		return MultiExpression;
	}


	/**
	 * @return the hasDocumentScope
	 */
	public boolean hasDocumentScope() {
		return HasDocumentScope;
	}


	/**
	 * @param hasDocumentScope the hasDocumentScope to set
	 */
	public void setHasDocumentScope(boolean hasDocumentScope) {
		HasDocumentScope = hasDocumentScope;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return Title;
	}


	/**
	 * @param title the title to set
	 */
	@JSONPropertyIgnore
	public void setTitle(String title) {
		Title = title;
	}


	/**
	 * @return the titleIncluded
	 */
	public boolean isTitleIncluded() {
		return TitleIncluded;
	}


	/**
	 * @param titleIncluded the titleIncluded to set
	 */
	public void setTitleIncluded(boolean titleIncluded) {
		TitleIncluded = titleIncluded;
	}


	/**
	 * @return the body
	 */
	public PredicateSet getBody() {
		return Body;
	}


	/**
	 * @param body the body to set
	 */
	@JSONPropertyIgnore
	public void setBody(PredicateSet body) {
		Body = body;
	}


	/**
	 * @return the hasHead
	 */
	public boolean hasHead() {
		return HasHead;
	}


	/**
	 * @return the ruleDeduction
	 */
	public Deduction getRuleDeduction() {
		return RuleDeduction;
	}

	/**
	 * @param ruleDeduction the ruleDeduction to set
	 */
	@JSONPropertyIgnore
	public void setRuleDeduction(Deduction ruleDeduction) {
		RuleDeduction = ruleDeduction;
	}

	
	/**
	 * @@param complete the complete to set
	 */
	@JSONPropertyIgnore
	public void setComplete(boolean complete) {
		Body.setComplete(complete);
		super.setComplete(complete);
	}

}
