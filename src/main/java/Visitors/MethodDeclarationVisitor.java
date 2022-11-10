package Visitors;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationVisitor extends ASTVisitor {
    List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
    private int nbreMaxParametre;
    private int nbreLignesCodeMethode;

    public MethodDeclarationVisitor() {
        nbreMaxParametre = 0;
        nbreLignesCodeMethode = 0;
    }

    public int getNbreLignesCodeMethode() {
        return nbreLignesCodeMethode;
    }

    public void setNbreLignesCodeMethode(int nbreLignesCodeMethode) {
        this.nbreLignesCodeMethode = nbreLignesCodeMethode;
    }

    public int getNbreMaxParametre() {
        return nbreMaxParametre;
    }

    public void setNbreMaxParametre(int nbreMaxParametre) {
        nbreMaxParametre = nbreMaxParametre;
    }

    public List<MethodDeclaration> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodDeclaration> methods) {
        this.methods = methods;
    }

    public boolean visit(MethodDeclaration node) {
        methods.add(node);
        nbreLignesCodeMethode = 0;
        if (nbreMaxParametre < node.parameters().size())
            nbreMaxParametre = node.parameters().size();
        return super.visit(node);
    }

    @Override
    public void endVisit(MethodDeclaration md) {
        setNbreLignesCodeMethode(nbreLignesCodeMethode);


    }

    @Override
    public boolean visit(AssertStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(Block node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(BreakStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(ConstructorInvocation node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(ContinueStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(DoStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(EmptyStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(EnhancedForStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(ExpressionStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(ForStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(IfStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(LabeledStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(ReturnStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(SuperConstructorInvocation node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(SwitchCase node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(SwitchStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(SynchronizedStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(ThrowStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(TryStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }


    @Override
    public boolean visit(TypeDeclarationStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }

    @Override
    public boolean visit(WhileStatement node) {
        nbreLignesCodeMethode++;
        return true;
    }


}
