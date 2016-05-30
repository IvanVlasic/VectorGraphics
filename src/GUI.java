import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

/**
 * Created by ivan on 5/27/16.
 */
public class GUI extends JFrame {
    private List<GraphicalObject> objects;
    private DocumentModel model;
    private Canvas canvas;
    private State currentState;

    public GUI(List<GraphicalObject> objects) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 480);
        this.objects = objects;
        setLocation(new Point(400, 200));

        currentState = new IdleState();
        model = new DocumentModel();

        canvas = new Canvas(model, this);
        add(canvas);

        JToolBar toolbar = new JToolBar();
        addButtons(toolbar);
        add(toolbar, BorderLayout.PAGE_START);

    }


    private void addButtons(JToolBar toolbar) {
        toolbar.setPreferredSize(new Dimension(getWidth(), 30));

        Action exportSvg = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String fileName = chooseName();
                SVGRendererImpl r = new SVGRendererImpl(fileName);
                for (GraphicalObject object : model.list()) {
                    object.render(r);
                }
                r.close();
            }

        };

        Action loadAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Map<String, GraphicalObject> graphObjects = new HashMap<>();
                for (GraphicalObject object : objects) {
                    graphObjects.put(object.getShapeID(), object);
                }
                CompositeShape comp = new CompositeShape(null);
                graphObjects.put(comp.getShapeID(), comp);

                JFileChooser chooser = new JFileChooser();
                int rval = chooser.showOpenDialog(GUI.this);
                if (rval == JFileChooser.APPROVE_OPTION) {
                    String fileName = chooser.getSelectedFile().toString();
                    Stack<GraphicalObject> stack = new Stack<>();
                    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(" ", 2);
                            graphObjects.get(parts[0]).load(stack, parts[1]);
                        }

                        for (GraphicalObject object: stack) {
                            model.addGraphicalObject(object);
                        }
                    } catch (IOException e) {
                        System.err.println("Couldn't read from file " + fileName);
                    }
                }

            }
        };

        Action saveAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String fileName = chooseName();
                List<String> rows = new ArrayList<>();
                for (GraphicalObject object : model.list()) {
                    object.save(rows);
                }

                try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
                    for (String row : rows) {
                        writer.println(row);
                    }
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Couldn't write to file " + fileName);
                }

            }
        };

        Action selectAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                currentState.onLeaving();
                currentState = new SelectShapeState(model);
            }
        };
        Action deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                currentState = new EraserState(model);

            }
        };

        class ShapeAction extends AbstractAction {

            private GraphicalObject go;

            public ShapeAction(GraphicalObject go) {
                this.go = go;
            }

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                currentState.onLeaving();
                currentState = new AddShapeState(go, model);
            }
        }

        loadAction.putValue(Action.NAME, "Učitaj");
        toolbar.add(loadAction);
        saveAction.putValue(Action.NAME, "Pohrani");
        toolbar.add(saveAction);
        exportSvg.putValue(Action.NAME, "SVG export");
        toolbar.add(exportSvg);

        for (GraphicalObject obj : objects) {
            Action action = new ShapeAction(obj);
            action.putValue(Action.NAME, obj.getShapeName());
            toolbar.add(action);
        }

        selectAction.putValue(Action.NAME, "Selektiraj");
        toolbar.add(selectAction);
        deleteAction.putValue(Action.NAME, "Brisalo");
        toolbar.add(deleteAction);



    }

    public State getCurrentState() {
        return currentState;
    }

    private String chooseName() {
        JFileChooser chooser = new JFileChooser();
        int rVal = chooser.showSaveDialog(GUI.this);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            String fileName = chooser.getSelectedFile() + ".txt";
            return fileName;
        }
        return null;


    }


}
