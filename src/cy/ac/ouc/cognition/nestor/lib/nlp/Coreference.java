package cy.ac.ouc.cognition.nestor.lib.nlp;

import org.json.JSONPropertyIgnore;

public class Coreference extends NLRelation {
	
		
	private String	RepMention;
	private String	Mention;
	private	int		MentionIndex;
	private int		SentenceIndex;

	
		
	public Coreference(String coreferenceName) {
		super(coreferenceName);
	}

	
	public Coreference(String coreferenceName, String repMention, String mention, int mentionIndex, int sentenceIndex) {
		
		this(coreferenceName);
		
		RepMention = repMention;
		Mention = mention;
		setMentionIndex(mentionIndex);
		setSentenceIndex(sentenceIndex);
		
		Complete = true;
	}
	


	/**
	 * @return the repMention
	 */
	public String getRepMention() {
		return RepMention;
	}
	
	/**
	 * @param repMention the repMention to set
	 */
	@JSONPropertyIgnore
	public void setRepMention(String repMention) {
		RepMention = repMention;
		Complete = false;
	}
	
	/**
	 * @return the mention
	 */
	public String getMention() {
		return Mention;
	}

	/**
	 * @param mention the mention to set
	 */
	public void setMention(String mention) {
		Mention = mention;
		Complete = false;
	}

	/**
	 * @return the mentionIndex
	 */
	public int getMentionIndex() {
		return MentionIndex;
	}

	/**
	 * @param mentionIndex the mentionIndex to set
	 */
	public void setMentionIndex(int mentionIndex) {
		MentionIndex = mentionIndex;
		Complete = false;
	}

	/**
	 * @return the sentenceIndex
	 */
	public int getSentenceIndex() {
		return SentenceIndex;
	}

	/**
	 * @param sentenceIndex the sentenceIndex to set
	 */
	public void setSentenceIndex(int sentenceIndex) {
		SentenceIndex = sentenceIndex;
		Complete = false;
	}
	
	
}
