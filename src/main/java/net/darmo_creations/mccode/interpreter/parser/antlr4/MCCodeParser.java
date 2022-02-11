// Generated from /home/damien/IdeaProjects/MC_Code/grammar/MCCode.g4 by ANTLR 4.9.2
package net.darmo_creations.mccode.interpreter.parser.antlr4;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MCCodeParser extends Parser {
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
		CONTINUE=57, WAIT=58, REPEAT=59, FOREVER=60, TRY=61, EXCEPT=62, NULL=63, 
		TRUE=64, FALSE=65, INT=66, FLOAT=67, STRING=68, IDENT=69, CMDARG=70;
	public static final int
		RULE_module = 0, RULE_expression = 1, RULE_import_statement = 2, RULE_global_statement = 3, 
		RULE_statement = 4, RULE_elseif = 5, RULE_else_ = 6, RULE_except = 7, 
		RULE_loop_stmt = 8, RULE_expr = 9;
	private static String[] makeRuleNames() {
		return new String[] {
			"module", "expression", "import_statement", "global_statement", "statement", 
			"elseif", "else_", "except", "loop_stmt", "expr"
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
			"'forever'", "'try'", "'except'", "'null'", "'true'", "'false'"
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
			"FOREVER", "TRY", "EXCEPT", "NULL", "TRUE", "FALSE", "INT", "FLOAT", 
			"STRING", "IDENT", "CMDARG"
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

	@Override
	public String getGrammarFileName() { return "MCCode.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MCCodeParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ModuleContext extends ParserRuleContext {
		public Token ticks;
		public Token times;
		public TerminalNode EOF() { return getToken(MCCodeParser.EOF, 0); }
		public TerminalNode SCHED() { return getToken(MCCodeParser.SCHED, 0); }
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public List<Import_statementContext> import_statement() {
			return getRuleContexts(Import_statementContext.class);
		}
		public Import_statementContext import_statement(int i) {
			return getRuleContext(Import_statementContext.class,i);
		}
		public List<Global_statementContext> global_statement() {
			return getRuleContexts(Global_statementContext.class);
		}
		public Global_statementContext global_statement(int i) {
			return getRuleContext(Global_statementContext.class,i);
		}
		public List<TerminalNode> INT() { return getTokens(MCCodeParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(MCCodeParser.INT, i);
		}
		public TerminalNode REPEAT() { return getToken(MCCodeParser.REPEAT, 0); }
		public TerminalNode FOREVER() { return getToken(MCCodeParser.FOREVER, 0); }
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitModule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitModule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_module);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(27);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SCHED) {
				{
				setState(20);
				match(SCHED);
				setState(21);
				((ModuleContext)_localctx).ticks = match(INT);
				setState(24);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==REPEAT) {
					{
					setState(22);
					match(REPEAT);
					setState(23);
					((ModuleContext)_localctx).times = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==FOREVER || _la==INT) ) {
						((ModuleContext)_localctx).times = (Token)_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				setState(26);
				match(SEMIC);
				}
			}

			setState(32);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(29);
				import_statement();
				}
				}
				setState(34);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(38);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << PUBLIC) | (1L << FUNC) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << TRY) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
				{
				{
				setState(35);
				global_statement();
				}
				}
				setState(40);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(41);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MCCodeParser.EOF, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			expr(0);
			setState(44);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Import_statementContext extends ParserRuleContext {
		public Token alias;
		public TerminalNode IMPORT() { return getToken(MCCodeParser.IMPORT, 0); }
		public List<TerminalNode> IDENT() { return getTokens(MCCodeParser.IDENT); }
		public TerminalNode IDENT(int i) {
			return getToken(MCCodeParser.IDENT, i);
		}
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public List<TerminalNode> DOT() { return getTokens(MCCodeParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(MCCodeParser.DOT, i);
		}
		public TerminalNode AS() { return getToken(MCCodeParser.AS, 0); }
		public Import_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_import_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterImport_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitImport_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitImport_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Import_statementContext import_statement() throws RecognitionException {
		Import_statementContext _localctx = new Import_statementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_import_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			match(IMPORT);
			setState(47);
			match(IDENT);
			setState(52);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(48);
				match(DOT);
				setState(49);
				match(IDENT);
				}
				}
				setState(54);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(57);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(55);
				match(AS);
				setState(56);
				((Import_statementContext)_localctx).alias = match(IDENT);
				}
			}

			setState(59);
			match(SEMIC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Global_statementContext extends ParserRuleContext {
		public Global_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_global_statement; }
	 
		public Global_statementContext() { }
		public void copyFrom(Global_statementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DefineFunctionStatementContext extends Global_statementContext {
		public Token name;
		public TerminalNode FUNC() { return getToken(MCCodeParser.FUNC, 0); }
		public TerminalNode LPAREN() { return getToken(MCCodeParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MCCodeParser.RPAREN, 0); }
		public TerminalNode END() { return getToken(MCCodeParser.END, 0); }
		public List<TerminalNode> IDENT() { return getTokens(MCCodeParser.IDENT); }
		public TerminalNode IDENT(int i) {
			return getToken(MCCodeParser.IDENT, i);
		}
		public TerminalNode PUBLIC() { return getToken(MCCodeParser.PUBLIC, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MCCodeParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MCCodeParser.COMMA, i);
		}
		public DefineFunctionStatementContext(Global_statementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterDefineFunctionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitDefineFunctionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitDefineFunctionStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DeclareGlobalVariableContext extends Global_statementContext {
		public Token name;
		public ExprContext value;
		public TerminalNode PUBLIC() { return getToken(MCCodeParser.PUBLIC, 0); }
		public TerminalNode VAR() { return getToken(MCCodeParser.VAR, 0); }
		public TerminalNode ASSIGN() { return getToken(MCCodeParser.ASSIGN, 0); }
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public TerminalNode IDENT() { return getToken(MCCodeParser.IDENT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EDITABLE() { return getToken(MCCodeParser.EDITABLE, 0); }
		public DeclareGlobalVariableContext(Global_statementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterDeclareGlobalVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitDeclareGlobalVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitDeclareGlobalVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DeclareGlobalConstantContext extends Global_statementContext {
		public Token name;
		public ExprContext value;
		public TerminalNode PUBLIC() { return getToken(MCCodeParser.PUBLIC, 0); }
		public TerminalNode CONST() { return getToken(MCCodeParser.CONST, 0); }
		public TerminalNode ASSIGN() { return getToken(MCCodeParser.ASSIGN, 0); }
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public TerminalNode IDENT() { return getToken(MCCodeParser.IDENT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public DeclareGlobalConstantContext(Global_statementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterDeclareGlobalConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitDeclareGlobalConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitDeclareGlobalConstant(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StmtContext extends Global_statementContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public StmtContext(Global_statementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Global_statementContext global_statement() throws RecognitionException {
		Global_statementContext _localctx = new Global_statementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_global_statement);
		int _la;
		try {
			int _alt;
			setState(106);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				_localctx = new DeclareGlobalVariableContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(61);
				match(PUBLIC);
				setState(63);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EDITABLE) {
					{
					setState(62);
					match(EDITABLE);
					}
				}

				setState(65);
				match(VAR);
				setState(66);
				((DeclareGlobalVariableContext)_localctx).name = match(IDENT);
				setState(67);
				match(ASSIGN);
				setState(68);
				((DeclareGlobalVariableContext)_localctx).value = expr(0);
				setState(69);
				match(SEMIC);
				}
				break;
			case 2:
				_localctx = new DeclareGlobalConstantContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				match(PUBLIC);
				setState(72);
				match(CONST);
				setState(73);
				((DeclareGlobalConstantContext)_localctx).name = match(IDENT);
				setState(74);
				match(ASSIGN);
				setState(75);
				((DeclareGlobalConstantContext)_localctx).value = expr(0);
				setState(76);
				match(SEMIC);
				}
				break;
			case 3:
				_localctx = new DefineFunctionStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PUBLIC) {
					{
					setState(78);
					match(PUBLIC);
					}
				}

				setState(81);
				match(FUNC);
				setState(82);
				((DefineFunctionStatementContext)_localctx).name = match(IDENT);
				setState(83);
				match(LPAREN);
				setState(95);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENT) {
					{
					setState(84);
					match(IDENT);
					setState(89);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(85);
							match(COMMA);
							setState(86);
							match(IDENT);
							}
							} 
						}
						setState(91);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
					}
					setState(93);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(92);
						match(COMMA);
						}
					}

					}
				}

				setState(97);
				match(RPAREN);
				setState(101);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << TRY) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
					{
					{
					setState(98);
					statement();
					}
					}
					setState(103);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(104);
				match(END);
				}
				break;
			case 4:
				_localctx = new StmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(105);
				statement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SetItemStatementContext extends StatementContext {
		public ExprContext target;
		public ExprContext key;
		public Token operator;
		public ExprContext value;
		public TerminalNode LBRACK() { return getToken(MCCodeParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(MCCodeParser.RBRACK, 0); }
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ASSIGN() { return getToken(MCCodeParser.ASSIGN, 0); }
		public TerminalNode PLUSA() { return getToken(MCCodeParser.PLUSA, 0); }
		public TerminalNode MINUSA() { return getToken(MCCodeParser.MINUSA, 0); }
		public TerminalNode MULA() { return getToken(MCCodeParser.MULA, 0); }
		public TerminalNode DIVA() { return getToken(MCCodeParser.DIVA, 0); }
		public TerminalNode INTDIVA() { return getToken(MCCodeParser.INTDIVA, 0); }
		public TerminalNode MODA() { return getToken(MCCodeParser.MODA, 0); }
		public TerminalNode POWERA() { return getToken(MCCodeParser.POWERA, 0); }
		public SetItemStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterSetItemStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitSetItemStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitSetItemStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DeleteItemStatementContext extends StatementContext {
		public ExprContext target;
		public ExprContext key;
		public TerminalNode DELETE() { return getToken(MCCodeParser.DELETE, 0); }
		public TerminalNode LBRACK() { return getToken(MCCodeParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(MCCodeParser.RBRACK, 0); }
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public DeleteItemStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterDeleteItemStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitDeleteItemStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitDeleteItemStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WhileLoopStatementContext extends StatementContext {
		public ExprContext cond;
		public TerminalNode WHILE() { return getToken(MCCodeParser.WHILE, 0); }
		public TerminalNode DO() { return getToken(MCCodeParser.DO, 0); }
		public TerminalNode END() { return getToken(MCCodeParser.END, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<Loop_stmtContext> loop_stmt() {
			return getRuleContexts(Loop_stmtContext.class);
		}
		public Loop_stmtContext loop_stmt(int i) {
			return getRuleContext(Loop_stmtContext.class,i);
		}
		public WhileLoopStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterWhileLoopStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitWhileLoopStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitWhileLoopStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DeclareVariableStatementContext extends StatementContext {
		public Token name;
		public ExprContext value;
		public TerminalNode ASSIGN() { return getToken(MCCodeParser.ASSIGN, 0); }
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public TerminalNode VAR() { return getToken(MCCodeParser.VAR, 0); }
		public TerminalNode CONST() { return getToken(MCCodeParser.CONST, 0); }
		public TerminalNode IDENT() { return getToken(MCCodeParser.IDENT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public DeclareVariableStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterDeclareVariableStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitDeclareVariableStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitDeclareVariableStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ForLoopStatementContext extends StatementContext {
		public Token variable;
		public ExprContext range;
		public TerminalNode FOR() { return getToken(MCCodeParser.FOR, 0); }
		public TerminalNode IN() { return getToken(MCCodeParser.IN, 0); }
		public TerminalNode DO() { return getToken(MCCodeParser.DO, 0); }
		public TerminalNode END() { return getToken(MCCodeParser.END, 0); }
		public TerminalNode IDENT() { return getToken(MCCodeParser.IDENT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<Loop_stmtContext> loop_stmt() {
			return getRuleContexts(Loop_stmtContext.class);
		}
		public Loop_stmtContext loop_stmt(int i) {
			return getRuleContext(Loop_stmtContext.class,i);
		}
		public ForLoopStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterForLoopStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitForLoopStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitForLoopStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariableAssignmentStatementContext extends StatementContext {
		public Token name;
		public Token operator;
		public ExprContext value;
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public TerminalNode IDENT() { return getToken(MCCodeParser.IDENT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(MCCodeParser.ASSIGN, 0); }
		public TerminalNode PLUSA() { return getToken(MCCodeParser.PLUSA, 0); }
		public TerminalNode MINUSA() { return getToken(MCCodeParser.MINUSA, 0); }
		public TerminalNode MULA() { return getToken(MCCodeParser.MULA, 0); }
		public TerminalNode DIVA() { return getToken(MCCodeParser.DIVA, 0); }
		public TerminalNode INTDIVA() { return getToken(MCCodeParser.INTDIVA, 0); }
		public TerminalNode MODA() { return getToken(MCCodeParser.MODA, 0); }
		public TerminalNode POWERA() { return getToken(MCCodeParser.POWERA, 0); }
		public VariableAssignmentStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterVariableAssignmentStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitVariableAssignmentStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitVariableAssignmentStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DeleteStatementContext extends StatementContext {
		public Token name;
		public TerminalNode DELETE() { return getToken(MCCodeParser.DELETE, 0); }
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public TerminalNode IDENT() { return getToken(MCCodeParser.IDENT, 0); }
		public DeleteStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterDeleteStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitDeleteStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitDeleteStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WaitStatementContext extends StatementContext {
		public TerminalNode WAIT() { return getToken(MCCodeParser.WAIT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public WaitStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterWaitStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitWaitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitWaitStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfStatementContext extends StatementContext {
		public ExprContext cond;
		public TerminalNode IF() { return getToken(MCCodeParser.IF, 0); }
		public TerminalNode THEN() { return getToken(MCCodeParser.THEN, 0); }
		public TerminalNode END() { return getToken(MCCodeParser.END, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<ElseifContext> elseif() {
			return getRuleContexts(ElseifContext.class);
		}
		public ElseifContext elseif(int i) {
			return getRuleContext(ElseifContext.class,i);
		}
		public Else_Context else_() {
			return getRuleContext(Else_Context.class,0);
		}
		public IfStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TryExceptStatementContext extends StatementContext {
		public TerminalNode TRY() { return getToken(MCCodeParser.TRY, 0); }
		public ExceptContext except() {
			return getRuleContext(ExceptContext.class,0);
		}
		public TerminalNode END() { return getToken(MCCodeParser.END, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TryExceptStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterTryExceptStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitTryExceptStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitTryExceptStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetPropertyStatementContext extends StatementContext {
		public ExprContext target;
		public Token name;
		public Token operator;
		public ExprContext value;
		public TerminalNode DOT() { return getToken(MCCodeParser.DOT, 0); }
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode IDENT() { return getToken(MCCodeParser.IDENT, 0); }
		public TerminalNode ASSIGN() { return getToken(MCCodeParser.ASSIGN, 0); }
		public TerminalNode PLUSA() { return getToken(MCCodeParser.PLUSA, 0); }
		public TerminalNode MINUSA() { return getToken(MCCodeParser.MINUSA, 0); }
		public TerminalNode MULA() { return getToken(MCCodeParser.MULA, 0); }
		public TerminalNode DIVA() { return getToken(MCCodeParser.DIVA, 0); }
		public TerminalNode INTDIVA() { return getToken(MCCodeParser.INTDIVA, 0); }
		public TerminalNode MODA() { return getToken(MCCodeParser.MODA, 0); }
		public TerminalNode POWERA() { return getToken(MCCodeParser.POWERA, 0); }
		public SetPropertyStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterSetPropertyStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitSetPropertyStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitSetPropertyStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionStatementContext extends StatementContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public ExpressionStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterExpressionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitExpressionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitExpressionStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReturnStatementContext extends StatementContext {
		public ExprContext returned;
		public TerminalNode RETURN() { return getToken(MCCodeParser.RETURN, 0); }
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ReturnStatementContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterReturnStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitReturnStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitReturnStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_statement);
		int _la;
		try {
			setState(210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				_localctx = new DeclareVariableStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(108);
				_la = _input.LA(1);
				if ( !(_la==VAR || _la==CONST) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(109);
				((DeclareVariableStatementContext)_localctx).name = match(IDENT);
				setState(110);
				match(ASSIGN);
				setState(111);
				((DeclareVariableStatementContext)_localctx).value = expr(0);
				setState(112);
				match(SEMIC);
				}
				break;
			case 2:
				_localctx = new DeleteStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(114);
				match(DELETE);
				setState(115);
				((DeleteStatementContext)_localctx).name = match(IDENT);
				setState(116);
				match(SEMIC);
				}
				break;
			case 3:
				_localctx = new DeleteItemStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(117);
				match(DELETE);
				setState(118);
				((DeleteItemStatementContext)_localctx).target = expr(0);
				setState(119);
				match(LBRACK);
				setState(120);
				((DeleteItemStatementContext)_localctx).key = expr(0);
				setState(121);
				match(RBRACK);
				setState(122);
				match(SEMIC);
				}
				break;
			case 4:
				_localctx = new IfStatementContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(124);
				match(IF);
				setState(125);
				((IfStatementContext)_localctx).cond = expr(0);
				setState(126);
				match(THEN);
				setState(130);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << TRY) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
					{
					{
					setState(127);
					statement();
					}
					}
					setState(132);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ELIF) {
					{
					{
					setState(133);
					elseif();
					}
					}
					setState(138);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(140);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(139);
					else_();
					}
				}

				setState(142);
				match(END);
				}
				break;
			case 5:
				_localctx = new WhileLoopStatementContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(144);
				match(WHILE);
				setState(145);
				((WhileLoopStatementContext)_localctx).cond = expr(0);
				setState(146);
				match(DO);
				setState(150);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << WAIT) | (1L << TRY) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
					{
					{
					setState(147);
					loop_stmt();
					}
					}
					setState(152);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(153);
				match(END);
				}
				break;
			case 6:
				_localctx = new ForLoopStatementContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(155);
				match(FOR);
				setState(156);
				((ForLoopStatementContext)_localctx).variable = match(IDENT);
				setState(157);
				match(IN);
				setState(158);
				((ForLoopStatementContext)_localctx).range = expr(0);
				setState(159);
				match(DO);
				setState(163);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << WAIT) | (1L << TRY) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
					{
					{
					setState(160);
					loop_stmt();
					}
					}
					setState(165);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(166);
				match(END);
				}
				break;
			case 7:
				_localctx = new TryExceptStatementContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(168);
				match(TRY);
				setState(172);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << TRY) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
					{
					{
					setState(169);
					statement();
					}
					}
					setState(174);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(175);
				except();
				setState(176);
				match(END);
				}
				break;
			case 8:
				_localctx = new WaitStatementContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(178);
				match(WAIT);
				setState(179);
				expr(0);
				setState(180);
				match(SEMIC);
				}
				break;
			case 9:
				_localctx = new ReturnStatementContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(182);
				match(RETURN);
				setState(184);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
					{
					setState(183);
					((ReturnStatementContext)_localctx).returned = expr(0);
					}
				}

				setState(186);
				match(SEMIC);
				}
				break;
			case 10:
				_localctx = new VariableAssignmentStatementContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(187);
				((VariableAssignmentStatementContext)_localctx).name = match(IDENT);
				setState(188);
				((VariableAssignmentStatementContext)_localctx).operator = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ASSIGN) | (1L << PLUSA) | (1L << MINUSA) | (1L << MULA) | (1L << DIVA) | (1L << INTDIVA) | (1L << MODA) | (1L << POWERA))) != 0)) ) {
					((VariableAssignmentStatementContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(189);
				((VariableAssignmentStatementContext)_localctx).value = expr(0);
				setState(190);
				match(SEMIC);
				}
				break;
			case 11:
				_localctx = new SetItemStatementContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(192);
				((SetItemStatementContext)_localctx).target = expr(0);
				setState(193);
				match(LBRACK);
				setState(194);
				((SetItemStatementContext)_localctx).key = expr(0);
				setState(195);
				match(RBRACK);
				setState(196);
				((SetItemStatementContext)_localctx).operator = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ASSIGN) | (1L << PLUSA) | (1L << MINUSA) | (1L << MULA) | (1L << DIVA) | (1L << INTDIVA) | (1L << MODA) | (1L << POWERA))) != 0)) ) {
					((SetItemStatementContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(197);
				((SetItemStatementContext)_localctx).value = expr(0);
				setState(198);
				match(SEMIC);
				}
				break;
			case 12:
				_localctx = new SetPropertyStatementContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(200);
				((SetPropertyStatementContext)_localctx).target = expr(0);
				setState(201);
				match(DOT);
				setState(202);
				((SetPropertyStatementContext)_localctx).name = match(IDENT);
				setState(203);
				((SetPropertyStatementContext)_localctx).operator = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ASSIGN) | (1L << PLUSA) | (1L << MINUSA) | (1L << MULA) | (1L << DIVA) | (1L << INTDIVA) | (1L << MODA) | (1L << POWERA))) != 0)) ) {
					((SetPropertyStatementContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(204);
				((SetPropertyStatementContext)_localctx).value = expr(0);
				setState(205);
				match(SEMIC);
				}
				break;
			case 13:
				_localctx = new ExpressionStatementContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(207);
				expr(0);
				setState(208);
				match(SEMIC);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElseifContext extends ParserRuleContext {
		public ExprContext cond;
		public TerminalNode ELIF() { return getToken(MCCodeParser.ELIF, 0); }
		public TerminalNode THEN() { return getToken(MCCodeParser.THEN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public ElseifContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseif; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterElseif(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitElseif(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitElseif(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseifContext elseif() throws RecognitionException {
		ElseifContext _localctx = new ElseifContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_elseif);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			match(ELIF);
			setState(213);
			((ElseifContext)_localctx).cond = expr(0);
			setState(214);
			match(THEN);
			setState(218);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << TRY) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
				{
				{
				setState(215);
				statement();
				}
				}
				setState(220);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Else_Context extends ParserRuleContext {
		public TerminalNode ELSE() { return getToken(MCCodeParser.ELSE, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public Else_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_else_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterElse_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitElse_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitElse_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Else_Context else_() throws RecognitionException {
		Else_Context _localctx = new Else_Context(_ctx, getState());
		enterRule(_localctx, 12, RULE_else_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(ELSE);
			setState(225);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << TRY) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
				{
				{
				setState(222);
				statement();
				}
				}
				setState(227);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExceptContext extends ParserRuleContext {
		public TerminalNode EXCEPT() { return getToken(MCCodeParser.EXCEPT, 0); }
		public TerminalNode IDENT() { return getToken(MCCodeParser.IDENT, 0); }
		public TerminalNode THEN() { return getToken(MCCodeParser.THEN, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public ExceptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_except; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterExcept(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitExcept(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitExcept(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExceptContext except() throws RecognitionException {
		ExceptContext _localctx = new ExceptContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_except);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			match(EXCEPT);
			setState(229);
			match(IDENT);
			setState(230);
			match(THEN);
			setState(234);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << TRY) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
				{
				{
				setState(231);
				statement();
				}
				}
				setState(236);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Loop_stmtContext extends ParserRuleContext {
		public Loop_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loop_stmt; }
	 
		public Loop_stmtContext() { }
		public void copyFrom(Loop_stmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BreakStatementContext extends Loop_stmtContext {
		public TerminalNode BREAK() { return getToken(MCCodeParser.BREAK, 0); }
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public BreakStatementContext(Loop_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterBreakStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitBreakStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitBreakStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ContinueStatementContext extends Loop_stmtContext {
		public TerminalNode CONTINUE() { return getToken(MCCodeParser.CONTINUE, 0); }
		public TerminalNode SEMIC() { return getToken(MCCodeParser.SEMIC, 0); }
		public ContinueStatementContext(Loop_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterContinueStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitContinueStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitContinueStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Statement_Context extends Loop_stmtContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public Statement_Context(Loop_stmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterStatement_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitStatement_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitStatement_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Loop_stmtContext loop_stmt() throws RecognitionException {
		Loop_stmtContext _localctx = new Loop_stmtContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_loop_stmt);
		try {
			setState(242);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BREAK:
				_localctx = new BreakStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(237);
				match(BREAK);
				setState(238);
				match(SEMIC);
				}
				break;
			case CONTINUE:
				_localctx = new ContinueStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(239);
				match(CONTINUE);
				setState(240);
				match(SEMIC);
				}
				break;
			case LPAREN:
			case LBRACK:
			case LCURL:
			case MINUS:
			case NOT:
			case VAR:
			case CONST:
			case RETURN:
			case IF:
			case WHILE:
			case FOR:
			case DELETE:
			case WAIT:
			case TRY:
			case NULL:
			case TRUE:
			case FALSE:
			case INT:
			case FLOAT:
			case STRING:
			case IDENT:
			case CMDARG:
				_localctx = new Statement_Context(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(241);
				statement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class VariableContext extends ExprContext {
		public TerminalNode IDENT() { return getToken(MCCodeParser.IDENT, 0); }
		public TerminalNode CMDARG() { return getToken(MCCodeParser.CMDARG, 0); }
		public VariableContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FloatLiteralContext extends ExprContext {
		public TerminalNode FLOAT() { return getToken(MCCodeParser.FLOAT, 0); }
		public FloatLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterFloatLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitFloatLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitFloatLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MethodCallContext extends ExprContext {
		public ExprContext object;
		public Token property;
		public TerminalNode DOT() { return getToken(MCCodeParser.DOT, 0); }
		public TerminalNode LPAREN() { return getToken(MCCodeParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MCCodeParser.RPAREN, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode IDENT() { return getToken(MCCodeParser.IDENT, 0); }
		public List<TerminalNode> COMMA() { return getTokens(MCCodeParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MCCodeParser.COMMA, i);
		}
		public MethodCallContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterMethodCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitMethodCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitMethodCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GetItemContext extends ExprContext {
		public ExprContext source;
		public ExprContext key;
		public TerminalNode LBRACK() { return getToken(MCCodeParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(MCCodeParser.RBRACK, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public GetItemContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterGetItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitGetItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitGetItem(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ListLiteralContext extends ExprContext {
		public TerminalNode LBRACK() { return getToken(MCCodeParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(MCCodeParser.RBRACK, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MCCodeParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MCCodeParser.COMMA, i);
		}
		public ListLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterListLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitListLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitListLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MapLiteralContext extends ExprContext {
		public TerminalNode LCURL() { return getToken(MCCodeParser.LCURL, 0); }
		public TerminalNode RCURL() { return getToken(MCCodeParser.RCURL, 0); }
		public List<TerminalNode> STRING() { return getTokens(MCCodeParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(MCCodeParser.STRING, i);
		}
		public List<TerminalNode> COLON() { return getTokens(MCCodeParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(MCCodeParser.COLON, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MCCodeParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MCCodeParser.COMMA, i);
		}
		public MapLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterMapLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitMapLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitMapLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolLiteralContext extends ExprContext {
		public TerminalNode TRUE() { return getToken(MCCodeParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(MCCodeParser.FALSE, 0); }
		public BoolLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterBoolLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitBoolLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitBoolLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringLiteralContext extends ExprContext {
		public TerminalNode STRING() { return getToken(MCCodeParser.STRING, 0); }
		public StringLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntLiteralContext extends ExprContext {
		public TerminalNode INT() { return getToken(MCCodeParser.INT, 0); }
		public IntLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterIntLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitIntLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitIntLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GetPropertyContext extends ExprContext {
		public ExprContext object;
		public Token property;
		public TerminalNode DOT() { return getToken(MCCodeParser.DOT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(MCCodeParser.IDENT, 0); }
		public GetPropertyContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterGetProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitGetProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitGetProperty(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionCallContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LPAREN() { return getToken(MCCodeParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MCCodeParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(MCCodeParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MCCodeParser.COMMA, i);
		}
		public FunctionCallContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryOperatorContext extends ExprContext {
		public Token operator;
		public ExprContext operand;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode MINUS() { return getToken(MCCodeParser.MINUS, 0); }
		public TerminalNode NOT() { return getToken(MCCodeParser.NOT, 0); }
		public UnaryOperatorContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterUnaryOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitUnaryOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitUnaryOperator(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BinaryOperatorContext extends ExprContext {
		public ExprContext left;
		public Token operator;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode POWER() { return getToken(MCCodeParser.POWER, 0); }
		public TerminalNode MUL() { return getToken(MCCodeParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(MCCodeParser.DIV, 0); }
		public TerminalNode INTDIV() { return getToken(MCCodeParser.INTDIV, 0); }
		public TerminalNode MOD() { return getToken(MCCodeParser.MOD, 0); }
		public TerminalNode PLUS() { return getToken(MCCodeParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(MCCodeParser.MINUS, 0); }
		public TerminalNode IN() { return getToken(MCCodeParser.IN, 0); }
		public TerminalNode NOT() { return getToken(MCCodeParser.NOT, 0); }
		public TerminalNode EQUAL() { return getToken(MCCodeParser.EQUAL, 0); }
		public TerminalNode NEQUAL() { return getToken(MCCodeParser.NEQUAL, 0); }
		public TerminalNode GT() { return getToken(MCCodeParser.GT, 0); }
		public TerminalNode GE() { return getToken(MCCodeParser.GE, 0); }
		public TerminalNode LT() { return getToken(MCCodeParser.LT, 0); }
		public TerminalNode LE() { return getToken(MCCodeParser.LE, 0); }
		public TerminalNode AND() { return getToken(MCCodeParser.AND, 0); }
		public TerminalNode OR() { return getToken(MCCodeParser.OR, 0); }
		public BinaryOperatorContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterBinaryOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitBinaryOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitBinaryOperator(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullLiteralContext extends ExprContext {
		public TerminalNode NULL() { return getToken(MCCodeParser.NULL, 0); }
		public NullLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterNullLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitNullLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitNullLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetLiteralContext extends ExprContext {
		public TerminalNode LCURL() { return getToken(MCCodeParser.LCURL, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RCURL() { return getToken(MCCodeParser.RCURL, 0); }
		public List<TerminalNode> COMMA() { return getTokens(MCCodeParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MCCodeParser.COMMA, i);
		}
		public SetLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterSetLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitSetLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitSetLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenthesesContext extends ExprContext {
		public ExprContext exp;
		public TerminalNode LPAREN() { return getToken(MCCodeParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MCCodeParser.RPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParenthesesContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).enterParentheses(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MCCodeListener ) ((MCCodeListener)listener).exitParentheses(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MCCodeVisitor ) return ((MCCodeVisitor<? extends T>)visitor).visitParentheses(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 18;
		enterRecursionRule(_localctx, 18, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				_localctx = new ParenthesesContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(245);
				match(LPAREN);
				setState(246);
				((ParenthesesContext)_localctx).exp = expr(0);
				setState(247);
				match(RPAREN);
				}
				break;
			case 2:
				{
				_localctx = new NullLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(249);
				match(NULL);
				}
				break;
			case 3:
				{
				_localctx = new BoolLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(250);
				match(TRUE);
				}
				break;
			case 4:
				{
				_localctx = new BoolLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(251);
				match(FALSE);
				}
				break;
			case 5:
				{
				_localctx = new IntLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(252);
				match(INT);
				}
				break;
			case 6:
				{
				_localctx = new FloatLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(253);
				match(FLOAT);
				}
				break;
			case 7:
				{
				_localctx = new StringLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(254);
				match(STRING);
				}
				break;
			case 8:
				{
				_localctx = new ListLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(255);
				match(LBRACK);
				setState(267);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
					{
					setState(256);
					expr(0);
					setState(261);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(257);
							match(COMMA);
							setState(258);
							expr(0);
							}
							} 
						}
						setState(263);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
					}
					setState(265);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(264);
						match(COMMA);
						}
					}

					}
				}

				setState(269);
				match(RBRACK);
				}
				break;
			case 9:
				{
				_localctx = new MapLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(270);
				match(LCURL);
				setState(286);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STRING) {
					{
					setState(271);
					match(STRING);
					setState(272);
					match(COLON);
					setState(273);
					expr(0);
					setState(280);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(274);
							match(COMMA);
							setState(275);
							match(STRING);
							setState(276);
							match(COLON);
							setState(277);
							expr(0);
							}
							} 
						}
						setState(282);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
					}
					setState(284);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(283);
						match(COMMA);
						}
					}

					}
				}

				setState(288);
				match(RCURL);
				}
				break;
			case 10:
				{
				_localctx = new SetLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(289);
				match(LCURL);
				setState(290);
				expr(0);
				setState(295);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(291);
						match(COMMA);
						setState(292);
						expr(0);
						}
						} 
					}
					setState(297);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
				}
				setState(299);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(298);
					match(COMMA);
					}
				}

				setState(301);
				match(RCURL);
				}
				break;
			case 11:
				{
				_localctx = new UnaryOperatorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(303);
				((UnaryOperatorContext)_localctx).operator = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==MINUS || _la==NOT) ) {
					((UnaryOperatorContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(304);
				((UnaryOperatorContext)_localctx).operand = expr(11);
				}
				break;
			case 12:
				{
				_localctx = new VariableContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(305);
				match(IDENT);
				}
				break;
			case 13:
				{
				_localctx = new VariableContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(306);
				match(CMDARG);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(377);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(375);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(309);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(310);
						((BinaryOperatorContext)_localctx).operator = match(POWER);
						setState(311);
						((BinaryOperatorContext)_localctx).right = expr(10);
						}
						break;
					case 2:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(312);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(313);
						((BinaryOperatorContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << INTDIV) | (1L << MOD))) != 0)) ) {
							((BinaryOperatorContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(314);
						((BinaryOperatorContext)_localctx).right = expr(9);
						}
						break;
					case 3:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(315);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(316);
						((BinaryOperatorContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((BinaryOperatorContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(317);
						((BinaryOperatorContext)_localctx).right = expr(8);
						}
						break;
					case 4:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(318);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(320);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==NOT) {
							{
							setState(319);
							match(NOT);
							}
						}

						setState(322);
						match(IN);
						setState(323);
						((BinaryOperatorContext)_localctx).right = expr(7);
						}
						break;
					case 5:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(324);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(325);
						((BinaryOperatorContext)_localctx).operator = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUAL) | (1L << NEQUAL) | (1L << GT) | (1L << GE) | (1L << LT) | (1L << LE))) != 0)) ) {
							((BinaryOperatorContext)_localctx).operator = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(326);
						((BinaryOperatorContext)_localctx).right = expr(6);
						}
						break;
					case 6:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(327);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(328);
						((BinaryOperatorContext)_localctx).operator = match(AND);
						setState(329);
						((BinaryOperatorContext)_localctx).right = expr(5);
						}
						break;
					case 7:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(330);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(331);
						((BinaryOperatorContext)_localctx).operator = match(OR);
						setState(332);
						((BinaryOperatorContext)_localctx).right = expr(4);
						}
						break;
					case 8:
						{
						_localctx = new MethodCallContext(new ExprContext(_parentctx, _parentState));
						((MethodCallContext)_localctx).object = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(333);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(334);
						match(DOT);
						setState(335);
						((MethodCallContext)_localctx).property = match(IDENT);
						setState(336);
						match(LPAREN);
						setState(348);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
							{
							setState(337);
							expr(0);
							setState(342);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
							while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
								if ( _alt==1 ) {
									{
									{
									setState(338);
									match(COMMA);
									setState(339);
									expr(0);
									}
									} 
								}
								setState(344);
								_errHandler.sync(this);
								_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
							}
							setState(346);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==COMMA) {
								{
								setState(345);
								match(COMMA);
								}
							}

							}
						}

						setState(350);
						match(RPAREN);
						}
						break;
					case 9:
						{
						_localctx = new GetPropertyContext(new ExprContext(_parentctx, _parentState));
						((GetPropertyContext)_localctx).object = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(351);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(352);
						match(DOT);
						setState(353);
						((GetPropertyContext)_localctx).property = match(IDENT);
						}
						break;
					case 10:
						{
						_localctx = new FunctionCallContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(354);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(355);
						match(LPAREN);
						setState(367);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << NULL))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (TRUE - 64)) | (1L << (FALSE - 64)) | (1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)) | (1L << (CMDARG - 64)))) != 0)) {
							{
							setState(356);
							expr(0);
							setState(361);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
							while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
								if ( _alt==1 ) {
									{
									{
									setState(357);
									match(COMMA);
									setState(358);
									expr(0);
									}
									} 
								}
								setState(363);
								_errHandler.sync(this);
								_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
							}
							setState(365);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==COMMA) {
								{
								setState(364);
								match(COMMA);
								}
							}

							}
						}

						setState(369);
						match(RPAREN);
						}
						break;
					case 11:
						{
						_localctx = new GetItemContext(new ExprContext(_parentctx, _parentState));
						((GetItemContext)_localctx).source = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(370);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(371);
						match(LBRACK);
						setState(372);
						((GetItemContext)_localctx).key = expr(0);
						setState(373);
						match(RBRACK);
						}
						break;
					}
					} 
				}
				setState(379);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 9:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 9);
		case 1:
			return precpred(_ctx, 8);
		case 2:
			return precpred(_ctx, 7);
		case 3:
			return precpred(_ctx, 6);
		case 4:
			return precpred(_ctx, 5);
		case 5:
			return precpred(_ctx, 4);
		case 6:
			return precpred(_ctx, 3);
		case 7:
			return precpred(_ctx, 14);
		case 8:
			return precpred(_ctx, 13);
		case 9:
			return precpred(_ctx, 12);
		case 10:
			return precpred(_ctx, 10);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3H\u017f\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\3\2\3\2\3\2\3\2\5\2\33\n\2\3\2\5\2\36\n\2\3\2\7\2!\n\2\f\2\16\2$\13"+
		"\2\3\2\7\2\'\n\2\f\2\16\2*\13\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\7"+
		"\4\65\n\4\f\4\16\48\13\4\3\4\3\4\5\4<\n\4\3\4\3\4\3\5\3\5\5\5B\n\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5R\n\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\7\5Z\n\5\f\5\16\5]\13\5\3\5\5\5`\n\5\5\5b\n\5\3\5\3\5"+
		"\7\5f\n\5\f\5\16\5i\13\5\3\5\3\5\5\5m\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\7\6\u0083\n\6\f\6"+
		"\16\6\u0086\13\6\3\6\7\6\u0089\n\6\f\6\16\6\u008c\13\6\3\6\5\6\u008f\n"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\7\6\u0097\n\6\f\6\16\6\u009a\13\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\7\6\u00a4\n\6\f\6\16\6\u00a7\13\6\3\6\3\6\3\6\3"+
		"\6\7\6\u00ad\n\6\f\6\16\6\u00b0\13\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\5\6\u00bb\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u00d5\n\6\3\7\3\7\3\7\3"+
		"\7\7\7\u00db\n\7\f\7\16\7\u00de\13\7\3\b\3\b\7\b\u00e2\n\b\f\b\16\b\u00e5"+
		"\13\b\3\t\3\t\3\t\3\t\7\t\u00eb\n\t\f\t\16\t\u00ee\13\t\3\n\3\n\3\n\3"+
		"\n\3\n\5\n\u00f5\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\7\13\u0106\n\13\f\13\16\13\u0109\13\13\3\13"+
		"\5\13\u010c\n\13\5\13\u010e\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\7\13\u0119\n\13\f\13\16\13\u011c\13\13\3\13\5\13\u011f\n\13\5"+
		"\13\u0121\n\13\3\13\3\13\3\13\3\13\3\13\7\13\u0128\n\13\f\13\16\13\u012b"+
		"\13\13\3\13\5\13\u012e\n\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u0136\n"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u0143"+
		"\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\7\13\u0157\n\13\f\13\16\13\u015a\13\13\3\13"+
		"\5\13\u015d\n\13\5\13\u015f\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\7\13\u016a\n\13\f\13\16\13\u016d\13\13\3\13\5\13\u0170\n\13\5"+
		"\13\u0172\n\13\3\13\3\13\3\13\3\13\3\13\3\13\7\13\u017a\n\13\f\13\16\13"+
		"\u017d\13\13\3\13\2\3\24\f\2\4\6\b\n\f\16\20\22\24\2\t\4\2>>DD\3\2+,\3"+
		"\2\17\26\4\2\30\30%%\3\2\31\34\3\2\27\30\3\2\36#\2\u01c1\2\35\3\2\2\2"+
		"\4-\3\2\2\2\6\60\3\2\2\2\bl\3\2\2\2\n\u00d4\3\2\2\2\f\u00d6\3\2\2\2\16"+
		"\u00df\3\2\2\2\20\u00e6\3\2\2\2\22\u00f4\3\2\2\2\24\u0135\3\2\2\2\26\27"+
		"\7*\2\2\27\32\7D\2\2\30\31\7=\2\2\31\33\t\2\2\2\32\30\3\2\2\2\32\33\3"+
		"\2\2\2\33\34\3\2\2\2\34\36\7\r\2\2\35\26\3\2\2\2\35\36\3\2\2\2\36\"\3"+
		"\2\2\2\37!\5\6\4\2 \37\3\2\2\2!$\3\2\2\2\" \3\2\2\2\"#\3\2\2\2#(\3\2\2"+
		"\2$\"\3\2\2\2%\'\5\b\5\2&%\3\2\2\2\'*\3\2\2\2(&\3\2\2\2()\3\2\2\2)+\3"+
		"\2\2\2*(\3\2\2\2+,\7\2\2\3,\3\3\2\2\2-.\5\24\13\2./\7\2\2\3/\5\3\2\2\2"+
		"\60\61\7(\2\2\61\66\7G\2\2\62\63\7\16\2\2\63\65\7G\2\2\64\62\3\2\2\2\65"+
		"8\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\67;\3\2\2\28\66\3\2\2\29:\7)\2\2"+
		":<\7G\2\2;9\3\2\2\2;<\3\2\2\2<=\3\2\2\2=>\7\r\2\2>\7\3\2\2\2?A\7.\2\2"+
		"@B\7-\2\2A@\3\2\2\2AB\3\2\2\2BC\3\2\2\2CD\7+\2\2DE\7G\2\2EF\7\17\2\2F"+
		"G\5\24\13\2GH\7\r\2\2Hm\3\2\2\2IJ\7.\2\2JK\7,\2\2KL\7G\2\2LM\7\17\2\2"+
		"MN\5\24\13\2NO\7\r\2\2Om\3\2\2\2PR\7.\2\2QP\3\2\2\2QR\3\2\2\2RS\3\2\2"+
		"\2ST\7/\2\2TU\7G\2\2Ua\7\5\2\2V[\7G\2\2WX\7\13\2\2XZ\7G\2\2YW\3\2\2\2"+
		"Z]\3\2\2\2[Y\3\2\2\2[\\\3\2\2\2\\_\3\2\2\2][\3\2\2\2^`\7\13\2\2_^\3\2"+
		"\2\2_`\3\2\2\2`b\3\2\2\2aV\3\2\2\2ab\3\2\2\2bc\3\2\2\2cg\7\6\2\2df\5\n"+
		"\6\2ed\3\2\2\2fi\3\2\2\2ge\3\2\2\2gh\3\2\2\2hj\3\2\2\2ig\3\2\2\2jm\78"+
		"\2\2km\5\n\6\2l?\3\2\2\2lI\3\2\2\2lQ\3\2\2\2lk\3\2\2\2m\t\3\2\2\2no\t"+
		"\3\2\2op\7G\2\2pq\7\17\2\2qr\5\24\13\2rs\7\r\2\2s\u00d5\3\2\2\2tu\79\2"+
		"\2uv\7G\2\2v\u00d5\7\r\2\2wx\79\2\2xy\5\24\13\2yz\7\7\2\2z{\5\24\13\2"+
		"{|\7\b\2\2|}\7\r\2\2}\u00d5\3\2\2\2~\177\7\61\2\2\177\u0080\5\24\13\2"+
		"\u0080\u0084\7\62\2\2\u0081\u0083\5\n\6\2\u0082\u0081\3\2\2\2\u0083\u0086"+
		"\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u008a\3\2\2\2\u0086"+
		"\u0084\3\2\2\2\u0087\u0089\5\f\7\2\u0088\u0087\3\2\2\2\u0089\u008c\3\2"+
		"\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u008e\3\2\2\2\u008c"+
		"\u008a\3\2\2\2\u008d\u008f\5\16\b\2\u008e\u008d\3\2\2\2\u008e\u008f\3"+
		"\2\2\2\u008f\u0090\3\2\2\2\u0090\u0091\78\2\2\u0091\u00d5\3\2\2\2\u0092"+
		"\u0093\7\65\2\2\u0093\u0094\5\24\13\2\u0094\u0098\7\67\2\2\u0095\u0097"+
		"\5\22\n\2\u0096\u0095\3\2\2\2\u0097\u009a\3\2\2\2\u0098\u0096\3\2\2\2"+
		"\u0098\u0099\3\2\2\2\u0099\u009b\3\2\2\2\u009a\u0098\3\2\2\2\u009b\u009c"+
		"\78\2\2\u009c\u00d5\3\2\2\2\u009d\u009e\7\66\2\2\u009e\u009f\7G\2\2\u009f"+
		"\u00a0\7$\2\2\u00a0\u00a1\5\24\13\2\u00a1\u00a5\7\67\2\2\u00a2\u00a4\5"+
		"\22\n\2\u00a3\u00a2\3\2\2\2\u00a4\u00a7\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5"+
		"\u00a6\3\2\2\2\u00a6\u00a8\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a8\u00a9\78"+
		"\2\2\u00a9\u00d5\3\2\2\2\u00aa\u00ae\7?\2\2\u00ab\u00ad\5\n\6\2\u00ac"+
		"\u00ab\3\2\2\2\u00ad\u00b0\3\2\2\2\u00ae\u00ac\3\2\2\2\u00ae\u00af\3\2"+
		"\2\2\u00af\u00b1\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b1\u00b2\5\20\t\2\u00b2"+
		"\u00b3\78\2\2\u00b3\u00d5\3\2\2\2\u00b4\u00b5\7<\2\2\u00b5\u00b6\5\24"+
		"\13\2\u00b6\u00b7\7\r\2\2\u00b7\u00d5\3\2\2\2\u00b8\u00ba\7\60\2\2\u00b9"+
		"\u00bb\5\24\13\2\u00ba\u00b9\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\3"+
		"\2\2\2\u00bc\u00d5\7\r\2\2\u00bd\u00be\7G\2\2\u00be\u00bf\t\4\2\2\u00bf"+
		"\u00c0\5\24\13\2\u00c0\u00c1\7\r\2\2\u00c1\u00d5\3\2\2\2\u00c2\u00c3\5"+
		"\24\13\2\u00c3\u00c4\7\7\2\2\u00c4\u00c5\5\24\13\2\u00c5\u00c6\7\b\2\2"+
		"\u00c6\u00c7\t\4\2\2\u00c7\u00c8\5\24\13\2\u00c8\u00c9\7\r\2\2\u00c9\u00d5"+
		"\3\2\2\2\u00ca\u00cb\5\24\13\2\u00cb\u00cc\7\16\2\2\u00cc\u00cd\7G\2\2"+
		"\u00cd\u00ce\t\4\2\2\u00ce\u00cf\5\24\13\2\u00cf\u00d0\7\r\2\2\u00d0\u00d5"+
		"\3\2\2\2\u00d1\u00d2\5\24\13\2\u00d2\u00d3\7\r\2\2\u00d3\u00d5\3\2\2\2"+
		"\u00d4n\3\2\2\2\u00d4t\3\2\2\2\u00d4w\3\2\2\2\u00d4~\3\2\2\2\u00d4\u0092"+
		"\3\2\2\2\u00d4\u009d\3\2\2\2\u00d4\u00aa\3\2\2\2\u00d4\u00b4\3\2\2\2\u00d4"+
		"\u00b8\3\2\2\2\u00d4\u00bd\3\2\2\2\u00d4\u00c2\3\2\2\2\u00d4\u00ca\3\2"+
		"\2\2\u00d4\u00d1\3\2\2\2\u00d5\13\3\2\2\2\u00d6\u00d7\7\64\2\2\u00d7\u00d8"+
		"\5\24\13\2\u00d8\u00dc\7\62\2\2\u00d9\u00db\5\n\6\2\u00da\u00d9\3\2\2"+
		"\2\u00db\u00de\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\r"+
		"\3\2\2\2\u00de\u00dc\3\2\2\2\u00df\u00e3\7\63\2\2\u00e0\u00e2\5\n\6\2"+
		"\u00e1\u00e0\3\2\2\2\u00e2\u00e5\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e3\u00e4"+
		"\3\2\2\2\u00e4\17\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e6\u00e7\7@\2\2\u00e7"+
		"\u00e8\7G\2\2\u00e8\u00ec\7\62\2\2\u00e9\u00eb\5\n\6\2\u00ea\u00e9\3\2"+
		"\2\2\u00eb\u00ee\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed"+
		"\21\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ef\u00f0\7:\2\2\u00f0\u00f5\7\r\2\2"+
		"\u00f1\u00f2\7;\2\2\u00f2\u00f5\7\r\2\2\u00f3\u00f5\5\n\6\2\u00f4\u00ef"+
		"\3\2\2\2\u00f4\u00f1\3\2\2\2\u00f4\u00f3\3\2\2\2\u00f5\23\3\2\2\2\u00f6"+
		"\u00f7\b\13\1\2\u00f7\u00f8\7\5\2\2\u00f8\u00f9\5\24\13\2\u00f9\u00fa"+
		"\7\6\2\2\u00fa\u0136\3\2\2\2\u00fb\u0136\7A\2\2\u00fc\u0136\7B\2\2\u00fd"+
		"\u0136\7C\2\2\u00fe\u0136\7D\2\2\u00ff\u0136\7E\2\2\u0100\u0136\7F\2\2"+
		"\u0101\u010d\7\7\2\2\u0102\u0107\5\24\13\2\u0103\u0104\7\13\2\2\u0104"+
		"\u0106\5\24\13\2\u0105\u0103\3\2\2\2\u0106\u0109\3\2\2\2\u0107\u0105\3"+
		"\2\2\2\u0107\u0108\3\2\2\2\u0108\u010b\3\2\2\2\u0109\u0107\3\2\2\2\u010a"+
		"\u010c\7\13\2\2\u010b\u010a\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010e\3"+
		"\2\2\2\u010d\u0102\3\2\2\2\u010d\u010e\3\2\2\2\u010e\u010f\3\2\2\2\u010f"+
		"\u0136\7\b\2\2\u0110\u0120\7\t\2\2\u0111\u0112\7F\2\2\u0112\u0113\7\f"+
		"\2\2\u0113\u011a\5\24\13\2\u0114\u0115\7\13\2\2\u0115\u0116\7F\2\2\u0116"+
		"\u0117\7\f\2\2\u0117\u0119\5\24\13\2\u0118\u0114\3\2\2\2\u0119\u011c\3"+
		"\2\2\2\u011a\u0118\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u011e\3\2\2\2\u011c"+
		"\u011a\3\2\2\2\u011d\u011f\7\13\2\2\u011e\u011d\3\2\2\2\u011e\u011f\3"+
		"\2\2\2\u011f\u0121\3\2\2\2\u0120\u0111\3\2\2\2\u0120\u0121\3\2\2\2\u0121"+
		"\u0122\3\2\2\2\u0122\u0136\7\n\2\2\u0123\u0124\7\t\2\2\u0124\u0129\5\24"+
		"\13\2\u0125\u0126\7\13\2\2\u0126\u0128\5\24\13\2\u0127\u0125\3\2\2\2\u0128"+
		"\u012b\3\2\2\2\u0129\u0127\3\2\2\2\u0129\u012a\3\2\2\2\u012a\u012d\3\2"+
		"\2\2\u012b\u0129\3\2\2\2\u012c\u012e\7\13\2\2\u012d\u012c\3\2\2\2\u012d"+
		"\u012e\3\2\2\2\u012e\u012f\3\2\2\2\u012f\u0130\7\n\2\2\u0130\u0136\3\2"+
		"\2\2\u0131\u0132\t\5\2\2\u0132\u0136\5\24\13\r\u0133\u0136\7G\2\2\u0134"+
		"\u0136\7H\2\2\u0135\u00f6\3\2\2\2\u0135\u00fb\3\2\2\2\u0135\u00fc\3\2"+
		"\2\2\u0135\u00fd\3\2\2\2\u0135\u00fe\3\2\2\2\u0135\u00ff\3\2\2\2\u0135"+
		"\u0100\3\2\2\2\u0135\u0101\3\2\2\2\u0135\u0110\3\2\2\2\u0135\u0123\3\2"+
		"\2\2\u0135\u0131\3\2\2\2\u0135\u0133\3\2\2\2\u0135\u0134\3\2\2\2\u0136"+
		"\u017b\3\2\2\2\u0137\u0138\f\13\2\2\u0138\u0139\7\35\2\2\u0139\u017a\5"+
		"\24\13\f\u013a\u013b\f\n\2\2\u013b\u013c\t\6\2\2\u013c\u017a\5\24\13\13"+
		"\u013d\u013e\f\t\2\2\u013e\u013f\t\7\2\2\u013f\u017a\5\24\13\n\u0140\u0142"+
		"\f\b\2\2\u0141\u0143\7%\2\2\u0142\u0141\3\2\2\2\u0142\u0143\3\2\2\2\u0143"+
		"\u0144\3\2\2\2\u0144\u0145\7$\2\2\u0145\u017a\5\24\13\t\u0146\u0147\f"+
		"\7\2\2\u0147\u0148\t\b\2\2\u0148\u017a\5\24\13\b\u0149\u014a\f\6\2\2\u014a"+
		"\u014b\7&\2\2\u014b\u017a\5\24\13\7\u014c\u014d\f\5\2\2\u014d\u014e\7"+
		"\'\2\2\u014e\u017a\5\24\13\6\u014f\u0150\f\20\2\2\u0150\u0151\7\16\2\2"+
		"\u0151\u0152\7G\2\2\u0152\u015e\7\5\2\2\u0153\u0158\5\24\13\2\u0154\u0155"+
		"\7\13\2\2\u0155\u0157\5\24\13\2\u0156\u0154\3\2\2\2\u0157\u015a\3\2\2"+
		"\2\u0158\u0156\3\2\2\2\u0158\u0159\3\2\2\2\u0159\u015c\3\2\2\2\u015a\u0158"+
		"\3\2\2\2\u015b\u015d\7\13\2\2\u015c\u015b\3\2\2\2\u015c\u015d\3\2\2\2"+
		"\u015d\u015f\3\2\2\2\u015e\u0153\3\2\2\2\u015e\u015f\3\2\2\2\u015f\u0160"+
		"\3\2\2\2\u0160\u017a\7\6\2\2\u0161\u0162\f\17\2\2\u0162\u0163\7\16\2\2"+
		"\u0163\u017a\7G\2\2\u0164\u0165\f\16\2\2\u0165\u0171\7\5\2\2\u0166\u016b"+
		"\5\24\13\2\u0167\u0168\7\13\2\2\u0168\u016a\5\24\13\2\u0169\u0167\3\2"+
		"\2\2\u016a\u016d\3\2\2\2\u016b\u0169\3\2\2\2\u016b\u016c\3\2\2\2\u016c"+
		"\u016f\3\2\2\2\u016d\u016b\3\2\2\2\u016e\u0170\7\13\2\2\u016f\u016e\3"+
		"\2\2\2\u016f\u0170\3\2\2\2\u0170\u0172\3\2\2\2\u0171\u0166\3\2\2\2\u0171"+
		"\u0172\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u017a\7\6\2\2\u0174\u0175\f\f"+
		"\2\2\u0175\u0176\7\7\2\2\u0176\u0177\5\24\13\2\u0177\u0178\7\b\2\2\u0178"+
		"\u017a\3\2\2\2\u0179\u0137\3\2\2\2\u0179\u013a\3\2\2\2\u0179\u013d\3\2"+
		"\2\2\u0179\u0140\3\2\2\2\u0179\u0146\3\2\2\2\u0179\u0149\3\2\2\2\u0179"+
		"\u014c\3\2\2\2\u0179\u014f\3\2\2\2\u0179\u0161\3\2\2\2\u0179\u0164\3\2"+
		"\2\2\u0179\u0174\3\2\2\2\u017a\u017d\3\2\2\2\u017b\u0179\3\2\2\2\u017b"+
		"\u017c\3\2\2\2\u017c\25\3\2\2\2\u017d\u017b\3\2\2\2-\32\35\"(\66;AQ[_"+
		"agl\u0084\u008a\u008e\u0098\u00a5\u00ae\u00ba\u00d4\u00dc\u00e3\u00ec"+
		"\u00f4\u0107\u010b\u010d\u011a\u011e\u0120\u0129\u012d\u0135\u0142\u0158"+
		"\u015c\u015e\u016b\u016f\u0171\u0179\u017b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}