import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConjuntoDifuso {
    private String nombre; // Nombre del conjunto
    private List<Double> parametros; // Puntos que definen la forma de la función de pertenencia
    private HashMap<Double, Double> valoresCalculados; // Mapa con los valores calculados

    // Constructor
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

    // Funcion para modifica un conjunto a partir de una linea de texto
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

    // Funcion para crear un conjunto a partir de una linea de texto
    public static ConjuntoDifuso obtenerConjunto(String line) {
        // Ejemplo de línea: "Frío: 0, 0, 20"
        String[] parts = line.split(":");
        String nombre = parts[0].trim();
        String[] paramStrs = parts[1].trim().split(",\\s*");
        double[] parametros = new double[paramStrs.length];
        for (int i = 0; i < paramStrs.length; i++) {
            parametros[i] = Double.parseDouble(paramStrs[i]);
        }
        // Crear el conjunto con su nombre
        ConjuntoDifuso conjunto = new ConjuntoDifuso(nombre);
        // Añadir los parametros a la lista de parametros
        for(double n : parametros) {
            conjunto.addParametro(n);
        }
        return conjunto;
    }

    // Funcion para calcular el grado de pertenencia
    public double calcularPertenencia(double valor) {
        // Si hay tres parametros es una funcion triangular y si hay cuatro en una trapezoidal
        if (parametros.size() == 3) { // Triangular
            // Obtener los valores de los parametros
            double a = parametros.get(0), b = parametros.get(1), c = parametros.get(2);
            // Verificar que caso de la funcion cumple y hacer el calculo correspondiente
            if (valor <= a || valor >= c) return 0.0;
            else if (valor == b) return 1.0;
            else if (valor > a && valor < b) return (valor - a) / (b - a);
            else return (c - valor) / (c - b);
        } else if (parametros.size() == 4) { // Trapezoidal
            // Obtener los valores de os parametros
            double a = parametros.get(0), b = parametros.get(1), c = parametros.get(2), d = parametros.get(3);
            // Verificar que caso de la funcion cumple y hacer el calculo correspondiente
            if (valor <= a || valor >= d) return 0.0;
            else if (valor >= b && valor <= c) return 1.0;
            else if (valor > a && valor < b) return (valor - a) / (b - a);
            else return (d - valor) / (d - c);
        }
        return 0.0;
    }

    // Funcion para colocar un grado de pertenencia ya calculado en el mapa de valores calculados
    public void setGradoPertenencia(double id, double grado) {
        valoresCalculados.put(id, grado); // Almacena el grado en el mapa
    }

    public HashMap<Double, Double> getValoresCalculados() {
        return valoresCalculados;
    }

    // Funcion para calcular el centroide del conjunto
    public double getCentroide() {
        // Si hay tres parametros se usa la funcion triagular si hay cuatro la trapezoidal
        if (parametros.size() == 3) { // Triangular
            // El centroide es el segundo parámetro (b)
            return parametros.get(1);
        } else if (parametros.size() == 4) { // Trapezoidal
            // El centroide se calcula como el promedio ponderado de las áreas
            double a = parametros.get(0), b = parametros.get(1), c = parametros.get(2), d = parametros.get(3);

            // Altura del trapezoide en la zona de 1.0
            double altura = 1.0;

            // Área del trapezoide y centroide aproximado como promedio ponderado
            double baseInferior = d - a;
            double baseSuperior = c - b;
            double area = 0.5 * (baseInferior + baseSuperior) * altura;
            double centroide = (a + b + c + d) / 4.0; // Aproximación del centroide para un trapezoide

            return centroide;
        }
        return 0.0;
    }

    // Funcion que contruye un string para poder imprimir un conjnto difuso
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
