package cy.ac.ouc.cognition.nestor.lib.reasoning.prudens;

import java.util.ArrayList;
import java.util.Iterator;

import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.TL;
import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.errln;
import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.outln;
import cy.ac.ouc.cognition.nestor.lib.utility.ParameterLib;
import cy.ac.ouc.cognition.nestor.lib.logic.PartType;
import cy.ac.ouc.cognition.nestor.lib.logic.RPartType;
import cy.ac.ouc.cognition.nestor.lib.logic.Conflict;
import cy.ac.ouc.cognition.nestor.lib.logic.Constant;
import cy.ac.ouc.cognition.nestor.lib.logic.ExpressionType;
import cy.ac.ouc.cognition.nestor.lib.logic.Implication;
import cy.ac.ouc.cognition.nestor.lib.logic.LEArgument;
import cy.ac.ouc.cognition.nestor.lib.logic.LEArgumentFunction;
import cy.ac.ouc.cognition.nestor.lib.logic.LogicExpression;
import cy.ac.ouc.cognition.nestor.lib.logic.LogicExpressionWithHead;
import cy.ac.ouc.cognition.nestor.lib.logic.LogicalAnnotation;
import cy.ac.ouc.cognition.nestor.lib.nlp.NLSentence;
import cy.ac.ouc.cognition.nestor.lib.reasoning.IReasoning;
import cy.ac.ouc.cognition.nestor.lib.reasoning.ITranslationPolicy;
import cy.ac.ouc.cognition.nestor.lib.reasoning.TranslationPolicy;
import cy.ac.ouc.cognition.nestor.lib.reasoning.TranslationPolicyException;



class RInt{
    int value;
}


public class PrudensTranslationPolicy extends TranslationPolicy implements ITranslationPolicy {

	private static final int	MAX_GROUPS = ParameterLib.TranslationPolicy_MaximumLayers;
	
	
	private ExpressionType extractExpressionType(Iterator<String> instantiatedArguments, RPartType type) throws TranslationPolicyException {

		// Throw an exception, if there is no variable to read
		if (!instantiatedArguments.hasNext()) {
	        	errln("\tTranslation Policy Rule Type cannot be read!");
	        	throw new TranslationPolicyException("Translation Policy Rule Type cannot be read!");
	    }

		
		String typeString = instantiatedArguments.next();
		
    	String traceMessage = "Translation Policy Rule Type: ";
    	
    	if (typeString.startsWith("head")) {
        	outln(TL, traceMessage + "Head. Will try to infer Implication.");
        	type.value = PartType.HEAD;
        	return ExpressionType.IMPLICATION;
    	}

    	else if (typeString.startsWith("body")) {
        	outln(TL, traceMessage + "Body. Will try to infer Implication.");
        	type.value = PartType.BODY;
        	return ExpressionType.IMPLICATION;
    	}

    	else if (typeString.startsWith("conflict")) {
        	outln(TL, traceMessage + "Conflict. Will try to infer Conflict.");
        	type.value = PartType.HEAD;
        	return ExpressionType.CONFLICT;
    	}

    	else if (typeString.startsWith("constant")) {
        	outln(TL, traceMessage + "Constant. Will try to infer Constant.");
        	type.value = PartType.BODY;
        	return ExpressionType.CONSTANT;
    	}

    	else {
        	errln("\tTranslation Policy Rule Type [" + typeString + "] is undefined!");
        	throw new TranslationPolicyException("Undefined Translation Policy Rule Type: " + typeString);
    	}
		
	}
        
        

	private int extractRuleGroup(Iterator<String> instantiatedArguments) throws TranslationPolicyException {

		// Throw an exception, if there is no variable to read
		if (!instantiatedArguments.hasNext()) {
	        	errln("\tTranslation Policy Rule Group cannot be read!");
	        	throw new TranslationPolicyException("Translation Policy Rule Group cannot be read!");
	    }

		
		String groupString = instantiatedArguments.next();	
		int ruleGroupID;

		try {
			ruleGroupID = Integer.parseInt(groupString);
    		
    		if (ruleGroupID >= MAX_GROUPS)
            	throw new TranslationPolicyException("Group ID greater than maximum number of groups");

        	outln(TL, "Translation Policy Rule Group: " + ruleGroupID);
        	
        	return ruleGroupID;
		}

		catch (Exception e) {
        	throw new TranslationPolicyException("Invalid Translation Policy Rule Group: " + groupString +	" (" + e.getMessage() + ")");
		}
				
	}
        
        

