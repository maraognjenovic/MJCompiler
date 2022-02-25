package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import org.apache.log4j.Logger;

public class CodeGenerator extends VisitorAdaptor {
	Logger log = Logger.getLogger(getClass());

	private static List<String> labelList = new ArrayList<>();
	private static List<String> notFoundLabels = new ArrayList<>();
	private static List<Integer> labelsOffs = new ArrayList<>();
	private static List<Integer> notFoundLabelsOffs = new ArrayList<>();
	private int mainPc;
	Obj programObj;
	Obj noObj = Tab.noObj;
	Obj monkey = noObj;

	private Obj finalArrayObj = null;

	private Obj myArray;

	public int getMainPc() {
		return this.mainPc;
	}

	public void visit(ProgName progName) {
		programObj = Tab.find(progName.getProgName());
	}

	public void visit(LabelDecl labelDecl) {

		if (notFoundLabels.contains(labelDecl.getI1())) {
			int k = notFoundLabels.indexOf(labelDecl.getI1());
			Code.fixup(notFoundLabelsOffs.get(k));
			notFoundLabelsOffs.remove(k);
			notFoundLabels.remove(k);
		}
		labelList.add(labelDecl.getI1());
		labelsOffs.add(Code.pc);

	}

	public void visit(NoColon gotoLabel) {
		int i;
		if (labelList.contains(gotoLabel.getI1())) {
			i = labelList.indexOf(gotoLabel.getI1());
			Code.putJump(labelsOffs.get(i));

		} else {
			notFoundLabels.add(gotoLabel.getI1());
			notFoundLabelsOffs.add(Code.pc + 1);
			Code.putJump(Code.pc);
			Code.fixup(notFoundLabelsOffs.get(0));
		}

	}

