package uq.deco2800.dangernoodles.physics;

import uq.deco2800.dangernoodles.util.ObjectPool;

import java.util.ArrayList;
import java.util.List;

/**
 * A tree representing a rectangle that can be split into four other rectangles.
 * Allows for efficient checking of overlaps between rectangles.
 */
public class QuadTree {
    private double x;
    private double y;
    private double width;
    private double height;

    private int level;

    private ObjectPool<QuadTree> pool;

    private QuadTree[] children;
    private List<Rectangle> elements;

    private static final int MAX_OBJECTS = 10;
    private static final int MAX_LEVELS = 10;

    /**
     * Empty constructor since ObjectPool needs it
     */
    public QuadTree() {}

    /**
     * Creates a new quadtree
     * @param x The x position of the rectangle
     * @param y The y position of the rectangle
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param pool The pool the QuadTree uses to allocate more Quadtrees
     */
    public QuadTree(double x, double y, double width, double height, ObjectPool<QuadTree> pool) {
        reset(x, y, width, height, 0, pool);
    }

    /**
     * Removes all the elements from the QuadTree and then recursively removes children and returns them
     * back to the object pool.
     */
    public void clear() {
        elements.clear();
        for (int i = 0; i < children.length; ++i) {
            if (children[i] != null) {
                children[i].clear();
                pool.free(children[i]);
                children[i] = null;
            }
        }
    }

    /**
     * Reset a quadtree with new values. Used because quadtrees are pulled from the object pool, not constructed.
     * @param x The x position of the rectangle
     * @param y The y position of the rectangle
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param level How deep down into the tree the node is
     * @param pool The pool the QuadTree uses to allocate more Quadtrees
     * @return The new quadtree.
     */
    private QuadTree reset(double x, double y, double width, double height, int level, ObjectPool<QuadTree> pool) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.level = level;

        this.pool = pool;

        this.children = new QuadTree[4];
        this.elements = new ArrayList<>();

        return this;
    }

    /**
     * Split the quadtree into 4 other quadtrees.
     */
    private void split() {
        double halfWidth = this.width/2;
        double halfHeight = this.height/2;

        int subLevel = level + 1;

        children[0] = pool.alloc()
                .reset(x + halfWidth, y, halfWidth, halfHeight, subLevel, pool);
        children[1] = pool.alloc()
                .reset(x, y, halfWidth, halfHeight, subLevel, pool);
        children[2] = pool.alloc()
                .reset(x, y + halfHeight, halfWidth, halfHeight, subLevel, pool);
        children[3] = pool.alloc()
                .reset(x + halfWidth, y + halfHeight, halfWidth, halfHeight, subLevel, pool);
    }

    /**
     * Gets one of the four quadrants (or the current tree) that a rectangle fits into.
     * @param r The rectangle
     * @return The spot it fits into
     */
    private QuadTree getSpot(Rectangle r) {
        double midX = x + this.width/2;
        double midY = y + this.height/2;

        boolean fitsTop = r.getY() < midY && r.getY() + r.getHeight() < midY;
        boolean fitsBottom = r.getY() > midY;

        if (r.getX() < midX && r.getX() + r.getWidth() < midX) {
            if (fitsTop) {
                return children[1];
            } else if (fitsBottom) {
                return children[2];
            }
        } else if (r.getX() > midX) {
            if (fitsTop) {
                return children[0];
            } else if (fitsBottom) {
                return children[3];
            }
        }

        return this;
    }

    /**
     * Insert a rectangle into the tree, it will recursively travel down until it finds a spot
     * @param r The rectangle to add
     */
    public void insert(Rectangle r) {
        if (children[0] != null) {
            QuadTree spot = getSpot(r);
            if (spot != this) {
                spot.insert(r);
                return;
            }
        }

        elements.add(r);

        if (elements.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (children[0] == null) {
                split();
            }

            int i = 0;
            while (i < elements.size()) {
                QuadTree spot = getSpot(elements.get(i));
                if (spot != this) {
                    spot.insert(elements.remove(i));
                } else {
                    ++i;
                }
            }
        }
    }

    /**
     * Gets all the collisions for a certain rectangle and inserts it into the provided list
     * @param ret The list to add the collisions into
     * @param r The rectangle to test collisions on
     * @return The ret list passed in.
     */
    public List<Rectangle> getCollisions(List<Rectangle> ret, Rectangle r) {
        QuadTree spot = getSpot(r);

        if (spot != this && children[0] != null) {
            //return elements inside child
            spot.getCollisions(ret, r);
        }

        if (spot == this && children[0] != null) {
            //return all elements in all children
            getAll(ret);
        }

        ret.addAll(elements);

        return ret;
    }

    /**
     * This function will add all elements inside this quadTree and all
     * children below it to a given array
     * @param ret
     */
    public void getAll(List<Rectangle> ret) {
        ret.addAll(elements);
        if (children[0] != null) {
            for (QuadTree q : children) {
                q.getAll(ret);
            }
        }
    }

    /**
     * Remove a Rectangle from the current quadTree
     *
     * @param r
     */
    public void remove(Rectangle r) {
        if (children[0] != null) {
            QuadTree spot = getSpot(r);
            if (spot != this) {
                spot.remove(r);
                return;
            }
        }

        elements.remove(r);
    }
}