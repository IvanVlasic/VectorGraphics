/**
 * Created by ivan on 5/26/16.
 */
public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point translate(Point dp) {
        return new Point(x + dp.getX(), y + dp.getY());
    }

    public Point difference(Point dp) {
        return new Point(x - dp.getX(), y - dp.getY());
    }

    public Point copy() {
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Point)) {
            return false;
        }

        Point other = (Point) o;
        return x == other.x && y == other.y;
    }


}