	private boolean extractPredicateName(Iterator<String> instantiatedArguments, ArrayList<String> predicateIdentifier) {
		
		// Stop iteration if we have reached the end, and return that NO arguments follow
		if (!instantiatedArguments.hasNext())
	    	return false;

		String varName = instantiatedArguments.next();

    	
		// Stop iteration if we have found argument separator literal, and return that arguments follow
    	if (varName.equals(ParameterLib.TranslationPolicy_ArgumentSeparator))
	    	return true;

    	// Stop iteration if we have found predicate separator literal, and return that NO arguments follow
    	else if (varName.equals(ParameterLib.TranslationPolicy_PredicateSeparator))
    		return false;

		
    	// If a valid identifier, add this variable as part of the predicate name
    	if (!varName.isBlank() && !varName.equals("_") && !varName.equals("\'\'") && !varName.equals(""))
    		predicateIdentifier.add(varName);

    	
		// Continue with the next variable
    	return extractPredicateName(instantiatedArguments, predicateIdentifier);

	}
	
	

	private int extractArguments(Iterator<String> instantiatedArguments, ArrayList<LEArgument> argumentIdentifiers, int varIndexBase, RInt newVarIndexBase) throws TranslationPolicyException {
		
		// Stop iteration if we have reached the end
		if (!instantiatedArguments.hasNext())
	    	return newVarIndexBase.value;

		String varName = instantiatedArguments.next();

    	
		// Stop iteration if we have found predicate separator literal
    	if (varName.equals(ParameterLib.TranslationPolicy_PredicateSeparator))
	    	return newVarIndexBase.value;

    	
		// If variable is a variable placeholder, try to create target variable and
    	// add it as argument of a predicate
		if (varName.startsWith(ParameterLib.TranslationPolicy_VarPlaceholder)) {
			
			try {
				
				int varIndexStart = ParameterLib.TranslationPolicy_VarPlaceholder.length();
        		int localVarIndex = Integer.parseInt(varName.substring(varIndexStart)) + varIndexBase;
        		
        		if (localVarIndex == varIndexBase)
                	throw new TranslationPolicyException("Invalid Translation Policy Variable PlaceHolder: " + varName + ". (Zero Index");

        		argumentIdentifiers.add(new LEArgument(ParameterLib.TargetPolicy_VariableName + localVarIndex));

        		newVarIndexBase.value = (localVarIndex > newVarIndexBase.value ? localVarIndex : newVarIndexBase.value);
    		}

			catch (Exception e) {
            	throw new TranslationPolicyException("Invalid Translation Policy Variable PlaceHolder: " + varName + ". (" + e.getMessage() + ")");
    		}
    	}

		
		else if (varName.startsWith(ParameterLib.TranslationPolicy_DynamicVarPlaceholder)) {
			
			try {
				
				int varIndexStart = ParameterLib.TranslationPolicy_DynamicVarPlaceholder.length();
        		int localVarIndex = Integer.parseInt(varName.substring(varIndexStart)) + varIndexBase;
        		
        		if (localVarIndex == varIndexBase)
                	throw new TranslationPolicyException("Invalid Translation Policy Dynamic Variable PlaceHolder: " + varName + ". (Zero Index");

        		if (instantiatedArguments.hasNext()) {
        			
        			String varNameRoot = instantiatedArguments.next();

        			argumentIdentifiers.add(new LEArgument(varNameRoot + localVarIndex));

        			newVarIndexBase.value = (localVarIndex > newVarIndexBase.value ? localVarIndex : newVarIndexBase.value);
        		}
        		else
                	errln("Translation Policy Dynamic Variable PlaceHolder Not Followed by an Argument: Ignored");

    		}

			catch (Exception e) {
            	throw new TranslationPolicyException("Invalid Translation Policy Dynamic Variable PlaceHolder: " + varName + ". (" + e.getMessage() + ")");
    		}
    	}

		
		else if (varName.equals("func")) {
			
			try {
				
				LEArgumentFunction funcArgument;			
				String funcVarName;
				
				if (instantiatedArguments.hasNext()) {
					
					funcVarName = instantiatedArguments.next();
					funcArgument = new LEArgumentFunction(funcVarName);
				
	        		while (instantiatedArguments.hasNext()) {
	        			
	        			funcVarName = instantiatedArguments.next();
	
	        			// Stop iteration if we have found end of func definition
	        	    	if (funcVarName.equals("end_func"))
	        	    			break;
	        	    	
	        	    	funcArgument.Function.addArgument(new LEArgument(funcVarName));
	        	    	
	        		}
	        	    	
        	    	argumentIdentifiers.add(funcArgument);

				}
        		else
                	errln("Translation Policy Function Keyword Not Followed by an Argument: Ignored");

    		}

			catch (Exception e) {
            	throw new TranslationPolicyException("Invalid Translation Policy Function Keyword: " + varName + ". (" + e.getMessage() + ")");
    		}
    	}

		
    	// If not universal variable, add this variable as argument of a predicate
		else if (!varName.equals("_"))
    		argumentIdentifiers.add(new LEArgument(varName));

  	
		// Continue with the next variable
    	return extractArguments(instantiatedArguments, argumentIdentifiers, varIndexBase, newVarIndexBase);

	}
	
	
	
