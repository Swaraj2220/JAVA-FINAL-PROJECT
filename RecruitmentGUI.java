import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

// Swing GUI: JFrame = window, components = interface, listeners = events.
public class RecruitmentGUI extends JFrame {
    private final RecruitmentManager manager = new RecruitmentManager();

    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField roleField = new JTextField();
    private final JTextField skillsField = new JTextField();
    private final JTextField searchField = new JTextField();

    private final JComboBox<String> stageBox = new JComboBox<>(
            new String[]{"Applied", "Screening", "Interview", "Selected", "Rejected"});
    private final JComboBox<String> decisionBox = new JComboBox<>(
            new String[]{"Pending", "Selected", "Rejected"});

    private final JTextArea output = new JTextArea();
    private final JLabel countLabel = new JLabel("Candidates: 0");

    public RecruitmentGUI() {
        setTitle("Recruitment Candidate Management System");
        setSize(900, 600);
        setMinimumSize(new Dimension(800, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(18, 25, 18, 25));
        header.setBackground(new Color(30, 60, 100));

        JLabel title = new JLabel("Recruitment Candidate Management");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel subtitle = new JLabel("Manage candidates, stages and interview decisions");
        subtitle.setForeground(new Color(220, 230, 240));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel headingText = new JPanel(new GridLayout(2, 1));
        headingText.setOpaque(false);
        headingText.add(title);
        headingText.add(subtitle);
        header.add(headingText, BorderLayout.WEST);
        header.add(countLabel, BorderLayout.EAST);
        countLabel.setForeground(Color.WHITE);
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Candidate form
        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));
        form.setBorder(new CompoundBorder(
                new TitledBorder("Candidate Details"),
                new EmptyBorder(10, 10, 10, 10)));

        addRow(form, "Candidate ID", idField);
        addRow(form, "Name", nameField);
        addRow(form, "Job Role", roleField);
        addRow(form, "Skills", skillsField);
        addRow(form, "Stage", stageBox);
        addRow(form, "Decision", decisionBox);

        JButton addButton = button("Add Candidate");
        JButton clearButton = button("Clear");
        form.add(addButton);
        form.add(clearButton);

        // Search/update panel
        JPanel actions = new JPanel(new GridLayout(4, 2, 10, 10));
        actions.setBorder(new CompoundBorder(
                new TitledBorder("Search & Manage"),
                new EmptyBorder(10, 10, 10, 10)));

        actions.add(new JLabel("Search / Update ID"));
        actions.add(searchField);

        JButton searchButton = button("Search");
        JButton updateButton = button("Update");
        JButton showAllButton = button("Show All");
        JButton sortButton = button("Sort by Name");

        actions.add(searchButton);
        actions.add(updateButton);
        actions.add(showAllButton);
        actions.add(sortButton);

        JLabel tip = new JLabel("Tip: Enter an ID to search or update a candidate.");
        tip.setFont(new Font("SansSerif", Font.ITALIC, 11));
        actions.add(tip);
        actions.add(new JLabel());

        // Output
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        output.setMargin(new Insets(10, 10, 10, 10));
        output.setText("Welcome! Add a candidate or use Search / Show All.");
        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(new CompoundBorder(
                new TitledBorder("Candidate Records"),
                new EmptyBorder(5, 5, 5, 5)));

        JPanel center = new JPanel(new GridLayout(1, 2, 12, 0));
        center.setBorder(new EmptyBorder(12, 15, 12, 15));
        center.add(form);
        center.add(actions);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        // Button events
        addButton.addActionListener(e -> addCandidate());
        searchButton.addActionListener(e -> searchCandidate());
        updateButton.addActionListener(e -> updateCandidate());
        showAllButton.addActionListener(e -> showAll());
        sortButton.addActionListener(e -> sortCandidates());
        clearButton.addActionListener(e -> clearFields());
    }

    private void addRow(JPanel panel, String label, JComponent component) {
        panel.add(new JLabel(label));
        panel.add(component);
    }

    private JButton button(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        return b;
    }

    private void addCandidate() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String role = roleField.getText().trim();
        String skills = skillsField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || role.isEmpty() || skills.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all candidate details.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Candidate c = new Candidate(id, name, role, skills);
        if (manager.add(c)) {
            updateCount();
            output.setText("Candidate added successfully!\n\n" + c);
            clearInputOnly();
        } else {
            JOptionPane.showMessageDialog(this, "Candidate ID already exists.",
                    "Duplicate ID", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void searchCandidate() {
        Candidate c = manager.search(searchField.getText().trim());
        output.setText(c == null
                ? "Candidate not found."
                : "SEARCH RESULT\n\n" + c);
    }

    private void updateCandidate() {
        String id = searchField.getText().trim();
        if (manager.update(id, (String) stageBox.getSelectedItem(),
                (String) decisionBox.getSelectedItem())) {
            output.setText("Candidate updated successfully.\n\n" + manager.search(id));
        } else {
            JOptionPane.showMessageDialog(this, "Candidate ID not found.",
                    "Update Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showAll() {
        StringBuilder sb = new StringBuilder("ALL CANDIDATES\n\n");
        if (manager.size() == 0) {
            sb.append("No candidates available.");
        } else {
            for (Candidate c : manager.getAll()) sb.append(c).append("\n");
        }
        output.setText(sb.toString());
    }

    private void sortCandidates() {
        StringBuilder sb = new StringBuilder("CANDIDATES SORTED BY NAME\n\n");
        for (Candidate c : manager.sortByName()) sb.append(c).append("\n");
        output.setText(sb.toString());
    }

    private void clearInputOnly() {
        idField.setText("");
        nameField.setText("");
        roleField.setText("");
        skillsField.setText("");
    }

    private void clearFields() {
        clearInputOnly();
        searchField.setText("");
        stageBox.setSelectedIndex(0);
        decisionBox.setSelectedIndex(0);
        output.setText("Fields cleared. Ready for a new candidate.");
    }

    private void updateCount() {
        countLabel.setText("Candidates: " + manager.size());
    }

    public static void main(String[] args) {
        // Starts Swing GUI on the event dispatch thread.
        SwingUtilities.invokeLater(() -> new RecruitmentGUI().setVisible(true));
    }
}
