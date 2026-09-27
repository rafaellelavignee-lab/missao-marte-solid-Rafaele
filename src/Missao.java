package solidexercicio10;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
public class Missao {
    private final Nave nave;
    private final List<Passageiro> passageiros;
    private final List<Perigo> perigos;
    public Missao(Nave nave) {
        this.nave = nave;
        this.passageiros = new ArrayList<>();
        this.perigos = new ArrayList<>();
    }
    public Nave getNave() { return nave; }
    public List<Passageiro> getPassageiros() { return passageiros; }
    public List<Perigo> getPerigos() { return perigos; }
    public void addPassageiro(Passageiro p) { passageiros.add(p); }
    public void addPerigo(Perigo p) { perigos.add(p); }
    public List<Asteroide> getAsteroides() {
        List<Asteroide> asteroides = new ArrayList<>();
        for (Perigo p : perigos) {
            if (p instanceof Asteroide) asteroides.add((Asteroide) p);
        }
        return asteroides;
    }
    public List<Inimigo> getInimigos() {
        List<Inimigo> inimigos = new ArrayList<>();
        for (Perigo p : perigos) {
            if (p instanceof Inimigo) inimigos.add((Inimigo) p);
        }
        return inimigos;
    }
    public boolean verificaColisao() {
        for (Perigo p : perigos) {
            if (p.colideCom(nave)) return true;
        }
        return false;
    }
    public void moverInimigos(Random random, int minX, int maxX, int minY, int maxY) {
        for (Inimigo i : getInimigos()) {
            i.mover(random, minX, maxX, minY, maxY);
        }
    }
    public Passageiro passagemNaPosicao() {
        for (Passageiro p : passageiros) {
            if (p.getX() == nave.getX() && p.getY() == nave.getY()) return p;
        }
        return null;
    }
    public boolean embarcarPassageiroNaPosicao() {
        Iterator<Passageiro> it = passageiros.iterator();
        while (it.hasNext()) {
            Passageiro p = it.next();
            if (p.getX() == nave.getX() && p.getY() == nave.getY()) {
                boolean ok = nave.embarcar(p);
                if (ok) it.remove();
                return ok;
            }
        }
        return false;
    }
    public boolean todosEmbarcados() {
        return passageiros.isEmpty();
    }
}
