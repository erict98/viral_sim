import javax.swing.*;

public class GUI extends JFrame {
    private JFrame f;

    public GUI() {
        f = new JFrame();
        JButton b = new JButton("click");
        b.setBounds(130, 100, 100, 40);
        f.add(b);
        f.setSize(400,500);
        f.setLayout(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
    }
}

/**
 * This class will be the GUI for the simulation. Should support different speeds (x1, x2, ,x3)
 * Clients should be able to specify population size and other factors (% masked, enforced 6 feet)
 * Game loop for concurrency? Look at threading as well
 */