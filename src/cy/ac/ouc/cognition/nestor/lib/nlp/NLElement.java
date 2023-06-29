package cy.ac.ouc.cognition.nestor.lib.nlp;

public abstract class NLElement extends NLProcessing {

	protected String	Text;
	
	NLElement(String text) {
		super();
		Text = new String(text);
	}

	

	/**
	 * @return the text
	 */
	public  String getText() {
		return Text;
	}
	
}
