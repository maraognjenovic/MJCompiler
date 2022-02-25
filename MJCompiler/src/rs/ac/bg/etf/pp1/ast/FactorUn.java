// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class FactorUn implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private Unary Unary;
    private FactorSub FactorSub;

    public FactorUn (Unary Unary, FactorSub FactorSub) {
        this.Unary=Unary;
        if(Unary!=null) Unary.setParent(this);
        this.FactorSub=FactorSub;
        if(FactorSub!=null) FactorSub.setParent(this);
    }

    public Unary getUnary() {
        return Unary;
    }

    public void setUnary(Unary Unary) {
        this.Unary=Unary;
    }

    public FactorSub getFactorSub() {
        return FactorSub;
    }

    public void setFactorSub(FactorSub FactorSub) {
        this.FactorSub=FactorSub;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Unary!=null) Unary.accept(visitor);
        if(FactorSub!=null) FactorSub.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Unary!=null) Unary.traverseTopDown(visitor);
        if(FactorSub!=null) FactorSub.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Unary!=null) Unary.traverseBottomUp(visitor);
        if(FactorSub!=null) FactorSub.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorUn(\n");

        if(Unary!=null)
            buffer.append(Unary.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FactorSub!=null)
            buffer.append(FactorSub.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorUn]");
        return buffer.toString();
    }
}
