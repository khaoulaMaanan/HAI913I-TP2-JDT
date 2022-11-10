package Visitors;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeDeclarationVisitor extends ASTVisitor {
    private int NbreClasses;
    private int NbreAttributs;
    private int NbreMethodes;
    private int nbreLignesCodeMethode;
    private Map<String, Integer> MapClassesNbreMethodes;
    private Map<String, Integer> mapClassesNbreAttributs;
    private List<TypeDeclaration> types;
    private List<TypeDeclaration> allTypes;

    public TypeDeclarationVisitor() {
        NbreClasses = 0;
        NbreAttributs = 0;
        NbreMethodes = 0;
        MapClassesNbreMethodes = new HashMap<>();
        mapClassesNbreAttributs = new HashMap<>();
        nbreLignesCodeMethode = 0;
        types = new ArrayList<>();
        allTypes = new ArrayList<>();
    }

    public int getNbreClasses() {
        return NbreClasses;
    }

    public void setNbreClasses(int nbreClasses) {
        NbreClasses = nbreClasses;
    }

    public int getNbreAttributs() {
        return NbreAttributs;
    }

    public void setNbreAttributs(int nbreAttributs) {
        NbreAttributs = nbreAttributs;
    }

    public int getNbreMethodes() {
        return NbreMethodes;
    }

    public void setNbreMethodes(int nbreMethodes) {
        NbreMethodes = nbreMethodes;
    }

    public Map<String, Integer> getMapClassesNbreAttributs() {
        return mapClassesNbreAttributs;
    }

    public void setMapClassesNbreAttributs(Map<String, Integer> mapClassesNbreAttributs) {
        this.mapClassesNbreAttributs = mapClassesNbreAttributs;
    }

    public Map<String, Integer> getMapClassesNbreMethodes() {
        return MapClassesNbreMethodes;
    }

    public void setMapClassesNbreMethodes(Map<String, Integer> mapClassesNbreMethodes) {
        MapClassesNbreMethodes = mapClassesNbreMethodes;
    }

    public int getNbreLignesCodeMethode() {
        return nbreLignesCodeMethode;
    }

    public void setNbreLignesCodeMethode(int nbreLignesCodeMethode) {
        this.nbreLignesCodeMethode = nbreLignesCodeMethode;
    }

    public List<TypeDeclaration> getAllTypes() {
        return allTypes;
    }

    public void setAllTypes(List<TypeDeclaration> allTypes) {
        this.allTypes = allTypes;
    }

    public List<TypeDeclaration> getTypes() {
        return types;
    }

    public void setTypes(List<TypeDeclaration> types) {
        this.types = types;
    }

    public boolean visit(TypeDeclaration node) {
        allTypes.add(node);
        if (!node.isInterface()) {
            types.add(node);
            NbreClasses++;
            NbreAttributs += node.getFields().length;
            NbreMethodes += node.getMethods().length;
            MapClassesNbreMethodes.put(node.getName().getFullyQualifiedName(), node.getMethods().length);
            mapClassesNbreAttributs.put(node.getName().getFullyQualifiedName(), node.getFields().length);
        }


        return super.visit(node);
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

