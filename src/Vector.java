public class Vector {
    private double x;
    private double y;
    private double z;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Vector() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString() {
        return "[ " + x + ", " + y + ", " + z + " ]";
    }

    public Vector set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector set(Vector v) {
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }

    public Vector copy() {
        return new Vector(x, y, z);
    }

    public Vector add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector add(Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public static Vector add(Vector u, Vector v) {
        return new Vector(u.x + v.x, u.y + v.y, u.z + v.z);
    }

    public Vector sub(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector sub(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector sub(Vector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public static Vector sub(Vector u, Vector v) {
        return new Vector(u.x - v.x, u.y - v.y, u.z - v.z);
    }

    public Vector mult(double n) {
        x *= n;
        y *= n;
        z *= n;
        return this;
    }

    public Vector div(double n) {
        x /= n;
        y /= n;
        z /= n;
        return this;
    }

    public double mag() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double dot(double x, double y) {
        return dot(x, y, 0);
    }

    public double dot(double x, double y, double z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public double dot(Vector v) {
        return dot(v.x, v.y, v.z);
    }

    public Vector cross(Vector v) {
        var x1 = this.y * v.z - this.z * v.y;
        var y1 = this.z * v.x - this.x * v.z;
        var z1 = this.x * v.y - this.y * v.x;
        return new Vector(x1, y1, z1);
    }

    public double dist(Vector v) {
        return v.copy().sub(this).mag();
    }

    public Vector normalize() {
        var len = mag();
        if (len != 0)
            this.mult(1 / len);
        return this;
    }

    public Vector limit(double max) {
        var magSq = x * x + y * y;
        if (magSq > max * max)
            this.div(Math.sqrt(magSq)).mult(max);
        return this;
    }

    public Vector setMag(double n) {
        return this.normalize().mult(n);
    }

    public double heading() {
        return Math.atan2(y, x);
    }

    public Vector rotate(double a) {
        var newHeading = heading() + a;
        var mag = mag();
        x = Math.cos(newHeading) * mag;
        y = Math.sin(newHeading) * mag;
        return this;
    }

    public double angleBetween(Vector v) {
        var dotmagmag = this.dot(v) / (this.mag() * v.mag());
        return Math.acos(Math.min(1, Math.max(-1, dotmagmag)));
    }

    public Vector lerp(double x, double y, double z, double amt) {
        this.x += (x - this.x) * amt;
        this.y += (y - this.y) * amt;
        this.z += (z - this.z) * amt;
        return this;
    }

    public Vector lerp(Vector v, double amt) {
        return lerp(v.x, v.y, v.z, amt);
    }

    public double[] toArray() {
        return new double[] { x, y, z };
    }

    public boolean isEqual(Vector v) {
        return x == v.x && y == v.y && z == v.z;
    }

    public static Vector fromAngle(double angle) {
        return fromAngle(angle, 1);
    }

    public static Vector fromAngle(double angle, double length) {
        return new Vector(length * Math.cos(angle), length * Math.sin(angle), 0);
    }

    public static Vector fromAngles(double theta, double phi) {
        return fromAngles(theta, phi, 1);
    }

    public static Vector fromAngles(double theta, double phi, double length) {
        var cosPhi = Math.cos(phi);
        var sinPhi = Math.sin(phi);
        var cosTheta = Math.cos(theta);
        var sinTheta = Math.sin(theta);

        return new Vector(length * sinTheta * sinPhi, -length * cosTheta, length * sinTheta * cosPhi);
    }

    public static Vector random2D() {
        return fromAngle(Math.random() * Math.PI * 2);
    }

    public static Vector random3D() {
        var angle = Math.random() * Math.PI * 2;
        var vz = Math.random() * 2 - 1;
        var vzBase = Math.sqrt(1 - vz * vz);
        var vx = vzBase * Math.cos(angle);
        var vy = vzBase * Math.sin(angle);
        return new Vector(vx, vy, vz);
    }
}
