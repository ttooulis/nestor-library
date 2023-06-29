package cy.ac.ouc.cognition.nestor.lib.nlp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONPropertyIgnore;

public class NLToken extends NLElement {
	
	/* NLPToken Attributes */
	private int					Index;
	private String				Lemma;
	private String				NER;
	private String				Tag;
	private List<Dependency>	Dependants;



	public NLToken(String originalText) {
		super(originalText);
		Dependants = new ArrayList<Dependency>();

	}


	NLToken(int index, String originalText, String lemma, String ner, String tag) {
		
		this(originalText);
		Index = index;
		Lemma = new String(lemma);
		NER = new String(ner);
		Tag = new String(tag);

		Complete = true;
		
	}


	
	public void addDependant(String dependencyName, NLToken dependentToken) {
		Dependants.add(	new Dependency(dependencyName,
										new NLToken("self"),
										dependentToken));
	}


	
		/**
	 * @return the index
	 */
	public int getIndex() {
		return Index;
	}

	/**
	 * @param index the index to set
	 */
	@JSONPropertyIgnore
	public void setIndex(int index) {
		Index = index;
		Complete = false;
	}

	
	
	/**
	 * @return the lemma
	 */
	public String getLemma() {
		return Lemma;
	}

	/**
	 * @param lemma the lemma to set
	 */
	@JSONPropertyIgnore
	public void setLemma(String lemma) {
		Lemma = new String(lemma);
		Complete = false;
	}



	/**
	 * @return the nER
	 */
	public String getNER() {
		return NER;
	}

	/**
	 * @param nER the nER to set
	 */
	@JSONPropertyIgnore
	public void setNER(String ner) {
		NER = new String(ner);
		Complete = false;
	}



	/**
	 * @return the tag
	 */
	public String getTag() {
		return Tag;
	}

	/**
	 * @param tag the tag to set
	 */
	@JSONPropertyIgnore
	public void setTag(String tag) {
		Tag = new String(tag);
		Complete = false;
	}


	/**
	 * @return the dependants
	 */
	public List<Dependency> getDependants() {
		return Dependants;
	}

}
