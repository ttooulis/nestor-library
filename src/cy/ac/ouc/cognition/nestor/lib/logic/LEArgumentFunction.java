package cy.ac.ouc.cognition.nestor.lib.logic;

import java.util.List;

public class LEArgumentFunction extends LEArgument {
	
	public LEPredicate		Function;


	public LEArgumentFunction(String  name) {
		this(new LiteralName(name));
	}

	public LEArgumentFunction(String name, List<LEArgument> arguments) {
		this(new LiteralName(name));
		Function = new LEPredicate(name, arguments);
	}
	
	public LEArgumentFunction(List<String> name, List<String> argumentIdentifiers) {
		this(new LiteralName(name));
		Function = new LEPredicate(name, argumentIdentifiers, true);
	}
	
	public LEArgumentFunction(LiteralName name) {
		super(name);
		Function = new LEPredicate(name.Name);
	}

	
	
	protected String buildTextRepresentation() {
		
 		String textRepresentation = Function.buildTextRepresentation();
	        
	    return textRepresentation;

	}

	
	
	public boolean sameAs(LogicElement argument, boolean ignoreNegation) {
		return this.Function.sameAs(((LEArgumentFunction) argument).Function, ignoreNegation);
	}


	
	public boolean sameAs(List<String> argumentName, boolean ignoreNegation) {
		return false;
	}

}
