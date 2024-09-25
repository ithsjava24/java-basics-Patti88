package org.example;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Scanner;
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double[] priser = new double[0]; // lagra dem inmatade priser

        while (true) {
            visaMeny();
            int val = scanner.nextInt();

            switch (val) {
                case 1:
                    priser = inmatning(scanner, priser);
                    break;
                case 2:
                    visaMinMaxMedel(priser);
                    break;
                case 3:
                    sortera(priser);
                    break;
                case 4:
                    bastaLaddningstid(priser);
                    break;
                case 5: visualisering(priser);
                case 'e':
                case 'E':
                    System.out.println("Programmet avslutas.");
                    return;
                default:
                    System.out.println("Ogiltigt val. Vänligen försök igen.");
            }
        }
    }
    //menyns utseende
    public static void visaMeny() {
        System.out.println("Elpriser");
        System.out.println("========");
        System.out.println("1. Inmatning");
        System.out.println("2. Min, Max och Medel");
        System.out.println("3. Sortera");
        System.out.println("4. Bästa Laddningstid (4h)");
        System.out.println("5. Visualisering");
        System.out.println("e. Avsluta");  }
    //skapa metod för varje val användaren väljer alla metoder 2-5 bör få information från inmatnings metoden
    public static double[] inmatning(Scanner scanner, double[] gamlaPriser) {
        // 24 timmars format
        int antalTimmar = 24;
        double[] nyaPriser = new double[gamlaPriser.length + antalTimmar];

        System.out.println("Ange elpriset i öre/kWh för varje timme på dygnet:");
        for (int i = 0; i < antalTimmar; i++) {
            String tidsintervall = String.format("%02d-%02d", i, (i + 1) % 24);
            System.out.print(tidsintervall + ": ");
            nyaPriser[gamlaPriser.length + i] = scanner.nextDouble();
        }

        return nyaPriser;
    }
    public static void visaMinMaxMedel(double[] priser) {
        if (priser.length == 0) {
            System.out.println("Inga priser har matats in.");
            return;
        }

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double summa = 0;
        int minIndex = -1;
        int maxIndex = -1;

        for (int i = 0; i < priser.length; i++) {
            double pris = priser[i];
            if (pris < min) {
                min = pris;
                minIndex = i;
            } else if (pris > max) {
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
        System.out.println("Minsta priset under dygnet gång: " + min + " (vid följande tidpunkt: " + minTid + ")");
        System.out.println("Högsta priset under dygnets gång: " + max + " (vid följande tidpunkt: " + maxTid + ")");
        System.out.println("Medelpriset för hela dygnet är Ca." + medelInt);


        // Pausa programmet
        System.out.println("Tryck Enter för att återgå till huvudmenyn");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
    public static String formateraTid(int index) {
        // 24-timmars format formatering
        return String.format("%02d:%02d", index + 1, 0);
    }
    public static void sortera(double[] priser) {
        double[] sorteradePriser = Arrays.copyOf(priser, priser.length);
        Arrays.sort(sorteradePriser);

        // för att få fallande ordning
        List<Double> lista = Arrays.stream(sorteradePriser)
                .boxed()
                .collect(Collectors.toList());
        Collections.reverse(lista);
        sorteradePriser = lista.stream().mapToDouble(Double::doubleValue).toArray();


        System.out.println("Sorterade priser (högsta först):");
        for (double pris : sorteradePriser) {
            int index = findIndex(priser, pris);
            String tid = formateraTid(index);
            System.out.println(pris + " öre/kWh (vid tid: " + tid + ")");
        }


        // Pausa programmet
        System.out.println("Tryck Enter för att återgå till huvudmenyn");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
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
    public static void bastaLaddningstid(double[] priser) {
        if (priser.length < 4) {
            System.out.println("För få priser för att beräkna bästa laddningstid.");
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

        String startTid = formateraTid(startIndex);
        String slutTid = formateraTid(startIndex + periodLength - 1);
        System.out.println("Bästa laddningstid:");
        System.out.println("Startar: " + startTid);
        System.out.println("Slutar: " + slutTid);
        System.out.println("Medelpris under perioden: " + minMedel);

        System.out.println("Tryck Enter för att återgå till huvudmenyn");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public static void visualisering(double[] priser) {
        if (priser.length == 0) {
            System.out.println("Inga priser att visualisera.");
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
