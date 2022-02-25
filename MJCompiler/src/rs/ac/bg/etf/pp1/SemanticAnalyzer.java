package rs.ac.bg.etf.pp1;

import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
import rs.ac.bg.etf.pp1.ast.*;
import java.util.List;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	public static Struct boolType;
	private Struct globalVarType = null;
	private boolean isFinal = false;
	private Obj currMethod = null;
	private MyTableVisitor myTableVisitor = new MyTableVisitor();
	Logger log = Logger.getLogger(getClass());
	boolean errorDetected = false;
	boolean haveReturn = false;
	boolean haveMain = false;
	public static int numOfVars = 0;
	Struct methodRetType = Tab.noType;
	Struct noType = Tab.noType;

	public static List<String> labels = new ArrayList<>();
	public static List<Integer> labelsOffs = new ArrayList<>();
	public static List<String> gotolabels = new ArrayList<>();

	public SemanticAnalyzer() {
		this.addBoolInTab();
	}

	private void addBoolInTab() {
		boolType = new Struct(Struct.Bool);
		boolType.setElementType(noType);
		Tab.insert(Obj.Type, "bool", boolType);
	}

	public void visit(Prog program) {
		numOfVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
		if (!haveMain) {
			report_error("SEMANTICKA GRESKA : Program mora imati globalnu metodu main", program);
		}
	}

	public void visit(ProgName programName) {
		programName.obj = Tab.insert(Obj.Prog, programName.getProgName(), noType);
		Tab.openScope();
	}

	public void visit(FinalArr finalArr) {

		isFinal = true;
	}

	public void visit(NonFinal nonFinal) {

		isFinal = false;
	}

	public void visit(Type type) {

		Obj typeNode = Tab.find(type.getTypeName());
		if (!isObject(typeNode)) {
			if (Obj.Type != typeNode.getKind()) {
				error_message(" Dati identifikator ne predstavlja tip!", type.getLine(), type.getTypeName());
				type.struct = noType;

			} else {
				type.struct = typeNode.getType();
			}

		} else {
			error_message("Nije pronadjen tip u tablei simbola", type.getLine(), type.getTypeName());
			type.struct = noType;
		}
		globalVarType = type.struct;
	}

	public void visit(MethodDecl methodDecl) {
		Obj obj = methodDecl.getMethodName().obj;

		Tab.chainLocalSymbols(currMethod);
		Tab.closeScope();
		for (int i = 0; i < gotolabels.size(); i++) {
			if (!labels.contains(gotolabels.get(i)))
				error_message("  ne postoji labela na koju ce se skociti goto ", methodDecl.getLine(),
						gotolabels.get(i));
		}
		if (obj.getName().equalsIgnoreCase("main"))
			haveMain = true;

		labels = null;
		gotolabels = null;
		currMethod = null;
		haveReturn = false;
		methodRetType = noType;
	}

	public void visit(MethName methName) {
		currMethod = Tab.insert(Obj.Meth, methName.getMethName(), methName.getMethRetType().struct);
		methName.obj = currMethod;
		Tab.openScope();
		info_message(". Vrsi se obrada funkcije ", methName.getLine(), methName.getMethName());

	}

	public void visit(GlobalVar globalVar) {

		if (!isObject(Tab.find(globalVar.getVarName())))
			error_message(" vec deklarisano.", globalVar.getLine(), globalVar.getVarName());

		else {

			if (isNoType(globalVarType))
				error_message(" losa deklaracija tipa globalne promenljive", globalVar.getLine(),
						globalVar.getVarName());

			else {
				globalVar.obj = Tab.insert(Obj.Var, globalVar.getVarName(), globalVarType);
				info_message(". Vrsi se deklaracija promenljive ", globalVar.getLine(), globalVar.getVarName());

			}
		}
	}

	public void visit(GlobalVarDeclaration globalVarDeclaration) {

		isFinal = false;

	}

	public void visit(GlobalArr globalArr) {

		Obj obj = Tab.find(globalArr.getVarName());

		if (isObject(obj) && !isNoType(globalVarType)) {
			Struct arrType = new Struct(Struct.Array);

			globalArr.obj = Tab.insert(Obj.Var, globalArr.getVarName(), arrType);
			globalArr.obj.getType().setElementType(globalVarType);
			if (isFinal) {
				globalArr.obj.setFpPos(2); // definisem da je konkretan niz final niz
			}
			info_message(". Vrsi se deklaracija promenljive ", globalArr.getLine(), globalArr.getVarName());

		}

		if (!isObject(obj))
			error_message(" vec deklarisano.", globalArr.getLine(), globalArr.getVarName());

		else if (isNoType(globalVarType))
			error_message(" losa deklaracija tipa globalne promenljive", globalArr.getLine(), globalArr.getVarName());

	}

	public void visit(LocalVar localVar) {
		Obj obj = Tab.currentScope().findSymbol(localVar.getVarName());
		if (isObject(obj))
			error_message(" vec deklarisano.", localVar.getLine(), localVar.getVarName());

		else {
			if (isNoType(globalVarType))
				error_message(":  Lose deklarisan tip za promenljivu ", localVar.getLine(), localVar.getVarName());
			else {
				info_message(". Vrsi se deklaracija promenljive ", localVar.getLine(), localVar.getVarName());
				localVar.obj = Tab.insert(Obj.Var, localVar.getVarName(), globalVarType);
				localVar.obj.getType().setElementType(noType);

			}
		}

	}

	public void visit(LocalArr localArr) {
		Obj obj = Tab.currentScope().findSymbol(localArr.getVarName());
		if (isObject(obj))
			error_message(" vec deklarisano.", localArr.getLine(), localArr.getVarName());

		else {
			if (isNoType(globalVarType))
				error_message(":  Lose deklarisan tip za promenljivu ", localArr.getLine(), localArr.getVarName());

			else {
				info_message(". Vrsi se deklaracije promenljive ", localArr.getLine(), localArr.getVarName());
				localArr.obj = Tab.insert(Obj.Var, localArr.getVarName(), new Struct(Struct.Array));
				localArr.obj.getType().setElementType(globalVarType);

			}
		}
	}

	public void visit(VoidRet voidRet) {
		voidRet.struct = new Struct(Struct.None);
	}

	private void setValueOfConst(ConstDeclVal constType, Obj obj) {
		if (constType.getClass() == BoolConstant.class) {
			BoolConstant cit = (BoolConstant) constType;
			obj.setAdr(cit.getValue().equals("true") ? 1 : 0);
		}
		if (constType.getClass() == CharConstant.class) {
			CharConstant cit = (CharConstant) constType;
			obj.setAdr(cit.getValue());
		}
		if (constType.getClass() == NumConstant.class) {
			NumConstant cit = (NumConstant) constType;
			obj.setAdr(cit.getValue());
		}

	}

	public void visit(BoolConstant boolConst) {
		Obj obj = Tab.find("bool");
		boolConst.struct = obj.getType();
	}

	public void visit(CharConstant charConst) {
		charConst.struct = Tab.charType;
	}

	public void visit(NumConstant numConst) {
		numConst.struct = Tab.intType;
	}

	public void visit(ConstrDeclOne constDecl) {
		Obj obj1 = Tab.find(constDecl.getConstName().getName());
		if (!isObject(obj1)) {
			error_message(" je vec definisana.", constDecl.getLine(), constDecl.getConstName().getName());

		} else {
			if (constDecl.getConstDeclVal().struct.assignableTo(globalVarType)) {
				Obj obj = Tab.insert(Obj.Con, constDecl.getConstName().getName(), globalVarType);
				setValueOfConst(constDecl.getConstDeclVal(), obj);
				info_message(". Vrsi se deklaracije konstante ", constDecl.getLine(),
						constDecl.getConstName().getName());

			} else
				error_message(" Vrednost koja se dodeljuje konstanti i tip konstante nisu kompatibilni",
						constDecl.getLine(), constDecl.getConstName().getName());
		}
	}

	public boolean checkType(Obj obj) {
		return (obj.getType() == Tab.intType || obj.getType() == Tab.charType || obj.getType() == boolType);
	}

	public boolean checkType(PrintStmt printStmt) {
		return (printStmt.getExpr().struct == Tab.intType || printStmt.getExpr().struct == Tab.charType
				|| printStmt.getExpr().struct == boolType || printStmt.getExpr().struct.getKind() == Struct.Array);
	}

	public void visit(ReadStmt readStmt) {

		Obj obj = Tab.find(readStmt.getDesignator().obj.getName());
		if (isObject(obj))
			report_errorNoDeclared(readStmt.getLine(), readStmt.getDesignator().obj.getName());
		else {

			Obj readObj = readStmt.getDesignator().obj;
			if (readObj.getType().getKind() == Struct.Array)
				error_message(" niz se ne moze citati.", readStmt.getLine(), readObj.getName());

			else {
				if (!checkType(readObj))
					error_message(" mora biti tipa int, char ili const.", readStmt.getLine(), readObj.getName());

				if (readObj.getKind() != Obj.Var && readObj.getKind() != Obj.Elem)
					error_message(" mora biti promenljiva.", readStmt.getLine(), readObj.getName());
			}
		}
	}

	public void visit(LabelDecl labelDecl) {
		if (!labels.contains(labelDecl.getI1().toString())) {
			labels.add(labelDecl.getI1().toString());
			labelsOffs.add(Code.pc);
			info_message(". U listu labela je dodata nova labela: ", labelDecl.getLine(), labelDecl.getI1().toString());

		} else
			error_message(" visestruko pojavljivanje lebele istog imena u okviru jedne metode", labelDecl.getLine(),
					labelDecl.getI1().toString());

	}

	public void visit(NoColon gotoLabel) {
		if (!gotolabels.contains(gotoLabel.getI1())) {
			gotolabels.add(gotoLabel.getI1());
			info_message(". U listu labela je dodata nova labela: ", gotoLabel.getLine(), gotoLabel.getI1().toString());
		}
	}

	public void visit(PrintStmt printStmt) {

		if (!checkType(printStmt)) {
			error_message(": Izraz unutar print naredbe mora biti tipa int, char ili bool.", printStmt.getLine(), "");
		}
	}

	public void visit(FactorDesignator factorDesignator) {
		if (isObject(factorDesignator.getDesignator().obj))
			factorDesignator.struct = noType;
		else
			factorDesignator.struct = factorDesignator.getDesignator().obj.getType();

	}

	public void visit(Inc inc) {
		Obj obj = inc.getDesignator().obj;
		if (isObject(obj))
			report_errorNoDeclared(inc.getLine(), inc.getDesignator().obj.getName());
		else {
			if (obj.getKind() != Obj.Var && obj.getKind() != Obj.Elem)
				error_message("mora biti promenljiva ili element niza", inc.getLine(), obj.getName());

			if (obj.getType() != Tab.intType)
				error_message(" mora biti tipa int.", inc.getLine(), obj.getName());

		}
	}

	public void visit(Dec dec) {
		Obj obj = dec.getDesignator().obj;
		if (isObject(obj))
			report_errorNoDeclared(dec.getLine(), dec.getDesignator().obj.getName());
		else {
			if (obj.getKind() != Obj.Var && obj.getKind() != Obj.Elem)
				error_message("mora biti promenljiva ili element niza", dec.getLine(), obj.getName());

			if (obj.getType() != Tab.intType)
				error_message(" mora biti tipa int.", dec.getLine(), obj.getName());
		}
	}

	public void visit(Expr expr) {
		expr.struct = expr.getAddopList().struct;
	}

	public void visit(Term term) {
		term.struct = term.getMulopList().struct;
	}

	public void visit(Assignment assignment) {

		if (!assignment.getExpr().struct.assignableTo(assignment.getDesignator().obj.getType())) {
			error_message("Doslo je do greske pri dodeli", assignment.getLine(),"");
		}
	}

	public void visit(ArrAddr arrAddr) {
		if (!isObject(((DesignatorArr) arrAddr.getParent()).obj))
			arrAddr.obj = Tab.find(((DesignatorArr) arrAddr.getParent()).getDesignatorName().getDesignName());
	}

	public boolean isObject(Obj obj) {
		return obj == Tab.noObj;
	}

	public void visit(DesignatorArr designatorArr) {

		designatorArr.obj = Tab.find(designatorArr.getDesignatorName().getDesignName());

		if (designatorArr.getExpr().struct == noType) {
			designatorArr.obj = Tab.noObj;
		} else {
			if (designatorArr.getExpr().struct.getKind() == Struct.Int) {
				if (isObject(Tab.find(designatorArr.obj.getName()))) {
					report_errorNoDeclared(designatorArr.getLine(), designatorArr.obj.getName());

				} else {
					if (designatorArr.obj.getType().getKind() != Struct.Array) {
						error_message(" nije niz.", designatorArr.getLine(),
								designatorArr.getDesignatorName().getDesignName());
						designatorArr.obj = Tab.noObj;
					} else {

						designatorArr.obj = new Obj(Obj.Elem, designatorArr.getDesignatorName().getDesignName(),
								designatorArr.obj.getType().getElemType(), designatorArr.obj.getAdr(),
								designatorArr.obj.getLevel());

						report_info(myTableVisitor.printObjNode(designatorArr.obj), designatorArr);

					}
				}
			}

			else
				error_message(": Izraz unutar zagrada mora biti tipa int.", designatorArr.getLine(), " ");

		}
	}

	public void visit(DesignatorOne designatorOne) {

		designatorOne.obj = Tab.find(designatorOne.getDesignatorName().getDesignName());

		if (isObject(designatorOne.obj)) {
			report_errorNoDeclared(designatorOne.getLine(), designatorOne.getDesignatorName().getDesignName());
		} else {
			info_message(myTableVisitor.printObjNode(designatorOne.obj), designatorOne.getLine(),
					designatorOne.getDesignatorName().getDesignName());
		}
	}

	public void visit(Designator designator) {
		designator.obj = Tab.find(designator.obj.getName());
	}

	public boolean isNoType(Struct s) {
		return s.equals(noType);
	}

	public void visit(AddopListDecl addopListDecl) {
		if (addopListDecl.getAddopList().struct.equals(Tab.intType)
				&& addopListDecl.getTerm().struct.equals(Tab.intType))
			addopListDecl.struct = Tab.intType;
		else {
			error_message("Pokusava se addOp sa vrednostima koje nisu tipa integer", addopListDecl.getLine(),"");
			addopListDecl.struct = noType;
		}
	}

	public void visit(NoAddopList noAddopList) {
		noAddopList.struct = noAddopList.getTerm().struct;
	}

	public void visit(Carret carret) {
		carret.getDesignator().obj = Tab.find(carret.getDesignator().obj.getName());
		if (carret.getDesignator().obj.getType().getKind() == Struct.Array) {
			if (carret.getDesignator().obj.getType().getElemType() != Tab.intType)
				error_message("Ne mozete koristiti operaciju carret ako niz nije tipa int", carret.getLine(),
						carret.obj.getName());
		} else
			error_message("Ne mozete koristiti operaciju carret na strukturi koja nije niz", carret.getLine(),
					carret.obj.getName());
	}

	public void visit(MulopListDecls mulopListDecls) {
		if (mulopListDecls.getMulopList().struct.equals(Tab.intType)
				&& mulopListDecls.getFactor().struct.equals(Tab.intType))
			mulopListDecls.struct = Tab.intType;
		else {
			error_message("Ne mozete koristiti operaciju mulop ako nije tipa int", mulopListDecls.getLine(),"");
			mulopListDecls.struct = noType;
		}
	}

	public void visit(FactorM factorM) {
		if (factorM.getFactorUn().struct.equals(Tab.intType) && factorM.getFactorUn1().struct.equals(Tab.intType))
			factorM.struct = Tab.intType;
		else {
			error_message("Ne moze monkey sa vrednostima koje nisu int", factorM.getLine(), "");
			factorM.struct = noType;
		}
	}
