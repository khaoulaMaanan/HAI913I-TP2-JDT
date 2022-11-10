package analyse;

import Visitors.MethodDeclarationVisitor;
import Visitors.MethodInvocationVisitor;
import Visitors.TypeDeclarationVisitor;
import clusters.AbstractCluster;
import clusters.ComplexCluster;
import clusters.SimpleCluster;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.javatuples.Pair;
import org.paukov.combinatorics3.Generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Processor {
    private final String projectPath;
    private final Parser parser;
    private final TypeDeclarationVisitor typeDeclarationVisitor;
    private final TypeDeclarationVisitor newtypeDeclarationVisitor;
    private final MethodDeclarationVisitor methodDeclarationVisitor;
    private final MethodInvocationVisitor methodInvocationVisitor;
    private final List<CompilationUnit> cu1;
    private final List<CompilationUnit> cu2;
    private Pair<String,String> mapNameClasses;
    private Map<Pair<String,String>,Double> couplage;
    private String methodInvocationWithoutExp;
    private int methodInvocationfilterdSize;

    private List<String> couplageClassesString;

    private HashMap<Pair<String, String>, Double> mapCouplageClasses;

    List<MethodInvocation> methodInvocationfilterd;
    private int sommeMethodInvocation;
    public Processor(String projectPath) {
        this.projectPath = projectPath;
        this.parser = new Parser(projectPath);
        this.typeDeclarationVisitor = new TypeDeclarationVisitor();
        this.newtypeDeclarationVisitor = new TypeDeclarationVisitor();
        this.methodDeclarationVisitor = new MethodDeclarationVisitor();
        this.methodInvocationVisitor = new MethodInvocationVisitor();
        cu1 = new ArrayList<>();
        cu2 = new ArrayList<>();
        this.mapNameClasses=new Pair<>("","");
        this.couplage=new HashMap<>();
        this.methodInvocationWithoutExp = null;
        this.methodInvocationfilterd = new ArrayList<MethodInvocation>();
        this.sommeMethodInvocation=0;
        this.couplageClassesString = new ArrayList<String>();
        this.mapCouplageClasses = new HashMap<>();
        this.methodInvocationfilterdSize=0;
    }

    public void process() throws IOException {
        List<File> javaFiles = parser.getListJavaFilesForFolder(new File(this.projectPath));
        for (File file : javaFiles) {
            CompilationUnit cu = parser.parse(file);
            cu.accept(typeDeclarationVisitor);
            cu.accept(methodDeclarationVisitor);
            cu.accept(methodInvocationVisitor);
            cu1.add(cu);
        }

    }


    public double getCouplageMetrique( String classA, String classB) {

        methodInvocationfilterdSize=0;
        List<TypeDeclaration> typesStream = typeDeclarationVisitor.getTypes().stream()
                .filter(t -> t.resolveBinding().getQualifiedName().equals(classA) || t.resolveBinding().getQualifiedName().equals(classB))
                .collect(Collectors.toList());
        for (int i=0;i<typesStream.size();i++) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            typesStream.get(i).accept(visitorMethod);
            for (MethodDeclaration nodeMethod : visitorMethod.getMethods()) {

                MethodInvocationVisitor visitorMethodInvoction = new MethodInvocationVisitor();
                nodeMethod.accept(visitorMethodInvoction);
                List<MethodInvocation> methodInvocations = visitorMethodInvoction.getMethods();
                int j= i==0 ? i+1 :i-1;
                methodInvocationfilterd = methodInvocations.stream()
                        .filter(m -> m.getExpression() != null && m.getExpression().resolveTypeBinding() != null && m
                                .getExpression().resolveTypeBinding().getName().toString().equals(typesStream.get(j).getName().toString()))
                        .collect(Collectors.toList());
                methodInvocationfilterdSize += methodInvocationfilterd.size();
            }

        }
        return methodInvocationfilterdSize/getNbreMethodInvocation();
    }
    public double getNbreMethodInvocation() {
        sommeMethodInvocation=0;
        for (TypeDeclaration node : typeDeclarationVisitor.getTypes()) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            node.accept(visitorMethod);

            for (MethodDeclaration nodeMethod : visitorMethod.getMethods()) {
                MethodInvocationVisitor visitorMethodInvoction = new MethodInvocationVisitor();
                nodeMethod.accept(visitorMethodInvoction);
                sommeMethodInvocation+=visitorMethodInvoction.getMethods().size();
            }
        }
        return sommeMethodInvocation;
    }
    public  Stream<List<String>> generate(List<String> list) {
        return Generator.combination(list).simple(2).stream();
    }

    public HashMap<Pair<String, String>, Double> getCouplageOfAllClasses(List<String> classes){
        mapCouplageClasses=new HashMap<>();
        List<String> nameClasses=new ArrayList<>();

        generate(classes).forEach(c->{
            mapCouplageClasses.put(new Pair<>(c.get(0), c.get(1)), getCouplageMetrique(c.get(0), c.get(1)));
            //Split pour avoir le dernier element
            int index0 = c.get(0).lastIndexOf('.');
            String name0 = c.get(0).substring(index0 +1);
            int index1 = c.get(1).lastIndexOf('.');
            String name1 = c.get(1).substring(index1 +1);

            if (getCouplageMetrique(c.get(0),c.get(1)) != 0){
                couplageClassesString.add("\t"+name0+" -- "+name1+"[label  =\""+getCouplageMetrique(c.get(0),c.get(1))+"\"];\n");}
        });

        return mapCouplageClasses;
    }

    public void graphToDotFile(String fileGraphPath) throws IOException {

        FileWriter fW = new FileWriter(fileGraphPath);
        fW.write("graph G {\n");
        for (String className :
                couplageClassesString) {

            fW.write(className);
        }
        fW.write("}");
        fW.close();
    }

    public  void dotToPng(String fileGraphPath) {
        try {

            MutableGraph mutableGraph = new guru.nidi.graphviz.parse.Parser().read(new File(fileGraphPath));
            Graphviz.fromGraph(mutableGraph).render(Format.PNG).toFile(new File("graph.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayGraph(){
        List<String> classes = new ArrayList<>();
        for (TypeDeclaration type : typeDeclarationVisitor.getTypes()){
            classes.add(type.resolveBinding().getQualifiedName());
        }
        getCouplageOfAllClasses(classes);
    }
    public List<TypeDeclaration> extractionInformation() throws IOException {
        List<File> javaFiles = parser.getListJavaFilesForFolder(new File(this.projectPath));
        for (File file : javaFiles) {
            CompilationUnit cu = parser.parse(file);
            cu.accept(newtypeDeclarationVisitor);
        }
        return  newtypeDeclarationVisitor.getTypes();
    }
    public Pair<AbstractCluster,AbstractCluster> clusterProche(List<AbstractCluster> clusters){
        Map<Pair<AbstractCluster,AbstractCluster>,Double> mapCouplageClusters=new HashMap<>();
        List<String> classes=new ArrayList<>();
        final double[] couplage = new double[1];
        Pair<AbstractCluster,AbstractCluster> key = null;
        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i+1; j <clusters.size(); j++) {
                int finalJ = j;
                couplage[0] =0;
                clusters.get(i).getClusterClasses().forEach(cl->{
                    clusters.get(finalJ).getClusterClasses().forEach(cl2->{
                        couplage[0] +=getCouplageMetrique(cl,cl2);
                    });
                });
                mapCouplageClusters.put(new Pair<>(clusters.get(i),clusters.get(j)), couplage[0]);
            }
        }
        if(mapCouplageClusters.size()>=1){
            Map<Pair<AbstractCluster,AbstractCluster>, Double> result = mapCouplageClusters.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            Map.Entry<Pair<AbstractCluster,AbstractCluster>,Double> entry = result.entrySet().iterator().next();
            key=entry.getKey();
            key.getValue0().setMetriqueCouplage(entry.getValue());
            key.getValue1().setMetriqueCouplage(entry.getValue());
        }

        return key;
    }
    public AbstractCluster clusteringHierarchique() throws IOException {

        List<TypeDeclaration> classes=extractionInformation();
        ComplexCluster complexCluster = new ComplexCluster();

        //initialization
        for(TypeDeclaration type:classes){
            SimpleCluster c=new SimpleCluster(type.resolveBinding().getQualifiedName());
            complexCluster.addCluster(c);
        }

        while (complexCluster.getClusters().size()>1 && clusterProche(complexCluster.getClusters())!=null){

            Pair<AbstractCluster,AbstractCluster> pairProche =  clusterProche(complexCluster.getClusters());

            ComplexCluster complexCluster1 = new ComplexCluster();
            complexCluster1.addCluster(pairProche.getValue0());
            complexCluster1.addCluster(pairProche.getValue1());
            complexCluster1.setMetriqueCouplage(pairProche.getValue0().getMetriqueCouplage());
            complexCluster1.setAdded(true);

            complexCluster.getClusters().remove(pairProche.getValue0());
            complexCluster.getClusters().remove(pairProche.getValue1());

            complexCluster.addCluster(complexCluster1);
        }
        List<AbstractCluster> complexClusterList=complexCluster.getClusters().stream().filter(c->c.isAdded()).collect(Collectors.toList());
        return complexClusterList.get(0);
    }
    public List<AbstractCluster> modulesIdentification(AbstractCluster abstractCluster ,double cp) throws IOException {

        List<AbstractCluster> modules = new ArrayList<>();
        double size= extractionInformation().size()/2;
        final double[] sommeCouplage = {0};

        if (abstractCluster.getClusters().size()>1){
            generate(abstractCluster.getClusterClasses()).collect(Collectors.toList()).forEach(c -> {
                sommeCouplage[0] += getCouplageMetrique(c.get(0), c.get(1));
            });
            double average = 0;
            average = sommeCouplage[0] / abstractCluster.getClusterClasses().size();
            if (average > cp) {
                if (modules.size() < size) {
                    modules.add(abstractCluster);
                    modules.addAll(modulesIdentification(abstractCluster.getClusters().get(0),cp));
                    modules.addAll(modulesIdentification(abstractCluster.getClusters().get(1), cp));
                }
            }

        }
        return modules;
    }

    public List<AbstractCluster> displayIdentificationModules(double cp) throws IOException {
        AbstractCluster dendrogramme = clusteringHierarchique();
        return modulesIdentification(dendrogramme, cp);
    }


}
