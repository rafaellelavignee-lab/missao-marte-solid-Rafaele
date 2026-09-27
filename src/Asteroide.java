package solidexercicio10;
public class Asteroide implements Perigo {
    private final int x;
    private final int y;
    public Asteroide(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public int getX() { return x; }
    @Override
    public int getY() { return y; }
    @Override
    public boolean colideCom(Nave nave) {
        return nave.getX() == this.x && nave.getY() == this.y;
    }
}
