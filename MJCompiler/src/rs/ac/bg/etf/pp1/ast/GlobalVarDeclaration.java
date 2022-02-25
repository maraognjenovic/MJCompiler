// generated with ast extension for cup
// version 0.8
// 25/0/2022 0:26:35


package rs.ac.bg.etf.pp1.ast;

public class GlobalVarDeclaration extends GlobalDecl {

    private FinalArrDecl FinalArrDecl;
    private Type Type;
    private GlobalParsList GlobalParsList;

    public GlobalVarDeclaration (FinalArrDecl FinalArrDecl, Type Type, GlobalParsList GlobalParsList) {
        this.FinalArrDecl=FinalArrDecl;
        if(FinalArrDecl!=null) FinalArrDecl.setParent(this);
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.GlobalParsList=GlobalParsList;
        if(GlobalParsList!=null) GlobalParsList.setParent(this);
    }

    public FinalArrDecl getFinalArrDecl() {
        return FinalArrDecl;
    }

    public void setFinalArrDecl(FinalArrDecl FinalArrDecl) {
        this.FinalArrDecl=FinalArrDecl;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public GlobalParsList getGlobalParsList() {
        return GlobalParsList;
    }

    public void setGlobalParsList(GlobalParsList GlobalParsList) {
        this.GlobalParsList=GlobalParsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FinalArrDecl!=null) FinalArrDecl.accept(visitor);
        if(Type!=null) Type.accept(visitor);
        if(GlobalParsList!=null) GlobalParsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FinalArrDecl!=null) FinalArrDecl.traverseTopDown(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(GlobalParsList!=null) GlobalParsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FinalArrDecl!=null) FinalArrDecl.traverseBottomUp(visitor);
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(GlobalParsList!=null) GlobalParsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVarDeclaration(\n");

        if(FinalArrDecl!=null)
            buffer.append(FinalArrDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalParsList!=null)
            buffer.append(GlobalParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVarDeclaration]");
        return buffer.toString();
    }
}
