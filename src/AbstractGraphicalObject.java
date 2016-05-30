import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 5/26/16.
 */
public abstract class AbstractGraphicalObject implements GraphicalObject {
    private Point[] hotPoints;
    private boolean[] hotPointsSelected;
    private boolean selected;
    private List<GraphicalObjectListener> listeners;

    public AbstractGraphicalObject(Point[] hotPoints) {
        this.hotPoints = hotPoints;
        this.hotPointsSelected = new boolean[hotPoints.length];
        this.listeners = new ArrayList<>();
    }

    public Point getHotPoint(int index) {
        checkIndex(index, hotPoints.length);
        return hotPoints[index];
    }

    public Point[] getHotPoints() {
        return hotPoints;
    }


    public void setHotPoint(int index, Point p) {
        checkIndex(index, hotPoints.length);
        hotPoints[index] = p;
        notifyListeners();
    }

    public int getNumberOfHotPoints() {
        return hotPoints.length;
    }

    public double getHotPointDistance(int index, Point p) {
        checkIndex(index, hotPoints.length);
        return GeometryUtil.distanceFromPoint(hotPoints[index], p);
    }

    public boolean isHotPointSelected(int index) {
        checkIndex(index, hotPointsSelected.length);
        return hotPointsSelected[index];
    }

    public void setHotPointSelected(int index, boolean selected) {
        checkIndex(index, hotPointsSelected.length);
        hotPointsSelected[index] = selected;
        notifySelectionListeners();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        notifySelectionListeners();
    }

    public void translate(Point that) {
        for (int i = 0; i < hotPoints.length; i++) {
            hotPoints[i] = hotPoints[i].translate(that);
        }
        notifyListeners();
    }

    public void addGraphicalObjectListener(GraphicalObjectListener listener) {
        listeners.add(listener);
    }

    public void removeGraphicalObjectListener(GraphicalObjectListener listener) {
        listeners.remove(listener);
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


    private void checkIndex(int index, int size) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index is out of range");
        }
    }
}