	public int addPredicatesToExpression(	Iterator<String> instantiatedArguments,
											int inferredExpressionIndex,
											PartType tpRulePart,
											RInt nextVariableIndexBase,
											LogicExpression expression) throws TranslationPolicyException {

		int predicatesAdded = 0;
		int variableIndexBase = nextVariableIndexBase.value;

		// Extract the first predicate
		ArrayList<String> predicateIdentifier = new ArrayList<String>();
		boolean hasArguments = extractPredicateName(instantiatedArguments, predicateIdentifier);
		
		// There should be at least one valid predicate. If not, throw an exception
		if (predicateIdentifier.isEmpty())
        	throw new TranslationPolicyException(	"Invalid Translation Policy Rule: " + ParameterLib.TranslationPolicy_InferencePredicate +
        											" (Should contain at least one valid predicate.");


		// Extract the arguments of the first predicate, if there are any
		ArrayList<LEArgument> argumentIdentifiers = new ArrayList<LEArgument>();
		if (hasArguments)
			extractArguments(instantiatedArguments, argumentIdentifiers, variableIndexBase, nextVariableIndexBase);

		
		// If the expression should have a head and the type of the translation policy rule is head,
		// set the first predicate extracted as head in the expression
		if (expression.hasHead() && tpRulePart == PartType.HEAD)
			predicatesAdded +=
				((LogicExpressionWithHead) expression).
		    	getHead().
		    	addPredicateConditionally(
		    			predicateIdentifier,
		    			argumentIdentifiers,
		    			((PrudensDeduction)expression.getRuleDeduction()).getMarkedRulesChain(inferredExpressionIndex)
		    	);
		
		// otherwise, set the first predicate extracted as body in the expression
		else
			predicatesAdded +=
				expression.
	        	getBody().
	        	addPredicateConditionally(
	        			predicateIdentifier,
	        			argumentIdentifiers,
		    			((PrudensDeduction)expression.getRuleDeduction()).getMarkedRulesChain(inferredExpressionIndex)
	        	);
	
		
		// Continue with the rest of the predicates, if there are any
		while (instantiatedArguments.hasNext()) {

			// Extract next predicate
        	predicateIdentifier = new ArrayList<String>();
    		hasArguments = extractPredicateName(instantiatedArguments, predicateIdentifier);

    		
    		// Extract arguments of predicate, if there are any
    		argumentIdentifiers = new ArrayList<LEArgument>();
    		if (!predicateIdentifier.isEmpty() && hasArguments)
    			extractArguments(instantiatedArguments, argumentIdentifiers, variableIndexBase, nextVariableIndexBase);

    		
    		predicatesAdded +=
	  			expression.
	        	getBody().
	        	addPredicateConditionally(
	        			predicateIdentifier,
	        			argumentIdentifiers,
		    			((PrudensDeduction)expression.getRuleDeduction()).getMarkedRulesChain(inferredExpressionIndex)
	        	);

       	}

		return predicatesAdded;
		
	}

	
	
