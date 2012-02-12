package org.inigma.lwrest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.inigma.lwrest.logger.Logger;

public abstract class InjectionHolder {
    private static Map<String, Object> injectables = new LinkedHashMap<String, Object>();
    private static Logger logger = Logger.getLogger(InjectionHolder.class);

    public static <T> T getInjectable(String id) {
        return (T) injectables.get(id);
    }

    public static <T> T getInjectable(Class<T> clazz) {
        for (Object o : injectables.values()) {
            if (clazz.isAssignableFrom(o.getClass())) {
                return (T) o;
            }
        }
        return null;
    }

    public static String addInjectable(Object o) {
        if (o == null) {
            throw new IllegalStateException("Null objects cannot be used for injection!");
        }
        String id = UUID.randomUUID().toString();
        injectables.put(id, o);
        return id;
    }

    public static void addInjectable(String key, Object o) {
        if (o == null) {
            throw new IllegalStateException("Null objects cannot be used for injection!");
        }
        injectables.put(key, o);
    }

    public static void injectFields(Object o) {
        if (o == null) {
            return;
        }
        for (Field field : getAllFields(o.getClass())) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                // TODO: Fix so that named and qualifiers also work
                if (Inject.class.isInstance(annotation)) {
                    try {
                        Object injectable = getInjectable(field.getType());
                        logger.debug("Injecting into %s field %s object %s", o.getClass(), field.getName(), injectable);
                        field.set(o, injectable);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Field " + field.getName() + " is not writable!");
                    }
                }
            }
        }
    }

    private static Collection<Field> getAllFields(Class clazz) {
        Collection<Field> fields = new HashSet<Field>();
        if (clazz.getSuperclass() != null) {
            fields.addAll(getAllFields(clazz.getSuperclass()));
        }
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            fields.add(field);
        }
        return fields;
    }

//  Set<Class<?>> classes = new HashSet<Class<?>>();
//
//  String packages = config.getInitParameter("autoscan.packages");
//  if (packages != null) {
//      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//      if (classLoader == null) {
//          classLoader = getClass().getClassLoader();
//      }
//      for (String pkg : packages.split("\\s+")) {
//          String pkgPath = pkg.replace('.', '/');
//          try {
//              Enumeration<URL> resources = classLoader.getResources(pkgPath);
//              if (resources == null) {
//                  continue;
//              }
//              while (resources.hasMoreElements()) {
//                  String filePath = resources.nextElement().getFile();
//                  if ((filePath.indexOf("!") > 0) && (filePath.indexOf(".jar") > 0)) { // jar file
//                      String jarPath = filePath.substring(filePath.indexOf(':') + 1, filePath.indexOf('!'));
//                      classes.addAll(getClassesFromJar(new JarFile(jarPath), pkg, classLoader));
//                  } else { // file or directory
//                      classes.addAll(getClassesFromDirectory(new File(filePath), pkg, classLoader));
//                  }
//              }
//          } catch (IOException e) {
//              e.printStackTrace();
//          }
//      }
//  }

//  private Set<Class<?>> getClassesFromJar(JarFile jarFile, String pkg, ClassLoader classLoader) {
//      Set<Class<?>> classes = new HashSet<Class<?>>();
//      for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
//          JarEntry entry = entries.nextElement();
//          String className = entry.getName().replace('/', '.');
//          if (className.endsWith(".class")) {
//              className = className.substring(0, className.length() - 6);
//              try {
//                  classes.add(Class.forName(className, false, classLoader));
//              } catch (ClassNotFoundException e) { // should not really ever happen that I know of
//                  throw new RuntimeException(e);
//              }
//          }
//      }
//      return classes;
//  }
//
//  private Set<Class<?>> getClassesFromDirectory(File directory, String pkg, ClassLoader classLoader) {
//      Set<Class<?>> classes = new HashSet<Class<?>>();
//      if (directory.isDirectory() && directory.exists()) {
//          for (File file : directory.listFiles()) {
//              String filename = file.getName();
//              if (file.isDirectory()) {
//                  classes.addAll(getClassesFromDirectory(file, pkg + "." + filename, classLoader));
//              } else if (filename.endsWith(".class")) {
//                  String className = pkg + "." + filename.substring(0, filename.length() - 6);
//                  try {
//                      classes.add(Class.forName(className, false, classLoader));
//                  } catch (ClassNotFoundException e) { // should not really ever happen that I know of
//                      throw new RuntimeException(e);
//                  }
//              }
//          }
//      }
//      return classes;
//  }
}
