package cy.ac.ouc.cognition.nestor.lib.utility;

import static cy.ac.ouc.cognition.nestor.lib.utility.Trace.outln;

public class ParameterLib extends Parameter {

	/********************************
	 * NESTOR Library General Parameters
	 ********************************/
	public static String NESTOR_LineSeperator = System.getProperty("line.separator");
	public static int NESTOR_TraceLevel = 2;
	public static String NESTOR_DefaultTraceLevel = "NORMAL";
	public static String NESTOR_TraceTimestampFormat = "uuuu/MM/dd HH:mm:ss.S";
	public static int NESTOR_TraceUseTimestamp = 1;

	
	
	/********************************
	 * Translation Policy Parameters
	 ********************************/
	public static int TranslationPolicy_PredicateMode = 0;
	public static String TranslationPolicy_RuleImportance = "asc";
	public static String TranslationPolicy_VersionPredicate = "tpinfo(version)";
	public static String TranslationPolicy_VersionDataPredicate = "tpinfo_data";
	public static String TranslationPolicy_InferencePredicate = "ruleterms";
	public static String TranslationPolicy_ArgumentSeparator = "args";
	public static String TranslationPolicy_PredicateSeparator = "next";
	public static String TranslationPolicy_VarPlaceholder = "vph_";
	public static String TranslationPolicy_DynamicVarPlaceholder = "dvph_";
	public static int TranslationPolicy_MaximumLayers = 100;
	public static int PrudensJS_AlwaysPrintResponseBody = 0;
	
	
	/******************************************
	 * Target Expression Generation Parameters
	 ******************************************/
	public static String TargetPolicy_PredNameConcatChar = "";
	public static int TargetPolicy_PredNameCapitalize = 1;
	public static String TargetPolicy_PredNameCapitalizeExceptions = "'-'|'!'";
	public static String TargetPolicy_NeckSymbol = "implies";
	public static String TargetPolicy_ConflictNeckSymbol = "#";
	public static int TargetPolicy_GenerateName = 1;
	public static String TargetPolicy_NameSeparator = "::";
	public static String TargetPolicy_VariableName = "X";
	public static String TargetPolicy_NegationOperator = "-";



	/********************************
	 * Stanford CoreNLP Parameters
	 ********************************/
	// KNOWN ANNOTATORS: tokenize, ssplit, pos, lemma, ner, parse, depparse, coref, kbp, quote
	// public static String StanfordCoreNLProcessor_Annotators = "tokenize, ssplit, pos, depparse, lemma, ner";
	public static String StanfordCoreNLProcessor_Annotators = "tokenize,ssplit,pos,parse,depparse,lemma,ner,coref";

	// The coref annotator is being set to use the neural algorithm
	public static String StanfordCoreNLProcessor_Algorithm = "neural";
	
	// Define how Stanford Core NLP will split a sentence
	public static String StanfordCoreNLProcessor_Split = "false";
	

	/********************************
	 * NESTOR Pipeline Parameters
	 ********************************/
	public static String NESTORPipeline_NLProcessor;
	public static String NESTORPipeline_TranslationPolicyType;
	public static String NESTORPipeline_TargetPolicyType;
	public static String NESTORPipeline_TranslationPolicyFile;
	public static String NESTORPipeline_TargetPolicyFile;


	
	
	/********************************
	 * BACKUP NESTOR Library General Parameters
	 ********************************/
	public static int 		SNESTOR_TraceLevel = 2;
	public static String	SNESTOR_DefaultTraceLevel = "NORMAL";
	public static int 		SNESTOR_TraceUseTimestamp = 1;

	
	/********************************
	 * BACKUP Translation Policy Parameters
	 ********************************/
	public static String STranslationPolicy_RuleImportance = "asc";
	public static String STranslationPolicy_VersionPredicate = "tpinfo(version)";
	public static String STranslationPolicy_VersionDataPredicate = "tpinfo_data";
	public static String STranslationPolicy_InferencePredicate = "ruleterms";
	public static String STranslationPolicy_ArgumentSeparator = "args";
	public static String STranslationPolicy_PredicateSeparator = "next";
	public static String STranslationPolicy_VarPlaceholder = "vph_";
	public static String STranslationPolicy_DynamicVarPlaceholder = "vph_";
	public static int SPrudensJS_AlwaysPrintResponseBody = 0;

	
	/******************************************
	 * BACKUP Target Expression Generation Parameters
	 ******************************************/
	public static String STargetPolicy_PredNameConcatChar = "";
	public static int STargetPolicy_PredNameCapitalize = 1;
	public static String STargetPolicy_PredNameCapitalizeExceptions = "'-'|'!'";
	public static String STargetPolicy_NeckSymbol = "implies";
	public static String STargetPolicy_ConflictNeckSymbol = "#";
	public static int STargetPolicy_GenerateName = 1;
	public static String STargetPolicy_NameSeparator = "::";
	public static String STargetPolicy_VariableName = "X";
	public static String STargetPolicy_NegationOperator = "-";