	private String generateExpressionTitle(int sentenceIndex, int inferIndex) {
		
		return new String("S" + String.format("%07d", sentenceIndex*100 + inferIndex));

	}
	
	
	
	private LogicExpression createLogicExpressionObject(ExpressionType tpExpressionType, IReasoning prudensAgent) throws TranslationPolicyException {

		LogicExpression expressionToCreate;
		
		if (tpExpressionType == ExpressionType.CONSTANT)

			expressionToCreate = new Constant();
			
		else if (tpExpressionType == ExpressionType.IMPLICATION)
			
			expressionToCreate = new Implication();
		
		else if (tpExpressionType == ExpressionType.CONFLICT)
			
			expressionToCreate = new Conflict();

		else {
        	errln("\tExpression Type [" + tpExpressionType + "] is unsupported!");
        	throw new TranslationPolicyException("Unsupported Translation Policy Rule Type: " + tpExpressionType);
    	}

		// Save marked rules for sentence
		expressionToCreate.setRuleDeduction(new PrudensDeduction());
		((PrudensDeduction) expressionToCreate.getRuleDeduction()).setPrudensAgent(prudensAgent);

		return expressionToCreate;
		
	}
	
	   
    
	private String extractTranslationPolicyVersion(int itemIndex, IReasoning prudensAgent) {
		
		String translationPolicyVersion = "Prudens (Unknown)";
		String ruleHeadPredicateName = prudensAgent.getPredicateNameForItem(itemIndex);
		
        if (ruleHeadPredicateName.equals(ParameterLib.TranslationPolicy_VersionDataPredicate)) {

        	ArrayList<String> arguments = prudensAgent.getInstantiatedArgumentsForItem(itemIndex);

        	if (arguments == null || arguments.isEmpty()) {
    			errln("Error inferring version of Translation Policy: Empty argument list for version predicate!");
            	translationPolicyVersion = "Prudens (Unknown)";    			            		
            	
            	return translationPolicyVersion;
        	}
        	
        	String infoData = prudensAgent.getInstantiatedArgumentsForItem(itemIndex).get(itemIndex);

        	if (infoData.startsWith("\'") && infoData.endsWith("\'"))
	     		translationPolicyVersion = infoData.substring(1, infoData.length()-1);
	    	else
	    		translationPolicyVersion = infoData;
        }
        
        else
        	translationPolicyVersion = "Prudens (Unknown)";    			            		
        	
     
        return translationPolicyVersion;

	}

	
	
	public String inferTranslationPolicyVersion(String translationPolicyString, IReasoning prudensAgent) {
		
		String	translationPolicyVersion = "Prudens (Unknown)";
		String	versionContext = ParameterLib.TranslationPolicy_VersionPredicate;

    	try {
    		
    		prudensAgent.infer(versionContext, translationPolicyString);
    		
    		if (prudensAgent.inferencesListIsEmpty()) {
    			errln("Error inferring version of Translation Policy: Cannot deduct Translation Policy Version!");
            	translationPolicyVersion = "Prudens (Unknown)";
            	
            	return translationPolicyVersion;
    		}
    		
    		translationPolicyVersion = extractTranslationPolicyVersion(0, prudensAgent);       	
        }

    	catch (Exception e) {
	       	errln("Error inferring version of Translation Policy: " + e.getMessage());
	       	translationPolicyVersion = "Load Error";   
	    }
     
        return translationPolicyVersion;

	}

	
	
