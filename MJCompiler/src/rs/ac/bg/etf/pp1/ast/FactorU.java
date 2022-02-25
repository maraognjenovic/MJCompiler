// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class FactorU extends Factor {

    private FactorUn FactorUn;

    public FactorU (FactorUn FactorUn) {
        this.FactorUn=FactorUn;
        if(FactorUn!=null) FactorUn.setParent(this);
    }

    public FactorUn getFactorUn() {
        return FactorUn;
    }

    public void setFactorUn(FactorUn FactorUn) {
        this.FactorUn=FactorUn;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FactorUn!=null) FactorUn.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FactorUn!=null) FactorUn.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FactorUn!=null) FactorUn.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorU(\n");

        if(FactorUn!=null)
            buffer.append(FactorUn.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorU]");
        return buffer.toString();
    }
}
