import java.util.HashMap;
import java.util.Arrays;


public class Main {
    @FunctionalInterface
    static interface Printer {
        String printer(String a);
    }

    public static void main(String[] args) {
        Printer print = (a) -> {
            String[] b = a.split("");
            int max = b.length-1;
            b[max] = "g";
            String c = String.join("",b);
            System.out.print(c);
            return "";
        };
        Printer printlit = (a) -> {
            System.out.print(a);
            return "";
        };
        // Create a HashMap object called capitalCities
        HashMap<String, Printer> capitalCities = new HashMap<String, Printer>();

        // Add keys and values (Country, City)
        capitalCities.put("England", print);
        capitalCities.put("India", printlit);
        // capitalCities.put("Austria", "Wien");
        // capitalCities.put("Norway", "Oslo");
        // capitalCities.put("Norway", "Oslo"); // Duplicate
        // capitalCities.put("USA", "Washington DC");

        System.out.println(capitalCities);
        System.out.println(capitalCities.get("England").printer("london"));
        System.out.println(capitalCities.get("India").printer("new dehli"));
    }
}
/* parser logic:
get verb save as action
eliminate unneccessary words (i.e. the, as, to, and)
if the function requires an action and an object,
save all words until a splitting word (i.e. with, and) as the action
all words after splitting word are object
if function needs one, do the same but set the non needed one to null */