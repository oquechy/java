package ru.spbau.mit.oquechy.injector;

public class ClassWithNestedDependencies {
    public final Interface dependency1;
    public final ClassWithOneClassDependency dependency2;

    public ClassWithNestedDependencies(Interface dependency1, ClassWithOneClassDependency dependency2) {
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
    }
}
