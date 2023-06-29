package cy.ac.ouc.cognition.nestor.lib.reasoning.prudens;

import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.errln;
import cy.ac.ouc.cognition.nestor.lib.utility.ParameterLib;
import cy.ac.ouc.cognition.nestor.lib.reasoning.ReasoningAgent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;

import coaching.Context;
import coaching.KnowledgeBase;
import coaching.Prudens;
import coaching.Rule;
import coaching.Variable;


public class PrudensJava extends ReasoningAgent implements IPrudensReasoning {
	
	private static String 	ls = ParameterLib.NESTOR_LineSeperator;
	private Version			PrudensVersion = Version.PRUDENSJAVA;
	private Importance		RuleImportance = Importance.ASCENDING;
	
	
	// JSON Fields
	ArrayList<Rule>					Inferences;

	
	 public PrudensJava() {
		 this.Inferences = new ArrayList <Rule> ();
	 }
	 
	 
	 public PrudensJava(Importance ruleImportance) {
		 this();
		 RuleImportance = ruleImportance;
	 }
	 
	 
	 public PrudensJava(ArrayList<Rule> inferences) {
		 this.Inferences = inferences;
	 }
	 
	 
	/**
	 * @return the inferences
	 */
	public ArrayList<Rule> getInferences() {
		return Inferences;
	}

	/**
	 * @param inferences the inferences to set
	 */
	public void setInferences(ArrayList<Rule> inferences) {
		this.Inferences = inferences;
	}

	
    public void infer(String logicalAnnotationText, String translationPolicyString) throws PrudensException {

		InputStream logicalAnnotationStream = new ByteArrayInputStream(logicalAnnotationText.getBytes());
		Path contextPath;

		try {
			
    		translationPolicyString += ls; // This version of Prudens requires this adjustment
        	InputStream stream = new ByteArrayInputStream(translationPolicyString.getBytes());
    		Path knowledgeBasePath;

    		knowledgeBasePath = Files.createTempFile(null, null);
			Files.copy(stream, knowledgeBasePath, StandardCopyOption.REPLACE_EXISTING);

			KnowledgeBase TranslationPolicy = new KnowledgeBase(new File(knowledgeBasePath.toUri()));
        	
			contextPath = Files.createTempFile(null, null);
			Files.copy(logicalAnnotationStream, contextPath, StandardCopyOption.REPLACE_EXISTING);
	        File logicalAnnotationtFile = new File(contextPath.toUri());
			
	        Context context = new Context(logicalAnnotationtFile, TranslationPolicy);
	        Prudens agent = new Prudens(TranslationPolicy, context);
	        this.Inferences = agent.getMarkedRules();
	        
	    	// Sort Rules by priority as defined by Rule Name in ascending or descending order
	    	// CID - Forcing Prudens priority. Should be avoided for PrudensJS
	        // CID - Check again if should be vise-versa!!!
	    	if (RuleImportance == Importance.DESCENDING)
	    		this.Inferences.sort(Comparator.comparing(Rule::getName));
	    	else
	    		this.Inferences.sort(Comparator.comparing(Rule::getName).reversed());

	        
		}

		catch (Exception e) {
			errln("PrudensJava Error inferring facts from Translation Policy: " + e.getMessage());
			throw new PrudensException("PrudensJava Error inferring facts from Translation Policy: " + e.getMessage());
		}

    }

	
	
    public boolean inferencesListIsEmpty() {

    	if (Inferences != null && !Inferences.isEmpty())
    		return false;

    	else
    		return true;

    }
    
    
    
    public int numberOfInferences() {
    	
    	if (Inferences == null)
    		return 0;
    	
    	return Inferences.size();
    	
    }
    
	public Object getInference(int expressionIndex) {
		
		return Inferences.get(expressionIndex);
	}

	public Version getAgentVersion() {
		return PrudensVersion;
	}
	
	public String getNameForItem(int expressionIndex) {
		
		return Inferences.get(expressionIndex).getName();
	}

	public String getPredicateNameForItem(int expressionIndex) {
		
		return Inferences.get(expressionIndex).getHead().getAtom().getPredicate().getName();
	}

	public String getExpressionTextForItem(int expressionIndex) {
		
		return Inferences.get(expressionIndex).toString();
	}

	public ArrayList<String> getInstantiatedArgumentsForItem(int expressionIndex) {
		
		ArrayList<String> arguments = new ArrayList<String>();

		for (Variable variable : Inferences.get(expressionIndex).getHead().getAtom().getPredicate().getVariables())
			arguments.add(variable.getValue().getName());
		
		return arguments;
	}

}