import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoFrame extends JFrame implements ActionListener {
    Timer resizeTimer;
    Timer delayTimer;

    public LogoFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setSize(160, 120);
        this.setLocationRelativeTo(null);

        ImagePanel imagePanel = new ImagePanel();

        this.add(imagePanel);
        this.setVisible(true);
    }

    public void launch() {
        resizeTimer = new Timer(100, this);
        resizeTimer.setRepeats(true);
        resizeTimer.start();
    }

    public static void main(String[] args) {
        //new TeamGenerator().launch();
        new LogoFrame().launch();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == resizeTimer) {
            int x = this.getWidth();
            int y = this.getHeight();
            this.setSize(x + 16, y + 12);
            this.setLocationRelativeTo(null);
            this.repaint();

            if (x >= 800 || y >= 600) {
                resizeTimer.stop();

                delayTimer = new Timer(2000, this);
                delayTimer.setRepeats(false);
                delayTimer.start();
            }
        }

        if (actionEvent.getSource() == delayTimer) {
            this.setVisible(false);
            new TeamGenerator().launch();
        }
    }
}
