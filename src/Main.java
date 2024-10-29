import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MotorInferencia motor = new MotorInferencia();
        String ruta = "src/variables.txt";
        // Cargar las varibales desde el archivo de texto
        motor.cargarVariables(ruta);
        System.out.println("Variables linguisticas:");
        motor.printVariables();
        System.out.println("-----------------------------------------------");
        // Cargar la base de conocimiento desde el archivo de texto
        motor.cargarBaseConocimiento("src/base_conocimiento.txt");
        System.out.println("Base de conocimientos: ");
        motor.printBaseConocimiento();
        // Declarar los valores que se quiere dar a las variables para evaluar
        String a1 = "Humedad = 65";
        String a2 = "Viento = 7";
        List<String> aProbar = new ArrayList<>();
        aProbar.add(a1);
        aProbar.add(a2);
        // Hacer la inferencia
        motor.inferencia(aProbar);
    }
}