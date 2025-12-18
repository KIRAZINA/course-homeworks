package app;

public class Main {

    private static final double KG_TO_POUNDS = 2.20462;
    private static final double POUNDS_TO_KG = 0.453592;

    public static void main(String[] args) {
        System.out.println("Simple Converter App (version 1.0)");
        System.out.println("Simple Converter App (version 1.1)");

        double kilograms = 5;
        double pounds = 12;

        double resultPounds = convertKgToPounds(kilograms);
        double resultKg = convertPoundsToKg(pounds);
        System.out.println(pounds + " lbs -> " + resultKg + " kg");
        System.out.println(kilograms + " kg -> " + resultPounds + " lbs");
    }

    private static double convertKgToPounds(double kg) {
        return kg * KG_TO_POUNDS;
    }

    private static double convertPoundsToKg(double pounds) {
        return pounds * POUNDS_TO_KG;
    }
}
