// Represents one recruitment candidate.
public class Candidate {
    private String id, name, role, skills, stage, decision; // Encapsulation

    public Candidate(String id, String name, String role, String skills) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.skills = skills;
        this.stage = "Applied";
        this.decision = "Pending";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getSkills() { return skills; }
    public String getStage() { return stage; }
    public String getDecision() { return decision; }

    public void setStage(String stage) { this.stage = stage; }
    public void setDecision(String decision) { this.decision = decision; }

    @Override
    public String toString() {
        return id + " | " + name + " | " + role + " | " + skills
                + " | " + stage + " | " + decision;
    }
}
