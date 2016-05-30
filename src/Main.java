import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 5/27/16.
 */
public class Main {
    public static void main(String[] args) {

        List objects = new ArrayList<>();

        objects.add(new LineSegment());
        objects.add(new Oval());


        GUI gui = new GUI(objects);
        gui.setVisible(true);

    }
}
