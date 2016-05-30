import java.util.*;

/**
 * Created by ivan on 5/27/16.
 */
public class CompositeShape implements GraphicalObject {
    private List<GraphicalObject> children;
    private boolean selected;
    private List<GraphicalObjectListener> listeners;


    public CompositeShape(List<GraphicalObject> objects) {
        this.children = objects;
        listeners = new ArrayList<>();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        notifySelectionListeners();
    }

    @Override
    public int getNumberOfHotPoints() {
        return 0;
    }

    @Override
    public Point getHotPoint(int index) {
        return null;
    }

    @Override
    public void setHotPoint(int index, Point point) {

    }

    @Override
    public Point[] getHotPoints() {
        List<Point> points = new ArrayList<>();
        for (GraphicalObject object : children) {
            points.addAll(Arrays.asList(object.getHotPoints()));
        }

        return points.toArray(new Point[points.size()]);
    }

    @Override
    public boolean isHotPointSelected(int index) {
        return false;
    }

    @Override
    public void setHotPointSelected(int index, boolean selected) {

    }

    @Override
    public double getHotPointDistance(int index, Point mousePoint) {
        return 0;
    }

    @Override
    public void translate(Point delta) {
        for (GraphicalObject object: children) {
            object.translate(delta);
        }

    }

    @Override
    public Rectangle getBoundingBox() {
        int[] corners = getCorners();
        int width = corners[1] - corners[0];
        int height = corners[3] - corners[2];
        return new Rectangle(corners[0], corners[2], width, height);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        int[] corners = getCorners();
        int width = corners[1] - corners[0];
        int height = corners[3] - corners[2];

        int x = mousePoint.getX();
        int y = mousePoint.getY();

        double cx = Math.max(Math.min(x, corners[0] + width), corners[0]);
        double cy = Math.max(Math.min(y, corners[2] + height), corners[2]);

        return Math.sqrt( Math.pow(x - cx, 2) + Math.pow(y - cy, 2) );
    }

    @Override
    public void render(Renderer r) {
        for (GraphicalObject object : children) {
            object.render(r);
        }
    }

    @Override
    public void addGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.add(l);

    }

    @Override
    public void removeGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.remove(l);
    }

    @Override
    public String getShapeName() {
        return "Comp";
    }

    @Override
    public GraphicalObject duplicate() {
        List<GraphicalObject> objects = new ArrayList<>();
        for (GraphicalObject object : children) {
            objects.add(object.duplicate());
        }

        return new CompositeShape(objects);
    }

    @Override
    public String getShapeID() {
        return "@COMP";
    }

    private int[] getCorners() {
        Point[] points = getHotPoints();
        int xmin = points[0].getX();
        int xmax = points[0].getX();
        int ymin = points[0].getY();
        int ymax = points[0].getY();

        for (int i = 1; i < points.length; i++) {
            if (xmin > points[i].getX()) xmin = points[i].getX();
            if (xmax < points[i].getX()) xmax = points[i].getX();
            if (ymin > points[i].getY()) ymin = points[i].getY();
            if (ymax < points[i].getY()) ymax = points[i].getY();
        }

        return new int[]{xmin, xmax, ymin, ymax};
    }

    public List<GraphicalObject> getChildren() {
        return children;
    }

    protected void notifyListeners() {
        for (GraphicalObjectListener listener : listeners) {
            listener.graphicalObjectChanged(this);
        }

    }

    protected void notifySelectionListeners() {
        for (GraphicalObjectListener listener : listeners) {
            listener.graphicalObjectSelectionChanged(this);
        }
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        List<GraphicalObject> objects = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(data); i++) {
            objects.add(stack.pop());
        }
        Collections.reverse(objects);
        stack.push(new CompositeShape(objects));
    }

    @Override
    public void save(List<String> rows) {
        String row = getShapeID() + " " + children.size();
        rows.add(row);
    }
}
