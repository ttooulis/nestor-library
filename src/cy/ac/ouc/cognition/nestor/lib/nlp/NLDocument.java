package cy.ac.ouc.cognition.nestor.lib.nlp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONPropertyIgnore;

import cy.ac.ouc.cognition.nestor.lib.logic.LogicExpression;
import cy.ac.ouc.cognition.nestor.lib.logic.ExpressionType;

public class NLDocument extends NLElement {

	private int							CurrentSentenceIndex = 0;
	private Object						NLPData;
	private List<Coreference>			Coreferences;
	private List<NLSentence>			DocumentSentences;
	private List<LogicExpression>		LogicRepresentation;
	
	
	public NLDocument(String documentText) {
		super(documentText);
		Coreferences = new ArrayList<>();
		DocumentSentences = new ArrayList<>();
		LogicRepresentation = new ArrayList<>();
	}
	
	
	
	public void addSentence(NLSentence sentence) {
		CurrentSentenceIndex++;
		sentence.setIndexInDocument(CurrentSentenceIndex);
		sentence.setDocument(this);
		DocumentSentences.add(sentence);
		Complete = false;
	}

	
	
	public void addCoreference(Coreference coreference) {
		// Add this coreference relation in the list of coreferences for this sentence
		Coreferences.add(coreference);
		Complete = false;
	}
	

	public void addCoreference(String coreferenceName, String repMention, String mention, int mentionIndex, int sentenceIndex) {
		this.addCoreference(new Coreference(coreferenceName, repMention, mention, mentionIndex, sentenceIndex));
	}
	

	@JSONPropertyIgnore
	public boolean coreferenceAdded(String mention, int mentionIndex, int sentenceIndex) {
		for (Coreference coref : Coreferences)
			if (coref.getMention().equals(mention) &&
				coref.getMentionIndex() == mentionIndex &&
				coref.getSentenceIndex() == sentenceIndex)
				
				return true;
		
		return false;
	}

	
	
	public void addToLogicRepresentation(LogicExpression inferredExpression) {
		LogicRepresentation.add(inferredExpression);
	}

	
	
	@JSONPropertyIgnore
	public boolean logicRepresentationContains(LogicExpression expressionToCheck) {
		
		// Iterate through all expressions of LogicRepresentation list
		for (LogicExpression logicExpression : this.LogicRepresentation) {
			
			// If the type of the expressions is not the same, then surely
			// they are not the same and continue to the next one
			// IMPORTANT: This check makes sure no exception is thrown by method sameAs
			// CID: Should improve sameAs to catch these exceptions and just return false
			if (logicExpression.getTypeOfExpression() != expressionToCheck.getTypeOfExpression())
				continue;
		
			// If an expression is the same as the checked expression, then
			// it means LogicRepresentation contains this expression and return true
			if (logicExpression.sameAs(expressionToCheck, false))					
					return true;
		}

		// If this point is reached, then the checked expression is not contained
		return false;

	}



	@JSONPropertyIgnore
	public boolean rebuildLogicRepresentation() {
		
		if (this.isComplete()) {

			LogicRepresentation.clear();

			for (NLSentence nlSentence : DocumentSentences)

				if (nlSentence.anExpressionIsInferred())

					for (LogicExpression inferredExpression : nlSentence.getInferredExpressions())
						
						if (inferredExpression.getTypeOfExpression() != ExpressionType.CONSTANT)
							LogicRepresentation.add(inferredExpression);
			
						else if (!this.logicRepresentationContains(inferredExpression))
							LogicRepresentation.add(inferredExpression);						
							
						
			return true;
		}

		return false;

	}
	
	
	
	
	/**
	 * @return the NLPData
	 */
	@JSONPropertyIgnore
	public Object getNLPData() {
		return NLPData;
	}


	/**
	 * @param NLPData the NLPData to set
	 */
	@JSONPropertyIgnore
	public void setNLPData(Object nlpData) {
		NLPData = nlpData;
	}

	
	/**
	 * @return the coreferences
	 */
	public List<Coreference> getCoreferences() {
		return Coreferences;
	}



	/**
	 * @return the documentSentences
	 */
	public List<NLSentence> getDocumentSentences() {
		return DocumentSentences;
	}



	/**
	 * @return the logicRepresentation
	 */
	@JSONPropertyIgnore
	public List<LogicExpression> getLogicRepresentation() {
		return LogicRepresentation;
	}

	
    public String toJSONString() {
    	
		return new JSONObject(this).toString();

    }

	
}
