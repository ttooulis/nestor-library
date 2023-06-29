package cy.ac.ouc.cognition.nestor.lib.nlp.corenlp;


import java.util.*;
import java.util.Map.Entry;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.IntPair;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.CorefChain.CorefMention;

import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.errln;
import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.outln;
import cy.ac.ouc.cognition.nestor.lib.utility.Trace;
import cy.ac.ouc.cognition.nestor.lib.utility.ParameterLib;
import cy.ac.ouc.cognition.nestor.lib.nlp.INLProcessor;
import cy.ac.ouc.cognition.nestor.lib.nlp.NLDocument;
import cy.ac.ouc.cognition.nestor.lib.nlp.NLProcessor;
import cy.ac.ouc.cognition.nestor.lib.nlp.NLSentence;

public class StanfordCoreNLProcessor extends NLProcessor implements INLProcessor {

    private static	Properties			CoreNLPProperties;
	private static	StanfordCoreNLP		CoreNLPPipeline;
	private 		CoreDocument		CoreNLPDocument;
	


	public StanfordCoreNLProcessor() {
		super();
	}
	
	StanfordCoreNLProcessor(boolean load) {
		super(load);
	}

	
	public void load() {
		
		/*
		 * ***************************** 
		 * Load Stanford CoreNLP System
		 * *****************************
		 */

		// Set up pipeline properties
		CoreNLPProperties = new Properties();
	
		// Set the list of annotators to run
		CoreNLPProperties.setProperty("annotators", ParameterLib.StanfordCoreNLProcessor_Annotators);

		// Set a property for coref algorithm annotator 
		CoreNLPProperties.setProperty("coref.algorithm", ParameterLib.StanfordCoreNLProcessor_Algorithm);
		
		// Set a property for how to split document to sentences 
		CoreNLPProperties.setProperty("ssplit.eolonly", ParameterLib.StanfordCoreNLProcessor_Split);
		
		CoreNLPProperties.setProperty("ner.useSUTime", "0");

		// Build pipeline
		CoreNLPPipeline = new StanfordCoreNLP(CoreNLPProperties);
		
		outln(Trace.TraceLevel.IMPORTANT, "Stanford Core NLP Properties Set: " + CoreNLPProperties.toString());

		super.load();

	}