	public void visit(MethodDecl methDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(MethName methName) {

		if ("main".equalsIgnoreCase(methName.getMethName())) {
			mainPc = Code.pc;
		}
		methName.obj.setAdr(mainPc);
		Code.put(Code.enter);
		Code.put(methName.obj.getLevel());
		Code.put(methName.obj.getLocalSymbols().size());
	}

	public void visit(ReadStmt stmt) {
		Obj obj = stmt.getDesignator().obj;
		if (obj.getType() == Tab.charType) {
			Code.put(Code.bread);
		} else {
			Code.put(Code.read);
		}
		Code.store(obj);
	}

	public void visit(PrintStmt stmt) {
		int loadConst = 0;
		int putCode, aload;
		if (stmt.getExpr().struct.getKind() == Struct.Array) {
			if (stmt.getExpr().struct.getElemType() == Tab.charType) {
				putCode = Code.bprint;
				aload = Code.baload;
			} else {
				putCode = Code.print;
				aload = Code.aload;
			}
			if (stmt.getPrintConstantants() instanceof HasPrintConstantants) {
				int index = ((HasPrintConstantants) stmt.getPrintConstantants()).getN1();

				Code.loadConst(index);
				Code.put(aload);
				Code.loadConst(1);
				Code.put(putCode);
			} else {

				Code.loadConst(0);
				Code.put(Code.dup2);

				Code.put(aload);
				Code.loadConst(1);
				Code.put(putCode);

				Code.put(Code.dup2);
				start = Code.pc;

				Code.loadConst(1);
				Code.put(Code.add);
				Code.put(Code.dup_x2);
				Code.put(Code.pop);
				Code.put(Code.pop);
				Code.put(Code.pop);
				Code.put(Code.dup2);
				Code.put(Code.dup_x1);
				Code.put(Code.pop);

				Code.put(Code.arraylength);

				Code.putFalseJump(Code.lt, 0);
				isFinish = Code.pc - 2;

				Code.put(Code.dup2);

				Code.put(aload);
				Code.loadConst(1);
				Code.put(putCode);
				Code.put(Code.dup2);

				Code.putJump(start);
				Code.fixup(isFinish);
				Code.put(Code.pop);
				Code.put(Code.pop);

			}

		} else {
			if (stmt.getExpr().struct == Tab.charType) {
				putCode = Code.bprint;
			} else {
				putCode = Code.print;
			}
			if (stmt.getPrintConstantants() instanceof HasPrintConstantants) {
				loadConst = ((HasPrintConstantants) stmt.getPrintConstantants()).getN1();
			}
			Code.loadConst(loadConst);
			Code.put(putCode);
		}
	}

	public void visit(FactorM factorM) {
		Code.put(Code.add);
		Code.put(Code.dup);
		Code.put(Code.mul);
	}

	public void visit(Swap mod) {
		Integer num1 = ((NumFactor) mod.getExpr().getAddopList().getParent()).getN1();
		Integer num2 = ((NumFactor) mod.getExpr().getAddopList().getParent()).getN1();
		Code.load(mod.getArrAddr().obj);
		Code.loadConst(num1);
		Code.load(mod.getArrAddr().obj);
		Code.loadConst(num2);
		Code.put(Code.aload);
		Code.load(mod.getArrAddr().obj);
		Code.loadConst(num2);
		Code.load(mod.getArrAddr().obj);
		Code.loadConst(num1);
		Code.put(Code.aload);
		Code.put(Code.astore);
		Code.put(Code.astore);
	}

//	public void visit(DesignatorArr mod) {
//		Code.load(mod.getArrAddr().obj);
//		Code.loadConst(mod.getExpr().getAddopList());
//		Code.load(mod.getArrAddr().obj);
//		Code.loadConst(mod.getSwapIndex().ge);
//		Code.put(Code.aload);
//		Code.load(mod.getArrAddr().obj);
//		Code.loadConst(mod.getN3());
//		Code.load(mod.getArrAddr().obj);
//		Code.loadConst(mod.getN2());
//		Code.put(Code.aload);
//		Code.put(Code.astore);
//		Code.put(Code.astore);
//	}

	public void visit(ArrAddr arrPrep) {
		myArray = arrPrep.obj;
		Code.load(myArray);
	}

//koliko puta se pristupilo elementu niza!!!!!!!!!!!!
//	public void visit(HashDesignator desArr) {
//		Code.put(Code.dup2);
//		Code.put(Code.dup2);
//		Code.put(Code.pop);
//		Code.put(Code.arraylength);
//		Code.loadConst(2);
//		Code.put(Code.div);
//		Code.put(Code.add);
//		Code.put(Code.dup2);
//		Code.put(Code.aload);
//		Code.loadConst(1);
//		Code.put(Code.add);
//		Code.put(Code.dup);
//		Code.put(Code.astore);
//	}

	public void visit(NewFactor fNew) {
		// Code.put(Code.dup); // koliko puta se pristupilo elementu niza
		// Code.put(Code.add); // koliko puta se pristupilo elementu niza
		if (finalArrayObj != null) {
			Code.loadConst(2);
			Code.put(Code.mul);
		}

		Code.put(Code.newarray);
		if (fNew.getType().struct == Tab.charType)
			Code.put(0);
		else
			Code.put(1);
	}

	public void visit(DesignatorOne design) {

		if (design.obj.getType().getKind() == Struct.Array && design.obj.getLevel() == 0
				&& design.obj.getFpPos() == 2) {
			finalArrayObj = design.obj;
		}
	}

	// MAKSIMUM NIZA!!!!!!!!!!!!!!!!!!!!!!!!1
	public void visit(HashDesignator mod) {
		Code.load(mod.getDesignator().obj);
		Code.loadConst(0);
		Code.put(Code.aload);
		Code.loadConst(0);

		start = Code.pc;
		Code.put(Code.dup);

		Code.loadConst(1);
		Code.put(Code.add);

		Code.load(mod.getDesignator().obj);
		Code.put(Code.arraylength);

		Code.putFalseJump(Code.lt, Code.pc + 1);
		isFinish = Code.pc - 2;

		Code.loadConst(1);
		Code.put(Code.add);

		Code.put(Code.dup);

		Code.load(mod.getDesignator().obj);

		Code.put(Code.dup_x1);
		Code.put(Code.pop);

		Code.put(Code.aload);

		Code.put(Code.dup_x1);
		Code.put(Code.pop);

		Code.put(Code.dup_x2);
		Code.put(Code.pop);

		Code.put(Code.dup2);

		Code.putFalseJump(Code.ge, Code.pc + 1);
		isNewMax = Code.pc - 2;

		Code.put(Code.pop);

		Code.put(Code.dup_x1);
		Code.put(Code.pop);

		Code.putJump(start);
		Code.fixup(isNewMax);

		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.pop);

		Code.putJump(start);
		Code.fixup(isFinish);
		Code.put(Code.pop);

	}

	int start = 0;
	int isFinish = 0;
	int isNewMax = 0;

	public void visit(FactorUn factorUn) {
		if (factorUn.getUnary() instanceof UnaryMinus)
			Code.put(Code.neg);
	}

