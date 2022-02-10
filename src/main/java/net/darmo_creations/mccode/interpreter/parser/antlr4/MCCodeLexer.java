// Generated from /home/damien/IdeaProjects/MC_Code/grammar/MCCode.g4 by ANTLR 4.9.2
package net.darmo_creations.mccode.interpreter.parser.antlr4;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MCCodeLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, COMMENT=2, LPAREN=3, RPAREN=4, LBRACK=5, RBRACK=6, LCURL=7, RCURL=8, 
		COMMA=9, COLON=10, SEMIC=11, DOT=12, ASSIGN=13, PLUSA=14, MINUSA=15, MULA=16, 
		DIVA=17, INTDIVA=18, MODA=19, POWERA=20, PLUS=21, MINUS=22, MUL=23, DIV=24, 
		INTDIV=25, MOD=26, POWER=27, EQUAL=28, NEQUAL=29, GT=30, GE=31, LT=32, 
		LE=33, IN=34, NOT=35, AND=36, OR=37, IMPORT=38, AS=39, SCHED=40, VAR=41, 
		CONST=42, EDITABLE=43, PUBLIC=44, FUNC=45, RETURN=46, IF=47, THEN=48, 
		ELSE=49, ELIF=50, WHILE=51, FOR=52, DO=53, END=54, DELETE=55, BREAK=56, 
		CONTINUE=57, WAIT=58, REPEAT=59, FOREVER=60, NULL=61, TRUE=62, FALSE=63, 
		INT=64, FLOAT=65, STRING=66, IDENT=67, CMDARG=68;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "COMMENT", "LPAREN", "RPAREN", "LBRACK", "RBRACK", "LCURL", "RCURL", 
			"COMMA", "COLON", "SEMIC", "DOT", "ASSIGN", "PLUSA", "MINUSA", "MULA", 
			"DIVA", "INTDIVA", "MODA", "POWERA", "PLUS", "MINUS", "MUL", "DIV", "INTDIV", 
			"MOD", "POWER", "EQUAL", "NEQUAL", "GT", "GE", "LT", "LE", "IN", "NOT", 
			"AND", "OR", "IMPORT", "AS", "SCHED", "VAR", "CONST", "EDITABLE", "PUBLIC", 
			"FUNC", "RETURN", "IF", "THEN", "ELSE", "ELIF", "WHILE", "FOR", "DO", 
			"END", "DELETE", "BREAK", "CONTINUE", "WAIT", "REPEAT", "FOREVER", "NULL", 
			"TRUE", "FALSE", "INT", "FLOAT", "STRING", "IDENT", "CMDARG"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'('", "')'", "'['", "']'", "'{'", "'}'", "','", "':'", 
			"';'", "'.'", "':='", "'+='", "'-='", "'*='", "'/='", "'//='", "'%='", 
			"'^='", "'+'", "'-'", "'*'", "'/'", "'//'", "'%'", "'^'", "'='", "'!='", 
			"'>'", "'>='", "'<'", "'<='", "'in'", "'not'", "'and'", "'or'", "'import'", 
			"'as'", "'schedule'", "'var'", "'const'", "'editable'", "'public'", "'function'", 
			"'return'", "'if'", "'then'", "'else'", "'elseif'", "'while'", "'for'", 
			"'do'", "'end'", "'del'", "'break'", "'continue'", "'wait'", "'repeat'", 
			"'forever'", "'null'", "'true'", "'false'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "COMMENT", "LPAREN", "RPAREN", "LBRACK", "RBRACK", "LCURL", 
			"RCURL", "COMMA", "COLON", "SEMIC", "DOT", "ASSIGN", "PLUSA", "MINUSA", 
			"MULA", "DIVA", "INTDIVA", "MODA", "POWERA", "PLUS", "MINUS", "MUL", 
			"DIV", "INTDIV", "MOD", "POWER", "EQUAL", "NEQUAL", "GT", "GE", "LT", 
			"LE", "IN", "NOT", "AND", "OR", "IMPORT", "AS", "SCHED", "VAR", "CONST", 
			"EDITABLE", "PUBLIC", "FUNC", "RETURN", "IF", "THEN", "ELSE", "ELIF", 
			"WHILE", "FOR", "DO", "END", "DELETE", "BREAK", "CONTINUE", "WAIT", "REPEAT", 
			"FOREVER", "NULL", "TRUE", "FALSE", "INT", "FLOAT", "STRING", "IDENT", 
			"CMDARG"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MCCodeLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MCCode.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2F\u01cf\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\3\2\6\2\u008d\n\2\r\2"+
		"\16\2\u008e\3\2\3\2\3\3\3\3\6\3\u0095\n\3\r\3\16\3\u0096\3\3\3\3\3\3\3"+
		"\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3"+
		"\f\3\f\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25"+
		"\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\34"+
		"\3\34\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3 \3 \3 \3!\3!\3\"\3\"\3\"\3"+
		"#\3#\3#\3$\3$\3$\3$\3%\3%\3%\3%\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3"+
		"(\3(\3(\3)\3)\3)\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3,\3"+
		",\3,\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3.\3.\3"+
		".\3/\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\62\3"+
		"\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3"+
		"\64\3\64\3\64\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\67\3\67\3\67\3\67\3"+
		"8\38\38\38\39\39\39\39\39\39\3:\3:\3:\3:\3:\3:\3:\3:\3:\3;\3;\3;\3;\3"+
		";\3<\3<\3<\3<\3<\3<\3<\3=\3=\3=\3=\3=\3=\3=\3=\3>\3>\3>\3>\3>\3?\3?\3"+
		"?\3?\3?\3@\3@\3@\3@\3@\3@\3A\6A\u0190\nA\rA\16A\u0191\3B\6B\u0195\nB\r"+
		"B\16B\u0196\3B\3B\7B\u019b\nB\fB\16B\u019e\13B\3B\5B\u01a1\nB\3B\6B\u01a4"+
		"\nB\rB\16B\u01a5\5B\u01a8\nB\3B\3B\5B\u01ac\nB\3B\6B\u01af\nB\rB\16B\u01b0"+
		"\5B\u01b3\nB\3C\3C\3C\3C\7C\u01b9\nC\fC\16C\u01bc\13C\3C\3C\3D\3D\7D\u01c2"+
		"\nD\fD\16D\u01c5\13D\3E\3E\3E\6E\u01ca\nE\rE\16E\u01cb\5E\u01ce\nE\4\u0096"+
		"\u01ba\2F\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"+
		"\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67"+
		"\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65"+
		"i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083C\u0085D\u0087E\u0089F\3\2"+
		"\n\5\2\13\f\17\17\"\"\4\2\f\f\17\17\3\2\62;\4\2GGgg\5\2$$^^pp\6\2\f\f"+
		"\17\17$$^^\5\2C\\aac|\6\2\62;C\\aac|\2\u01de\2\3\3\2\2\2\2\5\3\2\2\2\2"+
		"\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2"+
		"\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2"+
		"\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2"+
		"\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2"+
		"\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2"+
		"\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2"+
		"M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3"+
		"\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2"+
		"\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2"+
		"s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177"+
		"\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2"+
		"\2\2\u0089\3\2\2\2\3\u008c\3\2\2\2\5\u0092\3\2\2\2\7\u009c\3\2\2\2\t\u009e"+
		"\3\2\2\2\13\u00a0\3\2\2\2\r\u00a2\3\2\2\2\17\u00a4\3\2\2\2\21\u00a6\3"+
		"\2\2\2\23\u00a8\3\2\2\2\25\u00aa\3\2\2\2\27\u00ac\3\2\2\2\31\u00ae\3\2"+
		"\2\2\33\u00b0\3\2\2\2\35\u00b3\3\2\2\2\37\u00b6\3\2\2\2!\u00b9\3\2\2\2"+
		"#\u00bc\3\2\2\2%\u00bf\3\2\2\2\'\u00c3\3\2\2\2)\u00c6\3\2\2\2+\u00c9\3"+
		"\2\2\2-\u00cb\3\2\2\2/\u00cd\3\2\2\2\61\u00cf\3\2\2\2\63\u00d1\3\2\2\2"+
		"\65\u00d4\3\2\2\2\67\u00d6\3\2\2\29\u00d8\3\2\2\2;\u00da\3\2\2\2=\u00dd"+
		"\3\2\2\2?\u00df\3\2\2\2A\u00e2\3\2\2\2C\u00e4\3\2\2\2E\u00e7\3\2\2\2G"+
		"\u00ea\3\2\2\2I\u00ee\3\2\2\2K\u00f2\3\2\2\2M\u00f5\3\2\2\2O\u00fc\3\2"+
		"\2\2Q\u00ff\3\2\2\2S\u0108\3\2\2\2U\u010c\3\2\2\2W\u0112\3\2\2\2Y\u011b"+
		"\3\2\2\2[\u0122\3\2\2\2]\u012b\3\2\2\2_\u0132\3\2\2\2a\u0135\3\2\2\2c"+
		"\u013a\3\2\2\2e\u013f\3\2\2\2g\u0146\3\2\2\2i\u014c\3\2\2\2k\u0150\3\2"+
		"\2\2m\u0153\3\2\2\2o\u0157\3\2\2\2q\u015b\3\2\2\2s\u0161\3\2\2\2u\u016a"+
		"\3\2\2\2w\u016f\3\2\2\2y\u0176\3\2\2\2{\u017e\3\2\2\2}\u0183\3\2\2\2\177"+
		"\u0188\3\2\2\2\u0081\u018f\3\2\2\2\u0083\u01a7\3\2\2\2\u0085\u01b4\3\2"+
		"\2\2\u0087\u01bf\3\2\2\2\u0089\u01c6\3\2\2\2\u008b\u008d\t\2\2\2\u008c"+
		"\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008c\3\2\2\2\u008e\u008f\3\2"+
		"\2\2\u008f\u0090\3\2\2\2\u0090\u0091\b\2\2\2\u0091\4\3\2\2\2\u0092\u0094"+
		"\7%\2\2\u0093\u0095\13\2\2\2\u0094\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096"+
		"\u0097\3\2\2\2\u0096\u0094\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u0099\t\3"+
		"\2\2\u0099\u009a\3\2\2\2\u009a\u009b\b\3\2\2\u009b\6\3\2\2\2\u009c\u009d"+
		"\7*\2\2\u009d\b\3\2\2\2\u009e\u009f\7+\2\2\u009f\n\3\2\2\2\u00a0\u00a1"+
		"\7]\2\2\u00a1\f\3\2\2\2\u00a2\u00a3\7_\2\2\u00a3\16\3\2\2\2\u00a4\u00a5"+
		"\7}\2\2\u00a5\20\3\2\2\2\u00a6\u00a7\7\177\2\2\u00a7\22\3\2\2\2\u00a8"+
		"\u00a9\7.\2\2\u00a9\24\3\2\2\2\u00aa\u00ab\7<\2\2\u00ab\26\3\2\2\2\u00ac"+
		"\u00ad\7=\2\2\u00ad\30\3\2\2\2\u00ae\u00af\7\60\2\2\u00af\32\3\2\2\2\u00b0"+
		"\u00b1\7<\2\2\u00b1\u00b2\7?\2\2\u00b2\34\3\2\2\2\u00b3\u00b4\7-\2\2\u00b4"+
		"\u00b5\7?\2\2\u00b5\36\3\2\2\2\u00b6\u00b7\7/\2\2\u00b7\u00b8\7?\2\2\u00b8"+
		" \3\2\2\2\u00b9\u00ba\7,\2\2\u00ba\u00bb\7?\2\2\u00bb\"\3\2\2\2\u00bc"+
		"\u00bd\7\61\2\2\u00bd\u00be\7?\2\2\u00be$\3\2\2\2\u00bf\u00c0\7\61\2\2"+
		"\u00c0\u00c1\7\61\2\2\u00c1\u00c2\7?\2\2\u00c2&\3\2\2\2\u00c3\u00c4\7"+
		"\'\2\2\u00c4\u00c5\7?\2\2\u00c5(\3\2\2\2\u00c6\u00c7\7`\2\2\u00c7\u00c8"+
		"\7?\2\2\u00c8*\3\2\2\2\u00c9\u00ca\7-\2\2\u00ca,\3\2\2\2\u00cb\u00cc\7"+
		"/\2\2\u00cc.\3\2\2\2\u00cd\u00ce\7,\2\2\u00ce\60\3\2\2\2\u00cf\u00d0\7"+
		"\61\2\2\u00d0\62\3\2\2\2\u00d1\u00d2\7\61\2\2\u00d2\u00d3\7\61\2\2\u00d3"+
		"\64\3\2\2\2\u00d4\u00d5\7\'\2\2\u00d5\66\3\2\2\2\u00d6\u00d7\7`\2\2\u00d7"+
		"8\3\2\2\2\u00d8\u00d9\7?\2\2\u00d9:\3\2\2\2\u00da\u00db\7#\2\2\u00db\u00dc"+
		"\7?\2\2\u00dc<\3\2\2\2\u00dd\u00de\7@\2\2\u00de>\3\2\2\2\u00df\u00e0\7"+
		"@\2\2\u00e0\u00e1\7?\2\2\u00e1@\3\2\2\2\u00e2\u00e3\7>\2\2\u00e3B\3\2"+
		"\2\2\u00e4\u00e5\7>\2\2\u00e5\u00e6\7?\2\2\u00e6D\3\2\2\2\u00e7\u00e8"+
		"\7k\2\2\u00e8\u00e9\7p\2\2\u00e9F\3\2\2\2\u00ea\u00eb\7p\2\2\u00eb\u00ec"+
		"\7q\2\2\u00ec\u00ed\7v\2\2\u00edH\3\2\2\2\u00ee\u00ef\7c\2\2\u00ef\u00f0"+
		"\7p\2\2\u00f0\u00f1\7f\2\2\u00f1J\3\2\2\2\u00f2\u00f3\7q\2\2\u00f3\u00f4"+
		"\7t\2\2\u00f4L\3\2\2\2\u00f5\u00f6\7k\2\2\u00f6\u00f7\7o\2\2\u00f7\u00f8"+
		"\7r\2\2\u00f8\u00f9\7q\2\2\u00f9\u00fa\7t\2\2\u00fa\u00fb\7v\2\2\u00fb"+
		"N\3\2\2\2\u00fc\u00fd\7c\2\2\u00fd\u00fe\7u\2\2\u00feP\3\2\2\2\u00ff\u0100"+
		"\7u\2\2\u0100\u0101\7e\2\2\u0101\u0102\7j\2\2\u0102\u0103\7g\2\2\u0103"+
		"\u0104\7f\2\2\u0104\u0105\7w\2\2\u0105\u0106\7n\2\2\u0106\u0107\7g\2\2"+
		"\u0107R\3\2\2\2\u0108\u0109\7x\2\2\u0109\u010a\7c\2\2\u010a\u010b\7t\2"+
		"\2\u010bT\3\2\2\2\u010c\u010d\7e\2\2\u010d\u010e\7q\2\2\u010e\u010f\7"+
		"p\2\2\u010f\u0110\7u\2\2\u0110\u0111\7v\2\2\u0111V\3\2\2\2\u0112\u0113"+
		"\7g\2\2\u0113\u0114\7f\2\2\u0114\u0115\7k\2\2\u0115\u0116\7v\2\2\u0116"+
		"\u0117\7c\2\2\u0117\u0118\7d\2\2\u0118\u0119\7n\2\2\u0119\u011a\7g\2\2"+
		"\u011aX\3\2\2\2\u011b\u011c\7r\2\2\u011c\u011d\7w\2\2\u011d\u011e\7d\2"+
		"\2\u011e\u011f\7n\2\2\u011f\u0120\7k\2\2\u0120\u0121\7e\2\2\u0121Z\3\2"+
		"\2\2\u0122\u0123\7h\2\2\u0123\u0124\7w\2\2\u0124\u0125\7p\2\2\u0125\u0126"+
		"\7e\2\2\u0126\u0127\7v\2\2\u0127\u0128\7k\2\2\u0128\u0129\7q\2\2\u0129"+
		"\u012a\7p\2\2\u012a\\\3\2\2\2\u012b\u012c\7t\2\2\u012c\u012d\7g\2\2\u012d"+
		"\u012e\7v\2\2\u012e\u012f\7w\2\2\u012f\u0130\7t\2\2\u0130\u0131\7p\2\2"+
		"\u0131^\3\2\2\2\u0132\u0133\7k\2\2\u0133\u0134\7h\2\2\u0134`\3\2\2\2\u0135"+
		"\u0136\7v\2\2\u0136\u0137\7j\2\2\u0137\u0138\7g\2\2\u0138\u0139\7p\2\2"+
		"\u0139b\3\2\2\2\u013a\u013b\7g\2\2\u013b\u013c\7n\2\2\u013c\u013d\7u\2"+
		"\2\u013d\u013e\7g\2\2\u013ed\3\2\2\2\u013f\u0140\7g\2\2\u0140\u0141\7"+
		"n\2\2\u0141\u0142\7u\2\2\u0142\u0143\7g\2\2\u0143\u0144\7k\2\2\u0144\u0145"+
		"\7h\2\2\u0145f\3\2\2\2\u0146\u0147\7y\2\2\u0147\u0148\7j\2\2\u0148\u0149"+
		"\7k\2\2\u0149\u014a\7n\2\2\u014a\u014b\7g\2\2\u014bh\3\2\2\2\u014c\u014d"+
		"\7h\2\2\u014d\u014e\7q\2\2\u014e\u014f\7t\2\2\u014fj\3\2\2\2\u0150\u0151"+
		"\7f\2\2\u0151\u0152\7q\2\2\u0152l\3\2\2\2\u0153\u0154\7g\2\2\u0154\u0155"+
		"\7p\2\2\u0155\u0156\7f\2\2\u0156n\3\2\2\2\u0157\u0158\7f\2\2\u0158\u0159"+
		"\7g\2\2\u0159\u015a\7n\2\2\u015ap\3\2\2\2\u015b\u015c\7d\2\2\u015c\u015d"+
		"\7t\2\2\u015d\u015e\7g\2\2\u015e\u015f\7c\2\2\u015f\u0160\7m\2\2\u0160"+
		"r\3\2\2\2\u0161\u0162\7e\2\2\u0162\u0163\7q\2\2\u0163\u0164\7p\2\2\u0164"+
		"\u0165\7v\2\2\u0165\u0166\7k\2\2\u0166\u0167\7p\2\2\u0167\u0168\7w\2\2"+
		"\u0168\u0169\7g\2\2\u0169t\3\2\2\2\u016a\u016b\7y\2\2\u016b\u016c\7c\2"+
		"\2\u016c\u016d\7k\2\2\u016d\u016e\7v\2\2\u016ev\3\2\2\2\u016f\u0170\7"+
		"t\2\2\u0170\u0171\7g\2\2\u0171\u0172\7r\2\2\u0172\u0173\7g\2\2\u0173\u0174"+
		"\7c\2\2\u0174\u0175\7v\2\2\u0175x\3\2\2\2\u0176\u0177\7h\2\2\u0177\u0178"+
		"\7q\2\2\u0178\u0179\7t\2\2\u0179\u017a\7g\2\2\u017a\u017b\7x\2\2\u017b"+
		"\u017c\7g\2\2\u017c\u017d\7t\2\2\u017dz\3\2\2\2\u017e\u017f\7p\2\2\u017f"+
		"\u0180\7w\2\2\u0180\u0181\7n\2\2\u0181\u0182\7n\2\2\u0182|\3\2\2\2\u0183"+
		"\u0184\7v\2\2\u0184\u0185\7t\2\2\u0185\u0186\7w\2\2\u0186\u0187\7g\2\2"+
		"\u0187~\3\2\2\2\u0188\u0189\7h\2\2\u0189\u018a\7c\2\2\u018a\u018b\7n\2"+
		"\2\u018b\u018c\7u\2\2\u018c\u018d\7g\2\2\u018d\u0080\3\2\2\2\u018e\u0190"+
		"\t\4\2\2\u018f\u018e\3\2\2\2\u0190\u0191\3\2\2\2\u0191\u018f\3\2\2\2\u0191"+
		"\u0192\3\2\2\2\u0192\u0082\3\2\2\2\u0193\u0195\t\4\2\2\u0194\u0193\3\2"+
		"\2\2\u0195\u0196\3\2\2\2\u0196\u0194\3\2\2\2\u0196\u0197\3\2\2\2\u0197"+
		"\u0198\3\2\2\2\u0198\u019c\7\60\2\2\u0199\u019b\t\4\2\2\u019a\u0199\3"+
		"\2\2\2\u019b\u019e\3\2\2\2\u019c\u019a\3\2\2\2\u019c\u019d\3\2\2\2\u019d"+
		"\u01a8\3\2\2\2\u019e\u019c\3\2\2\2\u019f\u01a1\7\60\2\2\u01a0\u019f\3"+
		"\2\2\2\u01a0\u01a1\3\2\2\2\u01a1\u01a3\3\2\2\2\u01a2\u01a4\t\4\2\2\u01a3"+
		"\u01a2\3\2\2\2\u01a4\u01a5\3\2\2\2\u01a5\u01a3\3\2\2\2\u01a5\u01a6\3\2"+
		"\2\2\u01a6\u01a8\3\2\2\2\u01a7\u0194\3\2\2\2\u01a7\u01a0\3\2\2\2\u01a8"+
		"\u01b2\3\2\2\2\u01a9\u01ab\t\5\2\2\u01aa\u01ac\7/\2\2\u01ab\u01aa\3\2"+
		"\2\2\u01ab\u01ac\3\2\2\2\u01ac\u01ae\3\2\2\2\u01ad\u01af\t\4\2\2\u01ae"+
		"\u01ad\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0\u01ae\3\2\2\2\u01b0\u01b1\3\2"+
		"\2\2\u01b1\u01b3\3\2\2\2\u01b2\u01a9\3\2\2\2\u01b2\u01b3\3\2\2\2\u01b3"+
		"\u0084\3\2\2\2\u01b4\u01ba\7$\2\2\u01b5\u01b6\7^\2\2\u01b6\u01b9\t\6\2"+
		"\2\u01b7\u01b9\n\7\2\2\u01b8\u01b5\3\2\2\2\u01b8\u01b7\3\2\2\2\u01b9\u01bc"+
		"\3\2\2\2\u01ba\u01bb\3\2\2\2\u01ba\u01b8\3\2\2\2\u01bb\u01bd\3\2\2\2\u01bc"+
		"\u01ba\3\2\2\2\u01bd\u01be\7$\2\2\u01be\u0086\3\2\2\2\u01bf\u01c3\t\b"+
		"\2\2\u01c0\u01c2\t\t\2\2\u01c1\u01c0\3\2\2\2\u01c2\u01c5\3\2\2\2\u01c3"+
		"\u01c1\3\2\2\2\u01c3\u01c4\3\2\2\2\u01c4\u0088\3\2\2\2\u01c5\u01c3\3\2"+
		"\2\2\u01c6\u01cd\7&\2\2\u01c7\u01ce\7&\2\2\u01c8\u01ca\t\4\2\2\u01c9\u01c8"+
		"\3\2\2\2\u01ca\u01cb\3\2\2\2\u01cb\u01c9\3\2\2\2\u01cb\u01cc\3\2\2\2\u01cc"+
		"\u01ce\3\2\2\2\u01cd\u01c7\3\2\2\2\u01cd\u01c9\3\2\2\2\u01ce\u008a\3\2"+
		"\2\2\23\2\u008e\u0096\u0191\u0196\u019c\u01a0\u01a5\u01a7\u01ab\u01b0"+
		"\u01b2\u01b8\u01ba\u01c3\u01cb\u01cd\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}