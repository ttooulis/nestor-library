package cy.ac.ouc.cognition.nestor.lib.pipeline;

import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.errln;
import cy.ac.ouc.cognition.nestor.lib.base.NESTORException;
import cy.ac.ouc.cognition.nestor.lib.base.NESTORThing;
import cy.ac.ouc.cognition.nestor.lib.utility.ParameterLib;
import cy.ac.ouc.cognition.nestor.lib.logic.LogicExpression;
import cy.ac.ouc.cognition.nestor.lib.nlp.NLDocument;
import cy.ac.ouc.cognition.nestor.lib.nlp.NLSentence;
import cy.ac.ouc.cognition.nestor.lib.nlp.INLProcessor;
import cy.ac.ouc.cognition.nestor.lib.nlp.corenlp.StanfordCoreNLProcessor;
import cy.ac.ouc.cognition.nestor.lib.reasoning.ITranslationPolicy;
import cy.ac.ouc.cognition.nestor.lib.reasoning.TranslationPolicyException;
import cy.ac.ouc.cognition.nestor.lib.reasoning.prudens.PrudensJS;
import cy.ac.ouc.cognition.nestor.lib.reasoning.prudens.PrudensJava;
import cy.ac.ouc.cognition.nestor.lib.reasoning.prudens.PrudensTranslationPolicy;

public class Pipeline extends NESTORThing {
	
	private static String 		ls = ParameterLib.NESTOR_LineSeperator;
	
	private INLProcessor		NLDocumentProcessor;
	private NLDocument			ProcessedDocument;
	private ITranslationPolicy	ActiveTranslationPolicy;
	
	private boolean				NLProcessorSet = false;
	private boolean				TranslationPolicySet = false;


	public Pipeline() throws NESTORException {
		
		ParameterLib.Load("NESTOR.xml");
		setNLProcessor();
		setTranslationPolicy();
		
	}


	
	private void setNLProcessor() {

		if (ParameterLib.NESTORPipeline_NLProcessor == "corenlp") {
			NLDocumentProcessor = new StanfordCoreNLProcessor();
			NLProcessorSet = true;
		}

		else {
        	errln(ParameterLib.NESTORPipeline_NLProcessor + " type is not supported for natural language processing");
        	NLProcessorSet = false;
		}

	}



	public void loadNLProcessor() {

		if (NLProcessorSet)
			try {
				NLDocumentProcessor.load();
			}
		
			catch (Exception e) {
	        	errln(" Natural language processor error: " + e.getMessage());
	        	NLDocumentProcessor.setLoaded(false);			
			}

		else {
        	errln(ParameterLib.NESTORPipeline_NLProcessor + " natural language processor is not set correctly!");
        	NLDocumentProcessor.setLoaded(false);
		}

	}



	public void resetDocument() {

		ProcessedDocument = null;

		if (NLProcessorSet && NLDocumentProcessor.isLoaded())
			NLDocumentProcessor.resetNLDocument();

	}



	public void unloadNLProcessor() {

		resetDocument();

		if (NLProcessorSet && NLDocumentProcessor.isLoaded())
			NLDocumentProcessor.unload();;

	}
	


	public void resetNLProcessor() {

		unloadNLProcessor();
		setNLProcessor();

	}
	


	private void setTranslationPolicy() {

		if (ParameterLib.NESTORPipeline_TranslationPolicyType.startsWith("prudens")) {
			ActiveTranslationPolicy = new PrudensTranslationPolicy();
			TranslationPolicySet = true;
		}

		else
        	errln(ParameterLib.NESTORPipeline_TranslationPolicyType + " type is not supported for Translation Policy");

	}



	public String getTranslationPolicyVersion(String translationPolicyString) {

		if (TranslationPolicySet)
			if (ParameterLib.NESTORPipeline_TranslationPolicyType.equals("prudens-java"))
				return ActiveTranslationPolicy.inferTranslationPolicyVersion(translationPolicyString, new PrudensJava());

			else if (ParameterLib.NESTORPipeline_TranslationPolicyType.equals("prudensjs-web"))
				return ActiveTranslationPolicy.inferTranslationPolicyVersion(translationPolicyString, new PrudensJS());
			
			else {
				errln("Translation Policy Error: [" + ParameterLib.NESTORPipeline_TranslationPolicyType + "] Unknown Translation Policy Type");
				return "Unknown Translation Policy Type";
			}

		return "Translation Policy Not Set";

	}


        
        
	public void processNL(String nlText) {
		
		if (NLProcessorSet && NLDocumentProcessor.isLoaded()) {
			resetDocument();
			ProcessedDocument = NLDocumentProcessor.annotateDocument(nlText);
		}

		else
        	errln("Cannot do NLP on document. " + ParameterLib.NESTORPipeline_NLProcessor + " NL processor not set or not loaded!");

	}



	public String getParseData() {

		if (NLProcessorSet && NLDocumentProcessor.isAnnotated())
			return NLDocumentProcessor.generateParseData(ProcessedDocument);

		else
        	errln("Cannot get NLP data. " + ParameterLib.NESTORPipeline_NLProcessor + " NL processor not set or document not annotated!");
		
		return "";

	}
	
	
	
