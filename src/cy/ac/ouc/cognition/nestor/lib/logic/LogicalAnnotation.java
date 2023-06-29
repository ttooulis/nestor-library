package cy.ac.ouc.cognition.nestor.lib.logic;

import java.util.ArrayList;
import java.util.List;

import cy.ac.ouc.cognition.nestor.lib.utility.ParameterLib;
import cy.ac.ouc.cognition.nestor.lib.nlp.Coreference;
import cy.ac.ouc.cognition.nestor.lib.nlp.Dependency;
import cy.ac.ouc.cognition.nestor.lib.nlp.NLToken;

public class LogicalAnnotation extends PredicateSet {
	
	private static String 		ls = ParameterLib.NESTOR_LineSeperator;


	static enum AnnotationType {
		DEPNDENCY, POS, NER, TOKEN, UNDEFINED;
	}

	public LogicalAnnotation() {
		super();
		Part = PartType.LOGICAL_ANNOTATION;
		Delimiter = ";" + ls;
	}
	
	
	public void addAnnotation(int sentenceIndex) {

		List<LEArgument>	Arguments = new ArrayList<LEArgument>();
		
		Arguments.add(new LEArgument(Integer.toString(sentenceIndex)));
		
		this.addPredicate(
			new LEPredicate(
				"sentence_index",
				Arguments
		));

	}
	
	void addAnnotation(AnnotationType type, String nlTokenName, String posTag, String ner, String wordLemma, int wordIndex) {

		List<LEArgument>	Arguments = new ArrayList<LEArgument>();
		
		Arguments.add(new LEArgument(nlTokenName.toLowerCase()));
		Arguments.add(new LEArgument(posTag.toLowerCase()));
		String nerTag = "nner";
		if (!ner.equals("O"))
			nerTag = "ner";
		Arguments.add(new LEArgument(nerTag));
		Arguments.add(new LEArgument(ner.toLowerCase()));
		Arguments.add(new LEArgument(wordLemma.toLowerCase()));
		Arguments.add(new LEArgument(Integer.toString(wordIndex)));
		
		this.addPredicate(
			new LEPredicate(
				type.name().toLowerCase(),
				Arguments
		));

	}

	void addAnnotation(AnnotationType type, String tag, String wordLemma, int wordIndex) {

		List<LEArgument>	Arguments = new ArrayList<LEArgument>();
		
		Arguments.add(new LEArgument(tag.toLowerCase()));
		Arguments.add(new LEArgument(wordLemma.toLowerCase()));
		Arguments.add(new LEArgument(Integer.toString(wordIndex)));
		
		this.addPredicate(
			new LEPredicate(
				type.name().toLowerCase(),
				Arguments
		));

	}

	public void addAnnotation(NLToken nlToken) {

		String posTag = nlToken.getTag().toLowerCase();
		
		if (!posTag.equals(".") && !posTag.equals(",") && !posTag.equals("!") && !posTag.equals("?")) {
		
			if (ParameterLib.TranslationPolicy_PredicateMode == 0) {
				
				this.addAnnotation(
						AnnotationType.TOKEN,
						nlToken.getText(),
						nlToken.getTag(),
						nlToken.getNER(),
						nlToken.getLemma(),
						nlToken.getIndex()
				);
				
			}
			else {
				
				this.addAnnotation(
						AnnotationType.POS,
						nlToken.getTag(),
						nlToken.getLemma(),
						nlToken.getIndex()
				);
				
				if (!nlToken.getNER().equals("O"))
					
					this.addAnnotation(
							AnnotationType.NER,
							nlToken.getNER(),
							nlToken.getLemma(),
							nlToken.getIndex()
					);
			}
		}
	}


	void addAnnotation(String dependencyName, String governorName, int governorIndex, String dependentName, int dependentIndex) {

		List<LEArgument>	Arguments = new ArrayList<LEArgument>();
		
		Arguments.add(new LEArgument(governorName.toLowerCase()));
		Arguments.add(new LEArgument(Integer.toString(governorIndex)));
		Arguments.add(new LEArgument(dependentName.toLowerCase()));
		Arguments.add(new LEArgument(Integer.toString(dependentIndex)));
		
		this.addPredicate(
			new LEPredicate(
				dependencyName.toLowerCase(),
				Arguments
		));

	}

	public void addAnnotation(Dependency nlDependency) {

		String dependencyName = nlDependency.getRelationName().toLowerCase();

		if (dependencyName.equals("punct"))
			return;
		
		String governorName = nlDependency.getGovernor().getLemma().toLowerCase();
		if (governorName.equals(".") || governorName.equals(",") || governorName.equals("!") || governorName.equals("?"))
			governorName = "punct_symbol";

		String dependentName = nlDependency.getDependent().getLemma().toLowerCase();
		if (dependentName.equals(".") || dependentName.equals(",") || dependentName.equals("!") || dependentName.equals("?"))
			dependentName = "punct_symbol";

		this.addAnnotation(
				nlDependency.getRelationName(),
				governorName,
				nlDependency.getGovernor().getIndex(),
				dependentName,
				nlDependency.getDependent().getIndex()
		);

	}

	
	void addAnnotation(String coreferenceName, String repMention, String mention, int mentionIndex, int sentenceIndex) {

		List<LEArgument>	Arguments = new ArrayList<LEArgument>();
		
		Arguments.add(new LEArgument(repMention.toLowerCase()));
		Arguments.add(new LEArgument(mention.toLowerCase()));
		Arguments.add(new LEArgument(Integer.toString(mentionIndex)));
		Arguments.add(new LEArgument(Integer.toString(sentenceIndex)));
		
		this.addPredicate(
			new LEPredicate(
				coreferenceName.toLowerCase(),
				Arguments
		));

	}


	public void addAnnotation(Coreference nlCoreference) {
		this.addAnnotation(
				nlCoreference.getRelationName(),
				nlCoreference.getRepMention(), 
				nlCoreference.getMention(),
				nlCoreference.getMentionIndex(),
				nlCoreference.getSentenceIndex()
		);
	}
	
	
	
	protected String buildTextRepresentation() {

 		String textRepresentation = super.buildTextRepresentation();

	    return textRepresentation + Delimiter;
	}

}
