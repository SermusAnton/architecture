package org.example.command.dynamic;

import org.example.command.Command;
import org.example.exception.CommandException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LoadCommand implements Command {

    private final String pathToJar;

    public LoadCommand(String pathToJar) {
        this.pathToJar = pathToJar;
    }

    @Override
    public void execute() {
        JarFile jarFile;
        try {
            jarFile = new JarFile(pathToJar);
        } catch (IOException e) {
            throw new CommandException(e);
        }
        Enumeration<JarEntry> e = jarFile.entries();
        URL[] urls;
        try {
            urls = new URL[]{new URL("jar:file:" + pathToJar + "!/")};
        } catch (MalformedURLException ex) {
            throw new CommandException(ex);
        }
        Optional<Register> register;
        try (URLClassLoader cl = URLClassLoader.newInstance(urls)) {
            register = Optional.empty();
            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                Class<?> c = cl.loadClass(className);
                if (Arrays.asList(c.getInterfaces()).contains(Register.class)) {
                    register = Optional.of(convert(c));
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            throw new CommandException(ex);
        }
        register.ifPresentOrElse(Register::init, this::throwNotFound);
    }

    private Register convert(Class<?> register) {
        try {
            return (Register) register.getConstructor().newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new CommandException(e);
        }
    }

    private void throwNotFound() {
        throw new CommandException(new ClassNotFoundException());
    }
}
