package solidexercicio10.service;
import solidexercicio10.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
public class GameService {
    private final GameRenderer renderer;
    private final RankingService rankingService;
    public GameService(GameRenderer renderer, RankingService rankingService) {
        this.renderer = renderer;
        this.rankingService = rankingService;
    }
    public void jogarPartida(Scanner scanner, Random random, String pilotoNome, Dificuldade dificuldade, int tamanhoMapa) {
        int minX = -tamanhoMapa;
        int maxX = tamanhoMapa;
        int minY = -tamanhoMapa;
        int maxY = tamanhoMapa;
        System.out.println("\nIniciando missao na dificuldade " + dificuldade + "...");
        System.out.println("Pressione Enter para decolar!");
        scanner.nextLine();
        Missao missao = criarNovaMissao(random, minX, maxX, minY, maxY, dificuldade);
        Nave nave = missao.getNave();
        int score = definirPontuacaoInicial(dificuldade);
        int movimentos = 0;
        boolean partidaAtiva = true;
        long tempoInicio = System.currentTimeMillis();
        while (partidaAtiva) {
            renderer.desenharMapa(missao, minX, maxX, minY, maxY, score, pilotoNome);
            System.out.printf("Nave em (%d,%d) | Pontos: %d | Vidas: %d | A bordo: %d/%d | Restantes: %d%n",
                    nave.getX(), nave.getY(), score, nave.getVidas(),
                    nave.getPassageiros().size(), nave.getCapacidade(),
                    missao.todosEmbarcados() ? 0 : missao.getPassageiros().size());
            String direcaoInput = lerComando(scanner);
            if (direcaoInput.isEmpty()) continue;
            char cmd = direcaoInput.charAt(0);
            if (cmd == 'q') {
                System.out.println("Missao abortada pelo piloto.");
                partidaAtiva = false;
            } else if (cmd == 'c') {
                score = processarEmbarque(missao, score);
            } else if (isComandoMovimento(cmd)) {
                nave.moverComLimites(cmd, minX, maxX, minY, maxY);
                score--;
                movimentos++;
            } else {
                System.out.println("Comando invalido.");
                continue;
            }
            missao.moverInimigos(random, minX, maxX, minY, maxY);
            if (missao.verificaColisao()) {
                nave.perderVida();
                if (nave.getVidas() > 0) {
                    System.out.printf("Alerta! Colisao detectada! Vidas restantes: %d%n", nave.getVidas());
                } else {
                    System.out.println("GAME OVER! A nave foi destruida.");
                    partidaAtiva = false;
                }
            }
            if (score <= 0) {
                System.out.println("Combustivel/Pontuacao zerada! Missao perdida.");
                partidaAtiva = false;
            }
            if (missao.todosEmbarcados() && partidaAtiva) {
                if (nave.getX() == 0 && nave.getY() == 0) {
                    long tempoFim = System.currentTimeMillis();
                    long tempoJogoSegundos = (tempoFim - tempoInicio) / 1000;
                    System.out.println("\n================================================================");
                    System.out.println("DECOLAGEM AUTORIZADA! Nave acoplada a plataforma em (0,0).");
                    System.out.println("Retornando a orbita marciana com todos os passageiros. Missao cumprida!");
                    System.out.println("================================================================");
                    exibirEstatisticas(score, movimentos, tempoJogoSegundos, nave.getPassageiros().size());
                    RankingEntry novaEntrada = new RankingEntry(
                            pilotoNome, score, dificuldade, nave.getPassageiros().size(),
                            java.time.LocalDateTime.now().toString().substring(0, 19).replace('T', ' '),
                            tempoJogoSegundos
                    );
                    rankingService.adicionarAoRanking(novaEntrada);
                    partidaAtiva = false;
                } else {
                    System.out.println("ALERTA: Todos os passageiros resgatados! Retorne para a Plataforma de Pouso 'L' em (0,0).");
                }
            }
        }
    }
    private int processarEmbarque(Missao missao, int score) {
        Passageiro p = missao.passagemNaPosicao();
        if (p == null) {
            System.out.println("Nenhum passageiro nesta posicao.");
        } else {
            boolean embarcou = missao.embarcarPassageiroNaPosicao();
            if (embarcou) {
                int bonus = p.getPontuacao();
                score += bonus;
                System.out.printf("Passageiro %s embarcado com sucesso! +%d pontos!%n", p.getNome(), bonus);
            } else {
                System.out.println("Nave cheia! Nao ha espaco para mais passageiros.");
            }
        }
        return score;
    }
    private boolean isComandoMovimento(char cmd) {
        return cmd == 'w' || cmd == 's' || cmd == 'a' || cmd == 'd';
    }
    private String lerComando(Scanner scanner) {
        System.out.print("Comando (w/s/a/d/c/q): ");
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim().toLowerCase();
        }
        return "";
    }
    private Missao criarNovaMissao(Random random, int minX, int maxX, int minY, int maxY, Dificuldade dificuldade) {
        Nave nave = new Nave("A-1", 5);
        Missao missao = new Missao(nave);
        int qtdPassageiros = 5;
        int qtdAsteroides = 2;
        int qtdInimigos = 2;
        if (dificuldade == Dificuldade.FACIL) {
            qtdPassageiros = 4;
            qtdAsteroides = 1;
            qtdInimigos = 1;
        } else if (dificuldade == Dificuldade.DIFICIL) {
            qtdPassageiros = 5;
            qtdAsteroides = 3;
            qtdInimigos = 3;
        }
        while (missao.getPassageiros().size() < qtdPassageiros) {
            int x = random.nextInt(maxX - minX + 1) + minX;
            int y = random.nextInt(maxY - minY + 1) + minY;
            if (x == nave.getX() && y == nave.getY()) continue;
            if (posicaoOcupada(missao, x, y)) continue;
            int index = missao.getPassageiros().size();
            missao.addPassageiro(criarPassageiroPolimorfico(index, x, y));
        }
        while (missao.getAsteroides().size() < qtdAsteroides) {
            int x = random.nextInt(maxX - minX + 1) + minX;
            int y = random.nextInt(maxY - minY + 1) + minY;
            if (x == nave.getX() && y == nave.getY()) continue;
            if (posicaoOcupada(missao, x, y)) continue;
            missao.addPerigo(new Asteroide(x, y));
        }
        while (missao.getInimigos().size() < qtdInimigos) {
            int x = random.nextInt(maxX - minX + 1) + minX;
            int y = random.nextInt(maxY - minY + 1) + minY;
            if (x == nave.getX() && y == nave.getY()) continue;
            if (posicaoOcupada(missao, x, y)) continue;
            missao.addPerigo(new Inimigo(x, y));
        }
        return missao;
    }
    private Passageiro criarPassageiroPolimorfico(int indice, int x, int y) {
        switch (indice % 5) {
            case 0: return new Professor("Dr. Silva", x, y);
            case 1: return new Engenheiro("Eng. Rosa", x, y);
            case 2: return new Professor("Dr. Lima", x, y);
            case 3: return new Engenheiro("Eng. Carlos", x, y);
            default: return new Astronauta("Ast. Maria", x, y);
        }
    }
    private boolean posicaoOcupada(Missao missao, int x, int y) {
        Nave nave = missao.getNave();
        if (nave.getX() == x && nave.getY() == y) return true;
        for (Passageiro p : missao.getPassageiros()) {
            if (p.getX() == x && p.getY() == y) return true;
        }
        for (Perigo perigo : missao.getPerigos()) {
            if (perigo.getX() == x && perigo.getY() == y) return true;
        }
        return false;
    }
    private void exibirEstatisticas(int score, int movimentos, long tempoSegundos, int passageiros) {
        System.out.println("Estatisticas da Partida:");
        System.out.printf(" - Pontuacao Final: %d pontos%n", score);
        System.out.printf(" - Movimentos Efetuados: %d%n", movimentos);
        System.out.printf(" - Tempo de Jogo: %d segundos%n", tempoSegundos);
        System.out.printf(" - Passageiros Resgatados: %d%n", passageiros);
        System.out.println("================================================================");
    }
    private int definirPontuacaoInicial(Dificuldade dificuldade) {
        switch (dificuldade) {
            case FACIL: return 30;
            case DIFICIL: return 15;
            default: return 20;
        }
    }
}
