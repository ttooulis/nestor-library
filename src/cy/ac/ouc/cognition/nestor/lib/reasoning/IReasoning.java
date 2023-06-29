package cy.ac.ouc.cognition.nestor.lib.reasoning;

import java.util.ArrayList;

import cy.ac.ouc.cognition.nestor.lib.base.INESTORInterface;


public interface IReasoning extends INESTORInterface {
	
	static enum Version {
		PRUDENSJS,
		PRUDENSJAVA,
		UNDEFINED;
	}


	public void infer(String logicalAnnotationText, String translationPolicyString) throws ReasoningException;
    public boolean inferencesListIsEmpty();
    public int numberOfInferences();
	public Version getAgentVersion();
	public Object getInference(int expressionIndex);
	public String getNameForItem(int expressionIndex);
	public String getPredicateNameForItem(int expressionIndex);
	public String getExpressionTextForItem(int expressionIndex);
	public ArrayList<String> getInstantiatedArgumentsForItem(int expressionIndex);

}
