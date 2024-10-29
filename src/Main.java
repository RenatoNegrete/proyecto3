import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MotorInferencia motor = new MotorInferencia();
        String ruta = "src/variables.txt";
        motor.cargarVariables(ruta);
        System.out.println("Variables linguisticas:");
        motor.printVariables();
        System.out.println("-----------------------------------------------");
        motor.cargarBaseConocimiento("src/base_conocimiento.txt");
        System.out.println("Base de conocimientos: ");
        motor.printBaseConocimiento();
        String a1 = "Temperatura = 165";
        String a2 = "Presion = 75";
        List<String> aProbar = new ArrayList<>();
        aProbar.add(a1);
        aProbar.add(a2);
        motor.inferencia(aProbar);
    }
}