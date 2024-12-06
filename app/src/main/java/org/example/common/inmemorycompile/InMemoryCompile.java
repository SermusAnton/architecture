package org.example.common.inmemorycompile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.Collections;
import java.util.List;

public class InMemoryCompile {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryCompile.class);

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    private final InMemoryFileManager manager = new InMemoryFileManager(
        compiler.getStandardFileManager(null, null, null));

    private InMemoryCompile() {
    }

    public static class InMemoryCompileHolder {
        private InMemoryCompileHolder() {
        }

        public static final InMemoryCompile HOLDER_INSTANCE = new InMemoryCompile();
    }

    public static InMemoryCompile getInstance() {
        return InMemoryCompile.InMemoryCompileHolder.HOLDER_INSTANCE;
    }

    public Class<?> load(String qualifiedClassName, String sourceCode) {
        List<JavaFileObject> sourceFiles = Collections.singletonList(new JavaSourceFromString(qualifiedClassName, sourceCode));
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, sourceFiles);

        boolean result = task.call();
        if (!result) {
            diagnostics.getDiagnostics()
                .forEach(d -> logger.error(String.valueOf(d)));
        } else {
            ClassLoader classLoader = manager.getClassLoader(null);
            try {
                return classLoader.loadClass(qualifiedClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found", e);
            }

//            Class<?> clazz = classLoader.loadClass(qualifiedClassName);
//            Constructor<?> ctor = clazz.getConstructor(Map.class);
//            var gameObject = new HashMap<String, Object>();
//            var object = (MovingObject) ctor.newInstance(gameObject);
//
//            var a = "";
        }
        throw new IllegalArgumentException(String.format("Error load class: %s", qualifiedClassName));
    }
}