import java.util.List;
import java.util.Stack;

/**
 * Created by ivan on 5/27/16.
 */
public class Oval extends AbstractGraphicalObject {
    private static final int POINTS = 20;

    public Oval() {
        super(new Point[]{new Point(10, 0), new Point(0, 10)});
    }

    public Oval(Point[] hotPoints) {
        super(hotPoints);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Point[] points = getPolygonPoints();

        int x = getHotPoint(1).getX();
        int y = getHotPoint(0).getY();

        int a = (Math.abs(x - getHotPoint(0).getX()));
        int b = (Math.abs(y - getHotPoint(1).getY()));

        double d1 = Math.pow((mousePoint.getX() - x), 2) / Math.pow(a, 2) + Math.pow((mousePoint.getY() - y), 2) / Math.pow
                (b, 2);

        if (d1 <= 1) {
            return 0;
        }


        double distance = GeometryUtil.distanceFromPoint(points[0], mousePoint);
        for (int i = 1; i < points.length; i++) {
            Point p = points[i % points.length];
            double d = GeometryUtil.distanceFromPoint(p, mousePoint);
            if (d < distance) {
                distance = d;
            }
        }
        return distance;
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    @Override
    public Rectangle getBoundingBox() {

        int x = getHotPoint(1).getX();
        int y = getHotPoint(0).getY();

        int a = Math.abs(x - getHotPoint(0).getX());
        int b = Math.abs(y - getHotPoint(1).getY());

        int px = x - a;
        int py = y - b;

        return new Rectangle(px, py, a * 2, b * 2);
    }

    @Override
    public GraphicalObject duplicate() {
        Point[] points = new Point[2];
        points[0] = getHotPoint(0).copy();
        points[1] = getHotPoint(1).copy();

        return new Oval(points);
    }

    @Override
    public void render(Renderer r) {
        Point[] points = getPolygonPoints();
        r.fillPolygon(points);

    }

    private Point[] getPolygonPoints() {
        Point[] points = new Point[POINTS];

        int x = getHotPoint(1).getX();
        int y = getHotPoint(0).getY();

        int a = (Math.abs(x - getHotPoint(0).getX()));
        int b = (Math.abs(y - getHotPoint(1).getY()));

        for (int i = 0; i < POINTS; i++) {
            int px = x + (int) (a * Math.cos(2 * Math.PI * i / POINTS));
            int py = y + (int) (b * Math.sin(2 * Math.PI * i / POINTS));
            points[i] = new Point(px, py);
        }

        return points;
    }

    @Override
    public String getShapeID() {
        return "@OVAL";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] parts = data.split(" ");
        Point p1 = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        Point p2 = new Point(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));

        stack.push(new Oval(new Point[]{p1, p2}));
    }

    @Override
    public void save(List<String> rows) {
        StringBuilder b = new StringBuilder();
        b.append(getShapeID());
        b.append(" ");
        for (int i = 0;  i < getNumberOfHotPoints(); i++) {
            Point p = getHotPoint(i);
            b.append(p.getX() + " " + p.getY() + " ");
        }

        rows.add(b.toString());
    }
}




