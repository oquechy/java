package ru.spbau.mit.oquechy.injector;

public class NestedClassWithCyclicDependency {
    private ClassWithCyclicDependencies dependency;

    public NestedClassWithCyclicDependency(ClassWithCyclicDependencies dependency) {
        this.dependency = dependency;
    }
}