	public void inferTranslation(String translationPolicyString, NLSentence nlSentence, IReasoning prudensAgent) throws TranslationPolicyException {

		LogicalAnnotation	sentenceAnnotation = nlSentence.getSentenceAnnotation();

		RInt				nextVariableIndexBase = new RInt();
    	int[][]				expressionRegistry = new int[ExpressionType.values().length][MAX_GROUPS];
    	int					inferredExpressions = 0;
    	LogicExpression[]	expressionsToInferCache = new LogicExpression[ExpressionType.values().length];
    	
    	ExpressionType		singleExpressionTypeDecided = ExpressionType.UNDEFINED;
    	

    	try {
    		
			// Check if sentence logical annotation is complete
			if (!sentenceAnnotation.isComplete()) {
	    		errln("Error inferring from Translation Policy: Sentence Logical Annotations are not ready!");
	        	throw new TranslationPolicyException("Error inferring from Translation Policy: Sentence Logical Annotations are not ready!");
			}
	
			prudensAgent.infer(sentenceAnnotation.getTextRepresentation(), translationPolicyString);
			
			if (prudensAgent.inferencesListIsEmpty()) {
	    		errln("Error inferring from Translation Policy: No inferences deducted!");
	        	throw new TranslationPolicyException("Error inferring from Translation Policy:  No inferences deducted!");
			}
			
	   	
	    	// Iterate through all inferences
	       	outln(TL, "Iterate through Inferred Expressions for Sentence [" + nlSentence.getText() + "]:");
	
	       	nextVariableIndexBase.value = 0;
	
	       	for (int i=0; i < prudensAgent.numberOfInferences(); i++) {
	        	
	        	outln(TL, "");
	            outln(TL, "Translation Policy Rule Name=[" + prudensAgent.getNameForItem(i) + "]");
		        outln(TL, "Translation Policy Rule=[" + prudensAgent.getExpressionTextForItem(i) + "]");
	
	            // If this is a rule that has inference predicate in its head, then it is an inference candidate
		        String ruleHeadPredicateName = prudensAgent.getPredicateNameForItem(i);
	            if (ruleHeadPredicateName.equals(ParameterLib.TranslationPolicy_InferencePredicate)) {
	
	            	outln(TL, "Translation Policy Rule candidate for inference!");
	
	        		Iterator<String> instantiatedArguments = prudensAgent.getInstantiatedArgumentsForItem(i).iterator();
	        		RPartType tpPartType = new RPartType();
	
	        		// Extract the translation policy type
	        		ExpressionType tpExpressionType = extractExpressionType(instantiatedArguments, tpPartType);
	        		
	        		// Extract the group number of this rule
	        		int ruleGroupID = extractRuleGroup(instantiatedArguments);
	
	        		expressionRegistry[tpExpressionType.ordinal()][ruleGroupID]++;
	
	
	        		// Configure the correct objects according to expression type
	        		LogicExpression		expressionToInfer = createLogicExpressionObject(tpExpressionType, prudensAgent);
	       		
	        		// For expressions that are allowed to be inferred only once for each sentence (!isMultiExpression),
	        		// IMPLICATION, CONFLICT
	        		// For expressions that are allowed to be inferred multiple times for each sentence, new object is always used
	        		// CONSTANT
	        		if (!expressionToInfer.isMultiExpression()) {
	        			
	            		// If the relevant object has been created before (expressionToInferCache[X] != null)
	            		// use the object previously created
	        			if (expressionsToInferCache[tpExpressionType.ordinal()] != null)
	        				expressionToInfer = expressionsToInferCache[tpExpressionType.ordinal()];
	        			
	        			// otherwise use the newly created object and save it to cache
	        			else
	        				expressionsToInferCache[tpExpressionType.ordinal()] = expressionToInfer;
	
	            		// Only one single expression type can be inferred per sentence. Choose the type of the one first appeared
	            		if (singleExpressionTypeDecided == ExpressionType.UNDEFINED)
	            			singleExpressionTypeDecided = expressionToInfer.getTypeOfExpression();
	        				
	        		}
	        			       		
	       			
	        		// If multiple expressions of the expression type to be inferred are allowed to be inferred for each sentence
	        		// or if this is the strongest translation policy rule for this group of rules (and not multiple expressions allowed)
	        		// and of the expression type decided
	        		if (	expressionToInfer.isMultiExpression() ||
	        				(	expressionRegistry[tpExpressionType.ordinal()][ruleGroupID] <= 1 &&
	        					singleExpressionTypeDecided == expressionToInfer.getTypeOfExpression()
	        				)
	        			) {
	        			
	        			int predicatesAdded =
		        		addPredicatesToExpression(	instantiatedArguments,
		        									i,
		            								tpPartType.value,
		            								nextVariableIndexBase,
		            								expressionToInfer);
	        			
	        			if (predicatesAdded > 0)
	        				((PrudensDeduction) expressionToInfer.getRuleDeduction()).addToUsedMetaRules(i);
	        			
	        			else
	        				expressionRegistry[tpExpressionType.ordinal()][ruleGroupID]--;
	
	        		}
	
	        		
	        		// If multiple expressions of the expression type to be inferred are allowed to be inferred for each sentence
	        		// and predicates have been actually added to the expression
	        		// then add this expression to the list of inferred expressions for this sentence
	        		if (expressionToInfer.isMultiExpression() && expressionToInfer.arePredicatesAdded()) {
	
	        			expressionToInfer.setComplete(true);
	        			expressionToInfer.setTitle(generateExpressionTitle(nlSentence.getIndexInDocument(), inferredExpressions++));
	
	        			nlSentence.addInferredExpression(expressionToInfer);
	
	                	outln(TL, tpExpressionType + " Successfully Inferred and Added to Sentence.");
	            	    outln(TL, "");
	    	    	    outln(TL, "Inference Deduction Rules Chain for expression [" + expressionToInfer.getTitle() + "]");
	    	    	    outln(TL, expressionToInfer.getRuleDeduction().getMarkedRulesChainText());
	    	    	    outln(TL, "Inference Marked Rules for expression [" + expressionToInfer.getTitle() + "]");
	    	    	    outln(TL, expressionToInfer.getRuleDeduction().getMarkedRulesText());
	        		}
	
	            }
	                       
	        }
	
	
			// Add the expression of the type of which only one expression can exist for each sentence
	       	// and have been inferred to the list of inferred expressions for this sentence
	       	LogicExpression expressionToInfer = expressionsToInferCache[singleExpressionTypeDecided.ordinal()];
	       	
	   		if (expressionToInfer != null && expressionToInfer.arePredicatesAdded()) {
	
	   			expressionToInfer.setComplete(true);
				expressionToInfer.setTitle(generateExpressionTitle(nlSentence.getIndexInDocument(), inferredExpressions++));
	
				nlSentence.addInferredExpression(expressionToInfer);
	
	        	outln(TL, expressionToInfer.getTypeOfExpression() + " Successfully Inferred and Added to Sentence.");
	    	    outln(TL, "");
	    	    outln(TL, "Inference Deduction Rules Chain for expression [" + expressionToInfer.getTitle() + "]");
	    	    outln(TL, expressionToInfer.getRuleDeduction().getMarkedRulesChainText());
	    	    outln(TL, "Inference Marked Rules for expression [" + expressionToInfer.getTitle() + "]");
	    	    outln(TL, expressionToInfer.getRuleDeduction().getMarkedRulesText());
			}
    	}
    	
    	catch (TranslationPolicyException tpe) {
    		throw tpe;
    	}

    	catch (Exception e) {
    		errln("Error inferring from Translation Policy: " + e.getMessage());
        	throw new TranslationPolicyException("Error inferring from Translation Policy: " + e.getMessage());   		
    	}
            	                            	
	}
   	        
	
} // End Class
