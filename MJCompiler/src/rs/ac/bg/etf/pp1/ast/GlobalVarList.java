// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class GlobalVarList extends ProgDeclList {

    private GlobalDecl GlobalDecl;

    public GlobalVarList (GlobalDecl GlobalDecl) {
        this.GlobalDecl=GlobalDecl;
        if(GlobalDecl!=null) GlobalDecl.setParent(this);
    }

    public GlobalDecl getGlobalDecl() {
        return GlobalDecl;
    }

    public void setGlobalDecl(GlobalDecl GlobalDecl) {
        this.GlobalDecl=GlobalDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GlobalDecl!=null) GlobalDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalDecl!=null) GlobalDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalDecl!=null) GlobalDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVarList(\n");

        if(GlobalDecl!=null)
            buffer.append(GlobalDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVarList]");
        return buffer.toString();
    }
}
