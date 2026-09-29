package solidexercicio10.model;
import java.util.Random;
public class Inimigo implements Perigo, Movel {
    private int x;
    private int y;
    public Inimigo(int x, int y) {
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
    @Override
    public void mover(Random random, int minX, int maxX, int minY, int maxY) {
        int direcao = random.nextInt(4);
        switch (direcao) {
            case 0: if (x < maxX) x++; break;
            case 1: if (x > minX) x--; break;
            case 2: if (y < maxY) y++; break;
            case 3: if (y > minY) y--; break;
        }
    }
}
