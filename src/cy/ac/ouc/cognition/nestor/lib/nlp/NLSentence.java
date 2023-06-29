package cy.ac.ouc.cognition.nestor.lib.nlp;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONPropertyIgnore;

import cy.ac.ouc.cognition.nestor.lib.logic.LogicExpression;
import cy.ac.ouc.cognition.nestor.lib.logic.LogicalAnnotation;


public class NLSentence extends NLElement {

	private int							IndexInDocument;
	private NLDocument					Document;
	
	private Object						NLPData;
	
	private HashMap<Integer, NLToken>	Tokens;
	private List<Dependency>			Dependencies;
	private List<Coreference>			Coreferences;
	private LogicalAnnotation			SentenceAnnotation;
	private List<LogicExpression>		InferredExpressions;
	
	boolean								PredicatesGenerated;
	boolean								AnExpressionIsInferred;
	
	
	public NLSentence(String sentenceText) {
		this(sentenceText, 0);
	}
	
	public NLSentence(String sentenceText, int indexInDocument) {
		super(sentenceText);
		IndexInDocument = indexInDocument;
		Tokens = new HashMap<Integer, NLToken>();
		Dependencies = new ArrayList<>();
		Coreferences = new ArrayList<>();
		SentenceAnnotation = new LogicalAnnotation();
		InferredExpressions = new ArrayList<>();
		PredicatesGenerated = false;
		AnExpressionIsInferred = false;
	}
	
	
	
	public void addToken(int index, NLToken nlpToken) {
		Tokens.put(index, nlpToken);
		PredicatesGenerated = false;
		AnExpressionIsInferred = false;
		Complete = false;
	}
	
	public void addToken(int index, String originalText, String lemma, String ner, String tag) {
		this.addToken(index, new NLToken(index, originalText, lemma, ner, tag));
	}
	

	
	public void addDependency(Dependency dependency) {
		// Add this dependency relation in the list of dependencies for this sentence
		Dependencies.add(dependency);

		// Create a dependency relation tree between tokens
		dependency.getGovernor().addDependant(dependency.getRelationName(), dependency.getDependent());

		PredicatesGenerated = false;
		AnExpressionIsInferred = false;
		Complete = false;
	}
	
	public void addDependency(String dependencyName, NLToken governor, NLToken dependent) {
		this.addDependency(new Dependency(	dependencyName,
												governor,
												dependent));
	}
	
	public void addDependency(String dependencyName, int governorIndex, int dependentIndex) {
		this.addDependency(dependencyName, Tokens.get(governorIndex), Tokens.get(dependentIndex));
	}
	
	
	
	public void addCoreference(Coreference coreference) {
		// Add this coreference relation in the list of coreferences for this sentence
		Coreferences.add(coreference);

		PredicatesGenerated = false;
		AnExpressionIsInferred = false;
		Complete = false;
	}
	
	public void addCoreference(String coreferenceName, String repMention, String mention, int mentionIndex, int sentenceIndex) {
		this.addCoreference(new Coreference(coreferenceName, repMention, mention, mentionIndex, sentenceIndex));
	}
	
	
	
	public void buildLogicalAnnotation() {
		
		if (Complete) {

			SentenceAnnotation = new LogicalAnnotation();
			
			/* Add a predicate for the sentence index */
			SentenceAnnotation.addAnnotation(IndexInDocument);

			/* Add Document Coreference predicates */
			for (Coreference nlCoreference : Document.getCoreferences()) 
				SentenceAnnotation.addAnnotation(nlCoreference);

			/* Add NLToken predicates */
			for (NLToken nlToken : Tokens.values())
				SentenceAnnotation.addAnnotation(nlToken);			

			/* Add Dependency predicates */
			for (Dependency nlDependency : Dependencies) 
				SentenceAnnotation.addAnnotation(nlDependency);

			/* Add Sentence Coreference predicates */
			for (Coreference nlCoreference : Coreferences) 
				SentenceAnnotation.addAnnotation(nlCoreference);

		}
		
		SentenceAnnotation.setComplete(true);
		PredicatesGenerated = true;

	}

	
	/**
	 * @return the indexInDocument
	 */
	public int getIndexInDocument() {
		return IndexInDocument;
	}



	/**
	 * @param indexInDocument the indexInDocument to set
	 */
	@JSONPropertyIgnore
	public void setIndexInDocument(int indexInDocument) {
		IndexInDocument = indexInDocument;
	}

	/**
	 * @return the document
	 */
	@JSONPropertyIgnore
	public NLDocument getDocument() {
		return Document;
	}
	

	/**
	 * @param document the document to set
	 */
	@JSONPropertyIgnore
	public void setDocument(NLDocument document) {
		Document = document;
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
	 * @return the sentenceAnnotation
	 */
	public LogicalAnnotation getSentenceAnnotation() {
		return SentenceAnnotation;
	}

	
	@JSONPropertyIgnore
	public void addInferredExpression(LogicExpression inferredExpression) {
		
		InferredExpressions.add(inferredExpression);
		
		// Expressions that are not considered to be global for a document (like a CONSTANT)
		// add them into document logic representation list
		if (!inferredExpression.hasDocumentScope())
			Document.addToLogicRepresentation(inferredExpression);

		// Expressions that are considered to be global should be added only once
		else if (!Document.logicRepresentationContains(inferredExpression))
			Document.addToLogicRepresentation(inferredExpression);						

		AnExpressionIsInferred = true;
		
	}

	/**
	 * @return the inferredExpressions
	 */
	@JSONPropertyIgnore
	public List<LogicExpression> getInferredExpressions() {
		return InferredExpressions;
	}

	@JSONPropertyIgnore
	public boolean anExpressionIsInferred( ) {
		return AnExpressionIsInferred;
	}


	
	/**
	 * @return the tokens
	 */
	public  HashMap<Integer, NLToken> getTokens() {
		return Tokens;
	}


	
	/**
	 * @return the dependencies
	 */
	@JSONPropertyIgnore
	public List<Dependency> getDependencies() {
		return Dependencies;
	}

	
	
	/**
	 * @return the coreferences
	 */
	public List<Coreference> getCoreferences() {
		return Coreferences;
	}


    public String toJSONString() {
    	
		return new JSONObject(this).toString();

    }

	

}