	public static void Load(String settingsFile) {

		Initialize(settingsFile);
		
		outln(Trace.TraceLevel.IMPORTANT, "\nLoading NESTOR Lib parameters...");

		NESTOR_LineSeperator = ReadParameterTrace("LineSeperator", System.getProperty("line.separator"));
		NESTOR_DefaultTraceLevel = ReadParameterTrace("DefaultTraceLevel", "NORMAL");
		NESTOR_TraceTimestampFormat = ReadParameterTrace("TraceTimestampFormat", "uuuu/MM/dd HH:mm:ss.S");
		NESTOR_TraceUseTimestamp = ReadIntParameterTrace("TraceUseTimestamp", 1);


		TranslationPolicy_PredicateMode = ReadIntParameterTrace("TranslationPolicyPredicateMode", 0);
		TranslationPolicy_RuleImportance = ReadParameterTrace("RuleImportance", "asc");
		TranslationPolicy_VersionPredicate = ReadParameterTrace("VersionPredicate", "tpinfo(version)");
		TranslationPolicy_VersionDataPredicate = ReadParameterTrace("VersionDataPredicate", "tpinfo_data");
		TranslationPolicy_InferencePredicate = ReadParameterTrace("InferencePredicate", "ruleterms");
		TranslationPolicy_ArgumentSeparator = ReadParameterTrace("ArgumentSeparator", "args");
		TranslationPolicy_PredicateSeparator = ReadParameterTrace("PredicateSeparator", "next");
		TranslationPolicy_VarPlaceholder = ReadParameterTrace("VarPlaceholder", "vph_");
		TranslationPolicy_DynamicVarPlaceholder = ReadParameterTrace("DynamicVarPlaceholder", "dvph_");
		TranslationPolicy_MaximumLayers = ReadIntParameterTrace("MaximumLevel", 100);
		PrudensJS_AlwaysPrintResponseBody = ReadIntParameterTrace("AlwaysPrintResponseBody", 1);
		
		
		TargetPolicy_PredNameConcatChar = ReadParameterTrace("PredicateNameConcatChar", "");
		TargetPolicy_PredNameCapitalize = ReadIntParameterTrace("PredicateNameCapitalize", 1);
		TargetPolicy_PredNameCapitalizeExceptions = ReadParameterTrace("PredicateNameCapitalizeExceptions", "'-'|'!'");
		TargetPolicy_NeckSymbol = ReadParameterTrace("NeckSymbol", "implies");
		TargetPolicy_ConflictNeckSymbol = ReadParameterTrace("ConflictNeckSymbol", "#");
		TargetPolicy_GenerateName = ReadIntParameterTrace("GenerateName", 1);
		TargetPolicy_NameSeparator = ReadParameterTrace("NameSeparator", "::");
		TargetPolicy_VariableName = ReadParameterTrace("VariableName", "X");
		TargetPolicy_NegationOperator = ReadParameterTrace("NegationOperator", "-");


		StanfordCoreNLProcessor_Annotators = ReadParameter("Annotators", "tokenize,ssplit,pos,parse,depparse,lemma,ner,coref");
		StanfordCoreNLProcessor_Algorithm = ReadParameterTrace("Algorithm", "neural");
		StanfordCoreNLProcessor_Split = ReadParameterTrace("ssplit.eolonly", "false");

		
		NESTORPipeline_NLProcessor = ReadParameterTrace("NLProcessor", "corenlp");
		NESTORPipeline_TranslationPolicyType = ReadParameterTrace("TranslationPolicyType", "prudensjs");
		NESTORPipeline_TargetPolicyType = ReadParameterTrace("TargetPolicyType", "prudens");
		NESTORPipeline_TranslationPolicyFile =ReadParameterTrace("TranslationPolicyFile", "Translation-Policy.prudens");
		NESTORPipeline_TargetPolicyFile = ReadParameterTrace("TargetPolicyFile", "KnowledgeBase.txt");
	
		outln(Trace.TraceLevel.IMPORTANT, "NESTOR Lib Parameters Loaded!");

	}


