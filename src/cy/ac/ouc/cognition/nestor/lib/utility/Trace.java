package cy.ac.ouc.cognition.nestor.lib.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cy.ac.ouc.cognition.nestor.lib.base.NESTORThing;

public class Trace extends NESTORThing {
	

	static enum StreamType {
		OUT, ERR;
	}
	

	public static enum TraceLevel {
		CRITICAL, IMPORTANT, NORMAL, LOW, INFO;
	}
	
	
	public static		TraceLevel TL = TraceLevel.valueOf(ParameterLib.NESTOR_DefaultTraceLevel);
	protected static	DateTimeFormatter dtf = DateTimeFormatter.ofPattern(ParameterLib.NESTOR_TraceTimestampFormat);
	

	public static void println(StreamType stream, String textToPrint, boolean printTimestamp) {
		
        LocalDateTime now = LocalDateTime.now();
        String nowString = (printTimestamp && ParameterLib.NESTOR_TraceUseTimestamp > 0) ?
        						now.format(dtf) + " " :
        						"";

		if (stream == StreamType.ERR)
			System.err.println(nowString + textToPrint);
		else
			System.out.println(nowString + textToPrint);
	}

	

	public static void outln(TraceLevel traceLevel, String textToPrint, boolean printTimestamp) {
		if (traceLevel.ordinal() <= ParameterLib.NESTOR_TraceLevel)
		Trace.println(StreamType.OUT, textToPrint, printTimestamp);
	}

	public static void outln(TraceLevel traceLevel, String textToPrint) {
		Trace.outln(traceLevel, textToPrint, true);
	}


	
	public static void errln(String textToPrint, boolean printTimestamp) {
		Trace.println(StreamType.ERR, textToPrint, printTimestamp);
	}

	public static void errln(String textToPrint) {
		Trace.errln(textToPrint, true);
	}

}
