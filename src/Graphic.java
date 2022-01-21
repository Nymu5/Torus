import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Graphic extends JPanel {
    static int ny_height;
    static int ny_width;
    static int dot_size = 5;
    final boolean show_dots = false;

    public static void ny_setHeight(int value) {
        ny_height = value;
    }
    public static void ny_setWidth(int value) {
        ny_width = value;
    }

    public static void main(String[] args) {
        Graphic graphic = new Graphic();
        JFrame frame = new JFrame("Nymu5 Donut");
        showOnScreen(0, frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,1000);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize((int)ny_width,(int)ny_height);
        frame.setUndecorated(true);
        frame.add(graphic);
        frame.setVisible(true);
    }

    public static void showOnScreen( int screen, JFrame frame ) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if( screen > -1 && screen < gd.length ) {
            frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, frame.getY());
        } else if( gd.length > 0 ) {
            frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
        } else {
            throw new RuntimeException( "No Screens Found" );
        }
        ny_width = gd[screen].getDisplayMode().getWidth();
        ny_height = gd[screen].getDisplayMode().getHeight();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        g2d.setColor(Color.RED);
        g2d.drawLine(0,ny_height/2, ny_width,ny_height/2);
        g2d.drawLine(ny_width/2,0,ny_width/2, ny_height);

        if (ny_height <= ny_width) {
            g2d.drawLine(ny_convert(-ny_height/2, true), ny_convert(ny_height/2, false), ny_convert(ny_height/2, true), 0);
        } else {
            g2d.drawLine(ny_convert(-ny_width/2, true), ny_convert(ny_width/2, false), 0, ny_convert(ny_width/2, true));
        }

        int radius_torus = 100;
        int radius = 400;
        int segments_2d = 12;
        double angle_2d = 2*Math.PI/segments_2d;
        int segments_3d = 12;
        double angle_3d = 2*Math.PI/segments_3d;
        Point3D[][] points_3d_array = new Point3D[segments_3d][segments_2d];
        for (int r=0; r<segments_3d; r++) {
            for (int i=0; i<segments_2d; i++) {
                double temp_x = radius_torus*Math.cos(i*angle_2d)+radius;
                double temp_y = 0;
                double temp_z = radius_torus*Math.sin(i*angle_2d);
                Point3D temp_point3d = new Point3D(temp_x, temp_y, temp_z);
                temp_point3d.rotate(angle_3d*r);
                points_3d_array[r][i] = temp_point3d;
            }
        }

        Point2D[][] points_2d_array = new Point2D[segments_3d][segments_2d];
        for (int r = 0; r<points_3d_array.length; r++) {
            for (int i = 0; i<points_3d_array[r].length; i++) {
                points_2d_array[r][i] = points_3d_array[r][i].project();
            }
        }

        if (show_dots) {
            for (int r = 0; r<points_2d_array.length; r++) {
                for (int i = 0; i<points_2d_array[r].length; i++) {
                    g2d.setColor(Color.GREEN);
                    g2d.fillOval(ny_convert(points_2d_array[r][i].x-((dot_size-1)-4), true), ny_convert(points_2d_array[r][i].z-((dot_size-1)-4), false), dot_size, dot_size);
                }
            }
        }


        for (int r = 0; r<points_2d_array.length; r++) {
            for (int i = 0; i<points_2d_array[r].length; i++) {
                if (i==0) {
                    g2d.setColor(Color.BLUE);
                    g2d.drawLine(ny_convert(points_2d_array[r][points_2d_array[r].length-1].x, true), ny_convert(points_2d_array[r][points_2d_array[r].length-1].z, false), ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
                } else {
                    g2d.setColor(Color.BLUE);
                    g2d.drawLine(ny_convert(points_2d_array[r][i-1].x, true), ny_convert(points_2d_array[r][i-1].z, false), ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
                }
            }
        }

        for (int i = 0; i<points_2d_array[0].length; i++) {
            for (int r = 0; r<points_2d_array.length; r++) {
                if (r==0) {
                    g2d.setColor(Color.BLUE);
                    g2d.drawLine(ny_convert(points_2d_array[points_2d_array.length-1][i].x, true), ny_convert(points_2d_array[points_2d_array.length-1][i].z, false), ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
                } else {
                    g2d.setColor(Color.BLUE);
                    g2d.drawLine(ny_convert(points_2d_array[r-1][i].x, true), ny_convert(points_2d_array[r-1][i].z, false), ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
                }
            }
        }

    }

    public static Dimension getInnerSize(Frame frame) {
        Dimension size = frame.getSize();
        Insets insets = frame.getInsets();
        if (insets != null) {
            size.height -= insets.top + insets.bottom;
            size.width -= insets.left + insets.right;
        }
        return size;
    }

    public static int ny_convert(double value, boolean x_axis) {
        if (x_axis) {
            return (int)(ny_width/2+value);
        } else {
            return (int)(ny_height/2+value);
        }
    }
}
