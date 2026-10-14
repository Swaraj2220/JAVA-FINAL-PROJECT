import java.util.*;

// ArrayList stores records; HashMap gives fast ID-based lookup.
public class RecruitmentManager {
    private final ArrayList<Candidate> candidates = new ArrayList<>();
    private final HashMap<String, Candidate> byId = new HashMap<>();

    public boolean add(Candidate c) {
        if (c.getId().isEmpty() || byId.containsKey(c.getId())) return false;
        candidates.add(c);
        byId.put(c.getId(), c);
        return true;
    }

    public Candidate search(String id) {
        return byId.get(id);
    }

    public boolean update(String id, String stage, String decision) {
        Candidate c = search(id);
        if (c == null) return false;
        c.setStage(stage);
        c.setDecision(decision);
        return true;
    }

    public ArrayList<Candidate> sortByName() {
        ArrayList<Candidate> list = new ArrayList<>(candidates);
        list.sort(Comparator.comparing(Candidate::getName, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    public ArrayList<Candidate> getAll() {
        return new ArrayList<>(candidates);
    }

    public int size() {
        return candidates.size();
    }
}
