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
		CONTINUE=57, WAIT=58, REPEAT=59, FOREVER=60, NULL=61, TRUE=62, FALSE=63, 
		INT=64, FLOAT=65, STRING=66, IDENT=67;
	public static final int
		RULE_module = 0, RULE_global_statement = 1, RULE_statement = 2, RULE_loop_stmt = 3, 
		RULE_expr = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"module", "global_statement", "statement", "loop_stmt", "expr"
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
			"FOREVER", "NULL", "TRUE", "FALSE", "INT", "FLOAT", "STRING", "IDENT"
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
			setState(17);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SCHED) {
				{
				setState(10);
				match(SCHED);
				setState(11);
				((ModuleContext)_localctx).ticks = match(INT);
				setState(14);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==REPEAT) {
					{
					setState(12);
					match(REPEAT);
					setState(13);
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

				setState(16);
				match(SEMIC);
				}
			}

			setState(22);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << PUBLIC) | (1L << FUNC) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << NULL) | (1L << TRUE) | (1L << FALSE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)))) != 0)) {
				{
				{
				setState(19);
				global_statement();
				}
				}
				setState(24);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(25);
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
		enterRule(_localctx, 2, RULE_global_statement);
		int _la;
		try {
			int _alt;
			setState(69);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				_localctx = new DeclareGlobalVariableContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(27);
				match(PUBLIC);
				setState(29);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EDITABLE) {
					{
					setState(28);
					match(EDITABLE);
					}
				}

				setState(31);
				match(VAR);
				setState(32);
				((DeclareGlobalVariableContext)_localctx).name = match(IDENT);
				setState(33);
				match(ASSIGN);
				setState(34);
				((DeclareGlobalVariableContext)_localctx).value = expr(0);
				setState(35);
				match(SEMIC);
				}
				break;
			case 2:
				_localctx = new DeclareGlobalConstantContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(37);
				match(PUBLIC);
				setState(38);
				match(CONST);
				setState(39);
				((DeclareGlobalConstantContext)_localctx).name = match(IDENT);
				setState(40);
				match(ASSIGN);
				setState(41);
				((DeclareGlobalConstantContext)_localctx).value = expr(0);
				setState(42);
				match(SEMIC);
				}
				break;
			case 3:
				_localctx = new DefineFunctionStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(44);
				match(FUNC);
				setState(45);
				((DefineFunctionStatementContext)_localctx).name = match(IDENT);
				setState(46);
				match(LPAREN);
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENT) {
					{
					setState(47);
					match(IDENT);
					setState(52);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(48);
							match(COMMA);
							setState(49);
							match(IDENT);
							}
							} 
						}
						setState(54);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
					}
					setState(56);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(55);
						match(COMMA);
						}
					}

					}
				}

				setState(60);
				match(RPAREN);
				setState(64);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << NULL) | (1L << TRUE) | (1L << FALSE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)))) != 0)) {
					{
					{
					setState(61);
					statement();
					}
					}
					setState(66);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(67);
				match(END);
				}
				break;
			case 4:
				_localctx = new StmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(68);
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
	public static class IfStatementContext extends StatementContext {
		public ExprContext if_cond;
		public StatementContext if_stmts;
		public ExprContext elif_cond;
		public StatementContext elif_stmts;
		public StatementContext else_stmts;
		public TerminalNode IF() { return getToken(MCCodeParser.IF, 0); }
		public List<TerminalNode> THEN() { return getTokens(MCCodeParser.THEN); }
		public TerminalNode THEN(int i) {
			return getToken(MCCodeParser.THEN, i);
		}
		public TerminalNode END() { return getToken(MCCodeParser.END, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> ELIF() { return getTokens(MCCodeParser.ELIF); }
		public TerminalNode ELIF(int i) {
			return getToken(MCCodeParser.ELIF, i);
		}
		public TerminalNode ELSE() { return getToken(MCCodeParser.ELSE, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
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

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_statement);
		int _la;
		try {
			setState(177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				_localctx = new DeclareVariableStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(71);
				_la = _input.LA(1);
				if ( !(_la==VAR || _la==CONST) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(72);
				((DeclareVariableStatementContext)_localctx).name = match(IDENT);
				setState(73);
				match(ASSIGN);
				setState(74);
				((DeclareVariableStatementContext)_localctx).value = expr(0);
				setState(75);
				match(SEMIC);
				}
				break;
			case 2:
				_localctx = new DeleteStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(77);
				match(DELETE);
				setState(78);
				((DeleteStatementContext)_localctx).name = match(IDENT);
				setState(79);
				match(SEMIC);
				}
				break;
			case 3:
				_localctx = new DeleteItemStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(80);
				match(DELETE);
				setState(81);
				((DeleteItemStatementContext)_localctx).target = expr(0);
				setState(82);
				match(LBRACK);
				setState(83);
				((DeleteItemStatementContext)_localctx).key = expr(0);
				setState(84);
				match(RBRACK);
				setState(85);
				match(SEMIC);
				}
				break;
			case 4:
				_localctx = new IfStatementContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(87);
				match(IF);
				setState(88);
				((IfStatementContext)_localctx).if_cond = expr(0);
				setState(89);
				match(THEN);
				setState(93);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << NULL) | (1L << TRUE) | (1L << FALSE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)))) != 0)) {
					{
					{
					setState(90);
					((IfStatementContext)_localctx).if_stmts = statement();
					}
					}
					setState(95);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(107);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ELIF) {
					{
					{
					setState(96);
					match(ELIF);
					setState(97);
					((IfStatementContext)_localctx).elif_cond = expr(0);
					setState(98);
					match(THEN);
					setState(102);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << NULL) | (1L << TRUE) | (1L << FALSE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)))) != 0)) {
						{
						{
						setState(99);
						((IfStatementContext)_localctx).elif_stmts = statement();
						}
						}
						setState(104);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					}
					setState(109);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(117);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(110);
					match(ELSE);
					setState(114);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << WAIT) | (1L << NULL) | (1L << TRUE) | (1L << FALSE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)))) != 0)) {
						{
						{
						setState(111);
						((IfStatementContext)_localctx).else_stmts = statement();
						}
						}
						setState(116);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(119);
				match(END);
				}
				break;
			case 5:
				_localctx = new WhileLoopStatementContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(121);
				match(WHILE);
				setState(122);
				((WhileLoopStatementContext)_localctx).cond = expr(0);
				setState(123);
				match(DO);
				setState(127);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << WAIT) | (1L << NULL) | (1L << TRUE) | (1L << FALSE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)))) != 0)) {
					{
					{
					setState(124);
					loop_stmt();
					}
					}
					setState(129);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(130);
				match(END);
				}
				break;
			case 6:
				_localctx = new ForLoopStatementContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(132);
				match(FOR);
				setState(133);
				((ForLoopStatementContext)_localctx).variable = match(IDENT);
				setState(134);
				match(IN);
				setState(135);
				((ForLoopStatementContext)_localctx).range = expr(0);
				setState(136);
				match(DO);
				setState(140);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << VAR) | (1L << CONST) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << DELETE) | (1L << BREAK) | (1L << CONTINUE) | (1L << WAIT) | (1L << NULL) | (1L << TRUE) | (1L << FALSE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)))) != 0)) {
					{
					{
					setState(137);
					loop_stmt();
					}
					}
					setState(142);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(143);
				match(END);
				}
				break;
			case 7:
				_localctx = new WaitStatementContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(145);
				match(WAIT);
				setState(146);
				expr(0);
				setState(147);
				match(SEMIC);
				}
				break;
			case 8:
				_localctx = new ReturnStatementContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(149);
				match(RETURN);
				setState(151);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << NULL) | (1L << TRUE) | (1L << FALSE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)))) != 0)) {
					{
					setState(150);
					((ReturnStatementContext)_localctx).returned = expr(0);
					}
				}

				setState(153);
				match(SEMIC);
				}
				break;
			case 9:
				_localctx = new VariableAssignmentStatementContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(154);
				((VariableAssignmentStatementContext)_localctx).name = match(IDENT);
				setState(155);
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
				setState(156);
				((VariableAssignmentStatementContext)_localctx).value = expr(0);
				setState(157);
				match(SEMIC);
				}
				break;
			case 10:
				_localctx = new SetItemStatementContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(159);
				((SetItemStatementContext)_localctx).target = expr(0);
				setState(160);
				match(LBRACK);
				setState(161);
				((SetItemStatementContext)_localctx).key = expr(0);
				setState(162);
				match(RBRACK);
				setState(163);
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
				setState(164);
				((SetItemStatementContext)_localctx).value = expr(0);
				setState(165);
				match(SEMIC);
				}
				break;
			case 11:
				_localctx = new SetPropertyStatementContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(167);
				((SetPropertyStatementContext)_localctx).target = expr(0);
				setState(168);
				match(DOT);
				setState(169);
				((SetPropertyStatementContext)_localctx).name = match(IDENT);
				setState(170);
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
				setState(171);
				((SetPropertyStatementContext)_localctx).value = expr(0);
				setState(172);
				match(SEMIC);
				}
				break;
			case 12:
				_localctx = new ExpressionStatementContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(174);
				expr(0);
				setState(175);
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
		enterRule(_localctx, 6, RULE_loop_stmt);
		try {
			setState(184);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BREAK:
				_localctx = new BreakStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(179);
				match(BREAK);
				setState(180);
				match(SEMIC);
				}
				break;
			case CONTINUE:
				_localctx = new ContinueStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(181);
				match(CONTINUE);
				setState(182);
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
			case NULL:
			case TRUE:
			case FALSE:
			case INT:
			case FLOAT:
			case STRING:
			case IDENT:
				_localctx = new Statement_Context(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(183);
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
		public List<TerminalNode> IDENT() { return getTokens(MCCodeParser.IDENT); }
		public TerminalNode IDENT(int i) {
			return getToken(MCCodeParser.IDENT, i);
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
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				_localctx = new ParenthesesContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(187);
				match(LPAREN);
				setState(188);
				((ParenthesesContext)_localctx).exp = expr(0);
				setState(189);
				match(RPAREN);
				}
				break;
			case 2:
				{
				_localctx = new NullLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(191);
				match(NULL);
				}
				break;
			case 3:
				{
				_localctx = new BoolLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(192);
				match(TRUE);
				}
				break;
			case 4:
				{
				_localctx = new BoolLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(193);
				match(FALSE);
				}
				break;
			case 5:
				{
				_localctx = new IntLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(194);
				match(INT);
				}
				break;
			case 6:
				{
				_localctx = new FloatLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(195);
				match(FLOAT);
				}
				break;
			case 7:
				{
				_localctx = new StringLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(196);
				match(STRING);
				}
				break;
			case 8:
				{
				_localctx = new ListLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(197);
				match(LBRACK);
				setState(209);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << NULL) | (1L << TRUE) | (1L << FALSE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)))) != 0)) {
					{
					setState(198);
					expr(0);
					setState(203);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(199);
							match(COMMA);
							setState(200);
							expr(0);
							}
							} 
						}
						setState(205);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
					}
					setState(207);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(206);
						match(COMMA);
						}
					}

					}
				}

				setState(211);
				match(RBRACK);
				}
				break;
			case 9:
				{
				_localctx = new MapLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(212);
				match(LCURL);
				setState(228);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IDENT) {
					{
					setState(213);
					match(IDENT);
					setState(214);
					match(COLON);
					setState(215);
					expr(0);
					setState(222);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(216);
							match(COMMA);
							setState(217);
							match(IDENT);
							setState(218);
							match(COLON);
							setState(219);
							expr(0);
							}
							} 
						}
						setState(224);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
					}
					setState(226);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(225);
						match(COMMA);
						}
					}

					}
				}

				setState(230);
				match(RCURL);
				}
				break;
			case 10:
				{
				_localctx = new SetLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(231);
				match(LCURL);
				setState(232);
				expr(0);
				setState(237);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(233);
						match(COMMA);
						setState(234);
						expr(0);
						}
						} 
					}
					setState(239);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				}
				setState(241);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(240);
					match(COMMA);
					}
				}

				setState(243);
				match(RCURL);
				}
				break;
			case 11:
				{
				_localctx = new UnaryOperatorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(245);
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
				setState(246);
				((UnaryOperatorContext)_localctx).operand = expr(10);
				}
				break;
			case 12:
				{
				_localctx = new VariableContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(247);
				match(IDENT);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(318);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(316);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(250);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(251);
						((BinaryOperatorContext)_localctx).operator = match(POWER);
						setState(252);
						((BinaryOperatorContext)_localctx).right = expr(9);
						}
						break;
					case 2:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(253);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(254);
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
						setState(255);
						((BinaryOperatorContext)_localctx).right = expr(8);
						}
						break;
					case 3:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(256);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(257);
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
						setState(258);
						((BinaryOperatorContext)_localctx).right = expr(7);
						}
						break;
					case 4:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(259);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(261);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==NOT) {
							{
							setState(260);
							match(NOT);
							}
						}

						setState(263);
						match(IN);
						setState(264);
						((BinaryOperatorContext)_localctx).right = expr(6);
						}
						break;
					case 5:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(265);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(266);
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
						setState(267);
						((BinaryOperatorContext)_localctx).right = expr(5);
						}
						break;
					case 6:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(268);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(269);
						((BinaryOperatorContext)_localctx).operator = match(AND);
						setState(270);
						((BinaryOperatorContext)_localctx).right = expr(4);
						}
						break;
					case 7:
						{
						_localctx = new BinaryOperatorContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperatorContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(271);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(272);
						((BinaryOperatorContext)_localctx).operator = match(OR);
						setState(273);
						((BinaryOperatorContext)_localctx).right = expr(3);
						}
						break;
					case 8:
						{
						_localctx = new MethodCallContext(new ExprContext(_parentctx, _parentState));
						((MethodCallContext)_localctx).object = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(274);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(275);
						match(DOT);
						setState(276);
						((MethodCallContext)_localctx).property = match(IDENT);
						setState(277);
						match(LPAREN);
						setState(289);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << NULL) | (1L << TRUE) | (1L << FALSE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)))) != 0)) {
							{
							setState(278);
							expr(0);
							setState(283);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
							while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
								if ( _alt==1 ) {
									{
									{
									setState(279);
									match(COMMA);
									setState(280);
									expr(0);
									}
									} 
								}
								setState(285);
								_errHandler.sync(this);
								_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
							}
							setState(287);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==COMMA) {
								{
								setState(286);
								match(COMMA);
								}
							}

							}
						}

						setState(291);
						match(RPAREN);
						}
						break;
					case 9:
						{
						_localctx = new GetPropertyContext(new ExprContext(_parentctx, _parentState));
						((GetPropertyContext)_localctx).object = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(292);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(293);
						match(DOT);
						setState(294);
						((GetPropertyContext)_localctx).property = match(IDENT);
						}
						break;
					case 10:
						{
						_localctx = new FunctionCallContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(295);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(296);
						match(LPAREN);
						setState(308);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << LBRACK) | (1L << LCURL) | (1L << MINUS) | (1L << NOT) | (1L << NULL) | (1L << TRUE) | (1L << FALSE))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (INT - 64)) | (1L << (FLOAT - 64)) | (1L << (STRING - 64)) | (1L << (IDENT - 64)))) != 0)) {
							{
							setState(297);
							expr(0);
							setState(302);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
							while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
								if ( _alt==1 ) {
									{
									{
									setState(298);
									match(COMMA);
									setState(299);
									expr(0);
									}
									} 
								}
								setState(304);
								_errHandler.sync(this);
								_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
							}
							setState(306);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==COMMA) {
								{
								setState(305);
								match(COMMA);
								}
							}

							}
						}

						setState(310);
						match(RPAREN);
						}
						break;
					case 11:
						{
						_localctx = new GetItemContext(new ExprContext(_parentctx, _parentState));
						((GetItemContext)_localctx).source = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(311);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(312);
						match(LBRACK);
						setState(313);
						((GetItemContext)_localctx).key = expr(0);
						setState(314);
						match(RBRACK);
						}
						break;
					}
					} 
				}
				setState(320);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
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
		case 4:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 8);
		case 1:
			return precpred(_ctx, 7);
		case 2:
			return precpred(_ctx, 6);
		case 3:
			return precpred(_ctx, 5);
		case 4:
			return precpred(_ctx, 4);
		case 5:
			return precpred(_ctx, 3);
		case 6:
			return precpred(_ctx, 2);
		case 7:
			return precpred(_ctx, 13);
		case 8:
			return precpred(_ctx, 12);
		case 9:
			return precpred(_ctx, 11);
		case 10:
			return precpred(_ctx, 9);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3E\u0144\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\3\2\3\2\5\2\21\n\2\3\2\5\2\24\n"+
		"\2\3\2\7\2\27\n\2\f\2\16\2\32\13\2\3\2\3\2\3\3\3\3\5\3 \n\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3"+
		"\65\n\3\f\3\16\38\13\3\3\3\5\3;\n\3\5\3=\n\3\3\3\3\3\7\3A\n\3\f\3\16\3"+
		"D\13\3\3\3\3\3\5\3H\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4^\n\4\f\4\16\4a\13\4\3\4\3\4\3\4"+
		"\3\4\7\4g\n\4\f\4\16\4j\13\4\7\4l\n\4\f\4\16\4o\13\4\3\4\3\4\7\4s\n\4"+
		"\f\4\16\4v\13\4\5\4x\n\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4\u0080\n\4\f\4\16"+
		"\4\u0083\13\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4\u008d\n\4\f\4\16\4\u0090"+
		"\13\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u009a\n\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\5\4\u00b4\n\4\3\5\3\5\3\5\3\5\3\5\5\5\u00bb\n\5\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\7\6\u00cc\n\6\f\6\16\6"+
		"\u00cf\13\6\3\6\5\6\u00d2\n\6\5\6\u00d4\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\7\6\u00df\n\6\f\6\16\6\u00e2\13\6\3\6\5\6\u00e5\n\6\5\6\u00e7"+
		"\n\6\3\6\3\6\3\6\3\6\3\6\7\6\u00ee\n\6\f\6\16\6\u00f1\13\6\3\6\5\6\u00f4"+
		"\n\6\3\6\3\6\3\6\3\6\3\6\5\6\u00fb\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\5\6\u0108\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\7\6\u011c\n\6\f\6\16\6\u011f\13\6\3\6\5\6"+
		"\u0122\n\6\5\6\u0124\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\7\6\u012f"+
		"\n\6\f\6\16\6\u0132\13\6\3\6\5\6\u0135\n\6\5\6\u0137\n\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\7\6\u013f\n\6\f\6\16\6\u0142\13\6\3\6\2\3\n\7\2\4\6\b\n\2\t"+
		"\4\2>>BB\3\2+,\3\2\17\26\4\2\30\30%%\3\2\31\34\3\2\27\30\3\2\36#\2\u0183"+
		"\2\23\3\2\2\2\4G\3\2\2\2\6\u00b3\3\2\2\2\b\u00ba\3\2\2\2\n\u00fa\3\2\2"+
		"\2\f\r\7*\2\2\r\20\7B\2\2\16\17\7=\2\2\17\21\t\2\2\2\20\16\3\2\2\2\20"+
		"\21\3\2\2\2\21\22\3\2\2\2\22\24\7\r\2\2\23\f\3\2\2\2\23\24\3\2\2\2\24"+
		"\30\3\2\2\2\25\27\5\4\3\2\26\25\3\2\2\2\27\32\3\2\2\2\30\26\3\2\2\2\30"+
		"\31\3\2\2\2\31\33\3\2\2\2\32\30\3\2\2\2\33\34\7\2\2\3\34\3\3\2\2\2\35"+
		"\37\7.\2\2\36 \7-\2\2\37\36\3\2\2\2\37 \3\2\2\2 !\3\2\2\2!\"\7+\2\2\""+
		"#\7E\2\2#$\7\17\2\2$%\5\n\6\2%&\7\r\2\2&H\3\2\2\2\'(\7.\2\2()\7,\2\2)"+
		"*\7E\2\2*+\7\17\2\2+,\5\n\6\2,-\7\r\2\2-H\3\2\2\2./\7/\2\2/\60\7E\2\2"+
		"\60<\7\5\2\2\61\66\7E\2\2\62\63\7\13\2\2\63\65\7E\2\2\64\62\3\2\2\2\65"+
		"8\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\67:\3\2\2\28\66\3\2\2\29;\7\13\2"+
		"\2:9\3\2\2\2:;\3\2\2\2;=\3\2\2\2<\61\3\2\2\2<=\3\2\2\2=>\3\2\2\2>B\7\6"+
		"\2\2?A\5\6\4\2@?\3\2\2\2AD\3\2\2\2B@\3\2\2\2BC\3\2\2\2CE\3\2\2\2DB\3\2"+
		"\2\2EH\78\2\2FH\5\6\4\2G\35\3\2\2\2G\'\3\2\2\2G.\3\2\2\2GF\3\2\2\2H\5"+
		"\3\2\2\2IJ\t\3\2\2JK\7E\2\2KL\7\17\2\2LM\5\n\6\2MN\7\r\2\2N\u00b4\3\2"+
		"\2\2OP\79\2\2PQ\7E\2\2Q\u00b4\7\r\2\2RS\79\2\2ST\5\n\6\2TU\7\7\2\2UV\5"+
		"\n\6\2VW\7\b\2\2WX\7\r\2\2X\u00b4\3\2\2\2YZ\7\61\2\2Z[\5\n\6\2[_\7\62"+
		"\2\2\\^\5\6\4\2]\\\3\2\2\2^a\3\2\2\2_]\3\2\2\2_`\3\2\2\2`m\3\2\2\2a_\3"+
		"\2\2\2bc\7\64\2\2cd\5\n\6\2dh\7\62\2\2eg\5\6\4\2fe\3\2\2\2gj\3\2\2\2h"+
		"f\3\2\2\2hi\3\2\2\2il\3\2\2\2jh\3\2\2\2kb\3\2\2\2lo\3\2\2\2mk\3\2\2\2"+
		"mn\3\2\2\2nw\3\2\2\2om\3\2\2\2pt\7\63\2\2qs\5\6\4\2rq\3\2\2\2sv\3\2\2"+
		"\2tr\3\2\2\2tu\3\2\2\2ux\3\2\2\2vt\3\2\2\2wp\3\2\2\2wx\3\2\2\2xy\3\2\2"+
		"\2yz\78\2\2z\u00b4\3\2\2\2{|\7\65\2\2|}\5\n\6\2}\u0081\7\67\2\2~\u0080"+
		"\5\b\5\2\177~\3\2\2\2\u0080\u0083\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082"+
		"\3\2\2\2\u0082\u0084\3\2\2\2\u0083\u0081\3\2\2\2\u0084\u0085\78\2\2\u0085"+
		"\u00b4\3\2\2\2\u0086\u0087\7\66\2\2\u0087\u0088\7E\2\2\u0088\u0089\7$"+
		"\2\2\u0089\u008a\5\n\6\2\u008a\u008e\7\67\2\2\u008b\u008d\5\b\5\2\u008c"+
		"\u008b\3\2\2\2\u008d\u0090\3\2\2\2\u008e\u008c\3\2\2\2\u008e\u008f\3\2"+
		"\2\2\u008f\u0091\3\2\2\2\u0090\u008e\3\2\2\2\u0091\u0092\78\2\2\u0092"+
		"\u00b4\3\2\2\2\u0093\u0094\7<\2\2\u0094\u0095\5\n\6\2\u0095\u0096\7\r"+
		"\2\2\u0096\u00b4\3\2\2\2\u0097\u0099\7\60\2\2\u0098\u009a\5\n\6\2\u0099"+
		"\u0098\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u00b4\7\r"+
		"\2\2\u009c\u009d\7E\2\2\u009d\u009e\t\4\2\2\u009e\u009f\5\n\6\2\u009f"+
		"\u00a0\7\r\2\2\u00a0\u00b4\3\2\2\2\u00a1\u00a2\5\n\6\2\u00a2\u00a3\7\7"+
		"\2\2\u00a3\u00a4\5\n\6\2\u00a4\u00a5\7\b\2\2\u00a5\u00a6\t\4\2\2\u00a6"+
		"\u00a7\5\n\6\2\u00a7\u00a8\7\r\2\2\u00a8\u00b4\3\2\2\2\u00a9\u00aa\5\n"+
		"\6\2\u00aa\u00ab\7\16\2\2\u00ab\u00ac\7E\2\2\u00ac\u00ad\t\4\2\2\u00ad"+
		"\u00ae\5\n\6\2\u00ae\u00af\7\r\2\2\u00af\u00b4\3\2\2\2\u00b0\u00b1\5\n"+
		"\6\2\u00b1\u00b2\7\r\2\2\u00b2\u00b4\3\2\2\2\u00b3I\3\2\2\2\u00b3O\3\2"+
		"\2\2\u00b3R\3\2\2\2\u00b3Y\3\2\2\2\u00b3{\3\2\2\2\u00b3\u0086\3\2\2\2"+
		"\u00b3\u0093\3\2\2\2\u00b3\u0097\3\2\2\2\u00b3\u009c\3\2\2\2\u00b3\u00a1"+
		"\3\2\2\2\u00b3\u00a9\3\2\2\2\u00b3\u00b0\3\2\2\2\u00b4\7\3\2\2\2\u00b5"+
		"\u00b6\7:\2\2\u00b6\u00bb\7\r\2\2\u00b7\u00b8\7;\2\2\u00b8\u00bb\7\r\2"+
		"\2\u00b9\u00bb\5\6\4\2\u00ba\u00b5\3\2\2\2\u00ba\u00b7\3\2\2\2\u00ba\u00b9"+
		"\3\2\2\2\u00bb\t\3\2\2\2\u00bc\u00bd\b\6\1\2\u00bd\u00be\7\5\2\2\u00be"+
		"\u00bf\5\n\6\2\u00bf\u00c0\7\6\2\2\u00c0\u00fb\3\2\2\2\u00c1\u00fb\7?"+
		"\2\2\u00c2\u00fb\7@\2\2\u00c3\u00fb\7A\2\2\u00c4\u00fb\7B\2\2\u00c5\u00fb"+
		"\7C\2\2\u00c6\u00fb\7D\2\2\u00c7\u00d3\7\7\2\2\u00c8\u00cd\5\n\6\2\u00c9"+
		"\u00ca\7\13\2\2\u00ca\u00cc\5\n\6\2\u00cb\u00c9\3\2\2\2\u00cc\u00cf\3"+
		"\2\2\2\u00cd\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00d1\3\2\2\2\u00cf"+
		"\u00cd\3\2\2\2\u00d0\u00d2\7\13\2\2\u00d1\u00d0\3\2\2\2\u00d1\u00d2\3"+
		"\2\2\2\u00d2\u00d4\3\2\2\2\u00d3\u00c8\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4"+
		"\u00d5\3\2\2\2\u00d5\u00fb\7\b\2\2\u00d6\u00e6\7\t\2\2\u00d7\u00d8\7E"+
		"\2\2\u00d8\u00d9\7\f\2\2\u00d9\u00e0\5\n\6\2\u00da\u00db\7\13\2\2\u00db"+
		"\u00dc\7E\2\2\u00dc\u00dd\7\f\2\2\u00dd\u00df\5\n\6\2\u00de\u00da\3\2"+
		"\2\2\u00df\u00e2\3\2\2\2\u00e0\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1"+
		"\u00e4\3\2\2\2\u00e2\u00e0\3\2\2\2\u00e3\u00e5\7\13\2\2\u00e4\u00e3\3"+
		"\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e7\3\2\2\2\u00e6\u00d7\3\2\2\2\u00e6"+
		"\u00e7\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00fb\7\n\2\2\u00e9\u00ea\7\t"+
		"\2\2\u00ea\u00ef\5\n\6\2\u00eb\u00ec\7\13\2\2\u00ec\u00ee\5\n\6\2\u00ed"+
		"\u00eb\3\2\2\2\u00ee\u00f1\3\2\2\2\u00ef\u00ed\3\2\2\2\u00ef\u00f0\3\2"+
		"\2\2\u00f0\u00f3\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f2\u00f4\7\13\2\2\u00f3"+
		"\u00f2\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f6\7\n"+
		"\2\2\u00f6\u00fb\3\2\2\2\u00f7\u00f8\t\5\2\2\u00f8\u00fb\5\n\6\f\u00f9"+
		"\u00fb\7E\2\2\u00fa\u00bc\3\2\2\2\u00fa\u00c1\3\2\2\2\u00fa\u00c2\3\2"+
		"\2\2\u00fa\u00c3\3\2\2\2\u00fa\u00c4\3\2\2\2\u00fa\u00c5\3\2\2\2\u00fa"+
		"\u00c6\3\2\2\2\u00fa\u00c7\3\2\2\2\u00fa\u00d6\3\2\2\2\u00fa\u00e9\3\2"+
		"\2\2\u00fa\u00f7\3\2\2\2\u00fa\u00f9\3\2\2\2\u00fb\u0140\3\2\2\2\u00fc"+
		"\u00fd\f\n\2\2\u00fd\u00fe\7\35\2\2\u00fe\u013f\5\n\6\13\u00ff\u0100\f"+
		"\t\2\2\u0100\u0101\t\6\2\2\u0101\u013f\5\n\6\n\u0102\u0103\f\b\2\2\u0103"+
		"\u0104\t\7\2\2\u0104\u013f\5\n\6\t\u0105\u0107\f\7\2\2\u0106\u0108\7%"+
		"\2\2\u0107\u0106\3\2\2\2\u0107\u0108\3\2\2\2\u0108\u0109\3\2\2\2\u0109"+
		"\u010a\7$\2\2\u010a\u013f\5\n\6\b\u010b\u010c\f\6\2\2\u010c\u010d\t\b"+
		"\2\2\u010d\u013f\5\n\6\7\u010e\u010f\f\5\2\2\u010f\u0110\7&\2\2\u0110"+
		"\u013f\5\n\6\6\u0111\u0112\f\4\2\2\u0112\u0113\7\'\2\2\u0113\u013f\5\n"+
		"\6\5\u0114\u0115\f\17\2\2\u0115\u0116\7\16\2\2\u0116\u0117\7E\2\2\u0117"+
		"\u0123\7\5\2\2\u0118\u011d\5\n\6\2\u0119\u011a\7\13\2\2\u011a\u011c\5"+
		"\n\6\2\u011b\u0119\3\2\2\2\u011c\u011f\3\2\2\2\u011d\u011b\3\2\2\2\u011d"+
		"\u011e\3\2\2\2\u011e\u0121\3\2\2\2\u011f\u011d\3\2\2\2\u0120\u0122\7\13"+
		"\2\2\u0121\u0120\3\2\2\2\u0121\u0122\3\2\2\2\u0122\u0124\3\2\2\2\u0123"+
		"\u0118\3\2\2\2\u0123\u0124\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u013f\7\6"+
		"\2\2\u0126\u0127\f\16\2\2\u0127\u0128\7\16\2\2\u0128\u013f\7E\2\2\u0129"+
		"\u012a\f\r\2\2\u012a\u0136\7\5\2\2\u012b\u0130\5\n\6\2\u012c\u012d\7\13"+
		"\2\2\u012d\u012f\5\n\6\2\u012e\u012c\3\2\2\2\u012f\u0132\3\2\2\2\u0130"+
		"\u012e\3\2\2\2\u0130\u0131\3\2\2\2\u0131\u0134\3\2\2\2\u0132\u0130\3\2"+
		"\2\2\u0133\u0135\7\13\2\2\u0134\u0133\3\2\2\2\u0134\u0135\3\2\2\2\u0135"+
		"\u0137\3\2\2\2\u0136\u012b\3\2\2\2\u0136\u0137\3\2\2\2\u0137\u0138\3\2"+
		"\2\2\u0138\u013f\7\6\2\2\u0139\u013a\f\13\2\2\u013a\u013b\7\7\2\2\u013b"+
		"\u013c\5\n\6\2\u013c\u013d\7\b\2\2\u013d\u013f\3\2\2\2\u013e\u00fc\3\2"+
		"\2\2\u013e\u00ff\3\2\2\2\u013e\u0102\3\2\2\2\u013e\u0105\3\2\2\2\u013e"+
		"\u010b\3\2\2\2\u013e\u010e\3\2\2\2\u013e\u0111\3\2\2\2\u013e\u0114\3\2"+
		"\2\2\u013e\u0126\3\2\2\2\u013e\u0129\3\2\2\2\u013e\u0139\3\2\2\2\u013f"+
		"\u0142\3\2\2\2\u0140\u013e\3\2\2\2\u0140\u0141\3\2\2\2\u0141\13\3\2\2"+
		"\2\u0142\u0140\3\2\2\2\'\20\23\30\37\66:<BG_hmtw\u0081\u008e\u0099\u00b3"+
		"\u00ba\u00cd\u00d1\u00d3\u00e0\u00e4\u00e6\u00ef\u00f3\u00fa\u0107\u011d"+
		"\u0121\u0123\u0130\u0134\u0136\u013e\u0140";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}