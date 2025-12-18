package app;

public class Main {

    private static final double KG_TO_POUNDS = 2.20462;

    public static void main(String[] args) {
        System.out.println("Simple Converter App (version 1.0)");

        double kilograms = 5;

        double resultPounds = convertKgToPounds(kilograms);

        System.out.println(kilograms + " kg -> " + resultPounds + " lbs");
    }

    private static double convertKgToPounds(double kg) {
        return kg * KG_TO_POUNDS;
    }

}
