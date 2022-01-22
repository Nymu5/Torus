import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
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
        //g2d.drawLine(0,ny_height/2, ny_width,ny_height/2);
        //g2d.drawLine(ny_width/2,0,ny_width/2, ny_height);

        if (ny_height <= ny_width) {
            //g2d.drawLine(ny_convert(-ny_height/2, true), ny_convert(ny_height/2, false), ny_convert(ny_height/2, true), 0);
        } else {
            //g2d.drawLine(ny_convert(-ny_width/2, true), ny_convert(ny_width/2, false), 0, ny_convert(ny_width/2, true));
        }

        //g2d.setColor(Color.CYAN);
        Point3D view_point = new Point3D(-Math.sqrt(2), 4, -Math.sqrt(2));
        //g2d.drawOval(ny_convert(view_point.project().x, true), ny_convert(view_point.project().z, false), 10, 10);
        //g2d.drawOval(ny_convert(0, true), ny_convert(0, false), 10, 10);

        int rgb_r = 255;
        int rgb_g = 0;
        int rgb_b = 0;
        boolean draw_net = false;
        int radius_torus = 200;
        int radius = 300;
        int segments_2d = 360;
        double angle_2d = 2*Math.PI/segments_2d;
        int segments_3d = 360;
        double angle_3d = 2*Math.PI/segments_3d;
        //segments_2d = segments_2d/2;
        //segments_3d = segments_3d/2;
        Point3D[][] points_3d_array = new Point3D[segments_3d][segments_2d];
        Point2D[][] points_2d_array = new Point2D[segments_3d][segments_2d];
        Path2D[][][] path_2d_array = new Path2D[segments_3d][segments_2d][2];
        for (int r=0; r<segments_3d; r++) {
            for (int i=0; i<segments_2d; i++) {
                double temp_x = radius_torus*Math.cos(i*angle_2d-Math.toRadians(-90))+radius;
                double temp_y = 0;
                double temp_z = radius_torus*Math.sin(i*angle_2d-Math.toRadians(-90));
                Point3D temp_point3d = new Point3D(temp_x, temp_y, temp_z);
                temp_point3d.rotate(angle_3d*r-Math.toRadians(0));
                points_3d_array[r][i] = temp_point3d;
                points_2d_array[r][i] = points_3d_array[r][i].project();
            }
        }

        for (int r=0; r<segments_3d; r++) {
            for (int i = 0; i < segments_2d; i++) {
                //defaults
                int set_r = r-1;
                int set_i = i-1;


                if (r == 0) {
                    set_r = segments_3d-1;
                }
                if (i == 0) {
                    set_i = segments_2d-1;
                }

                ArrayList<Color> colors = new ArrayList<Color>();
                colors.add(Color.GREEN);
                colors.add(Color.BLUE);

                path_2d_array[r][i][0] = new Path2D.Double();
                path_2d_array[r][i][0].moveTo(ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
                path_2d_array[r][i][0].lineTo(ny_convert(points_2d_array[set_r][i].x, true), ny_convert(points_2d_array[set_r][i].z, false));
                path_2d_array[r][i][0].lineTo(ny_convert(points_2d_array[set_r][set_i].x, true), ny_convert(points_2d_array[set_r][set_i].z, false));
                path_2d_array[r][i][0].lineTo(ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
                path_2d_array[r][i][0].closePath();

                Point3D temp_1 = new Point3D(points_3d_array[r][i].x-points_3d_array[set_r][i].x, points_3d_array[r][i].y-points_3d_array[set_r][i].y, points_3d_array[r][i].z-points_3d_array[set_r][i].z);
                Point3D temp_2 = new Point3D(points_3d_array[r][i].x-points_3d_array[set_r][set_i].x, points_3d_array[r][i].y-points_3d_array[set_r][set_i].y, points_3d_array[r][i].z-points_3d_array[set_r][set_i].z);
                double angle_1 = view_point.angle(temp_1.perpendicular(temp_2));
                //System.out.println(Math.toDegrees(.5*Math.PI));
                if (angle_1 >= .5*Math.PI) {
                    g2d.setColor(new Color((int) ((angle_1/Math.PI)*rgb_r), (int) ((angle_1/Math.PI)*rgb_g), (int) ((angle_1/Math.PI)*rgb_b)));
                    g2d.fill(path_2d_array[r][i][0]);
                    if (draw_net) {
                        g2d.setColor(Color.BLACK);
                        g2d.draw(path_2d_array[r][i][0]);
                    }

                    //System.out.println(Math.toDegrees(angle_1));
                } else {
                    g2d.setColor(Color.RED);
                    //g2d.fill(path_2d_array[r][i][0]);
                }

                path_2d_array[r][i][1] = new Path2D.Double();
                path_2d_array[r][i][1].moveTo(ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
                path_2d_array[r][i][1].lineTo(ny_convert(points_2d_array[set_r][set_i].x, true), ny_convert(points_2d_array[set_r][set_i].z, false));
                path_2d_array[r][i][1].lineTo(ny_convert(points_2d_array[r][set_i].x, true), ny_convert(points_2d_array[r][set_i].z, false));
                path_2d_array[r][i][1].lineTo(ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
                path_2d_array[r][i][1].closePath();

                Point3D temp_3 = new Point3D(points_3d_array[r][i].x-points_3d_array[r][set_i].x, points_3d_array[r][i].y-points_3d_array[r][set_i].y, points_3d_array[r][i].z-points_3d_array[r][set_i].z);
                Point3D temp_4 = new Point3D(points_3d_array[r][i].x-points_3d_array[set_r][set_i].x, points_3d_array[r][i].y-points_3d_array[set_r][set_i].y, points_3d_array[r][i].z-points_3d_array[set_r][set_i].z);
                double angle_2 = view_point.angle(temp_3.perpendicular(temp_4));
                if (angle_2 <= .5*Math.PI) {
                    //g2d.setColor(new Color((int) ((angle_2/Math.PI)*255), (int) ((angle_2/Math.PI)*255), (int) ((angle_2/Math.PI)*255)));
                    g2d.setColor(new Color((int) ((angle_1/Math.PI)*rgb_r), (int) ((angle_1/Math.PI)*rgb_g), (int) ((angle_1/Math.PI)*rgb_b)));
                    g2d.fill(path_2d_array[r][i][1]);
                    if (draw_net) {
                        g2d.setColor(Color.BLACK);
                        g2d.draw(path_2d_array[r][i][1]);
                    }

                } else {
                    g2d.setColor(Color.RED);
                    //g2d.fill(path_2d_array[r][i][1]);
                }
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

        //g2d.setColor(Color.RED);
        //g2d.drawLine(ny_width/2,0,ny_width/2, ny_height/2);
        //g2d.drawLine(ny_convert(-radius, true),ny_height/2, ny_convert(0,true), ny_height/2);
        //g2d.drawLine(ny_convert(radius, true),ny_height/2, ny_width, ny_height/2);

        //for (int i = 0; i<points_2d_array[0].length; i++) {
        //    for (int r = 0; r<points_2d_array.length; r++) {
        //        g2d.setColor(Color.BLUE);
        //        if (r==0) {
        //            g2d.drawLine(ny_convert(points_2d_array[points_2d_array.length-1][i].x, true), ny_convert(points_2d_array[points_2d_array.length-1][i].z, false), ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
        //        } else {
        //            g2d.drawLine(ny_convert(points_2d_array[r-1][i].x, true), ny_convert(points_2d_array[r-1][i].z, false), ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
        //        }
        //        if (i==0) {
        //            g2d.drawLine(ny_convert(points_2d_array[r][points_2d_array[r].length-1].x, true), ny_convert(points_2d_array[r][points_2d_array[r].length-1].z, false), ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
        //        } else {
        //            g2d.drawLine(ny_convert(points_2d_array[r][i-1].x, true), ny_convert(points_2d_array[r][i-1].z, false), ny_convert(points_2d_array[r][i].x, true), ny_convert(points_2d_array[r][i].z, false));
        //        }
        //    }
        //}


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
