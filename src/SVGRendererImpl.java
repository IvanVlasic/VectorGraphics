import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 5/27/16.
 */
public class SVGRendererImpl implements Renderer {

    private List<String> lines = new ArrayList<>();
    private String fileName;


    public SVGRendererImpl(String fileName) {
        this.fileName = fileName;
        lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
    }

    public void close() {
        lines.add("</svg>");

        try(PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
            for (String line : lines) {
                writer.println(line);
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Couldn't write to file " + fileName);
        }

    }

    @Override
    public void drawLine(Point s, Point e) {
        lines.add("<line x1=\"" + s.getX() +"\" y1=\"" + s.getY() + "\" x2=\"" + e.getX() + "\" y2=\"" + e.getY() +
                "\" style=\"stroke:#0000ff;\"/>");

    }

    @Override
    public void fillPolygon(Point[] points) {
        StringBuilder b = new StringBuilder();
        b.append("<polygon points=\"");
        for (Point point : points) {
            b.append(point.getX() + "," + point.getY());
            b.append(" ");
        }
        b.setLength(b.length() - 1);
        b.append("\" style=\"stroke:#ff0000; fill:#0000ff;\"/>");

        lines.add(b.toString());
    }
}
