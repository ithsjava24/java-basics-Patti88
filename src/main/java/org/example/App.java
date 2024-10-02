package org.example;
import java.util.*;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        Locale.setDefault(Locale.of("SE"));
        Scanner scanner = new Scanner(System.in);
        double[] priser = new double[0]; // lagra dem inmatade priser

        while (true) {
            visaMeny();
            String val = scanner.nextLine();

            switch (val) {
                case "1":
                    priser = inmatning(scanner, priser);
                    break;
                case "2":
                    visaMinMaxMedel(priser);
                    break;
                case "3":
                    printPricesSorted2(priser);
                    break;
                case "4":
                    cheapest4Hours(priser);
                    break;
                case "5": visualisering(priser);
                case "e":
                case "E":
                    System.out.println("Programmet avslutas.\n");
                    return;
                default:
                    System.out.print("Ogiltigt val. Vänligen försök igen.\n");
            }
        }
    }
    //menyns utseende
    public static void visaMeny() {
        System.out.print("Elpriser\n");
        System.out.print("========\n");
        System.out.print("1. Inmatning\n");
        System.out.print("2. Min, Max och Medel\n");
        System.out.print("3. Sortera\n");
        System.out.print("4. Bästa Laddningstid (4h)\n");
        System.out.print("5. Visualisering\n");
        System.out.print("e. Avsluta\n");  }
    //skapa metod för varje val användaren väljer alla metoder 2-5 bör få information från inmatnings metoden
    public static double[] inmatning(Scanner scanner, double[] gamlaPriser) {
        // 24 timmars format
        int antalTimmar = 24;
        double[] nyaPriser = new double[24];

        System.out.print("Ange elpriset i öre/kWh för varje timme på dygnet:\n");
        for (int i = 0; i < antalTimmar; i++) {
            String tidsintervall = String.format("%02d-%02d", i, (i + 1) % 24);
            System.out.print(tidsintervall + ": ");
            nyaPriser[ i] = scanner.nextDouble();

        }
        scanner.nextLine();

        return nyaPriser;
    }
    public static void visaMinMaxMedel(double[] priser) {
        if (priser.length == 0) {
            System.out.print("Inga priser har matats in.\n");
            return;
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        double summa = 0;
        int minIndex = -1;
        int maxIndex = -1;

        for (int i = 0; i < priser.length; i++) {
            int pris = (int)priser[i];
            if (pris < min) {
                min = pris;
                minIndex = i;
            }
            if (pris > max) {
                max = pris;
                maxIndex = i;
            }
            summa += pris;
        }

        double medel = summa / priser.length;
        int medelInt = (int) Math.floor(medel);

        // formateraTid för att få tidssträngen
        // formateraTid för både min och max tid
        String minTid = formateraTid(minIndex);
        String maxTid = formateraTid(maxIndex);
        System.out.print("Lägsta pris: " + minTid + ", " + min + " öre/kWh"+"\n");
        System.out.print("Högsta pris: " + maxTid + ", " + max + " öre/kWh"+"\n");
        System.out.printf("Medelpris: %.2f öre/kWh\n", medel);

    }
    public static String formateraTid(int index) {
        // 24-timmars format formatering
        return String.format("%02d-%02d", index, index+1);
    }
    public static void printPricesSorted2(double[] priser) {
        double[] sorteradePriser = Arrays.copyOf(priser, priser.length);
        Arrays.sort(sorteradePriser);

        // för att få fallande ordning
        List<Double> lista = Arrays.stream(sorteradePriser)
                .boxed()
                .collect(Collectors.toList());
        Collections.reverse(lista);
        sorteradePriser = lista.stream().mapToDouble(Double::doubleValue).toArray();


        System.out.print("Sorterade priser (högsta först):\n");
        for (double pris : sorteradePriser) {
            int index = findIndex(priser, pris);
            String tid = formateraTid(index);
            System.out.print(tid + " " +(int) pris + " öre\n");

            // 23-24 40 öre

        }


        // Pausa programmet

    }
    // Hjälpmetod för att hitta indexet för ett visst pris
    public static int findIndex(double[] array, double target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    public static void cheapest4Hours(double[] priser) {
        if (priser.length < 4) {
            System.out.print("För få priser för att beräkna bästa laddningstid.\n");
            return;
        }

        int periodLength = 4;
        double minMedel;
        int startIndex = 0;

        // Beräkna medelvärdet för den första perioden
        double summa = 0;
        for (int i = 0; i < periodLength; i++) {
            summa += priser[i];
        }
        double medel = summa / periodLength;
        minMedel = medel;

        // för att beräkna medelvärde för övriga perioder
        for (int i = 1; i <= priser.length - periodLength; i++) {
            summa -= priser[i - 1];
            summa += priser[i + periodLength - 1];
            medel = summa / periodLength;

            if (medel < minMedel) {
                minMedel = medel;
                startIndex = i;
            }
        }


        System.out.print("Påbörja laddning klockan " + startIndex+ "\n");
        System.out.printf("Medelpris 4h: %.1f öre/kWh\n",minMedel);

    }

    public static void visualisering(double[] priser) {
        if (priser.length == 0) {
            System.out.print("Inga priser att visualisera.\n");
            return;
        }

        // y-axeln
        int stegStorlek = 20;
        int antalRader = (int) Math.ceil(Arrays.stream(priser).max().getAsDouble() / stegStorlek);

        // Skapa en StringBuilder för att bygga upp diagrammet
        StringBuilder diagram = new StringBuilder();

        // Rita y-axeln och diagrammet
        for (int rad = antalRader; rad >= 0; rad--) {
            double aktuelltVarde = rad * stegStorlek;
            diagram.append(String.format("%4d ", (int) aktuelltVarde)); // Format to 4 characters
//diagram
            for (double pris : priser) {
                if (pris >= aktuelltVarde - (double) stegStorlek / 2 && pris < aktuelltVarde + (double) stegStorlek / 2) {
                    diagram.append("x");
                } else {
                    diagram.append(" ");
                }
            }
            diagram.append("\n");
        }
        // x-axeln
        diagram.append("  ");
        for (int i = 0; i < priser.length; i++) {
            diagram.append(String.format("%02d", i));
            if (i < priser.length - 1) {
                diagram.append(" ");
            }
        }
        diagram.append("\n");

        System.out.println(diagram);

    }
}
