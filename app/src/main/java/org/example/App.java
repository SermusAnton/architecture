package org.example;

public class App {
    public static void main(String[] args) {
        var quadraticEquation = new QuadraticEquation();
        var result = quadraticEquation.solve(1d, -5d, 6d, QuadraticEquation.EPSILON);
        System.out.println(result);
    }
}
