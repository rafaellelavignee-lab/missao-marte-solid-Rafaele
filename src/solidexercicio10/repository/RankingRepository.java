package solidexercicio10.repository;
import java.util.List;
public interface RankingRepository {
    List<RankingEntry> load();
    void save(List<RankingEntry> ranking);
    void clear();
}
