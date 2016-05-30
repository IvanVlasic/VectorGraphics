import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * Created by ivan on 5/27/16.
 */
public class Canvas extends JComponent implements DocumentModelListener {
    private DocumentModel model;
    private GUI gui;


    public Canvas(DocumentModel model, GUI gui) {
        this.model = model;
        this.gui = gui;
        model.addDocumentModelListener(this);

        addListeners();

    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());
                gui.getCurrentState().mouseDown(p, e.isShiftDown(), e.isControlDown());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());
                gui.getCurrentState().mouseUp(p, e.isShiftDown(), e.isControlDown());
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                gui.getCurrentState().keyPressed(keyEvent.getKeyCode());
            }


        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());
                gui.getCurrentState().mouseDragged(p);
            }
        });

    }


    @Override
    protected void paintComponent(Graphics graphics) {
        requestFocus();
        Graphics2D g2d = (Graphics2D) graphics;
        Renderer r = new G2DRendererImpl(g2d);
        for (GraphicalObject object : model.list()) {
            object.render(r);
            gui.getCurrentState().afterDraw(r, object);
        }

        gui.getCurrentState().afterDraw(r);
    }

    @Override
    public void documentChange() {
        revalidate();
        repaint();
    }
}
