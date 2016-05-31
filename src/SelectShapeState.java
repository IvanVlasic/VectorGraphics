import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ivan on 5/27/16.
 */
public class SelectShapeState extends IdleState {
    private static final int HOTPOINT_R = 3;
    private DocumentModel model;


    public SelectShapeState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        if (!ctrlDown) {
            model.deSelectAll();
        }
        GraphicalObject object = model.findSelectedGraphicalObject(mousePoint);
        if (object != null) {
            object.setSelected(true);

            if (model.getSelectedObjects().size() == 1) {
                for (int i = 0; i < object.getNumberOfHotPoints(); i++) {
                    object.setHotPointSelected(i, true);
                }
            } else {
                model.deselectHotPoints(model.getSelectedObjects().get(0));
            }

        }
    }


    @Override
    public void mouseDragged(Point mousePoint) {
        if (model.getSelectedObjects().size() != 1) {
            return;
        }

        GraphicalObject object = model.getSelectedObjects().get(0);
        int index = model.findSelectedHotPoint(object, mousePoint);
        if (index != -1) {
            Point p = object.getHotPoint(index);
            Point diff = mousePoint.difference(p);
            object.setHotPoint(index, p.translate(diff));
        }

    }

    @Override
    public void keyPressed(int keyCode) {
        Iterator<GraphicalObject> iter = model.getSelectedObjects().iterator();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                while (iter.hasNext()) {
                    GraphicalObject object = iter.next();
                    object.translate(new Point(-1, 0));
                }
                break;
            case KeyEvent.VK_UP:
                while (iter.hasNext()) {
                    GraphicalObject object = iter.next();
                    object.translate(new Point(0, -1));
                }
                break;
            case KeyEvent.VK_RIGHT:
                while (iter.hasNext()) {
                    GraphicalObject object = iter.next();
                    object.translate(new Point(1, 0));
                }
                break;
            case KeyEvent.VK_DOWN:
                while (iter.hasNext()) {
                    GraphicalObject object = iter.next();
                    object.translate(new Point(0, 1));
                }
                break;
            case KeyEvent.VK_ADD:
            case KeyEvent.VK_PLUS:
                if (model.getSelectedObjects().size() == 1) {
                    model.increaseZ(model.getSelectedObjects().get(0));
                }
                break;
            case KeyEvent.VK_SUBTRACT:
            case KeyEvent.VK_MINUS:
                if (model.getSelectedObjects().size() == 1) {
                    model.decreaseZ(model.getSelectedObjects().get(0));
                }
                break;
            case KeyEvent.VK_G:
                List<GraphicalObject> objects = new ArrayList<>();
                while (iter.hasNext()) {
                    GraphicalObject object = iter.next();
                    model.removeGraphicalObject(object);
                    objects.add(object);
                }
                model.deSelectAll();
                CompositeShape shape = new CompositeShape(objects);
                model.addGraphicalObject(shape);
                shape.setSelected(true);
                break;
            case KeyEvent.VK_U:
                if (model.getSelectedObjects().size() == 1) {
                    GraphicalObject object = model.getSelectedObjects().get(0);
                    if (!(object instanceof CompositeShape)) {
                        return;
                    }
                    model.deSelectAll();
                    model.removeGraphicalObject(object);
                    for (GraphicalObject g : ((CompositeShape) object).getChildren()) {
                        model.addGraphicalObject(g);
                        g.setSelected(true);
                    }
                }


        }
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if (go.isSelected()) {
            Rectangle rect = go.getBoundingBox();
            Point p1 = new Point(rect.getX(), rect.getY());
            Point p2 = new Point(p1.getX() + rect.getWidth(), p1.getY());
            Point p3 = new Point(p2.getX(), p2.getY() + rect.getHeight());
            Point p4 = new Point(p1.getX(), p1.getY() + rect.getHeight());

            r.drawLine(p1, p2);
            r.drawLine(p2, p3);
            r.drawLine(p3, p4);
            r.drawLine(p4, p1);
        }

        for (int i = 0; i < go.getNumberOfHotPoints(); i++) {
            if (go.isHotPointSelected(i)) {
                Point p = go.getHotPoint(i);
                Point p1 = new Point(p.getX() - HOTPOINT_R, p.getY() - HOTPOINT_R);
                Point p2 = new Point(p.getX() + HOTPOINT_R, p.getY() - HOTPOINT_R);
                Point p3 = new Point(p.getX() + HOTPOINT_R, p.getY() + HOTPOINT_R);
                Point p4 = new Point(p.getX() - HOTPOINT_R, p.getY() + HOTPOINT_R);

                r.drawLine(p1, p2);
                r.drawLine(p2, p3);
                r.drawLine(p3, p4);
                r.drawLine(p4, p1);

            }
        }
    }


    @Override
    public void onLeaving() {
        model.deSelectAll();
    }
}
