// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class PrintStmt extends SingleStatement {

    private Expr Expr;
    private PrintConstantants PrintConstantants;

    public PrintStmt (Expr Expr, PrintConstantants PrintConstantants) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.PrintConstantants=PrintConstantants;
        if(PrintConstantants!=null) PrintConstantants.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public PrintConstantants getPrintConstantants() {
        return PrintConstantants;
    }

    public void setPrintConstantants(PrintConstantants PrintConstantants) {
        this.PrintConstantants=PrintConstantants;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(PrintConstantants!=null) PrintConstantants.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(PrintConstantants!=null) PrintConstantants.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(PrintConstantants!=null) PrintConstantants.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintStmt(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PrintConstantants!=null)
            buffer.append(PrintConstantants.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintStmt]");
        return buffer.toString();
    }
}
