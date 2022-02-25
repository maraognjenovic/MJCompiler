// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class GoToStatement extends SingleStatement {

    private LabelWithoutColon LabelWithoutColon;

    public GoToStatement (LabelWithoutColon LabelWithoutColon) {
        this.LabelWithoutColon=LabelWithoutColon;
        if(LabelWithoutColon!=null) LabelWithoutColon.setParent(this);
    }

    public LabelWithoutColon getLabelWithoutColon() {
        return LabelWithoutColon;
    }

    public void setLabelWithoutColon(LabelWithoutColon LabelWithoutColon) {
        this.LabelWithoutColon=LabelWithoutColon;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LabelWithoutColon!=null) LabelWithoutColon.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LabelWithoutColon!=null) LabelWithoutColon.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LabelWithoutColon!=null) LabelWithoutColon.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GoToStatement(\n");

        if(LabelWithoutColon!=null)
            buffer.append(LabelWithoutColon.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GoToStatement]");
        return buffer.toString();
    }
}
