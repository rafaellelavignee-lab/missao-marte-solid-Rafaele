package solidexercicio10.service;
import solidexercicio10.*;
import solidexercicio10.repository.RankingRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
public class RankingService {
    private final RankingRepository repository;
    private List<RankingEntry> ranking;
    public RankingService(RankingRepository repository) {
        this.repository = repository;
        this.ranking = repository.load();
    }
    public void adicionarAoRanking(RankingEntry entrada) {
        if (isTopScore(entrada.score)) {
            ranking.add(entrada);
            List<RankingEntry> rankingFiltrado = ranking.stream()
                    .sorted(Comparator.comparingInt((RankingEntry e) -> e.score).reversed())
                    .limit(5)
                    .collect(Collectors.toList());
            repository.save(rankingFiltrado);
            ranking = rankingFiltrado;
            System.out.println("Parabens! Voce entrou para o Top 5 de pilotos!");
        }
    }
    public void exibirRankingCompleto() {
        System.out.println("\n====== RANKING TOP 5 PILOTOS ======");
        if (ranking.isEmpty()) {
            System.out.println(" - Nenhum registro encontrado. Seja o primeiro a jogar!");
        } else {
            int pos = 1;
            for (RankingEntry entry : ranking) {
                System.out.printf("%d. %s - %d pts | Dificuldade: %s | Coletados: %d | Tempo: %ds | %s%n",
                        pos++, entry.name, entry.score, entry.dificuldade, entry.passageirosColetados, entry.tempoJogo, entry.dataHora);
            }
        }
        System.out.println("===================================");
    }
    public void resetarRanking(Scanner scanner) {
        System.out.print("Voce realmente deseja limpar o historico de ranking? (s/n): ");
        String confirmacao = lerLinha(scanner).toLowerCase();
        if (confirmacao.equals("s") || confirmacao.equals("sim")) {
            repository.clear();
            ranking.clear();
            System.out.println("Ranking resetado com sucesso!");
        } else {
            System.out.println("Operacao cancelada.");
        }
    }
    private boolean isTopScore(int score) {
        if (ranking.size() < 5) return true;
        return score > ranking.stream()
                .min(Comparator.comparingInt((RankingEntry e) -> e.score))
                .map(e -> e.score)
                .orElse(0);
    }
    private String lerLinha(Scanner scanner) {
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "";
    }
}