	public static void Set(	String nestorDefaultTraceLevel,
							String traceUseTimestamp,
							String translationPolicyRuleImportance,
							String translationPolicyVersionPredicate,
							String translationPolicyVersionDataPredicate,
							String translationPolicyInferencePredicate,
							String translationPolicyArgumentSeparator,
							String translationPolicyPredicateSeparator,
							String translationPolicyVarPlaceholder,
							String translationPolicyDynamicVarPlaceholder,
							int	prudensJS_AlwaysPrintResponseBody,
							String targetPolicyPredNameConcatChar,
							String targetPolicyPredNameCapitalize,
							String targetPolicyPredNameCapitalizeExceptions,
							String targetPolicyNeckSymbol,
							String targetPolicyConflictNeckSymbol,
							String targetPolicyGenerateName,
							String targetPolicyNameSeparator,
							String targetPolicyVariableName,
							String targetPolicyNegationOperator
							) {

		outln(Trace.TraceLevel.IMPORTANT, "\nSetting NESTOR Lib parameters...");

		/* Backup parameters */
		SNESTOR_DefaultTraceLevel = NESTOR_DefaultTraceLevel;
		SNESTOR_TraceUseTimestamp = NESTOR_TraceUseTimestamp;


		STranslationPolicy_RuleImportance = TranslationPolicy_RuleImportance;
		STranslationPolicy_VersionPredicate = TranslationPolicy_VersionPredicate;
		STranslationPolicy_VersionDataPredicate = TranslationPolicy_VersionDataPredicate;
		STranslationPolicy_InferencePredicate = TranslationPolicy_InferencePredicate;
		STranslationPolicy_ArgumentSeparator = TranslationPolicy_ArgumentSeparator;
		STranslationPolicy_PredicateSeparator = TranslationPolicy_PredicateSeparator;
		STranslationPolicy_VarPlaceholder = TranslationPolicy_VarPlaceholder;
		STranslationPolicy_DynamicVarPlaceholder = TranslationPolicy_DynamicVarPlaceholder;
		SPrudensJS_AlwaysPrintResponseBody = PrudensJS_AlwaysPrintResponseBody;

		
		STargetPolicy_PredNameConcatChar = TargetPolicy_PredNameConcatChar;
		STargetPolicy_PredNameCapitalize = TargetPolicy_PredNameCapitalize;
		STargetPolicy_PredNameCapitalizeExceptions = TargetPolicy_PredNameCapitalizeExceptions;
		STargetPolicy_NeckSymbol = TargetPolicy_NeckSymbol;
		STargetPolicy_ConflictNeckSymbol = TargetPolicy_ConflictNeckSymbol;
		STargetPolicy_GenerateName = TargetPolicy_GenerateName;
		STargetPolicy_NameSeparator = TargetPolicy_NameSeparator;
		STargetPolicy_VariableName = TargetPolicy_VariableName;
		STargetPolicy_NegationOperator = TargetPolicy_NegationOperator;

		
		// Set new values
		NESTOR_DefaultTraceLevel = (!nestorDefaultTraceLevel.isEmpty() ? nestorDefaultTraceLevel : NESTOR_DefaultTraceLevel);
		NESTOR_TraceUseTimestamp = (!traceUseTimestamp.isEmpty() ? Integer.parseInt(traceUseTimestamp) : NESTOR_TraceUseTimestamp);


		TranslationPolicy_RuleImportance = (!translationPolicyRuleImportance.isEmpty() ? translationPolicyRuleImportance : TranslationPolicy_RuleImportance);
		TranslationPolicy_VersionPredicate = (!translationPolicyVersionPredicate.isEmpty() ? translationPolicyVersionPredicate : TranslationPolicy_VersionPredicate);
		TranslationPolicy_VersionDataPredicate = (!translationPolicyVersionDataPredicate.isEmpty() ? translationPolicyVersionDataPredicate : TranslationPolicy_VersionDataPredicate);
		TranslationPolicy_InferencePredicate = (!translationPolicyInferencePredicate.isEmpty() ? translationPolicyInferencePredicate : TranslationPolicy_InferencePredicate);
		TranslationPolicy_ArgumentSeparator = (!translationPolicyArgumentSeparator.isEmpty() ? translationPolicyArgumentSeparator : TranslationPolicy_ArgumentSeparator);
		TranslationPolicy_PredicateSeparator = (!translationPolicyPredicateSeparator.isEmpty() ? translationPolicyPredicateSeparator : TranslationPolicy_PredicateSeparator);
		TranslationPolicy_VarPlaceholder = (!translationPolicyVarPlaceholder.isEmpty() ? translationPolicyVarPlaceholder : TranslationPolicy_VarPlaceholder);
		TranslationPolicy_DynamicVarPlaceholder = (!translationPolicyDynamicVarPlaceholder.isEmpty() ? translationPolicyDynamicVarPlaceholder : TranslationPolicy_DynamicVarPlaceholder);
		PrudensJS_AlwaysPrintResponseBody = prudensJS_AlwaysPrintResponseBody;

		
		TargetPolicy_PredNameConcatChar = (!targetPolicyPredNameConcatChar.isEmpty() ? targetPolicyPredNameConcatChar : TargetPolicy_PredNameConcatChar);
		TargetPolicy_PredNameCapitalize = (!targetPolicyPredNameCapitalize.isEmpty() ? Integer.parseInt(targetPolicyPredNameCapitalize) : TargetPolicy_PredNameCapitalize);
		TargetPolicy_PredNameCapitalizeExceptions = (!targetPolicyPredNameCapitalizeExceptions.isEmpty() ? targetPolicyPredNameCapitalizeExceptions : TargetPolicy_PredNameCapitalizeExceptions);
		TargetPolicy_NeckSymbol = (!targetPolicyNeckSymbol.isEmpty() ? targetPolicyNeckSymbol : TargetPolicy_NeckSymbol);
		TargetPolicy_ConflictNeckSymbol = (!targetPolicyConflictNeckSymbol.isEmpty() ? targetPolicyConflictNeckSymbol : TargetPolicy_ConflictNeckSymbol);
		TargetPolicy_GenerateName = (!targetPolicyGenerateName.isEmpty() ? Integer.parseInt(targetPolicyGenerateName) : TargetPolicy_GenerateName);
		TargetPolicy_NameSeparator = (!targetPolicyNameSeparator.isEmpty() ? targetPolicyNameSeparator : TargetPolicy_NameSeparator);
		TargetPolicy_VariableName = (!targetPolicyVariableName.isEmpty() ? targetPolicyVariableName : TargetPolicy_VariableName);
		TargetPolicy_NegationOperator = (!targetPolicyNegationOperator.isEmpty() ? targetPolicyNegationOperator : TargetPolicy_NegationOperator);

		outln(Trace.TraceLevel.IMPORTANT, "NESTOR Lib Parameters Set!");

	}



