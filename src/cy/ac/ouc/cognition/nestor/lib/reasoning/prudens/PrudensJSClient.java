package cy.ac.ouc.cognition.nestor.lib.reasoning.prudens;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.TL;
import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.outln;
import cy.ac.ouc.cognition.nestor.lib.utility.ParameterLib;
import cy.ac.ouc.cognition.nestor.lib.reasoning.ReasoningObject;

public class PrudensJSClient extends ReasoningObject {

	private String 	requestBody;
	private String 	responseBody;
	private int		responseCode;
	
	PrudensJSClient(String messageBody) {
		
		if (messageBody == null)
			this.requestBody = "";
		else
			this.requestBody = messageBody;
		
	}

	
	public String send() throws PrudensException {
		
		try {
		
        	outln(TL, "PrudensJS Web Service Request body: [" + requestBody + "]");

        	// Create an HTTP Request Object - CID : Arguments should be parameterized
	    	HttpRequest request = HttpRequest.newBuilder()
	    			  .uri(new URI("https://us-central1-prudens---dev.cloudfunctions.net/app/deduce"))
	    			  .headers("Content-Type", "application/json")
	    			  .headers("Accept", "application/json*")
	    			  .headers("User-Agent", "NESTORApp/2.0")
	    			  .POST(HttpRequest.BodyPublishers.ofString(requestBody))
	    			  .build();
	    	
        	// Create an HTTP Client Object
        	HttpClient client = HttpClient.newHttpClient();
        	
        	// Send HTTP Request and receive response, synchronously
        	HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        	
        	// Return the string of the response body
        	responseCode = response.statusCode();
        	responseBody = response.body();

        	outln(TL, "PrudensJS Web Service Response status code: [" + Integer.valueOf(responseCode).toString() + "]");
        	if (ParameterLib.PrudensJS_AlwaysPrintResponseBody == 1)
        		outln(TL, "PrudensJS Web Service Response body: [" + responseBody.replaceAll("\\s+", " ") + "]");
        	
        	if (response.statusCode() != 200)
    			throw new PrudensException("PrudensJS Web Service responed with an error code: " + response.statusCode());
        	
        	return responseBody;       	

		}
		
		catch (Exception e) {
			throw new PrudensException("PrudensJS Error sending request to PrudensJS service: " + e.getMessage());
		}
    	

	}
	
	
	
	public ArrayList <PrudensJSInference> extractInferencesFromResponse() throws PrudensException {
		
		try {

			Gson gson = new Gson();
	    	// Create a JsonObject from the response body
	    	JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
	    	// Create the correct type object for converting the list of inferences
	    	Type listOfMyClassObject = new TypeToken<ArrayList<PrudensJSInference>>(){}.getType();
	    	// Get the list of inferences from the response
	    	// Convert received list of inferences to PrudensJSInference list
	    	return gson.fromJson(jsonObject.getAsJsonArray("inferences"), listOfMyClassObject);	

		}
		
		catch (Exception e) {
			throw new PrudensException("PrudensJS Error extracting inferences from response body: " + e.getMessage());
		}
	}

	
	/**
	 * @return the requestBody
	 */
	public String getRequestBody() {
		return requestBody;
	}

	/**
	 * @param requestBody the requestBody to set
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}


	/**
	 * @return the responseBody
	 */
	public String getResponseBody() {
		return responseBody;
	}


	/**
	 * @param responseBody the responseBody to set
	 */
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}


	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}


	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

}
