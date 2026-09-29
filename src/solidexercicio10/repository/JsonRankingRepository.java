package solidexercicio10.repository;
import solidexercicio10.model.Dificuldade;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
public class JsonRankingRepository implements RankingRepository {
    private final Path rankingPath = Paths.get("ranking.json");
    @Override
    public List<RankingEntry> load() {
        if (!Files.exists(rankingPath)) return new ArrayList<>();
        try {
            String json = new String(Files.readAllBytes(rankingPath), StandardCharsets.UTF_8).trim();
            return parseRankingJson(json);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    @Override
    public void save(List<RankingEntry> ranking) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < ranking.size(); i++) {
            RankingEntry entry = ranking.get(i);
            builder.append("{\"name\":\"").append(entry.name.replace("\"", "\\\""))
                    .append("\",\"score\":").append(entry.score)
                    .append(",\"dificuldade\":\"").append(entry.dificuldade.name())
                    .append("\",\"passageirosColetados\":").append(entry.passageirosColetados)
                    .append(",\"dataHora\":\"").append(entry.dataHora)
                    .append("\",\"tempoJogo\":").append(entry.tempoJogo).append("}");
            if (i < ranking.size() - 1) builder.append(",");
        }
        builder.append("]");
        try {
            Files.write(rankingPath, builder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Erro ao salvar ranking: " + e.getMessage());
        }
    }
    @Override
    public void clear() {
        try {
            Files.deleteIfExists(rankingPath);
        } catch (IOException e) {
            System.out.println("Erro ao limpar ranking: " + e.getMessage());
        }
    }
    private List<RankingEntry> parseRankingJson(String json) {
        List<RankingEntry> ranking = new ArrayList<>();
        if (json.isEmpty() || json.equals("[]")) return ranking;
        json = json.trim();
        if (json.startsWith("[")) json = json.substring(1);
        if (json.endsWith("]")) json = json.substring(0, json.length() - 1);
        int index = 0;
        while (index < json.length()) {
            int start = json.indexOf('{', index);
            if (start < 0) break;
            int end = json.indexOf('}', start);
            if (end < 0) break;
            String object = json.substring(start + 1, end);
            String name = null;
            Integer score = null;
            Dificuldade dificuldade = Dificuldade.MEDIO;
            Integer passageirosColetados = 0;
            String dataHora = "";
            long tempoJogo = 0;
            for (String part : object.split(",")) {
                String[] pair = part.split(":", 2);
                if (pair.length != 2) continue;
                String key = pair[0].trim().replaceAll("\"", "");
                String value = pair[1].trim();
                if (key.equals("name")) {
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        name = value.substring(1, value.length() - 1).replace("\\\"", "\"");
                    }
                } else if (key.equals("score")) {
                    try { score = Integer.parseInt(value); } catch (NumberFormatException ignored) {}
                } else if (key.equals("dificuldade")) {
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        dificuldade = Dificuldade.deString(value.substring(1, value.length() - 1));
                    }
                } else if (key.equals("passageirosColetados")) {
                    try { passageirosColetados = Integer.parseInt(value); } catch (NumberFormatException ignored) {}
                } else if (key.equals("dataHora")) {
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        dataHora = value.substring(1, value.length() - 1);
                    }
                } else if (key.equals("tempoJogo")) {
                    try { tempoJogo = Long.parseLong(value); } catch (NumberFormatException ignored) {}
                }
            }
            if (name != null && score != null) {
                ranking.add(new RankingEntry(name, score, dificuldade, passageirosColetados, dataHora, tempoJogo));
            }
            index = end + 1;
        }
        ranking.sort(Comparator.comparingInt((RankingEntry e) -> e.score).reversed());
        return ranking;
    }
}