	public static void Reset() {

		outln(Trace.TraceLevel.IMPORTANT, "\nResetting NESTOR Lib parameters...");

		NESTOR_DefaultTraceLevel = SNESTOR_DefaultTraceLevel;
		NESTOR_TraceUseTimestamp = SNESTOR_TraceUseTimestamp;


		TranslationPolicy_RuleImportance = STranslationPolicy_RuleImportance;
		TranslationPolicy_VersionPredicate = STranslationPolicy_VersionPredicate;
		TranslationPolicy_VersionDataPredicate = STranslationPolicy_VersionDataPredicate;
		TranslationPolicy_InferencePredicate = STranslationPolicy_InferencePredicate;
		TranslationPolicy_ArgumentSeparator = STranslationPolicy_ArgumentSeparator;
		TranslationPolicy_PredicateSeparator = STranslationPolicy_PredicateSeparator;
		TranslationPolicy_VarPlaceholder = STranslationPolicy_VarPlaceholder;
		TranslationPolicy_DynamicVarPlaceholder = STranslationPolicy_DynamicVarPlaceholder;
		PrudensJS_AlwaysPrintResponseBody = SPrudensJS_AlwaysPrintResponseBody;
		
		
		TargetPolicy_PredNameConcatChar = STargetPolicy_PredNameConcatChar;
		TargetPolicy_PredNameCapitalize = STargetPolicy_PredNameCapitalize;
		TargetPolicy_PredNameCapitalizeExceptions = STargetPolicy_PredNameCapitalizeExceptions;
		TargetPolicy_NeckSymbol = STargetPolicy_NeckSymbol;
		TargetPolicy_ConflictNeckSymbol = STargetPolicy_ConflictNeckSymbol;
		TargetPolicy_GenerateName = STargetPolicy_GenerateName;
		TargetPolicy_NameSeparator = STargetPolicy_NameSeparator;
		TargetPolicy_VariableName = STargetPolicy_VariableName;
		TargetPolicy_NegationOperator = STargetPolicy_NegationOperator;

		
		outln(Trace.TraceLevel.IMPORTANT, "NESTOR Lib Parameters Reset!");

	}

}
