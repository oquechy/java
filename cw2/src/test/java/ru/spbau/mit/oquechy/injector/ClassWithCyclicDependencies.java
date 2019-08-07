package ru.spbau.mit.oquechy.injector;

public class ClassWithCyclicDependencies {

    private NestedClassWithCyclicDependency dependency;

    public ClassWithCyclicDependencies(NestedClassWithCyclicDependency dependency) {
        this.dependency = dependency;
    }
}
