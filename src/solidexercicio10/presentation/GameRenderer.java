package solidexercicio10.presentation;
import solidexercicio10.model.*;
import java.util.List;
public class GameRenderer {
    public void desenharMapa(Missao missao, int minX, int maxX, int minY, int maxY, int score, String pilotoNome) {
        System.out.println();
        System.out.printf("Mapa da Miss?o (Pontos: %d) - Piloto: %s%n", score, pilotoNome);
        System.out.print("    ");
        for (int x = minX; x <= maxX; x++) {
            System.out.printf(" %2d", x);
        }
        System.out.println();
        System.out.print("    ");
        for (int x = minX; x <= maxX; x++) {
            System.out.print(" __");
        }
        System.out.println();
        for (int y = minY; y <= maxY; y++) {
            System.out.printf("%3d|", y);
            for (int x = minX; x <= maxX; x++) {
                char symbol = obterSymbolo(missao, x, y);
                System.out.printf(" %2c", symbol);
            }
            System.out.println();
        }
        exibirLegenda();
        exibirPassageiros(missao);
    }
    private char obterSymbolo(Missao missao, int x, int y) {
        Nave nave = missao.getNave();
        if (nave.getX() == x && nave.getY() == y) return '@';
        for (Passageiro p : missao.getPassageiros()) {
            if (p.getX() == x && p.getY() == y) return obterSymboloPassageiro(p);
        }
        for (Perigo perigo : missao.getPerigos()) {
            if (perigo.getX() == x && perigo.getY() == y) return obterSymboloPerigo(perigo);
        }
        if (x == 0 && y == 0) return 'L';
        return '.';
    }
    private char obterSymboloPassageiro(Passageiro p) {
        if (p instanceof Engenheiro) return 'E';
        if (p instanceof Astronauta) return 'T';
        return 'P';
    }
    private char obterSymboloPerigo(Perigo perigo) {
        if (perigo instanceof Asteroide) return '#';
        if (perigo instanceof Inimigo) return 'X';
        return '?';
    }
    private void exibirLegenda() {
        System.out.println("Legenda: @=Nave, P=Professor, E=Engenheiro, T=Astronauta, #=Asteroide, X=Inimigo, L=Plataforma, .=Vazio");
        System.out.println("Comandos: w/s/a/d (mover), c (embarcar), q (sair)");
    }
    private void exibirPassageiros(Missao missao) {
        System.out.println("Passageiros na superficie marciana:");
        for (Passageiro p : missao.getPassageiros()) {
            System.out.printf(" - %s (%s) em (%d,%d)%n", p.getNome(), p.getTipo(), p.getX(), p.getY());
        }
    }
}