	public void visit(Assignment assignStmt) {
		Obj designatorObj = assignStmt.getDesignator().obj;
		if (designatorObj.getKind() == Obj.Elem) {
			Obj arr = ((DesignatorArr) assignStmt.getDesignator()).getArrAddr().obj;
			if (arr.getLevel() == 0 && arr.getFpPos() == 2) {
				// arr index val
				Code.put(Code.dup2);
				// arr index val index val
				Code.put(Code.pop);
				// arr index val index
				Code.load(arr);
				// arr index val index arr
				Code.put(Code.arraylength);
				// arr index val index n
				Code.loadConst(2);
				// arr index val index n 2
				Code.put(Code.div);
				// arr index val index n
				Code.put(Code.add);
				// arr index val index+n
				Code.load(arr);
				// arr index val index+n arr
				Code.put(Code.dup_x1);
				// arr index val arr index+n arr
				Code.put(Code.pop);
				// arr index val arr index+n
				Code.put(Code.aload);
				// arr index val elem
				Code.loadConst(0);
				// arr index val elem 0
				Code.putFalseJump(Code.ne, 0);
				int izvrsidodelu = Code.pc - 2;
				/*
				 * //arr index val Code.put(Code.pop); //arr index Code.put(Code.dup2); //arr
				 * index arr index Code.put(Code.aload); //arr index val_old
				 */
				Code.put(Code.trap);
				Code.put(1);

				Code.fixup(izvrsidodelu);
				// arr index val(_old)

				// arr index val(_old)
				Code.put(Code.dup2);
				// arr index val(_old) index val(_old)
				Code.put(Code.pop);
				// arr index val(_old) index
				Code.load(arr);
				// arr index val(_old) index arr
				Code.put(Code.dup_x1);
				// arr index val(_old) arr index arr
				Code.put(Code.arraylength);
				// arr index val(_old) arr index n
				Code.loadConst(2);
				// arr index val(_old) arr index n 2
				Code.put(Code.div);
				// arr index val(_old) arr index n
				Code.put(Code.add);
				// arr index val(_old) arr index+n
				Code.loadConst(1);
				// arr index val(_old) arr index+n 1
				Code.put(Code.astore);
				// arr index val(_old)

			}
		}
		Code.store(designatorObj);

		finalArrayObj = null;
	}

	public void visit(Carret mod) {

		Code.load(mod.getDesignator().obj);
		Code.loadConst(0);
		Code.load(mod.getDesignator().obj);
		Code.loadConst(0);
		Code.put(Code.aload);
		Code.loadConst(mod.getN2());
		Code.put(Code.mul);
		Code.put(Code.astore);

		Code.load(mod.getDesignator().obj);
		Code.loadConst(0);

		start = Code.pc;
		Code.put(Code.dup);

		Code.loadConst(1);
		Code.put(Code.add);

		Code.load(mod.getDesignator().obj);
		Code.put(Code.arraylength);

		Code.putFalseJump(Code.lt, 0);
		isFinish = Code.pc - 2;

		Code.put(Code.dup2);

		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup_x2);
		Code.put(Code.dup2);

		Code.put(Code.aload);
		Code.loadConst(mod.getN2());
		Code.put(Code.mul);
		Code.put(Code.astore);
		Code.put(Code.pop);

		Code.putJump(start);
		Code.fixup(isFinish);
		Code.put(Code.pop);
		Code.put(Code.pop);

	}

	public void visit(Inc inc) {

		if (inc.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);

		Code.load(inc.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(inc.getDesignator().obj);
	}

	public void visit(Dec dec) {
		if (dec.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);

		Code.load(dec.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(dec.getDesignator().obj);
	}

	public void visit(AddopListDecl addopListDecl) {
		Class<? extends Addop> addop = addopListDecl.getAddop().getClass();
		if (addop.equals(AddopDecl.class))
			Code.put(Code.add);
		if (addop.equals(SubopDecl.class))
			Code.put(Code.sub);
	}

	public void visit(MulopListDecls dulopListDecls) {
		Class<? extends Mulop> mulop = dulopListDecls.getMulop().getClass();
		if (mulop.equals(MulDecl.class))
			Code.put(Code.mul);
		if (mulop.equals(DivDecl.class))
			Code.put(Code.div);
		if (mulop.equals(ModDecl.class))
			Code.put(Code.rem);
	}

	public void visit(CharFactor fact) {
		Obj obj = new Obj(Obj.Con, "$", fact.struct);
		obj.setAdr(fact.getC1());
		Code.load(obj);
	}

	public void visit(NumFactor fact) {
		Obj obj = new Obj(Obj.Con, "$", fact.struct);
		obj.setAdr(fact.getN1());
		Code.load(obj);
	}

	public void visit(BoolFactor fact) {
		Obj obj = new Obj(Obj.Con, "$", fact.struct);
		obj.setAdr(fact.getB1().equals("true") ? 1 : 0);
		Code.load(obj);
	}

	public void visit(FactorDesignator fact) {
		Code.load(fact.getDesignator().obj);
	}

	public void visit(ReturnNoExpr returnNoExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void report_error(String message, SyntaxNode info) {

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
}
