package ru.spbau.mit.oquechy.injector;

public class ClassWithoutCyclicDependencies {
    private ClassWithOneClassDependency dependency1;
    private ClassWithNestedDependencies dependency2;

    public ClassWithoutCyclicDependencies(ClassWithOneClassDependency dependency1,
                                          ClassWithNestedDependencies dependency2) {
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
    }
}
