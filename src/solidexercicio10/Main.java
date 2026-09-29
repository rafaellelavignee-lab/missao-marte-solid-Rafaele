package solidexercicio10;
import solidexercicio10.model.Dificuldade;
import solidexercicio10.service.GameService;
import solidexercicio10.presentation.GameRenderer;
import solidexercicio10.service.RankingService;
import solidexercicio10.repository.RankingRepository;
import solidexercicio10.repository.JsonRankingRepository;
import java.util.Random;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        RankingRepository rankingRepository = new JsonRankingRepository();
        GameRenderer renderer = new GameRenderer();
        RankingService rankingService = new RankingService(rankingRepository);
        GameService gameService = new GameService(renderer, rankingService);
        exibirBoasVindas();
        boolean rodando = true;
        while (rodando) {
            exibirMenu();
            String opcao = lerOpcao(scanner);
            switch (opcao) {
                case "1":
                    jogarPartida(scanner, gameService);
                    break;
                case "2":
                    rankingService.exibirRankingCompleto();
                    break;
                case "3":
                    rankingService.resetarRanking(scanner);
                    break;
                case "4":
                    rodando = false;
                    System.out.println("\nObrigado por jogar a Missao Marte Unifor!");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        }
        scanner.close();
    }
    private static void exibirBoasVindas() {
        System.out.println("================================================================");
        System.out.println("             MISSAO MARTE UNIFOR - VERSAO REFATORADA SOLID      ");
        System.out.println("================================================================");
        System.out.println("  Pilote sua nave, salve os passageiros e desvie dos perigos!   ");
        System.out.println("================================================================");
    }
    private static void exibirMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Iniciar Nova Missao");
        System.out.println("2. Visualizar Ranking Top 5");
        System.out.println("3. Resetar Historico de Ranking");
        System.out.println("4. Sair do Jogo");
        System.out.println("----------------------");
    }
    private static void jogarPartida(Scanner scanner, GameService gameService) {
        String pilotoNome = lerLinha(scanner, "\nDigite o nome do piloto: ", "Piloto Anonimo");
        if (pilotoNome.isEmpty()) pilotoNome = "Piloto Anonimo";
        Dificuldade dificuldade = lerDificuldade(scanner);
        int tamanhoMapa = lerTamanhoMapa(scanner);
        gameService.jogarPartida(scanner, new Random(), pilotoNome, dificuldade, tamanhoMapa);
    }
    private static Dificuldade lerDificuldade(Scanner scanner) {
        System.out.print("Escolha a Dificuldade (facil/medio/dificil): ");
        String difStr = lerLinha(scanner, "", "medio");
        return Dificuldade.deString(difStr);
    }
    private static int lerTamanhoMapa(Scanner scanner) {
        try {
            int tamanho = Integer.parseInt(lerLinha(scanner, "Tamanho do mapa (ex: 5 para mapa de -5 a +5): ", "5"));
            return tamanho > 0 ? tamanho : 5;
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida, usando tamanho padrao (5).");
            return 5;
        }
    }
    private static String lerOpcao(Scanner scanner) {
        System.out.print("Escolha uma opcao: ");
        return lerLinha(scanner, "", "1");
    }
    private static String lerLinha(Scanner scanner, String prompt, String fallback) {
        if (prompt != null && !prompt.isEmpty()) System.out.print(prompt);
        if (scanner.hasNextLine()) return scanner.nextLine().trim();
        return fallback;
    }
}
