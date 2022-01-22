import static java.lang.Math.*;

public class Point3D {
    double x;
    double y;
    double z;

    public Point3D(double x, double  y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point2D project() {
        double temp_x = this.x;
        double temp_z = -this.z;

        double temp_y = (sqrt(2)/4)*this.y;

        //System.out.println(temp_x);
        //System.out.println(temp_y);
        //System.out.println(temp_z);

        Point2D temp_point = new Point2D(0,0);
        if (y < 0) {
            temp_point = new Point2D(temp_x+temp_y, temp_z-temp_y);
        } else if (y > 0) {
            temp_point = new Point2D(temp_x+temp_y, temp_z-temp_y);
        } else {
            temp_point = new Point2D(temp_x, temp_z);
        }

        return temp_point;
    }

    public void rotate(double angle) {
        this.y = this.x*Math.sin(angle);
        this.x = this.x*Math.cos(angle);
    }

    public double angle(Point3D point) {
        double angle = Math.acos((this.x*point.x + this.y*point.y + this.z*point.z)/(Math.sqrt(Math.pow(this.x, 2)+Math.pow(this.y, 2)+Math.pow(this.z, 2))*Math.sqrt(Math.pow(point.x, 2)+Math.pow(point.y, 2)+Math.pow(point.z, 2))));
        return angle;
    }

    public Point3D perpendicular(Point3D point) {
        Point3D perpendicular;
        perpendicular = new Point3D(this.y*point.z - this.z*point.y, this.z*point.x-this.x*point.z, this.x*point.y-this.y*point.x);
        return perpendicular;
    }
}
