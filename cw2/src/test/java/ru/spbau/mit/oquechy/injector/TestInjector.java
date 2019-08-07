package ru.spbau.mit.oquechy.injector;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertTrue;


public class TestInjector {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize("ru.spbau.mit.oquechy.injector.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "ru.spbau.mit.oquechy.injector.ClassWithOneClassDependency",
                Collections.singletonList(Class.forName("ru.spbau.mit.oquechy.injector.ClassWithoutDependencies"))
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "ru.spbau.mit.oquechy.injector.ClassWithOneInterfaceDependency",
                Collections.singletonList(Class.forName("ru.spbau.mit.oquechy.injector.InterfaceImpl"))
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }

    @Test
    public void injectorShouldInitializeClassWithNestedDependencies()
            throws Exception {
        Object object = Injector.initialize(
                "ru.spbau.mit.oquechy.injector.ClassWithNestedDependencies",
                Arrays.asList(Class.forName("ru.spbau.mit.oquechy.injector.InterfaceImpl"),
                        Class.forName("ru.spbau.mit.oquechy.injector.ClassWithoutDependencies"),
                        Class.forName("ru.spbau.mit.oquechy.injector.ClassWithOneClassDependency")
                )
        );
        assertTrue(object instanceof ClassWithNestedDependencies);
        ClassWithNestedDependencies instance = (ClassWithNestedDependencies) object;
        assertTrue(instance.dependency1 instanceof InterfaceImpl);
        assertTrue(instance.dependency2 != null);
    }

    @Test(expected = InjectionCycleException.class)
    public void injectorShouldDetectCyclicDependencies()
            throws Exception {
        Injector.initialize(
                "ru.spbau.mit.oquechy.injector.ClassWithCyclicDependencies",
                Arrays.asList(Class.forName("ru.spbau.mit.oquechy.injector.ClassWithCyclicDependencies"),
                        Class.forName("ru.spbau.mit.oquechy.injector.NestedClassWithCyclicDependency")
                )
        );
    }

    @Test
    public void injectorShouldNotDetectCyclicDependencies()
            throws Exception {
        Injector.initialize(
                "ru.spbau.mit.oquechy.injector.ClassWithoutCyclicDependencies",
                Arrays.asList(Class.forName("ru.spbau.mit.oquechy.injector.ClassWithNestedDependencies"),
                        Class.forName("ru.spbau.mit.oquechy.injector.NestedClassWithCyclicDependency"),
                        Class.forName("ru.spbau.mit.oquechy.injector.InterfaceImpl"),
                        Class.forName("ru.spbau.mit.oquechy.injector.ClassWithoutDependencies"),
                        Class.forName("ru.spbau.mit.oquechy.injector.ClassWithOneClassDependency")
                )
        );
    }

    @Test(expected = AmbiguousImplementationException.class)
    public void injectorShouldDetectDependenciesConflict()
            throws Exception {
        Injector.initialize(
                "ru.spbau.mit.oquechy.injector.ClassWithoutCyclicDependencies",
                Arrays.asList(Class.forName("ru.spbau.mit.oquechy.injector.ClassWithCyclicDependencies"),
                        Class.forName("ru.spbau.mit.oquechy.injector.ClassWithNestedDependencies"),
                        Class.forName("ru.spbau.mit.oquechy.injector.InterfaceImpl"),
                        Class.forName("ru.spbau.mit.oquechy.injector.InterfaceImpl"),
                        Class.forName("ru.spbau.mit.oquechy.injector.ClassWithoutDependencies"),
                        Class.forName("ru.spbau.mit.oquechy.injector.ClassWithOneClassDependency")
                )
        );
    }

    @Test(expected = ImplementationNotFoundException.class)
    public void injectorShouldDetectAbsentImplementation()
            throws Exception {
        Injector.initialize(
                "ru.spbau.mit.oquechy.injector.ClassWithoutCyclicDependencies",
                Arrays.asList(Class.forName("ru.spbau.mit.oquechy.injector.ClassWithCyclicDependencies"),
                        Class.forName("ru.spbau.mit.oquechy.injector.ClassWithOneClassDependency")
                )
        );
    }
}
