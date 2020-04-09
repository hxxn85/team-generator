import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class TeamGenerator extends JFrame implements ActionListener {
    private DefaultListModel<String> studentsModel;
    private JList<String> studentList;
    private ArrayList<JList<String>> teamList;
    private JComboBox<String> classComboBox;
    private JPanel teamPanel;
    private JButton generatorButton;
    private Timer repaintTimer;
    private int n;
    private Random r = new Random();

    public void launch() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Team Generator");
        this.setSize(800, 600);

        this.setLayout(new BorderLayout(10, 10));
        drawLayout(0);

        this.setLocationRelativeTo(null);
        this.setVisible(true);

        classComboBox.setSelectedIndex(0);
        repaintTimer = new Timer(1500, this);
    }

    private void drawLayout(int x) {
        JPanel studentListPanel = new JPanel(new BorderLayout());
        teamPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        JPanel commandPanel = new JPanel(new GridLayout(0, 2));

        // studentListPanel (left, east)
        studentsModel = new DefaultListModel<>();
        studentList = new JList<>(studentsModel);
        studentList.setFixedCellWidth(100);
        JScrollPane studentListPane = new JScrollPane(studentList);
        studentListPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        studentListPanel.add(studentListPane);
        studentListPanel.setBorder(new EmptyBorder(0, 10, 0, 0));

        // teamPanel (center)
        teamList = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            JList<String> team = new JList<>(new DefaultListModel<>());
            teamList.add(team);
            teamPanel.add(team);
        }

        // commandPanel (bottom, south)
        String[] classes = {"A", "B"};
        classComboBox = new JComboBox<>(classes);
        classComboBox.addActionListener(this);
        commandPanel.add(classComboBox);
        generatorButton = new JButton("시작");
        generatorButton.addActionListener(this);
        commandPanel.add(generatorButton);

        this.add(studentListPanel, BorderLayout.WEST);
        this.add(teamPanel, BorderLayout.CENTER);
        this.add(commandPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == generatorButton) {
            n = 0;
            repaintTimer.setRepeats(true);
            repaintTimer.start();
            classComboBox.setEnabled(false);
            generatorButton.setEnabled(false);
        }

        if (actionEvent.getSource() == classComboBox) {
            if (classComboBox.getSelectedIndex() == 0 && teamList.size() == 12) {
                teamList.remove(11);
                teamPanel.remove(11);
                teamPanel.repaint();
            }

            if (classComboBox.getSelectedIndex() == 1 && teamList.size() == 11) {
                JList<String> team = new JList<>(new DefaultListModel<>());
                teamList.add(team);
                teamPanel.add(team);
                teamPanel.repaint();
            }

            String fileName = Objects.requireNonNull(classComboBox.getSelectedItem()).toString();
            try {
                studentsModel.removeAllElements();
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                reader.lines().forEach(line -> studentsModel.addElement(line));

                for (JList<String> team : teamList) {
                    ((DefaultListModel<String>) team.getModel()).removeAllElements();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (actionEvent.getSource() == repaintTimer) {
            int x = r.nextInt(studentList.getModel().getSize());
            studentList.setSelectedIndex(x);
            String student = studentList.getSelectedValue();
            ((DefaultListModel<String>) studentList.getModel()).remove(x);
            ((DefaultListModel<String>) teamList.get(n).getModel()).addElement(student);
            n = (n + 1) % teamList.size();

            if (studentList.getModel().getSize() == 0) {
                repaintTimer.stop();
                classComboBox.setEnabled(true);
                generatorButton.setEnabled(true);
            }
        }
    }
}
