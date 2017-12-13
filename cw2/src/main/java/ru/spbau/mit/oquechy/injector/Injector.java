package ru.spbau.mit.oquechy.injector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class for getting an instance of class, which name is given
 * Class shouldn't be an interface or an abstract class
 */
public class Injector {

    /**
     * Returns object of given class. All constructor parameters for rootClass and nested others
     * must be in implementations list. Cyclic dependency on rootClass or any from implementations
     * causes an exception.
     *
     * @param rootClassName name of needed class
     * @param implementations list of dependencies
     * @throws ClassNotFoundException if no such class as rootClass
     * @throws InstantiationException if constructor fails
     * @throws AmbiguousImplementationException if more than one suitable dependency was found
     * @throws ImplementationNotFoundException if none of dependencies fits
     * @throws InjectionCycleException if cyclic dependency detected
     */
    public static Object initialize(String rootClassName, Iterable<Class<?>> implementations)
            throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, AmbiguousImplementationException, ImplementationNotFoundException, InjectionCycleException {
        Class<?> rootClass = Class.forName(rootClassName, false, Thread.currentThread().getContextClassLoader());
        Map<Class<?>, Object> instances = new HashMap<>();
        Set<Object> stack = new HashSet<>();

        return createInstance(rootClass, implementations, instances, stack, true);
    }

    private static Object createInstance(Class<?> type, Iterable<Class<?>> implementations,
                                         Map<Class<?>, Object> instances, Set<Object> stack, boolean isRoot)
            throws IllegalAccessException, InstantiationException, InvocationTargetException, AmbiguousImplementationException, ImplementationNotFoundException, InjectionCycleException {

        Class<?> impl = isRoot ? type : chooseImpl(type, implementations);

        if (stack.contains(impl)) {
            throw new InjectionCycleException();
        }

        stack.add(impl);

        Object instance = instances.get(impl);
        if (instance != null) {
            return instance;
        }


        Constructor<?>[] constructors = impl.getConstructors();

        if (constructors.length == 0) {
            return impl.newInstance();
        } else if (constructors.length > 1) {
            throw new UnsupportedOperationException(impl.getSimpleName() + " has more than one constructor");
        }

        Constructor<?> constructor = constructors[0];

        int parameterCount = constructor.getParameterCount();

        if (parameterCount == 0) {
            return constructor.newInstance();
        }

        Object[] parameters = new Object[parameterCount];
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = createInstance(parameterTypes[i], implementations, instances, stack, false);
        }

        instance = constructor.newInstance(parameters);

        if (!isRoot) {
            instances.put(impl, instance);
        }

        stack.remove(impl);

        return instance;
    }

    private static Class<?> chooseImpl(Class<?> type, Iterable<Class<?>> implementations)
            throws AmbiguousImplementationException, ImplementationNotFoundException {
        Class<?> implementation = null;
        for (Class<?> i : implementations) {
            if (type.isAssignableFrom(i)) {
                if (implementation != null) {
                    throw new AmbiguousImplementationException();
                }

                implementation = i;
            }
        }

        if (implementation == null) {
            throw new ImplementationNotFoundException("for " + type.getSimpleName());
        }

        return implementation;
    }
}
