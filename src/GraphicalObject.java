import java.util.List;
import java.util.Stack;

/**
 * Created by ivan on 5/26/16.
 */
public interface GraphicalObject {


    boolean isSelected();

    void setSelected(boolean selected);

    int getNumberOfHotPoints();

    Point getHotPoint(int index);

    void setHotPoint(int index, Point point);

    Point[] getHotPoints();

    boolean isHotPointSelected(int index);

    void setHotPointSelected(int index, boolean selected);

    double getHotPointDistance(int index, Point mousePoint);

    void translate(Point delta);

    Rectangle getBoundingBox();

    double selectionDistance(Point mousePoint);

   void render(Renderer r);

    void addGraphicalObjectListener(GraphicalObjectListener l);

    void removeGraphicalObjectListener(GraphicalObjectListener l);

    String getShapeName();

    GraphicalObject duplicate();

    String getShapeID();

    void load(Stack<GraphicalObject> stack, String data);

    void save(List<String> rows);

    boolean doesCollide(List<GraphicalObject> segments);
}
