package solidexercicio10.model;
public class Passageiro implements Posicionavel {
    private final String nome;
    private final String tipo;
    private final int x;
    private final int y;
    public Passageiro(String nome, String tipo, int x, int y) {
        this.nome = nome;
        this.tipo = tipo;
        this.x = x;
        this.y = y;
    }
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    @Override
    public int getX() { return x; }
    @Override
    public int getY() { return y; }
    public int getPontuacao() { return 10; }
}
