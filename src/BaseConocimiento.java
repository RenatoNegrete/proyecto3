import java.util.ArrayList;
import java.util.List;

public class BaseConocimiento {
    private List<Condicion> condiciones;

    public BaseConocimiento() {
        this.condiciones = new ArrayList<>();
    }

    public List<Condicion> getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(List<Condicion> condiciones) {
        this.condiciones = condiciones;
    }

    public void addCondicion(Condicion condicion) {
        this.condiciones.add(condicion);
    }

    public void print() {
        for (Condicion c : condiciones) {
            System.out.println(c.print());
        }
    }
}