//	public void visit(FactorM2 factorM) {
//		if ((factorM.getFactorUn().getFactorSub().struct.getKind() == Struct.Array)
//				&& (factorM.getFactorUn1().getFactorSub().struct.getKind() == Struct.Array))
//			if (factorM.getFactorUn().getFactorSub().struct.getElemType() == Tab.intType
//					&& factorM.getFactorUn1().getFactorSub().struct.getElemType() == Tab.intType)
//				factorM.struct = new Struct(Struct.Array, factorM.getFactorUn().struct.getElemType());
//			else {
//				report_error("monkey ne moze ako tip podataka koji cine niz nije int", factorM);
//				factorM.struct = noType;
//			}
//
//		else {
//			report_error("monkey ne moze ako nije niz", factorM);
//			factorM.struct = noType;
//		}
//	}

	public void visit(NoMulopListDecls noMulopListDecls) {
		noMulopListDecls.struct = noMulopListDecls.getFactor().struct;
	}

	public void visit(NewFactor newFactor) {
		if (newFactor.getExpr().struct == Tab.intType) {
			newFactor.struct = new Struct(Struct.Array, newFactor.getType().struct);

		} else {
			error_message(". Tip unutar uglastih zagrada mora biti int", newFactor.getLine(), " ");
			newFactor.struct = noType;
		}
	}

	public void visit(HashDesignator hashDesignator) {
		hashDesignator.getDesignator().obj = Tab.find(hashDesignator.getDesignator().obj.getName());
		if (hashDesignator.getDesignator().obj.getType().getKind() == Struct.Array) {
			if (hashDesignator.getDesignator().obj.getType().getElemType() == Tab.intType)
				hashDesignator.struct = Tab.intType;
			else {
				error_message(" Ne moze se koristiti HASH ako int nije tip niza", hashDesignator.getLine(),
						hashDesignator.getDesignator().obj.getName());
				hashDesignator.struct = noType;
			}
		} else
			error_message(" Ne moze se koristiti HASH ako nije tipa niz", hashDesignator.getLine(),
					hashDesignator.getDesignator().obj.getName());

	}

	public void visit(FactorUn factorUn) {
		if (factorUn.getUnary() instanceof UnaryMinus) {
			if (factorUn.getFactorSub().struct.equals(Tab.intType))
				factorUn.struct = Tab.intType;
			else {
				factorUn.struct = noType;
				error_message("Ne moze se negirati vrednost koja nije tipa int", factorUn.getLine(),
						factorUn.getFactorSub().toString());
			}
		} else if (factorUn.getUnary() instanceof UnaryE)
			factorUn.struct = factorUn.getFactorSub().struct;
	}

	public void visit(FactorU factorU) {
		factorU.struct = factorU.getFactorUn().struct;
	}

	public void visit(NumFactor numFactor) {
		numFactor.struct = Tab.intType;
	}

	public void visit(CharFactor charFactor) {
		charFactor.struct = Tab.charType;
	}

	public void visit(BoolFactor boolFactor) {
		boolFactor.struct = boolType;
	}

	public void visit(ExprFactor exprFactor) {
		exprFactor.struct = exprFactor.getExpr().struct;
	}

	public void visit(ReturnNoExpr returnNoExpr) {

		if (!isNoType(methodRetType)) {
			report_error("SEMANTICKA GRESKA : Tip u return naredbi nije isti kao tip funkcije", returnNoExpr);
		}
		haveReturn = true;
	}

	public boolean passed() {
		return errorDetected == false;
	}

	public MyTableVisitor getMyTableVisitor() {
		return this.myTableVisitor;
	}

	public void error_message(String str, int line, String name) {
		report_error("SEMANTICKA GRESKA :Greska na liniji " + line + ": " + name + str, null);
	}

	public void info_message(String str, int line, String name) {
		report_info("Na liniji : " + line + str + name, null);
	}

	public void report_errorNoDeclared(int line, String name) {
		report_error("SEMANTICKA GRESKA : Greska na liniji " + line + ": Ime " + name + " nije deklarisano.", null);
	}

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());

	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	class MyTableVisitor extends DumpSymbolTableVisitor {

		@Override
		public void visitStructNode(Struct structToVisit) {
			super.visitStructNode(structToVisit);
			if (structToVisit.getKind() == Struct.Bool)
				output.append("boolType");
		}

		private String printObjNode(Obj objToVisit) {
			output.setLength(0);
			this.visitObjNode(objToVisit);
			String st = new String(super.output);
			output.setLength(0);
			return st;
		}
	}

//	public void visit(OtherRetType retType) {
//	retType.struct = retType.getType().struct;
//	haveReturn = true;
//}

//	  public void visit(ReturnExpr returnExpr) { Struct currMethType =
//	  currMethod.getType(); if
//	  (!currMethType.compatibleWith(returnExpr.getExpr().struct)) {
//	  report_error("SEMANTICKA GRESKA : Tip u return naredbi nije kompatibilan sa povratnim tipom funkcije"
//	  , returnExpr); } haveReturn = true; }	

//	public String printNumbers() {
//	StringBuilder sb = new StringBuilder();
//	sb.append("\n\nSEMANTICKA ANALIZA\n\n Statements :" + statementCnt + "\n Global cons: " + glConsCnt
//			+ "\n Global vars:" + glVarCnt + "\n Global arrays" + glArrCnt + "\n Local vars in main:" + locVarCnt
//			+ "\n");
//	return sb.toString();
//}

}
