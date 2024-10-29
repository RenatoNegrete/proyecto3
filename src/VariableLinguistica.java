import java.util.ArrayList;
import java.util.List;

public class VariableLinguistica {
    private String nombre; // Nombre de la varibale linguistica
    private double rangoMin; // Valor minimo del rango
    private double rangoMax; // Valor maximo del rango
    private List<ConjuntoDifuso> conjuntosDifusos = new ArrayList<ConjuntoDifuso>(); // Lista que contiene los conjuntos de la variable

    // Constructor
    public VariableLinguistica(String nombre, double rangoMin, double rangoMax) {
        this.nombre = nombre;
        this.rangoMin = rangoMin;
        this.rangoMax = rangoMax;
    }

    public void agregarConjuntoDifuso(ConjuntoDifuso conjunto) {
        conjuntosDifusos.add(conjunto);
    }

    public String getNombre() {
        return nombre;
    }

    public List<ConjuntoDifuso> getConjuntosDifusos() {
        return conjuntosDifusos;
    }

    // Funcion para imprimir una variable linguistica
    public void print() {
        System.out.println("# Variable: " + nombre);
        System.out.println("Rango: " + rangoMin + ", " + rangoMax);
        System.out.println("Conjuntos: ");
        for (ConjuntoDifuso c : conjuntosDifusos) {
            System.out.println(c.print());
        }
    }

    // Funcion que hace la fuzificacin de un valor
    public void fuzificar(double valor) {
        System.out.println("Fuzificación para valor " + valor + " de la variable " + nombre + ":");
        // Recorrer todos los conjuntos
        for (ConjuntoDifuso conjunto : conjuntosDifusos) {
            // Calcular el grado de pertenencia del valor en un conjunto
            double gradoPertenencia = conjunto.calcularPertenencia(valor);
            System.out.println("  Grado de pertenencia a " + conjunto.getNombre() + ": " + gradoPertenencia);
            // Si el grado de pertenencia es diferente de 0 se guarda en el conjunto como un valor calculado
            if (gradoPertenencia != 0) {
                conjunto.setGradoPertenencia(valor, gradoPertenencia);
            }
        }
    }
}
