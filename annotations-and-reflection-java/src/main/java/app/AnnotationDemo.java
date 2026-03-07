package app;

import app.utils.ArrayUtils;
import app.annotations.MethodInfo;
import app.annotations.Author;

import java.lang.reflect.Method;

/**
 * Demonstration of custom annotations with reflection.
 */
public class AnnotationDemo {
    public static void main(String[] args) {
        int[] numbers = {5, 2, 9, 1, 7};

        // Call methods
        System.out.println("Max: " + ArrayUtils.findMax(numbers));
        System.out.println("Min: " + ArrayUtils.findMin(numbers));

        // Use reflection to read annotations
        Class<ArrayUtils> clazz = ArrayUtils.class;

        // Print class-level Author annotation
        if (clazz.isAnnotationPresent(Author.class)) {
            Author author = clazz.getAnnotation(Author.class);
            System.out.println("\nClass Author: " + author.firstName() + " " + author.lastName());
        }

        // Print method-level annotations
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MethodInfo.class)) {
                MethodInfo info = method.getAnnotation(MethodInfo.class);

                System.out.println("\n--- Method: " + method.getName() + " ---");
                System.out.println("Return type: " + method.getReturnType().getSimpleName());
                System.out.println("Description: " + info.description());

                Author methodAuthor = method.getAnnotation(Author.class);
                if (methodAuthor != null) {
                    System.out.println("Author: " + methodAuthor.firstName() + " " + methodAuthor.lastName() + " (method-level)");
                } else if (clazz.isAnnotationPresent(Author.class)) {
                    Author classAuthor = clazz.getAnnotation(Author.class);
                    System.out.println("Author: " + classAuthor.firstName() + " " + classAuthor.lastName() + " (class-level)");
                }
            }
        }
    }
}