	public NLDocument annotateDocument(String nlText) {
		
		// Create a document object
		CoreNLPDocument = new CoreDocument(nlText);

		// Annotate the document
		CoreNLPPipeline.annotate(CoreNLPDocument);
		DocumentAnnotated  = true;
		
		/*
		 * *******************************************
		 * Process document and generate predicates
		 * *******************************************
		 */
		
		int chainIndex = 1;
		NLDocument nlDocument = new NLDocument(nlText);
		nlDocument.setNLPData(CoreNLPDocument);

		
		// Generate coreference logical annotations which apply for the whole document
		if (CoreNLPProperties.getProperty("annotators").contains("coref")) {

		    for(Map.Entry<Integer, CorefChain> entry : CoreNLPDocument.corefChains().entrySet()) {
		    	
		        CorefChain corefChain = entry.getValue();
		        CorefMention representativeMention = corefChain.getRepresentativeMention();

		        CoreSentence representativeMentionSentence = CoreNLPDocument.sentences().get(representativeMention.sentNum - 1);
		        CoreLabel representativeMentionToken = representativeMentionSentence.tokens().get(representativeMention.startIndex + 1);

		        for(Entry<IntPair, Set<CorefMention>> mentionEntry : corefChain.getMentionMap().entrySet()) {

			        int mentionIndex = mentionEntry.getKey().get(1);
			        int sentenceIndex = mentionEntry.getKey().get(0);
			        
			        for (Iterator<CorefMention> cmi = mentionEntry.getValue().iterator(); cmi.hasNext();) {
			        
			        	CorefMention corefMention = cmi.next();
			        	
			        	if (corefMention.equals(representativeMention)) {
			        		representativeMentionSentence = CoreNLPDocument.sentences().get(sentenceIndex-1);
			        		representativeMentionToken = representativeMentionSentence.tokens().get(mentionIndex-1);
			        		break;
			        	}
			        	
			        	
			        }
			    }	        
        
        		String representativeMentionText = representativeMentionToken.lemma();
        		if (representativeMentionToken.ner() == null || representativeMentionToken.ner().equals("O")) {
        			representativeMentionText += chainIndex;
    			    chainIndex++;
        		}
	        	
		        
		        
			    for(Entry<IntPair, Set<CorefMention>> mentionEntry : corefChain.getMentionMap().entrySet()) {

			        int mentionIndex = mentionEntry.getKey().get(1);
			        int sentenceIndex = mentionEntry.getKey().get(0);
			        
//			        for (Iterator<CorefMention> cmi = mentionEntry.getValue().iterator(); cmi.hasNext();) {
			        
//			        	CorefMention corefMention = cmi.next();
			        	
		        		CoreSentence corefMentionSentence = CoreNLPDocument.sentences().get(sentenceIndex-1);
		        		CoreLabel corefMentionToken = corefMentionSentence.tokens().get(mentionIndex-1);
		        		
			        	nlDocument.addCoreference(	"coref",
			        								representativeMentionText,
			        								corefMentionToken.lemma(),
				        							mentionIndex,
				        							sentenceIndex
				        						);
//			        }
			    }			    
		    }
		}


		
		
		for (CoreSentence coreNLPSentence : CoreNLPDocument.sentences()) {
			
			NLSentence  nlSentence = new NLSentence(coreNLPSentence.text());
			nlSentence.setNLPData(coreNLPSentence);

			nlSentence.addToken(0, "root", "root", "O", "ROOT");

			/* Get NLToken Information */
			for (CoreLabel coreNLPToken : coreNLPSentence.tokens()) {

				String lemma = coreNLPToken.lemma();
				if (lemma == null)
					lemma = coreNLPToken.originalText();

				/* In case NER annotator is not loaded set to default value */
				String nerTag = coreNLPToken.ner();
				if (nerTag == null)
					nerTag = "O";

				String tag = coreNLPToken.tag();
				if (tag == null)
					tag = "UNKN";

				nlSentence.addToken(	coreNLPToken.index(),
										coreNLPToken.originalText(),
										lemma,
										nerTag,
										tag
									);
				
				// CID - This should become a system parameter
				if (tag.startsWith("NN")) {
					// Add a self coref for nouns
					if (!nlDocument.coreferenceAdded(lemma, coreNLPToken.index(), coreNLPToken.sentIndex() + 1))
				       	nlDocument.addCoreference(	"coref",
								lemma + ((nerTag.equals("O")) ? chainIndex++ : ""),
								lemma,
								coreNLPToken.index(),
								coreNLPToken.sentIndex() + 1
	    					);
				}

			}


			/* If Dependency Parser Annotator is set */
			/* CID - Find a way to make a better check! */
			if (CoreNLPProperties.getProperty("annotators").contains("depparse")) {
				/* Get Dependency Information */
				SemanticGraph dependencyParse = coreNLPSentence.dependencyParse();
	
				IndexedWord root = dependencyParse.getFirstRoot();

				// CID - MAYBE RETHINK "ROOT" TOKEN AND DEPENDENCY
				nlSentence.addDependency("root", 0, root.index());
				
				for (SemanticGraphEdge edge : dependencyParse.edgeListSorted()) {
					
					nlSentence.addDependency(	edge.getRelation().getShortName().toLowerCase(),
												edge.getGovernor().index(),
												edge.getDependent().index()
											);
				}
			}
			
			
			nlSentence.setComplete(true);
			nlDocument.addSentence(nlSentence);
			
		}
		
		nlDocument.setComplete(true);

		return nlDocument;

	}

	
	
