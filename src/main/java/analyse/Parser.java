package analyse;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Parser {
    public String projectPath;
    public String jrePath;

    public Parser(String projectPath) {
        this.projectPath = projectPath + "\\src";
        this.jrePath = System.getProperty("java.home");
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getJrePath() {
        return jrePath;
    }

    public void setJrePath(String jrePath) {
        this.jrePath = jrePath;
    }

    public ArrayList<File> getListJavaFilesForFolder(final File folder) {
        ArrayList<File> javaFiles = new ArrayList<File>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                javaFiles.addAll(getListJavaFilesForFolder(fileEntry));
            } else if (fileEntry.getName().contains(".java")) {
                javaFiles.add(fileEntry);
            }
        }
        return javaFiles;
    }

    public String getFileContent(File file) throws IOException {
        return FileUtils.readFileToString(file);
    }

    public CompilationUnit parse(File sourceFile) throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);
        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);
        parser.setUnitName("");
        String[] sources = {getProjectPath()};
        String[] classpath = {getJrePath()};

        parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
        parser.setSource(getFileContent(sourceFile).toCharArray());

        return (CompilationUnit) parser.createAST(null); // create and parse
    }
}
