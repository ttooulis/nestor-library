package cy.ac.ouc.cognition.nestor.lib.nlp;

import cy.ac.ouc.cognition.nestor.lib.base.INESTORInterface;

public interface INLProcessor  extends INESTORInterface{

	public void load();

	public NLDocument annotateDocument(String nlText);

	public String generateParseData(NLDocument nlDocument);
	
	public void resetNLDocument();

	public void unload();
	
	public boolean isLoaded();

	public void setLoaded(boolean loaded);

	public boolean isAnnotated();

}
