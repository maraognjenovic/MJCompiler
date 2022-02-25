// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class ProgListDecls extends ProgList {

    private ProgList ProgList;
    private ProgDeclList ProgDeclList;

    public ProgListDecls (ProgList ProgList, ProgDeclList ProgDeclList) {
        this.ProgList=ProgList;
        if(ProgList!=null) ProgList.setParent(this);
        this.ProgDeclList=ProgDeclList;
        if(ProgDeclList!=null) ProgDeclList.setParent(this);
    }

    public ProgList getProgList() {
        return ProgList;
    }

    public void setProgList(ProgList ProgList) {
        this.ProgList=ProgList;
    }

    public ProgDeclList getProgDeclList() {
        return ProgDeclList;
    }

    public void setProgDeclList(ProgDeclList ProgDeclList) {
        this.ProgDeclList=ProgDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ProgList!=null) ProgList.accept(visitor);
        if(ProgDeclList!=null) ProgDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ProgList!=null) ProgList.traverseTopDown(visitor);
        if(ProgDeclList!=null) ProgDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ProgList!=null) ProgList.traverseBottomUp(visitor);
        if(ProgDeclList!=null) ProgDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgListDecls(\n");

        if(ProgList!=null)
            buffer.append(ProgList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ProgDeclList!=null)
            buffer.append(ProgDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgListDecls]");
        return buffer.toString();
    }
}
