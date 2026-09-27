package solidexercicio10.repository;
import java.util.List;
import solidexercicio10.RankingEntry;
public interface RankingRepository {
    List<RankingEntry> load();
    void save(List<RankingEntry> ranking);
    void clear();
}
