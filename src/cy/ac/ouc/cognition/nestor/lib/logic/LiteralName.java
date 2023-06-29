package cy.ac.ouc.cognition.nestor.lib.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONPropertyIgnore;

import cy.ac.ouc.cognition.nestor.lib.utility.ParameterLib;

public class LiteralName extends LogicElement {

	protected	List<String>	Name;


	public LiteralName() {
		this("");
	}
	
	
	public LiteralName(String name) {
		this(new ArrayList<String>(Arrays.asList(name)));
	}
	

	public LiteralName(List<String> name) {
		super();
		Name = name;
	}


	
	protected String buildNameText(boolean ignoreNegation) {
		
		String nameText = "";
		int i = 0, doNotCapitalizeIndex = 1;
		
		for (String namePart : Name) {
			
			String namePartToUse = namePart.replaceAll("\'", "");

			i++;
			
			if (i==1 && ignoreNegation && namePart.equals("\'-\'"))
				i = 0;
			
			// If current word belongs in capitalisation exceptions list, do not capitalise next word, if should
			else if (namePart.matches(ParameterLib.TargetPolicy_PredNameCapitalizeExceptions)) {
				doNotCapitalizeIndex = i + 1;
				nameText += namePartToUse;
			}

			else if (i==1)
				nameText += namePartToUse;

			else {
				nameText += ParameterLib.TargetPolicy_PredNameConcatChar;
				if (ParameterLib.TargetPolicy_PredNameCapitalize == 1 && i != doNotCapitalizeIndex)
					nameText += namePartToUse.substring(0, 1).toUpperCase() + namePartToUse.substring(1);
				else
					nameText += namePartToUse;
			}
		}
		
	    return nameText;

	}


	
	protected String buildTextRepresentation() {
		
		String textRepresentation = this.buildNameText(false);
	        
	    return textRepresentation;

	}
	
	
	
	public boolean sameAs(LogicElement name, boolean ignoreNegation) {
		return this.buildNameText(ignoreNegation).equals(((LiteralName) name).buildNameText(ignoreNegation));
	}

	
	
	public boolean sameAs(List<String> name, boolean ignoreNegation) {
		return this.sameAs(new LiteralName(name), ignoreNegation);
	}

	

	
	/**
	 * @return the name
	 */
	@JSONPropertyIgnore
	public List<String> getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	@JSONPropertyIgnore
	public void setName(List<String> name) {
		Name = name;
	}

	/**
	 * @param name the name to set
	 */
	@JSONPropertyIgnore
	public void setName(String name) {
		Name = new ArrayList<String>(Arrays.asList(name));
	}

}
