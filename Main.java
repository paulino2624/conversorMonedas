import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    static final String API_KEY = "c18d64d647b12c67fe5e2341";
    static final Gson gson = new Gson();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("****************************************************");
            System.out.println(" Conversor de Moneda =]");
            System.out.println("****************************************************");
            System.out.println("1) Dólar  => Peso argentino");
            System.out.println("2) Peso argentino => Dólar");
            System.out.println("3) Dólar => Real brasileño");
            System.out.println("4) Real brasileño => Dólar");
            System.out.println("5) Dólar => Peso colombiano");
            System.out.println("6) Peso colombiano => Dólar");
            System.out.println("7) Salir");
            System.out.print("\nElija una opción válida: ");

            opcion = scanner.nextInt();

            if (opcion == 7) break;

            System.out.print("Ingrese la cantidad a convertir: ");
            double cantidad = scanner.nextDouble();

            switch (opcion) {
                case 1 -> convertir("USD", "ARS", cantidad);
                case 2 -> convertir("ARS", "USD", cantidad);
                case 3 -> convertir("USD", "BRL", cantidad);
                case 4 -> convertir("BRL", "USD", cantidad);
                case 5 -> convertir("USD", "COP", cantidad);
                case 6 -> convertir("COP", "USD", cantidad);
                default -> System.out.println("Opción inválida. Intente de nuevo.");
            }

        } while (opcion != 7);

        System.out.println("¡Gracias por usar el conversor!");
    }

    // ======================
    // MÉTODO DE CONVERSIÓN
    // ======================
    public static void convertir(String base, String destino, double cantidad) {
        try {
            String urlStr = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + base;

            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(request.getInputStream())
            );

            StringBuilder respuesta = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                respuesta.append(line);
            }
            reader.close();

            ApiResponse data = gson.fromJson(respuesta.toString(), ApiResponse.class);

            if (!data.conversion_rates.containsKey(destino)) {
                System.out.println("No existe la moneda destino.");
                return;
            }

            double tasa = data.conversion_rates.get(destino);
            double resultado = cantidad * tasa;

            System.out.println("\n→ " + cantidad + " " + base + " = " + resultado + " " + destino);

        } catch (Exception e) {
            System.out.println("Error al conectar con la API: " + e.getMessage());
        }
    }
}
