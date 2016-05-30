import java.util.List;
import java.util.Stack;

/**
 * Created by ivan on 5/27/16.
 */
public class LineSegment extends AbstractGraphicalObject {

    public LineSegment() {
        super(new Point[]{new Point(0, 0), new Point(10, 0)});
    }

    public LineSegment(Point[] hotPoints) {
        super(hotPoints);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
    }

    @Override
    public GraphicalObject duplicate() {
        Point[] points = new Point[2];
        points[0] = getHotPoint(0).copy();
        points[1] = getHotPoint(1).copy();

        return new LineSegment(points);
    }

    @Override
    public Rectangle getBoundingBox() {
        Point p1 = getHotPoint(0);
        Point p2 = getHotPoint(1);

        int x = p1.getX() < p2.getX() ? p1.getX() : p2.getX();
        int y = p1.getY() < p2.getY() ? p1.getY() : p2.getY();

        int dx = Math.abs(getHotPoint(0).getX() - getHotPoint(1).getX());
        int dy = Math.abs(getHotPoint(0).getY() - getHotPoint(1).getY());

        return new Rectangle(x, y, dx, dy);
    }

    @Override
    public String getShapeName() {
        return "Linija";
    }

    @Override
    public void render(Renderer r) {
        r.drawLine(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String getShapeID() {
        return "@LINE";
    }

    @Override
    public void save(List<String> rows) {
        StringBuilder b = new StringBuilder();
        b.append(getShapeID());
        b.append(" ");
        for (int i = 0; i < getNumberOfHotPoints(); i++) {
            Point p = getHotPoint(i);
            b.append(p.getX() + " " + p.getY() + " ");
        }

        rows.add(b.toString());
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] parts = data.split(" ");
        Point p1 = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        Point p2 = new Point(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));

        stack.push(new LineSegment(new Point[]{p1, p2}));

    }
}
