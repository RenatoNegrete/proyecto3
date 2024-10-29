import java.util.ArrayList;
import java.util.List;

public class BaseConocimiento {
    private List<Condicion> condiciones; // Lista de las condiciones o reglas

    public BaseConocimiento() { // Constructor
        this.condiciones = new ArrayList<>();
    }

    public List<Condicion> getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(List<Condicion> condiciones) {
        this.condiciones = condiciones;
    }

    // Funcion para añadir una condicion a la lista
    public void addCondicion(Condicion condicion) {
        this.condiciones.add(condicion);
    }

    // Funcion para imprimir la base de conocimiento
    public void print() {
        for (Condicion c : condiciones) {
            System.out.println(c.print());
        }
    }
}
