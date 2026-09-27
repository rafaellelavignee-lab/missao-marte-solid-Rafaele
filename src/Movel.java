package solidexercicio10;
import java.util.Random;
public interface Movel extends Posicionavel {
    void mover(Random random, int minX, int maxX, int minY, int maxY);
}
