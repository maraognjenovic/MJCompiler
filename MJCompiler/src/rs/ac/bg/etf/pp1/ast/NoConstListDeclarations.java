// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class NoConstListDeclarations extends ConstDeclList {

    private ConstrDeclOne ConstrDeclOne;

    public NoConstListDeclarations (ConstrDeclOne ConstrDeclOne) {
        this.ConstrDeclOne=ConstrDeclOne;
        if(ConstrDeclOne!=null) ConstrDeclOne.setParent(this);
    }

    public ConstrDeclOne getConstrDeclOne() {
        return ConstrDeclOne;
    }

    public void setConstrDeclOne(ConstrDeclOne ConstrDeclOne) {
        this.ConstrDeclOne=ConstrDeclOne;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstrDeclOne!=null) ConstrDeclOne.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstrDeclOne!=null) ConstrDeclOne.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstrDeclOne!=null) ConstrDeclOne.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoConstListDeclarations(\n");

        if(ConstrDeclOne!=null)
            buffer.append(ConstrDeclOne.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NoConstListDeclarations]");
        return buffer.toString();
    }
}
