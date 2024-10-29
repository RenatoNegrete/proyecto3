import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MotorInferencia {
    private List<VariableLinguistica> variables;
    private BaseConocimiento baseConocimiento;

    public MotorInferencia() {
        this.variables = new ArrayList<VariableLinguistica>();
        this.baseConocimiento = new BaseConocimiento();
    }

    public void agregarVariable(VariableLinguistica variable) {
        this.variables.add(variable);
    }

    public void printVariables() {
        for (VariableLinguistica v : variables) {
            v.print();
        }
    }

    public void printBaseConocimiento() {
        this.baseConocimiento.print();
    }

    public void inferencia(List<String> list) {
        HashMap<String, Double> valores = new HashMap<>();
        HashMap<String, Double> maximosConjuntos = new HashMap<>();
        HashMap<String, Double> centroidesConjuntos = new HashMap<>();
        for (String s : list) {
            System.out.println("-----------------------------------------------");
            String[] datos = s.split("=");
            String variable = datos[0].trim();
            String valorStr = datos[1].trim();
            double valor = Double.parseDouble(valorStr);
            for (VariableLinguistica v : this.variables) {
                if (variable.equals(v.getNombre())) {
                    v.fuzificar(valor);
                    valores.put(variable, valor);
                }
            }
        }
        for (Condicion c : this.baseConocimiento.getCondiciones()) {
            if (evaluaCondicion(c, valores)) {
                System.out.println("Regla cumplida: " + c.getSalida());
                // Calcula el valor de pertenencia de la regla cumplida
                double val1 = obtenerValor(c.getNomVariable1(), c.getEntrada1(), valores.get(c.getNomVariable1()));
                double val2 = obtenerValor(c.getNomVariable2(), c.getEntrada2(), valores.get(c.getNomVariable2()));
                double valorRegla = Math.min(val1, val2); // Toma el mínimo como valor de la regla cumplida
                maximosConjuntos.merge(c.getSalida(), valorRegla, Math::max);
                if (!centroidesConjuntos.containsKey(c.getSalida())) {
                    centroidesConjuntos.put(c.getSalida(), obtenerCentroide(c.getSalida()));
                }
            }
        }
        double valorDefuzzificado = defuzzificar(maximosConjuntos, centroidesConjuntos);
        System.out.println("Valor defuzzificado: " + valorDefuzzificado);
    }

    public static double defuzzificar(HashMap<String, Double> fuzzyValues, HashMap<String, Double> centroides) {
        double numerador = 0.0;
        double denominador = 0.0;

        for (Map.Entry<String, Double> entry : fuzzyValues.entrySet()) {
            String conjunto = entry.getKey();
            double gradoPertenencia = entry.getValue();
            double valorCentroide = centroides.get(conjunto); // Valor representativo o centroide

            numerador += gradoPertenencia * valorCentroide;
            denominador += gradoPertenencia;
        }

        // Evita la división por cero en caso de que el denominador sea cero
        return (denominador == 0.0) ? 0.0 : numerador / denominador;
    }

    public double obtenerCentroide(String conjunto) {
        for (VariableLinguistica v : this.variables) {
            for (ConjuntoDifuso c : v.getConjuntosDifusos()) {
                if (c.getNombre().equals(conjunto)) {
                    return c.getCentroide(); // Suponiendo que tienes un método para obtener el centroide
                }
            }
        }
        return 0.0; // En caso de que no se encuentre
    }

    private boolean evaluaCondicion(Condicion c, HashMap<String, Double> valores) {
        boolean resultado = true;
        if (!enConjunto(c.getNomVariable1(), c.getEntrada1()) || !valores.containsKey(c.getNomVariable1())) {
            resultado = false;
        }
        if (!enConjunto(c.getNomVariable2(), c.getEntrada2()) || !valores.containsKey(c.getNomVariable2())) {
            resultado = false;
        }
        return resultado;
    }

    public boolean enConjunto(String nombre, String s) {
        for (VariableLinguistica v : this.variables) {
            if (v.getNombre().equals(nombre)) {
                for (ConjuntoDifuso c : v.getConjuntosDifusos()) {
                    if (c.getNombre().equals(s) && !(c.getValoresCalculados().isEmpty())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public double obtenerValor(String nombre, String s, double n) {
        for (VariableLinguistica v : this.variables) {
            if (v.getNombre().equals(nombre)) {
                for (ConjuntoDifuso c : v.getConjuntosDifusos()) {
                    if (c.getNombre().equals(s)) {
                        return c.getValoresCalculados().get(n);
                    }
                }
            }
        }
        return 0;
    }

    public void cargarVariables(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            VariableLinguistica variableActual = null;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.startsWith("# Variable:")) {
                    // Extraer nombre de la variable
                    String nombre = linea.split(":")[1].trim();
                    variableActual = new VariableLinguistica(nombre, 0, 0); // Rango será actualizado después
                    variables.add(variableActual);
                } else if (linea.startsWith("Rango:")) {
                    // Extraer rango de la variable
                    String[] rango = linea.split(":")[1].trim().split(",");
                    double rangoMin = Double.parseDouble(rango[0].trim());
                    double rangoMax = Double.parseDouble(rango[1].trim());
                    if (variableActual != null) {
                        variableActual = new VariableLinguistica(variableActual.getNombre(), rangoMin, rangoMax);
                        variables.set(variables.size() - 1, variableActual); // Reemplazar con rango actualizado
                    }
                } else if (!linea.isEmpty() && !linea.equals("Conjuntos:")) {
                    // Crear conjunto difuso desde la línea y agregarlo a la variable actual
                    if (variableActual != null) {
                        ConjuntoDifuso conjunto = ConjuntoDifuso.obtenerConjunto(linea);
                        variableActual.agregarConjuntoDifuso(conjunto);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cargarBaseConocimiento(String ruta) {
        Pattern pattern = Pattern.compile(
                "if \\((\\w+) es (\\w+) (\\w+) (\\w+) es (\\w+)\\) then (\\w+) es (\\w+)",
                Pattern.CASE_INSENSITIVE
        );

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(linea);
                if (matcher.find()) {
                    String nomVariable1 = matcher.group(1);
                    String entrada1 = matcher.group(2);
                    String operador = matcher.group(3);
                    String nomVariable2 = matcher.group(4);
                    String entrada2 = matcher.group(5);
                    String nomSalida = matcher.group(6);
                    String salida = matcher.group(7);

                    Condicion condicion = new Condicion(nomVariable1, entrada1, nomVariable2, entrada2, nomSalida, salida, operador);
                    this.baseConocimiento.addCondicion(condicion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
