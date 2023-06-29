package cy.ac.ouc.cognition.nestor.lib.utility;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.errln;
import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.outln;
import cy.ac.ouc.cognition.nestor.lib.base.NESTORThing;

public abstract class Parameter extends NESTORThing {
	
	private static Document document;

	public static void Initialize(String settingsFile) {

		outln(Trace.TraceLevel.IMPORTANT, "Loading settings from file " + settingsFile + "...");
		File file = new File(settingsFile);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(file);
			outln(Trace.TraceLevel.IMPORTANT, settingsFile + " loaded!");
		} catch (ParserConfigurationException | IOException | SAXException e) {
			errln("Cannot open settings file (" + settingsFile + "): " + e.getMessage() + "\n");
			outln(Trace.TraceLevel.IMPORTANT, "Using Default Settings...");
		}
	}
	
	protected static String ReadParameter(String tagName, String defaultValue) {
		try {
			String parameterValue = document.getElementsByTagName(tagName).item(0).getTextContent();
			
			if (parameterValue == null || parameterValue == "")
				return defaultValue;

			return parameterValue;
		}
		catch (Exception e) {
			return defaultValue;
		}
	}

	
	protected static int ReadIntParameter(String tagName, int defaultValue) {
		try {
			Integer parameterValue = Integer.parseInt(ReadParameter(tagName, Integer.toString(defaultValue)));
			return parameterValue.intValue();
		}
		catch (Exception e) {
			return defaultValue;
		}
	}


	protected static boolean ReadBooleanParameter(String tagName, boolean defaultValue) {
		try {
			String StringValue = ReadParameter(tagName, Boolean.toString(defaultValue));
			if (StringValue.equals("true") || StringValue.equals("True"))
				return true;
			else
				return false;
		}
		catch (Exception e) {
			return defaultValue;
		}
	}


	protected static String ReadParameterTrace(String tagName, String defaultValue) {
		String returnValue =	ReadParameter(tagName, defaultValue);
		outln(Trace.TraceLevel.IMPORTANT, tagName + "=[" + returnValue + "]");
		return returnValue;
	}


	protected static int ReadIntParameterTrace(String tagName, int defaultValue) {
		int returnValue =	ReadIntParameter(tagName, defaultValue);
		outln(Trace.TraceLevel.IMPORTANT, tagName + "=[" + returnValue + "]");
		return returnValue;
	}

	protected static boolean ReadBooleanParameterTrace(String tagName, boolean defaultValue) {
		boolean returnValue =	ReadBooleanParameter(tagName, defaultValue);
		outln(Trace.TraceLevel.IMPORTANT, tagName + "=[" + Boolean.toString(returnValue) + "]");
		return returnValue;
	}

}
