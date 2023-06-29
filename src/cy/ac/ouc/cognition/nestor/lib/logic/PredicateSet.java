package cy.ac.ouc.cognition.nestor.lib.logic;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONPropertyIgnore;

import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.TL;
import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.outln;
import cy.ac.ouc.cognition.nestor.lib.reasoning.DisplayNode;


public class PredicateSet extends LogicElement {


	protected PartType						Part;				
	protected HashMap<String, LEPredicate>	Predicates;
	protected int							CurrentPredicateIndex;
	protected String						Delimiter;
	protected boolean						PredicatesAdded;


	PredicateSet() {
		this(PartType.UNDEFINED);
	}
	

	PredicateSet(PartType part) {
		super();
		PredicatesAdded = false;
		Part = part;
		Predicates = new HashMap<String, LEPredicate>();
		CurrentPredicateIndex = 0;
		Delimiter = ", ";
	}
	

	
	public void addPredicate(String predicateIndex, LEPredicate predicate) {
		Predicates.put(predicateIndex, predicate);
		PredicatesAdded = true;
		Complete = false;
		outln(TL, Part.toString() + ": Added predicate ["+predicateIndex+"]: " + predicate.getName().buildNameText(false));	
	}


	public void addPredicate(LEPredicate predicate) {
		String predicateKey = Integer.toString(this.CurrentPredicateIndex++);	
		addPredicate(predicateKey, predicate);
	}


	public int addPredicateConditionally(
			ArrayList<String> predicateIdentifier,
			ArrayList<String> argumentIdentifiers) {

		LEPredicate predicate = new LEPredicate(predicateIdentifier, argumentIdentifiers, true);
		//predicate.setDisplayRuleList(displayRuleList);
		
		/* If new predicate with the same arguments is  not already added to the set
		 * proceed to add it
		 */	
		if (!this.containsPredicateWithArguments(predicate, true)) {
	
			String predicateKey = Integer.toString(this.CurrentPredicateIndex++);
		
			addPredicate(predicateKey, predicate);
		
			return 1;
		}
		
		return 0;

	}


	public int addPredicateConditionally(
			ArrayList<String> predicateIdentifier,
			ArrayList<LEArgument> argumentIdentifiers,
			ArrayList<DisplayNode> displayRuleList) {

		LEPredicate predicate = new LEPredicate(predicateIdentifier, argumentIdentifiers);
		//predicate.setDisplayRuleList(displayRuleList);
		
		/* If new predicate with the same arguments is  not already added to the set
		 * proceed to add it
		 */	
		if (!this.containsPredicateWithArguments(predicate, true)) {
	
			String predicateKey = Integer.toString(this.CurrentPredicateIndex++);
		
			addPredicate(predicateKey, predicate);
		
			return 1;
		}

		return 0;
	}
	
	
	public void addArgumentToPredicate(LEArgument argument, String predicateIndex) {
		Predicates.get(predicateIndex).addArgument(argument);
		Complete = false;
		outln(TL, Part.toString() + ": Added Argument to predicate " + Predicates.get(predicateIndex).getName().buildNameText(false) + "["+predicateIndex+"]: " + argument.getTextRepresentation());
	}


	public boolean containsPredicateName(LEPredicate testPred, boolean ignoreNegation) {

		for (LEPredicate predicate : Predicates.values())
			if (predicate.Name.sameAs(testPred.getName(), ignoreNegation))
				return true;

		return false;
	}

	
	public boolean containsPredicateWithArguments(LEPredicate testPred, boolean ignoreNegation) {

		for (LEPredicate predicate : Predicates.values())
			if (predicate.sameAs(testPred, ignoreNegation))
				return true;

		return false;
	}

	
	protected String buildTextRepresentation() {

 		String textRepresentation = "";
 		
        for (LEPredicate setPredicate : Predicates.values()) {

        	String predicateString = setPredicate.getTextRepresentation();
        	
       		if (!textRepresentation.isEmpty() && !predicateString.isEmpty())
       			textRepresentation += Delimiter;
       		
       		textRepresentation += predicateString;

        }
	        
	    return textRepresentation;
	}

	
	
	public boolean sameAs(LogicElement logicElement, boolean ignoreNegation) {
		
		// If the size of the two predicate sets is not the same, then they are not the same
		if (this.Predicates.size() != ((PredicateSet) logicElement).Predicates.size())
			return false;
		
		// Make sure all predicates of the set checked are included in this set
		for (LEPredicate predicate : ((PredicateSet) logicElement).Predicates.values())
			
			// Even if one of the predicates of the set checked is not included in this set
			// then they are not the same
			if (!this.containsPredicateWithArguments(predicate, ignoreNegation))
				return false;
		
		// If we reach this point, then they are the same (all are included)
		return true;
	
	}




	/**
	 * @return the predicates
	 */
	public HashMap<String, LEPredicate> getPredicates() {
		return Predicates;
	}


	/**
	 * @param predicates the predicates to set
	 */
	@JSONPropertyIgnore
	public void setPredicates(HashMap<String, LEPredicate> predicates) {
		Predicates = predicates;
	}


	/**
	 * @return the predicatesAdded
	 */
	@JSONPropertyIgnore
	public boolean arePredicatesAdded() {
		return PredicatesAdded;
	}


	/**
	 * @@param complete the complete to set
	 */
	@JSONPropertyIgnore
	public void setComplete(boolean complete) {

        for (LEPredicate setPredicate : Predicates.values())
        	setPredicate.setComplete(complete);
 				
		super.setComplete(complete);

	}


	/**
	 * @return the delimiter
	 */
	@JSONPropertyIgnore
	public String getDelimiter() {
		return Delimiter;
	}


	/**
	 * @param delimiter the delimiter to set
	 */
	@JSONPropertyIgnore
	public void setDelimiter(String delimiter) {
		Delimiter = delimiter;
	}

}
