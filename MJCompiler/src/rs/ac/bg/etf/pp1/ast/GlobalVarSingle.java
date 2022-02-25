// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class GlobalVarSingle extends GlobalParsList {

    private GlobalParsDecl GlobalParsDecl;

    public GlobalVarSingle (GlobalParsDecl GlobalParsDecl) {
        this.GlobalParsDecl=GlobalParsDecl;
        if(GlobalParsDecl!=null) GlobalParsDecl.setParent(this);
    }

    public GlobalParsDecl getGlobalParsDecl() {
        return GlobalParsDecl;
    }

    public void setGlobalParsDecl(GlobalParsDecl GlobalParsDecl) {
        this.GlobalParsDecl=GlobalParsDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GlobalParsDecl!=null) GlobalParsDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalParsDecl!=null) GlobalParsDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalParsDecl!=null) GlobalParsDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVarSingle(\n");

        if(GlobalParsDecl!=null)
            buffer.append(GlobalParsDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVarSingle]");
        return buffer.toString();
    }
}
