package components;

import org.jogamp.vecmath.Vector2f;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.vecmath.Vector4f;

import tools.Util;

import ECS.Component;
import ECS.Entity;

public class BoxCollider extends Component {
    private Vector2f size;
    private float trim = 0.1f; /* between 0 and 1 */

    public static boolean collision(float radius, Vector2f position, Vector4f b) {
        Vector2f pt = new Vector2f(position);

        if (pt.x > b.x + b.z / 2) pt.x = b.x + b.z / 2;
        if (pt.x < b.x - b.z / 2) pt.x = b.x - b.z / 2;
        if (pt.y > b.y + b.w / 2) pt.y = b.y + b.w / 2;
        if (pt.y < b.y - b.w / 2) pt.y = b.y - b.w / 2;

        return Util.distance(pt, position) < radius;
    }

    public static boolean collision(Vector4f a, Vector4f b) {
        boolean xCollisionFromLeft = a.x + (a.z / 2) >= b.x - (b.z / 2);
        boolean xCollisionFromRight = b.x + (b.z / 2) >= a.x - (a.z / 2);

        boolean zCollisionFromLeft = a.y + (a.w / 2) >= b.y - (b.w / 2);
        boolean zCollisionFromRight = b.y + (b.w / 2) >= a.y - (a.w / 2);
        
        return xCollisionFromLeft && xCollisionFromRight && zCollisionFromLeft && zCollisionFromRight;
    }

    public BoxCollider(Vector2f size, Entity parent) {
        super(parent);
        this.size = size;
    }

    public Vector4f getBoundsTop(Vector3f position) {
        Vector4f bounds = new Vector4f();

        float trimX = this.trim * size.x;
        float trimZ = this.trim * size.y;

        bounds.z = size.x - trimX * 2;
        bounds.w = trimZ * 2;

        bounds.x = position.x;
        bounds.y = position.z - size.y / 2;

        return bounds;
    }

    public Vector4f getBoundsBottom(Vector3f position) {
        Vector4f bounds = new Vector4f();

        float trimX = this.trim * size.x;
        float trimZ = this.trim * size.y;

        bounds.z = size.x - trimX * 2;
        bounds.w = trimZ * 2;

        bounds.x = position.x;
        bounds.y = position.z + size.y / 2;

        return bounds;
    }

    public Vector4f getBoundsRight(Vector3f position) {
        Vector4f bounds = new Vector4f();

        float trimX = this.trim * size.x;
        float trimZ = this.trim * size.y;

        bounds.z = trimX * 2;
        bounds.w = size.y - trimZ * 2;

        bounds.x = position.x + size.x / 2;
        bounds.y = position.z;

        return bounds;
    }

    public Vector4f getBoundsLeft(Vector3f position) {
        Vector4f bounds = new Vector4f();

        float trimX = this.trim * size.x;
        float trimZ = this.trim * size.y;

        bounds.z = trimX * 2;
        bounds.w = size.y - trimZ * 2;

        bounds.x = position.x - size.x / 2;
        bounds.y = position.z;

        return bounds;
    }

    public Vector4f getBounds(Vector3f position) {
        return new Vector4f(position.x, position.z, size.x, size.y);
    }

    public void update() {
    }
}
