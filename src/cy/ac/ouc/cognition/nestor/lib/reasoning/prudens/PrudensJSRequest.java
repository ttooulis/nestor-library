package cy.ac.ouc.cognition.nestor.lib.reasoning.prudens;


import com.google.gson.Gson;

import cy.ac.ouc.cognition.nestor.lib.reasoning.ReasoningObject;

public class PrudensJSRequest extends ReasoningObject {

	private String 	policyString;
	private String	contextString;
	
	PrudensJSRequest(String policyString, String contextString) {
		
		this.policyString = policyString.replaceAll("\\s+", " ");
		this.contextString = contextString.replaceAll("\\s+", " ");
		this.contextString = this.contextString.replace("$", "");
		
	}

	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	/**
	 * @return the policyString
	 */
	public String getPolicyString() {
		return policyString;
	}

	/**
	 * @param policyString the policyString to set
	 */
	public void setPolicyString(String policyString) {
		this.policyString = policyString;
	}

	/**
	 * @return the contextString
	 */
	public String getContextString() {
		return contextString;
	}

	/**
	 * @param contextString the contextString to set
	 */
	public void setContextString(String contextString) {
		this.contextString = contextString;
	}

}
