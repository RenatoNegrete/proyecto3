public class Condicion {
    private String nomVariable1;
    private String entrada1;
    private String nomVariable2;
    private String entrada2;
    private String nomSalida;
    private String salida;
    private String operador;

    public Condicion(String nomVariable1, String entrada1, String nomVariable2, String entraga2, String nomSalida, String salida, String operador) {
        this.nomVariable1 = nomVariable1;
        this.entrada1 = entrada1;
        this.nomVariable2 = nomVariable2;
        this.entrada2 = entraga2;
        this.nomSalida = nomSalida;
        this.salida = salida;
        this.operador = operador;
    }

    public String getEntrada1() {
        return entrada1;
    }

    public void setEntrada1(String entrada1) {
        this.entrada1 = entrada1;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getNomVariable1() {
        return nomVariable1;
    }

    public void setNomVariable1(String nomVariable1) {
        this.nomVariable1 = nomVariable1;
    }

    public String getNomVariable2() {
        return nomVariable2;
    }

    public void setNomVariable2(String nomVariable2) {
        this.nomVariable2 = nomVariable2;
    }

    public String getEntrada2() {
        return entrada2;
    }

    public void setEntrada2(String entrada2) {
        this.entrada2 = entrada2;
    }

    public String getNomSalida() {
        return nomSalida;
    }

    public void setNomSalida(String nomSalida) {
        this.nomSalida = nomSalida;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public StringBuilder print() {
        StringBuilder str = new StringBuilder();
        str.append("if (").append(nomVariable1).append(" es ").append(entrada1).append(" ").append(operador).append(" ").append(nomVariable2).append(" es ").append(entrada2).append(") then ").append(nomSalida).append(nomSalida).append(" es ").append(salida);
        return str;
    }
}