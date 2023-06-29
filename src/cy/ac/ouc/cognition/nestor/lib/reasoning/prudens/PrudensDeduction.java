package cy.ac.ouc.cognition.nestor.lib.reasoning.prudens;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONPropertyIgnore;

import coaching.Literal;
import coaching.Rule;
import cy.ac.ouc.cognition.nestor.lib.reasoning.Deduction;
import cy.ac.ouc.cognition.nestor.lib.reasoning.DisplayNode;
import cy.ac.ouc.cognition.nestor.lib.reasoning.IReasoning;
import cy.ac.ouc.cognition.nestor.lib.reasoning.IReasoning.Version;

public class PrudensDeduction extends Deduction {

	IReasoning							PrudensAgent;
	private ArrayList<Rule>				PrudensMarkedRules;
	private ArrayList<Rule>				InferenceMarkedRules;

	private	ArrayList<DisplayNode>		MarkedRules;
	private	ArrayList<DisplayNode>		MarkedRulesChain;

	private boolean						PrudensMarkedRulesSet = false;
	private boolean						PrudensAgentSet = false;
	private boolean						MarkedRulesBuilt = false;
	private boolean						MarkedRulesChainBuilt = false;
	

	PrudensDeduction() {
		InferenceMarkedRules = new ArrayList<Rule>();
		MarkedRulesChain = new ArrayList<DisplayNode>();
		MarkedRules = new ArrayList<DisplayNode>();
	}

	
	/**
	 * @return the prudensMarkedRules
	 */
	@JSONPropertyIgnore
	public ArrayList<Rule> getPrudensMarkedRules() {
		return PrudensMarkedRules;
	}

	
	/**
	 * @return the prudensAgent
	 */
	@JSONPropertyIgnore
	public IReasoning getPrudensAgent() {
		return PrudensAgent;
	}



	/**
	 * @param prudensAgent the prudensAgent to set
	 */
	@JSONPropertyIgnore
	public void setPrudensAgent(IReasoning prudensAgent) {
		PrudensAgent = prudensAgent;
		if (PrudensAgent.getAgentVersion() == Version.PRUDENSJAVA) {
			PrudensMarkedRules = ((PrudensJava) PrudensAgent).getInferences();
			PrudensMarkedRulesSet = true;
		}
			
		PrudensAgentSet = true;
	}

	

