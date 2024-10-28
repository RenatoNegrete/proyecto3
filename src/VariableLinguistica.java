import java.util.ArrayList;
import java.util.List;

public class VariableLinguistica {
    private String nombre;
    private double rangoMin;
    private double rangoMax;
    private List<ConjuntoDifuso> conjuntosDifusos = new ArrayList<ConjuntoDifuso>();

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

    public void print() {
        System.out.println("# Variable: " + nombre);
        System.out.println("Rango: " + rangoMin + ", " + rangoMax);
        System.out.println("Conjuntos: ");
        for (ConjuntoDifuso c : conjuntosDifusos) {
            System.out.println(c.print());
        }
    }

    public void fuzificar(double valor) {
        System.out.println("Fuzificación para valor " + valor + " de la variable " + nombre + ":");
        for (ConjuntoDifuso conjunto : conjuntosDifusos) {
            double gradoPertenencia = conjunto.calcularPertenencia(valor);
            if (gradoPertenencia != 0) {
                conjunto.setGradoPertenencia(valor, gradoPertenencia);
                System.out.println("  Grado de pertenencia a " + conjunto.getNombre() + ": " + gradoPertenencia);
            }
        }
    }
}