	public void buildLogicalAnnotation() {
		
		if (ProcessedDocument != null && ProcessedDocument.isComplete()) {

			for (NLSentence nlSentence : ProcessedDocument.getDocumentSentences()) 
				nlSentence.buildLogicalAnnotation();

		}

		else
        	errln("Cannot build sentences logical annotations. Document processing not completed!");

	}
	

	
	public String getLogicalAnnotationText() {

		/*
		 * ******************************************************** 
		 * Get Logical Annotation String
		 * ********************************************************
		 */
		
		String logicalAnnotationText = "";

		if (ProcessedDocument != null && ProcessedDocument.isComplete()) {

			for (NLSentence nlSentence : ProcessedDocument.getDocumentSentences()) {
			
				logicalAnnotationText += "Sentence " + nlSentence.getIndexInDocument() + " : " + nlSentence.getText() + ls;
				logicalAnnotationText += "************************************************" + ls;

				/* Create sentence logical annotation text */
				if (nlSentence.isComplete())
					logicalAnnotationText += nlSentence.getSentenceAnnotation().getTextRepresentation();

			}
		}
		
		else
        	errln("Cannot get logical annotation text data for sentencesa. Document processing not completed!");

		return logicalAnnotationText;

	}

	
	
	public void inferTranslation(String translationPolicyString) {
		
		if (ProcessedDocument != null && ProcessedDocument.isComplete()) {

			for (NLSentence nlSentence : ProcessedDocument.getDocumentSentences()) {
				
				if (nlSentence.isComplete()) {
					
					try {

						if (ParameterLib.NESTORPipeline_TranslationPolicyType.equals("prudens-java"))
							ActiveTranslationPolicy.inferTranslation(translationPolicyString, nlSentence, new PrudensJava());

						else if (ParameterLib.NESTORPipeline_TranslationPolicyType.equals("prudensjs-web"))
							ActiveTranslationPolicy.inferTranslation(translationPolicyString, nlSentence, new PrudensJS());
						
						else {
							errln("Translation Policy Error: [" + ParameterLib.NESTORPipeline_TranslationPolicyType + "] Unknown Translation Policy Type");
						}

					} catch (TranslationPolicyException e) {

						errln("Error inferring translation using translation policy: " + e.getMessage());
					}
											
				}
				
			}
			
		}

		else
        	errln("Cannot infer expression. Document processing not completed!");

		
	}


	public String getTranslationText() {
		
		String	translationText = "";
		
		if (ProcessedDocument != null && ProcessedDocument.isComplete())

			for (LogicExpression expression : ProcessedDocument.getLogicRepresentation())

				translationText += expression.getTextRepresentation() + ls;

		else
        	errln("Cannot retreive inferred expressions text. Document processing not completed!");

		return translationText;

	}


	public String getDocumentJSON() {
		
		String	documentJSON = "";
		
		if (ProcessedDocument != null && ProcessedDocument.isComplete())
			documentJSON = ProcessedDocument.toJSONString();

		else
        	errln("Cannot retreive document JSON. Document processing not completed!");

		return documentJSON;

	}
	

	
	public String getMarkedRulesChainText() {
		
		String	markedRulesChainText = "";
		
		if (ProcessedDocument != null && ProcessedDocument.isComplete()) {

			for (NLSentence nlSentence : ProcessedDocument.getDocumentSentences()) {

				markedRulesChainText += "Sentence " + nlSentence.getIndexInDocument() + " : " + nlSentence.getText() + ls;
				markedRulesChainText += "************************************************" + ls;

				if (nlSentence.anExpressionIsInferred()) 

					for (LogicExpression inferredExpression : nlSentence.getInferredExpressions()) {
						
						markedRulesChainText += inferredExpression.getTitle() + ":" + ls;
						markedRulesChainText += inferredExpression.getRuleDeduction().getMarkedRulesChainText() + ls;

					}

				else
		        	errln("Cannot get marked rules chain. No rule inferred yet!");
			}
		}

		else
        	errln("Cannot get marked rules chain. Document processing not completed!");

		return markedRulesChainText;

	}
	

	
	public String getMarkedRulesText() {
		
		String	markedRulesText = "";
		
		if (ProcessedDocument != null && ProcessedDocument.isComplete()) {

			for (NLSentence nlSentence : ProcessedDocument.getDocumentSentences()) {

				markedRulesText += "Sentence " + nlSentence.getIndexInDocument() + " : " + nlSentence.getText() + ls;
				markedRulesText += "************************************************" + ls;

				if (nlSentence.anExpressionIsInferred() /*&& nlSentence.MarkedTranslationPolicySet*/) 

					for (LogicExpression inferredExpression : nlSentence.getInferredExpressions()) {
						
						markedRulesText += inferredExpression.getTitle() + ":" + ls;
						markedRulesText += inferredExpression.getRuleDeduction().getMarkedRulesText() + ls;

					}

				else
		        	errln("Cannot get marked rules chain. No rule generated yet!");
			}
		}

		else
        	errln("Cannot get marked rules. Document processing not completed!");

		return markedRulesText;

	}
	

	
	public String getAllExplanationText() {
		
		String	markedRulesText = "";
		
		if (ProcessedDocument != null && ProcessedDocument.isComplete()) {

			for (NLSentence nlSentence : ProcessedDocument.getDocumentSentences()) {

				markedRulesText += "Sentence " + nlSentence.getIndexInDocument() + " : " + nlSentence.getText() + ls;
				markedRulesText += "************************************************" + ls;

				if (nlSentence.anExpressionIsInferred() /*&& nlSentence.MarkedTranslationPolicySet*/) {

					for (LogicExpression inferredExpression : nlSentence.getInferredExpressions()) {
						
						markedRulesText += inferredExpression.getTitle() + ":" + ls;
						markedRulesText += inferredExpression.getRuleDeduction().getMarkedRulesChainText() + ls;
						markedRulesText += inferredExpression.getRuleDeduction().getMarkedRulesText() + ls;

					}

				}

				else
		        	errln("Cannot get marked rules chain. No rule generated yet!");
			}
		}

		else
        	errln("Cannot get marked rules. Document processing not completed!");

		return markedRulesText;

	}
	

	
	/**
	 * @return the processedDocument
	 */
	public  NLDocument getProcessedDocument() {
		return ProcessedDocument;
	}
	

}