	/* CID - PORTED AS IS FROM PREVIOUS VERSION. NEEDS TO BE TAKEN CARE!! */
	public String generateParseData(NLDocument nlDocument) {

		/*
		 * *******************************************
		 * Process (print annotators output) document 
		 * *******************************************
		 */
		String ParseDataText = "";
		
		if (DocumentAnnotated) {
			String ls = ParameterLib.NESTOR_LineSeperator;
	
			ParseDataText += "Document Sentences:" + ls;
			ParseDataText += "-------------------" + ls + ls;
	
			for (NLSentence nlSentence : nlDocument.getDocumentSentences()) {
				
				CoreSentence coreNLPSentence;
				if (!(nlSentence.getNLPData() instanceof CoreSentence)) {
					errln("Error generating Parse Data in Sentence " + nlSentence.getIndexInDocument());
					continue;
				}
				coreNLPSentence = (CoreSentence) nlSentence.getNLPData();
		
				String sentenceText = nlSentence.getText();
				ParseDataText += "Sentence " + nlSentence.getIndexInDocument() + " : " + sentenceText + ls;
			
				// Print sentence tokens
				ParseDataText += "Tokens: " + ls;
				for (CoreLabel token : coreNLPSentence.tokens()) {

					ParseDataText += "Category:\t\t" + token.category() + ls;
					ParseDataText += "index:\t\t" + token.index() + ls;
					ParseDataText += "DocID:\t\t" + token.docID() + ls;
					ParseDataText += "Lemma:\t\t" + token.lemma() + ls;
					ParseDataText += "NER:\t\t" + token.ner() + ls;
					ParseDataText += "OriginalText:\t" + token.originalText() + ls;
					ParseDataText += "Tag:\t\t" + token.tag() + ls;
					ParseDataText += "Value:\t\t" + token.value() + ls;
					ParseDataText += "Word:\t\t" + token.word() + ls;
					ParseDataText += "BeginPosition:\t" + token.beginPosition() + ls;
					ParseDataText += "EndPosition:\t" + token.endPosition() + ls;
				}
				ParseDataText += ls + ls;
			
				/* If Dependency Parser Annotator is set */
				/* CID - Find a way to make a better check! */
				if (CoreNLPProperties.toString().contains("depparse")) {
					// Print dependency parse for the sentence
					ParseDataText += "Dependency Parse:" + ls;
					SemanticGraph dependencyParse = coreNLPSentence.dependencyParse();
					ParseDataText += dependencyParse + ls;
					ParseDataText += "Dependency Parse (CompactString):" + ls;
					ParseDataText += dependencyParse.toCompactString() + ls;
					ParseDataText += "Dependency Parse (DotFormat):" + ls;
					ParseDataText += dependencyParse.toDotFormat() + ls;
					ParseDataText += "Dependency Parse (FormattedString):" + ls;
					ParseDataText += dependencyParse.toFormattedString() + ls;
					ParseDataText += "Dependency Parse (List):" + ls;
					ParseDataText += dependencyParse.toList() + ls;
					ParseDataText += "Dependency Parse (POSList):" + ls;
					ParseDataText += dependencyParse.toPOSList() + ls;
		
					ParseDataText += "Root: " + ls;
					IndexedWord root = dependencyParse.getFirstRoot();
					ParseDataText += "\troot (after):\t\t" + root.after() + ls;
					ParseDataText += "\troot (before):\t\t" + root.before() + ls;
					ParseDataText += "\troot (docID):\t\t" + root.docID() + ls;
					ParseDataText += "\troot (index):\t\t" + root.index() + ls;
					ParseDataText += "\troot (lemma):\t\t" + root.lemma() + ls;
					ParseDataText += "\troot (ner):\t\t" + root.ner() + ls;
					ParseDataText += "\troot (tag):\t\t" + root.tag() + ls;
					ParseDataText += "\troot (value):\t\t" + root.value() + ls;
					ParseDataText += "\troot (word):\t\t" + root.word() + ls;
		
					ParseDataText += "Edges: " + ls;
					for (SemanticGraphEdge edge : dependencyParse.edgeListSorted()) {
						ParseDataText += "Edge:\t\t" + edge.toString() + ls;
						ParseDataText += "Dependent:\t\t" + edge.getDependent().originalText() + ls;
						ParseDataText += "\t\tDependent (after):\t\t" + edge.getDependent().after() + ls;
						ParseDataText += "\t\tDependent (before):\t\t" + edge.getDependent().before() + ls;
						ParseDataText += "\t\tDependent (docID):\t\t" + edge.getDependent().docID() + ls;
						ParseDataText += "\t\tDependent (index):\t\t" + edge.getDependent().index() + ls;
						ParseDataText += "\t\tDependent (lemma):\t\t" + edge.getDependent().lemma() + ls;
						ParseDataText += "\t\tDependent (ner):\t\t" + edge.getDependent().ner() + ls;
						ParseDataText += "\t\tDependent (tag):\t\t" + edge.getDependent().tag() + ls;
						ParseDataText += "\t\tDependent (value):\t\t" + edge.getDependent().value() + ls;
						ParseDataText += "\t\tDependent (word):\t\t" + edge.getDependent().word() + ls;
						ParseDataText += "Governor:\t\t" + edge.getGovernor().originalText() + ls;
						ParseDataText += "Relation Short Name:\t\t" + edge.getRelation().getShortName() + ls;
						ParseDataText += "Relation Long Name:\t\t" + edge.getRelation().getLongName() + ls;
						ParseDataText += "\t\tRelation (specific):\t\t" + edge.getRelation().getSpecific() + ls;
						ParseDataText += "\t\tRelation (toString):\t\t" + edge.getRelation().toString() + ls;
						ParseDataText += "Source:\t\t" + edge.getSource().originalText() + ls;
						ParseDataText += "Target:\t\t" + edge.getTarget().originalText() + ls;
		
					}
				}
	
				// Print the list of the part-of-speech tags for the sentence
				ParseDataText += "List of the part-of-speech tags: ";
				List<String> posTags = coreNLPSentence.posTags();
				ParseDataText += posTags + ls + ls;
				
				/* Annotators ner and lemma required! */
				// Print the list of the ner tags for the sentence
				ParseDataText += "List of the ner tags: ";
				List<String> nerTags = coreNLPSentence.nerTags();
				ParseDataText += nerTags + ls + ls;
	
			
				// Print constituency parse for the sentence
				ParseDataText += "Constituency parse: ";
				Tree constituencyParse = coreNLPSentence.constituencyParse();
				ParseDataText += constituencyParse + ls + ls;
			
				/* Annotators ner and lemma required! */
			    // Print entity mentions in the sentence
				ParseDataText += "Entity mentions: ";
				List<CoreEntityMention> entityMentions = coreNLPSentence.entityMentions();
				ParseDataText += entityMentions + ls + ls;
	
				
				/* Annotators ner, lemma and coref required!
				 */
			    // Print coreference between entity mentions
				if (CoreNLPProperties.getProperty("annotators").contains("coref")) {
				    ParseDataText += "Coreference between entity mentions:" + ls;
					for (CoreEntityMention originalEntityMention : coreNLPSentence.entityMentions()) {
						ParseDataText += "\t Original Entity Mention Tokens = [" + originalEntityMention.tokens() + "]" + ls;
						ParseDataText += "\t Canonical Entity Mention Tokens = [" + originalEntityMention.canonicalEntityMention().get().tokens() + "]" + ls;
					}
				    ParseDataText += ls;
				}
	
				ParseDataText += "***********************************************************" + ls + ls;
	
			}	// Document sentence processing end
	
	
			CoreDocument coreNLPDocument;
			if (!(nlDocument.getNLPData() instanceof CoreDocument)) {
				errln("Error generating document wide coref info: Wrong document object type!");
				return ParseDataText;
			}
			coreNLPDocument = (CoreDocument) nlDocument.getNLPData();

		    /* Print document wide coref info */
			/* Annotators ner, lemma and coref required!
			 */
			if (CoreNLPProperties.getProperty("annotators").contains("coref")) {

				ParseDataText += "Document Wide coref info:" + ls;
				ParseDataText += "-------------------------" + ls + ls;

			    for(Map.Entry<Integer, CorefChain> entry : coreNLPDocument.corefChains().entrySet()) {
			        CorefChain c = entry.getValue();
			        
			        ParseDataText += "Coref Chain =[" + c + "]" + ls;
			        ParseDataText += "Chain ID=[" + c.getChainID() + "]" + ls;
			        ParseDataText += "Chain Hash Code =[" + c.hashCode() + "]" + ls;
			        ParseDataText += "Mention Map =[" + c.getMentionMap() + "]" + ls;
				    for(Entry<IntPair, Set<CorefMention>> mentionEntry : c.getMentionMap().entrySet()) {
				        Set<CorefMention> MentionEntrySet = mentionEntry.getValue();
				        IntPair sentenceIndex = mentionEntry.getKey();
				        ParseDataText += "\tKey: [" + sentenceIndex + "] Set: [" + MentionEntrySet + "]" + ls;
				    }
			        ParseDataText += "Mentions in Textual Order =[" + c.getMentionsInTextualOrder() + "]" + ls;
			        ParseDataText += "Representative Mention =[" + c.getRepresentativeMention() + "]" + ls + ls;
			        
			    }
			}


			/* Print quote annotators */
			/* Annotators ner, lemma and quote required!
			 * CAUSES OUT OF MEMORY ERROR. NOT TRIED!
			ParseDataText += "Document Quotes:" + ls;
			ParseDataText += "----------------" + ls + ls;
	
			count = 0;
			for (CoreQuote quote : document.quotes()) {
		
				count++;
	
				// Print quotes in document
				ParseDataText += "Quote " + count + ":" + quote + ls + ls;
				
			    // Print original speaker of quote
			    // Note that quote.speaker() returns an Optional
			    ParseDataText += "Original Speaker:" + quote.speaker().get( + ls + ls;
	
			    // Print canonical speaker of quote
			    ParseDataText += "Canonical Speaker:" + quote.canonicalSpeaker().get() +  + ls + ls + ls;
	
			} // Quote processing end
			*/
		}
	    
		return ParseDataText;
		
	} // public String Generate end



	public void resetNLDocument() {

		CoreNLPDocument = null;

		super.resetNLDocument();

	}



	public void unload() {

		resetNLDocument();

		CoreNLPProperties.clear();
		CoreNLPProperties = null;
		CoreNLPPipeline = null;
		
		super.unload();
	}

}