	private void buildMarkedRulesChain(Iterator<Rule> inferenceMarkedRules, ArrayList<DisplayNode> markedRulesChain) {
		
		if (!inferenceMarkedRules.hasNext())
	    	return;

		
		Rule inferenceRule = inferenceMarkedRules.next();

		DisplayNode newDisplayRuleNode = new DisplayNode(inferenceRule.getName(), inferenceRule.getHead().toString(), inferenceRule.getBody().toString()
														);
		markedRulesChain.add(newDisplayRuleNode);

		
		ArrayList<Rule> bodyMarkedRules = new ArrayList<Rule>();

		// Search for marked rules from body predicates
		for (Literal bodyLiteral : inferenceRule.getBody())
			for (Rule rule : PrudensMarkedRules)
				if (bodyLiteral.coincidesWith(rule.getHead(), true))
					bodyMarkedRules.add(rule);
		
		// Build deduction list for marked rules from body predicates, if there are any
		// if (bodyMarkedRules.size() > 0) // Should be OK without this because recursion ends when there is no data in list
			buildMarkedRulesChain(bodyMarkedRules.iterator(), newDisplayRuleNode.ChainedMarkedRules);

		// Continue with the next rule
		buildMarkedRulesChain(inferenceMarkedRules, markedRulesChain);

	}
	
	
//	public ArrayList<DisplayNode> getMarkedRulesChain(Rule markedRule) {
//
//		ArrayList<DisplayNode> markedRuleChain = new ArrayList<DisplayNode>();
//		
//		if (PrudensMarkedRulesSet) {
//			
//			ArrayList<Rule> ruleArray = new ArrayList<Rule>();
//			ruleArray.add(markedRule);
//			
//			buildMarkedRulesChain(ruleArray.iterator(), markedRuleChain);
//		
//		}
//		
//		return markedRuleChain;
//
//	}

	
	public ArrayList<DisplayNode> getMarkedRulesChain(int inferredExpressionIndex) {

		ArrayList<DisplayNode> markedRuleChain = new ArrayList<DisplayNode>();
		
		if (PrudensMarkedRulesSet) {
			
			ArrayList<Rule> ruleArray = new ArrayList<Rule>();
			if (PrudensAgentSet && PrudensAgent.getAgentVersion() == Version.PRUDENSJAVA)
				ruleArray.add((Rule)PrudensAgent.getInference(inferredExpressionIndex));
			
			buildMarkedRulesChain(ruleArray.iterator(), markedRuleChain);
		
		}
		
		return markedRuleChain;

	}

	
	public ArrayList<DisplayNode> getMarkedRulesChain() {

		if (PrudensMarkedRulesSet) {
			
			if (!MarkedRulesChainBuilt) {
				buildMarkedRulesChain(InferenceMarkedRules.iterator(), MarkedRulesChain);
				MarkedRulesChainBuilt = true;
			}
		
			return MarkedRulesChain;
		}
		
		return new ArrayList<DisplayNode>();

	}

	
	private void buildMarkedRules(Iterator<Rule> prudensMarkedRules, ArrayList<DisplayNode> markedRules) {
		
		if (!prudensMarkedRules.hasNext())
	    	return;

		
		Rule inferenceRule = prudensMarkedRules.next();

		DisplayNode newDisplayRuleNode = new DisplayNode(inferenceRule.getName(), inferenceRule.getHead().toString(), inferenceRule.getBody().toString());
		markedRules.add(newDisplayRuleNode);

		
		// Continue with the next rule
		buildMarkedRules(prudensMarkedRules, markedRules);

	}
	
	
	public ArrayList<DisplayNode> getMarkedRules() {

		if (PrudensMarkedRulesSet) {
			
			if (!MarkedRulesBuilt) {
				buildMarkedRules(PrudensMarkedRules.iterator(), MarkedRules);
				MarkedRulesBuilt = true;
			}
		
			return MarkedRules;
		}
		
		return new ArrayList<DisplayNode>();

	}

	
	@JSONPropertyIgnore
    public String getMarkedRulesChainText() {
	
    	getMarkedRulesChain();
    	
    	String markedRulesChainString = "";
    	
		for (DisplayNode displayRule : MarkedRulesChain)
			markedRulesChainString += displayRule.buildText(0);
		
		return markedRulesChainString;

    }

	
	@JSONPropertyIgnore
    public String getMarkedRulesText() {
    	
    	getMarkedRules();
    	
    	String markedRulesString = "";
    	
		for (DisplayNode displayRule : MarkedRules)
			markedRulesString += displayRule.buildText(0);
		
		return markedRulesString;

    }

	
//    public String toJSONString() {
//    	  	
//    	String displayRuleJSONString = "";
//    	
//    	getMarkedRulesChain();
//		for (DisplayNode displayRule : MarkedRulesChain)
//			displayRuleJSONString += displayRule.toJSONString();
//		displayRuleJSONString += '\n';
//		
//    	getMarkedRules();
//		for (DisplayNode displayRule : MarkedRules)
//			displayRuleJSONString += displayRule.toJSONString();
//		
//		return displayRuleJSONString;
//
//    }
//
//	
	public void addToUsedMetaRules(int inferredExpressionIndex) {
		
		if (PrudensAgentSet && PrudensAgent.getAgentVersion() == Version.PRUDENSJAVA) {

			Rule inferenceRule = (Rule) PrudensAgent.getInference(inferredExpressionIndex);

			for (Rule rule : InferenceMarkedRules)
				if (inferenceRule.getHead().coincidesWith(rule.getHead(), true))
					return;
	
			InferenceMarkedRules.add(inferenceRule);
		}
	}

}
