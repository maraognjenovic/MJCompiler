// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class Expr implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private AddopList AddopList;

    public Expr (AddopList AddopList) {
        this.AddopList=AddopList;
        if(AddopList!=null) AddopList.setParent(this);
    }

    public AddopList getAddopList() {
        return AddopList;
    }

    public void setAddopList(AddopList AddopList) {
        this.AddopList=AddopList;
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
        if(AddopList!=null) AddopList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AddopList!=null) AddopList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AddopList!=null) AddopList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expr(\n");

        if(AddopList!=null)
            buffer.append(AddopList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expr]");
        return buffer.toString();
    }
}
