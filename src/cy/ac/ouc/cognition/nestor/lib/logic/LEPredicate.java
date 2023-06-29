package cy.ac.ouc.cognition.nestor.lib.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONPropertyIgnore;

import cy.ac.ouc.cognition.nestor.lib.reasoning.DisplayNode;

public class LEPredicate extends LogicElement {

	
	protected	LiteralName				Name;
	protected	List<LEArgument>	Arguments;

	private		ArrayList<DisplayNode>	MarkedRulesChain;


	public LEPredicate() {
		this("");
	}
	

	public LEPredicate(String name) {
		this(new LiteralName(name), new ArrayList<LEArgument>());
	}
	

	
	public LEPredicate(List<String> name) {
		this(new LiteralName(name), new ArrayList<LEArgument>());
	}
	

	
	public LEPredicate(String name, List<LEArgument> arguments) {
		this(new LiteralName(name), arguments);
	}
	

	
	public LEPredicate(List<String> name, List<LEArgument> arguments) {
		this(new LiteralName(name), arguments);
	}
	
	
	public LEPredicate(List<String> name, List<String> argumentIdentifiers, boolean buildArguments) {

		this(name);
		
		List<LEArgument> arguments = new ArrayList<LEArgument>();	
		for (String argumentIdentifier : argumentIdentifiers)
			arguments.add(new LEArgument(argumentIdentifier));
		
		this.setArguments(arguments);

	}

	
	public LEPredicate(LiteralName name, List<LEArgument> arguments) {
		super();
		Name = name;
		Arguments = arguments;
	}

	
	
	protected String buildPredicateText(boolean ignoreNegation) {
		
		String predicateText = Name.buildNameText(ignoreNegation);
		
	    if (!predicateText.isEmpty()) {

		    if (!Arguments.isEmpty()) {
		    		
		    	String argumentsListText = "";
		
		    	for (LEArgument argument : Arguments) {
			
		        	String argumentString = argument.getTextRepresentation();
		        	
		       		if (!argumentsListText.isEmpty() && !argumentString.isEmpty())
		       			argumentsListText += ", ";
			
		       		argumentsListText += argumentString;
			
		    	}
		    	
		    	if (!argumentsListText.isEmpty())
		    		predicateText += "(" + argumentsListText + ")";
			
		    }
	    }
      
	    return predicateText;

	}



	protected String buildTextRepresentation() {
		
 		String textRepresentation = this.buildPredicateText(false);
	        
	    return textRepresentation;

	}

	
	
	public boolean sameAs(LogicElement predicate, boolean ignoreNegation) {
		return this.buildPredicateText(ignoreNegation).equals(((LEPredicate) predicate).buildPredicateText(ignoreNegation));
	
	}
	

	
	/**
	 * @return the name
	 */
	public LiteralName getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	@JSONPropertyIgnore
	public void setName(LiteralName name) {
		Name = name;
	}

	
	/**
	 * @return the arguments
	 */
	public List<LEArgument> getArguments() {
		return Arguments;
	}


	/**
	 * @param arguments the arguments to set
	 */
	@JSONPropertyIgnore
	public void setArguments(List<LEArgument> arguments) {
		Arguments = arguments;
	}

	public void addArgument(LEArgument argument) {
		Arguments.add(argument);
	}


    /**
	 * @return the markedRulesChain
	 */
	@JSONPropertyIgnore
	public ArrayList<DisplayNode> getMarkedRulesChain() {
		return MarkedRulesChain;
	}


	/**
	 * @param markedRulesChain the markedRulesChain to set
	 */
	public void setDisplayRuleList(ArrayList<DisplayNode> markedRulesChain) {
		MarkedRulesChain = markedRulesChain;
	}


	/**
	 * @@param complete the complete to set
	 */
	@JSONPropertyIgnore
	public void setComplete(boolean complete) {

		Name.setComplete(complete);

		for (LEArgument argument : Arguments)
	    	argument.setComplete(complete);
 				
		super.setComplete(complete);

	}

}
