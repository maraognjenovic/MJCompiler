// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class FactorM extends Factor {

    private FactorUn FactorUn;
    private FactorUn FactorUn1;

    public FactorM (FactorUn FactorUn, FactorUn FactorUn1) {
        this.FactorUn=FactorUn;
        if(FactorUn!=null) FactorUn.setParent(this);
        this.FactorUn1=FactorUn1;
        if(FactorUn1!=null) FactorUn1.setParent(this);
    }

    public FactorUn getFactorUn() {
        return FactorUn;
    }

    public void setFactorUn(FactorUn FactorUn) {
        this.FactorUn=FactorUn;
    }

    public FactorUn getFactorUn1() {
        return FactorUn1;
    }

    public void setFactorUn1(FactorUn FactorUn1) {
        this.FactorUn1=FactorUn1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FactorUn!=null) FactorUn.accept(visitor);
        if(FactorUn1!=null) FactorUn1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FactorUn!=null) FactorUn.traverseTopDown(visitor);
        if(FactorUn1!=null) FactorUn1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FactorUn!=null) FactorUn.traverseBottomUp(visitor);
        if(FactorUn1!=null) FactorUn1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorM(\n");

        if(FactorUn!=null)
            buffer.append(FactorUn.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FactorUn1!=null)
            buffer.append(FactorUn1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorM]");
        return buffer.toString();
    }
}
