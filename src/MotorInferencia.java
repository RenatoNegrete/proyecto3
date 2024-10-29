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
    private List<VariableLinguistica> variables; // lista de las variables linguisticas
    private BaseConocimiento baseConocimiento; // Base de conocimientos

    public MotorInferencia() { // Constructor
        this.variables = new ArrayList<VariableLinguistica>();
        this.baseConocimiento = new BaseConocimiento();
    }

    // Funcion para agregar una variable a la lista de variables
    public void agregarVariable(VariableLinguistica variable) {
        this.variables.add(variable);
    }

    // Funcion para imprimir las variables
    public void printVariables() {
        for (VariableLinguistica v : variables) {
            v.print();
        }
    }

    // Funcion para imprimir la base de conocimiento
    public void printBaseConocimiento() {
        this.baseConocimiento.print();
    }

    // Funcion para hacer la inferencia
    public void inferencia(List<String> list) {
        HashMap<String, Double> valores = new HashMap<>(); // Mapa para guardar los valores fuzificados
        HashMap<String, Double> maximosConjuntos = new HashMap<>(); // Mapa para guardar el valor del conjunto que se evalua la regla
        HashMap<String, Double> centroidesConjuntos = new HashMap<>(); // Mapa para guardar los centroides de un conjunto
        for (String s : list) {
            System.out.println("-----------------------------------------------");
            String[] datos = s.split("=");
            String variable = datos[0].trim();
            String valorStr = datos[1].trim();
            double valor = Double.parseDouble(valorStr);
            // Se hace la fuzzificacion
            for (VariableLinguistica v : this.variables) {
                // Si la varibale corresponde con la variable que se quiere evaluar
                if (variable.equals(v.getNombre())) {
                    // Se hace la fuzzificacion del valor
                    v.fuzificar(valor);
                    // Se coloca en el mapa de valores a la variable como llave y el valor como valor
                    valores.put(variable, valor);
                }
            }
        }
        System.out.println("---------------------------");
        System.out.println("Reglas");
        // Se itera la base de conocimiento
        for (Condicion c : this.baseConocimiento.getCondiciones()) {
            // Si la condicion esta en un conjunto y tiene un valor calculado
            if (evaluaCondicion(c, valores)) {
                System.out.println("Regla cumplida: " + c.print());
                // Calcula el valor de pertenencia de la regla cumplida
                double val1 = obtenerValor(c.getNomVariable1(), c.getEntrada1(), valores.get(c.getNomVariable1())); // Se obtiene el valor calculado del primer argumento
                double val2 = obtenerValor(c.getNomVariable2(), c.getEntrada2(), valores.get(c.getNomVariable2())); // Se obtiene el valor calculado del segundo argumento
                double valorRegla = Math.min(val1, val2); // Toma el mínimo como valor de la regla cumplida
                System.out.println("Valor minimo: " + valorRegla);
                // Para el caso donde las reglas tienen el mismo resultado toca colocar el valor maximo de los dos valores calculados
                maximosConjuntos.merge(c.getSalida(), valorRegla, Math::max);
                // Si el centroide no exite se calcula
                if (!centroidesConjuntos.containsKey(c.getSalida())) {
                    // Se calcula el centroide y se lo coloca en el mapa de centroides
                    centroidesConjuntos.put(c.getSalida(), obtenerCentroide(c.getSalida()));
                }
            }
        }
        System.out.println("--------------------------------");
        System.out.println("Valores finales: ");
        for (Map.Entry<String, Double> entry : maximosConjuntos.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        // Se calcula el valor de la defuzzificacion
        System.out.println("----------------------------------");
        System.out.println("Defuzzificacion");
        double valorDefuzzificado = defuzzificar(maximosConjuntos, centroidesConjuntos);
        System.out.println("Valor defuzzificado: " + valorDefuzzificado);
    }

    // Funcion para hacer la defuzzificacion
    public static double defuzzificar(HashMap<String, Double> fuzzyValues, HashMap<String, Double> centroides) {
        double numerador = 0.0; // Numerador
        double denominador = 0.0; // Denominador

        for (Map.Entry<String, Double> entry : fuzzyValues.entrySet()) {
            String conjunto = entry.getKey(); // Valor original de la variable
            double gradoPertenencia = entry.getValue(); // Grado de pertenencia calculado
            double valorCentroide = centroides.get(conjunto); // Valor del centroide

            numerador += gradoPertenencia * valorCentroide; // El numerador es la suma de los grados de pertenencia multiplicados por los centroides
            denominador += gradoPertenencia; // El denominador es la suma de los grados de pertenencia
        }
        System.out.println(numerador + " / " + denominador);

        // Evita la división por cero en caso de que el denominador sea cero
        return (denominador == 0.0) ? 0.0 : numerador / denominador;
    }

    // Funcion para obtener el centroide
    public double obtenerCentroide(String conjunto) {
        // Recorrer las variables
        for (VariableLinguistica v : this.variables) {
            // Recorrer los conjuntos de cada variable
            for (ConjuntoDifuso c : v.getConjuntosDifusos()) {
                // Si el conjunto que se esta buscando coincide
                if (c.getNombre().equals(conjunto)) {
                    return c.getCentroide(); // Calcular el centroide
                }
            }
        }
        return 0.0; // En caso de que no se encuentre
    }

    // Funcion para evaluar una regla
    private boolean evaluaCondicion(Condicion c, HashMap<String, Double> valores) {
        boolean resultado = true;
        // Se verifica que la varibale y el conjunto del primer argumento si existan y tengan un valor calculado
        if (!enConjunto(c.getNomVariable1(), c.getEntrada1()) || !valores.containsKey(c.getNomVariable1())) {
            resultado = false;
        }
        // Se verifica que la varibale y el conjunto del segundo argumento si existan y tengan un valor calculado
        if (!enConjunto(c.getNomVariable2(), c.getEntrada2()) || !valores.containsKey(c.getNomVariable2())) {
            resultado = false;
        }
        return resultado;
    }

    // Funcion para ver si una varibale y un conjunto existen
    public boolean enConjunto(String nombre, String s) {
        // Recorrer las variables
        for (VariableLinguistica v : this.variables) {
            // Si el nombre de la variable coincide
            if (v.getNombre().equals(nombre)) {
                // Recorrer los conjunto de la variable que coincidio
                for (ConjuntoDifuso c : v.getConjuntosDifusos()) {
                    // Verificar que el nombre del conjunto coincida y que tenga un valor calculado
                    if (c.getNombre().equals(s) && !(c.getValoresCalculados().isEmpty())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Funcion para obtener un valor calculado de un conjunto en especifico
    public double obtenerValor(String nombre, String s, double n) {
        // Se recorre las variables para buscar la variable especifica
        for (VariableLinguistica v : this.variables) {
            if (v.getNombre().equals(nombre)) {
                // Se recorre el conjunto para encontrar el conjunto especifico
                for (ConjuntoDifuso c : v.getConjuntosDifusos()) {
                    if (c.getNombre().equals(s)) {
                        // Se retorna el valor calculado
                        return c.getValoresCalculados().get(n);
                    }
                }
            }
        }
        return 0;
    }

    // Funcion para cargar las variables desde el archivo de texto
    public void cargarVariables(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            VariableLinguistica variableActual = null;
            // Recorrer las lineas del archivo
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                // Si la linea comienza con # Variable significa que tenemos una variables nueva
                if (linea.startsWith("# Variable:")) {
                    // Extraer nombre de la variable
                    String nombre = linea.split(":")[1].trim();
                    variableActual = new VariableLinguistica(nombre, 0, 0); // Rango será actualizado después
                    // Añadimos la variable que se esta leyendo a la lista de variables
                    variables.add(variableActual);
                    // Si la linea comienza con Rango los dos numeros siguientes son el rango de la variable
                } else if (linea.startsWith("Rango:")) {
                    // Extraer rango de la variable
                    String[] rango = linea.split(":")[1].trim().split(",");
                    double rangoMin = Double.parseDouble(rango[0].trim());
                    double rangoMax = Double.parseDouble(rango[1].trim());
                    if (variableActual != null) {
                        // Se actualizan los valores del rango
                        variableActual = new VariableLinguistica(variableActual.getNombre(), rangoMin, rangoMax);
                        variables.set(variables.size() - 1, variableActual); // Reemplazar con rango actualizado
                    }
                    // Si la linea comienza con Conjuntos a continuacion las proximas lineas describiran los conjuntos de la variable
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

    // Funcion para cargar la base de conocimiento desde el archivo
    public void cargarBaseConocimiento(String ruta) {
        // Se declara una expresion regular para facilitar la lectura del archivo
        Pattern pattern = Pattern.compile(
                "if \\((\\w+) es (\\w+) (\\w+) (\\w+) es (\\w+)\\) then (\\w+) es (\\w+)",
                Pattern.CASE_INSENSITIVE
        );

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            // Recorrer las lineas del archivo
            while ((linea = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(linea);
                // Obtener los valores importantes de la linea
                if (matcher.find()) {
                    String nomVariable1 = matcher.group(1);
                    String entrada1 = matcher.group(2);
                    String operador = matcher.group(3);
                    String nomVariable2 = matcher.group(4);
                    String entrada2 = matcher.group(5);
                    String nomSalida = matcher.group(6);
                    String salida = matcher.group(7);

                    // Crear la condicion y añadirla a la base de conocimiento
                    Condicion condicion = new Condicion(nomVariable1, entrada1, nomVariable2, entrada2, nomSalida, salida, operador);
                    this.baseConocimiento.addCondicion(condicion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
