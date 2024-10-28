import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConjuntoDifuso {
    private String nombre;
    private List<Double> parametros; // Puntos que definen la forma de la función de pertenencia
    private HashMap<Double, Double> valoresCalculados;

    public ConjuntoDifuso(String nombre) {
        this.nombre = nombre;
        this.parametros = new ArrayList<>();
        this.valoresCalculados = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Double> getParametros() {
        return parametros;
    }

    public void addParametro(double n) {
        this.parametros.add(n);
        return;
    }

    public void fromLine(String line) {
        // Ejemplo de línea: "Frío: 0, 0, 20"
        String[] parts = line.split(":");
        String nombre = parts[0].trim();
        String[] paramStrs = parts[1].trim().split(",\\s*");
        double[] parametros = new double[paramStrs.length];
        for (int i = 0; i < paramStrs.length; i++) {
            parametros[i] = Double.parseDouble(paramStrs[i]);
        }
        this.nombre = nombre;
        for(double n : parametros) {
            this.addParametro(n);
        }
    }

    public static ConjuntoDifuso obtenerConjunto(String line) {
        // Ejemplo de línea: "Frío: 0, 0, 20"
        String[] parts = line.split(":");
        String nombre = parts[0].trim();
        String[] paramStrs = parts[1].trim().split(",\\s*");
        double[] parametros = new double[paramStrs.length];
        for (int i = 0; i < paramStrs.length; i++) {
            parametros[i] = Double.parseDouble(paramStrs[i]);
        }
        ConjuntoDifuso conjunto = new ConjuntoDifuso(nombre);
        for(double n : parametros) {
            conjunto.addParametro(n);
        }
        return conjunto;
    }

    public double calcularPertenencia(double valor) {
        if (parametros.size() == 3) { // Triangular
            double a = parametros.get(0), b = parametros.get(1), c = parametros.get(2);
            if (valor <= a || valor >= c) return 0.0;
            else if (valor == b) return 1.0;
            else if (valor > a && valor < b) return (valor - a) / (b - a);
            else return (c - valor) / (c - b);
        } else if (parametros.size() == 4) { // Trapezoidal
            double a = parametros.get(0), b = parametros.get(1), c = parametros.get(2), d = parametros.get(3);
            if (valor <= a || valor >= d) return 0.0;
            else if (valor >= b && valor <= c) return 1.0;
            else if (valor > a && valor < b) return (valor - a) / (b - a);
            else return (d - valor) / (d - c);
        }
        return 0.0;
    }

    public void setGradoPertenencia(double id, double grado) {
        valoresCalculados.put(id, grado); // Almacena el grado en el mapa
    }

    public HashMap<Double, Double> getValoresCalculados() {
        return valoresCalculados;
    }

    public StringBuilder print() {
        StringBuilder str = new StringBuilder();
        str.append(this.nombre).append(": ");
        for(Double n : this.parametros) {
            str.append(n).append(", ");
        }
        return str;
    }

    public void printMap() {
        for (Map.Entry<Double, Double> entry : valoresCalculados.entrySet()) {
            System.out.println("Llave: " + entry.getKey() + ", Valor: " + entry.getValue());
        }
    }
}
