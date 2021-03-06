package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Prog;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void tsdump() {
		Tab.dump();
	}
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(Compiler.class);
		
		Reader br = null;
		try {
			File sourceCode = new File("test/program.mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();
	        
	        Prog prog = (Prog)(s.value); 
	        
	        Tab.init();
	        log.info("=================SINTAKSNO STABLO==================");
			//log.info(prog.toString(""));
			log.info("===================================");

			SemanticAnalyzer v = new SemanticAnalyzer();
			
			prog.traverseBottomUp(v); 
			
			if(!p.errorDetected && v.passed()){
				tsdump();
				
				File objFile = new File("test/program.obj"); 
				if(objFile.exists()) objFile.delete();
				
				CodeGenerator codeGen = new CodeGenerator();
				
				prog.traverseBottomUp(codeGen);
				
				Code.dataSize = v.numOfVars;
				Code.mainPc = codeGen.getMainPc();
				Code.write(new FileOutputStream(objFile));
				log.info("Parsiranje uspesno zavrseno!");
				
			}else{
				log.error("Parsiranje NIJE uspesno zavrseno!");
			}
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
	
	
}

