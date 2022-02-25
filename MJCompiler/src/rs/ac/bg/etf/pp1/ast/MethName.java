// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class MethName extends MethodName {

    private MethRetType MethRetType;
    private String methName;

    public MethName (MethRetType MethRetType, String methName) {
        this.MethRetType=MethRetType;
        if(MethRetType!=null) MethRetType.setParent(this);
        this.methName=methName;
    }

    public MethRetType getMethRetType() {
        return MethRetType;
    }

    public void setMethRetType(MethRetType MethRetType) {
        this.MethRetType=MethRetType;
    }

    public String getMethName() {
        return methName;
    }

    public void setMethName(String methName) {
        this.methName=methName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethRetType!=null) MethRetType.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethRetType!=null) MethRetType.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethRetType!=null) MethRetType.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethName(\n");

        if(MethRetType!=null)
            buffer.append(MethRetType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+methName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethName]");
        return buffer.toString();
    }
}
