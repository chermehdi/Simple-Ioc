package com.mehdi.ioc.core;

import com.mehdi.ioc.annotations.Component;
import com.mehdi.ioc.annotations.Wire;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * the Base class of the library, does the classpath scanning, bean discovery, and DI
 *
 * @author Mehdi Maick
 */
public class Container {

    private static Map<Class, Object> registry;
    private static Map<String, Class<?>> qualifiedClasses;
    private static Container instance = null;

    private Container() {
        registry = new HashMap<>();
        try {
            discover();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("could not start discovery " + e.getMessage());
        }
    }

    public static Container boot() {
        if (instance == null)
            instance = new Container();
        return instance;
    }

    private void discover() throws Exception {
        String[] classPath = System.getProperty("java.class.path").split(":");
        Optional<Path> path = Stream.of(classPath)
                .map(Paths::get)
                .filter(Files::isDirectory)
                .findFirst();

        FileClassLoader classLoader = new FileClassLoader.FileClassLoaderBuilder().and(path.get()).build();
        Stream<Class> loadedClasses = classLoader.load();

        loadedClasses.filter(e -> e.isAnnotationPresent(Component.class))
                .forEach(clazz -> {
                    try {
                        register(clazz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    private static void injectClassDependencies(Object obj) throws Exception {

        Class<?> objectClass = obj.getClass();

        for (Field f : objectClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(Wire.class)) {
                Class<?> fieldClass = f.getType();
                if (!registry.containsKey(fieldClass)) {
                    register(fieldClass);
                }
                f.setAccessible(true);
                f.set(obj, registry.get(fieldClass));
            }
        }
    }

    /**
     * register a given class with a resolved instance in the container's registry
     *
     * @param clazz the desired class
     * @throws Exception all kind of reflection exceptions are handled by the calling class
     */
    public static void register(Class<?> clazz) throws Exception {
        if (clazz.isAnnotation() || clazz.isInterface()) // TODO : should resolve interfaces
            return;

        Field[] fields = clazz.getDeclaredFields();

        if (!registry.containsKey(clazz)) {
            registerClass(clazz);
        }

        for (Field f : fields) {
            if (f.isAnnotationPresent(Wire.class)) {
                if (!registry.containsKey(f.getType())) {
                    registerClass(f.getType());
                }
            }
        }

    }

    /**
     * register the given class and add it's dependencies
     *
     * @param clazz the target class
     * @throws Exception in case of exception, the calling method should be able to handle that
     */
    private static void registerClass(Class<?> clazz) throws Exception {
        registry.put(clazz, clazz.newInstance());
        register(clazz);
        injectClassDependencies(registry.get(clazz));
    }

    /**
     * return an instance of the given class
     *
     * @param appClass the desired class
     * @return an instance of appClass if the class is already registered or the class is annotated with @Component
     * or null in case it does not find it in the registry
     */
    public static Object resolve(Class<?> appClass) {
        if (registry.containsKey(appClass))
            return appClass.cast(registry.get(appClass));
        return null;
    }
}
