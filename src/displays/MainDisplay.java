package displays;

import controllers.BoardController;

import javax.swing.*;
import java.awt.*;

public class MainDisplay extends JFrame {
    /**
     * Initialize the main display.
     *
     * @param boardController Board controller to use.
     */
    public MainDisplay(BoardController boardController) {
        setTitle("Sorting");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        Container container = getContentPane();
        setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        initializeViews(boardController);
        setSystemLookAndFeel();

        pack();
        // Show frame in the center of the screen.
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initialize all the sub views for the display.
     *
     * @param boardController Board controller to use.
     */
    private void initializeViews(BoardController boardController) {
        TopControlsDisplay topControlsDisplay = new TopControlsDisplay(boardController);
        add(topControlsDisplay);

        BoardDisplay boardDisplay = new BoardDisplay(boardController);
        add(boardDisplay);

        BottomControlsDisplay bottomControlsDisplay = new BottomControlsDisplay(boardController);
        add(bottomControlsDisplay);
    }

    /**
     * Try to set the system look and feel.
     */
    private void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception exception) {
            System.out.println("Failed to apply system look and feel.");
        }
    }
}
