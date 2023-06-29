package cy.ac.ouc.cognition.nestor.lib.nlp;

public abstract class NLProcessor extends NLProcessing {
	
	protected boolean	DocumentAnnotated;
	public boolean		NLProcessorLoaded;


	protected NLProcessor() {
		DocumentAnnotated = false;
		NLProcessorLoaded = false;
	}
	
	protected NLProcessor(boolean load) {
		this();
		if (load)
			load();
	}
	
	public void load() {
		NLProcessorLoaded = true;
	}

	public void resetNLDocument() {
		DocumentAnnotated = false;
	}

	public void unload() {
		DocumentAnnotated = false;
		NLProcessorLoaded = false;
	}
	
	public boolean isLoaded( ) {
		return NLProcessorLoaded;
	}

	
	public void setLoaded(boolean loaded) {
		NLProcessorLoaded = loaded;
	}

	
	public boolean isAnnotated( ) {
		return DocumentAnnotated;
	}


}
