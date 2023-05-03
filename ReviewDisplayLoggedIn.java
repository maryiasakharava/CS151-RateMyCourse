import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.*;
import java.util.ArrayList;

public class ReviewDisplayLoggedIn extends JFrame {
    private ArrayList<String[]> reviews2;
    private JTable display;
    JColorChooser c, c1;

    Font f, f1;
    private String searchEntry;

    public ReviewDisplayLoggedIn(String se) throws PrinterException {
        searchEntry = se;

        setSize(800, 800);
        setUndecorated(true);
        setVisible(true);

        c = new JColorChooser();
        c.setColor(0, 153, 153);

        c1 = new JColorChooser();
        c1.setColor(255, 255, 204);

        f = new Font(Font.DIALOG_INPUT, Font.BOLD, 25);
        f1 = new Font(Font.DIALOG_INPUT, Font.BOLD, 18);

        reviews2 = new ArrayList<String[]>();

        backToHomePanel();
        displayPanel();

        JPanel bottom = new JPanel();
        bottom.setBackground(c.getColor());
        add(bottom, BorderLayout.SOUTH);

    }

    public void ReadInFile(String filepath) {
        //Read in the values that are in the username, password, full name, and email fields into a hashmap
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] keyValue = line.split(":");
                if(keyValue[0].equals(searchEntry))
                {
                    String[] arr = keyValue[1].split(",");
                    reviews2.add(arr);
                }
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

    }

    public void backToHomePanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(c.getColor());
        JButton backToHome = new JButton("Return to Home");
        topPanel.add(backToHome);

        JButton writeReview = new JButton("Write Review");
        topPanel.add(writeReview);

        backToHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                CourseCommentaryLoggedIn home= new CourseCommentaryLoggedIn();
            }
        });

        writeReview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Review r1 = null;
                try {
                    dispose();
                    r1 = new Review();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        add(topPanel, BorderLayout.NORTH);
    }

    public void displayPanel() throws PrinterException {
        ReadInFile("reviews.txt");

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBounds(0, 30, getWidth(), getHeight() - 15);

        display = new JTable();
        DefaultTableModel model = (DefaultTableModel) display.getModel();
        model.addColumn("Course");
        model.addColumn("Professor");
        model.addColumn("Review");
        model.addColumn("Rating");
        model.addColumn("Would Take Again");

        for(String[] rev: reviews2) {
            model.addRow(rev);
        }

        JScrollPane jp = new JScrollPane(display);
        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.setBackground(c1.getColor());
        JLabel courseName = new JLabel("Showing reviews for " + searchEntry);
        courseName.setFont(f);
        courseName.setForeground(c.getColor());
        topPanel.add(courseName, BorderLayout.CENTER);

        JPanel stats = new JPanel(new FlowLayout());
        JLabel averageRating = new JLabel("Rating: ");
        JLabel takeAgain = new JLabel("Would take again: ");
        averageRating.setFont(f);
        averageRating.setForeground(c.getColor());
        takeAgain.setFont(f);
        takeAgain.setForeground(c.getColor());
        stats.add(averageRating);
        stats.setBackground(c1.getColor());
        stats.add(takeAgain);

        displayPanel.add(stats, BorderLayout.SOUTH);
        displayPanel.add(topPanel, BorderLayout.NORTH);
        displayPanel.add(jp, BorderLayout.CENTER);

        add(displayPanel);
    }

}
