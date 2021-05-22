package io.anuke.ucore.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

/* renamed from: io.anuke.ucore.util.QuadTree */
public class QuadTree<T extends QuadTreeObject> {
    private static Rectangle tmp = new Rectangle();
    private QuadTree<T> bottomLeftChild;
    private QuadTree<T> bottomRightChild;
    private Rectangle bounds;
    private boolean leaf;
    private int level;
    private int maxObjectsPerNode;
    private Array<T> objects;
    private QuadTree<T> topLeftChild;
    private QuadTree<T> topRightChild;

    /* renamed from: io.anuke.ucore.util.QuadTree$QuadTreeObject */
    public interface QuadTreeObject {
        void getBoundingBox(Rectangle rectangle);
    }

    public QuadTree(int maxObjectsPerNode2, Rectangle bounds2) {
        this(maxObjectsPerNode2, 0, bounds2);
    }

    private QuadTree(int maxObjectsPerNode2, int level2, Rectangle bounds2) {
        this.level = level2;
        this.bounds = bounds2;
        this.maxObjectsPerNode = maxObjectsPerNode2;
        this.objects = new Array<>();
        this.leaf = true;
    }

    private void split() {
        if (this.leaf) {
            float subW = this.bounds.width / 2.0f;
            float subH = this.bounds.height / 2.0f;
            this.leaf = false;
            this.bottomLeftChild = new QuadTree<>(this.maxObjectsPerNode, this.level + 1, new Rectangle(this.bounds.x, this.bounds.y, subW, subH));
            this.bottomRightChild = new QuadTree<>(this.maxObjectsPerNode, this.level + 1, new Rectangle(this.bounds.x + subW, this.bounds.y, subW, subH));
            this.topLeftChild = new QuadTree<>(this.maxObjectsPerNode, this.level + 1, new Rectangle(this.bounds.x, this.bounds.y + subH, subW, subH));
            this.topRightChild = new QuadTree<>(this.maxObjectsPerNode, this.level + 1, new Rectangle(this.bounds.x + subW, this.bounds.y + subH, subW, subH));
            Iterator<T> iterator = this.objects.iterator();
            while (iterator.hasNext()) {
                T obj = iterator.next();
                obj.getBoundingBox(tmp);
                QuadTree<T> child = getFittingChild(tmp);
                if (child != null) {
                    child.insert(obj);
                    iterator.remove();
                }
            }
        }
    }

    private void unsplit() {
        if (!this.leaf) {
            this.leaf = true;
            this.objects.addAll(this.bottomLeftChild.objects);
            this.objects.addAll(this.bottomRightChild.objects);
            this.objects.addAll(this.topLeftChild.objects);
            this.objects.addAll(this.topRightChild.objects);
            this.topRightChild = null;
            this.topLeftChild = null;
            this.bottomRightChild = null;
            this.bottomLeftChild = null;
        }
    }

    public void insert(T obj) {
        obj.getBoundingBox(tmp);
        if (this.bounds.overlaps(tmp)) {
            if (this.leaf && this.objects.size + 1 > this.maxObjectsPerNode) {
                split();
            }
            if (this.leaf) {
                this.objects.add(obj);
                return;
            }
            obj.getBoundingBox(tmp);
            QuadTree<T> child = getFittingChild(tmp);
            if (child != null) {
                child.insert(obj);
            } else {
                this.objects.add(obj);
            }
        }
    }

    public void remove(T obj) {
        if (this.leaf) {
            this.objects.removeValue(obj, true);
            return;
        }
        obj.getBoundingBox(tmp);
        QuadTree<T> child = getFittingChild(tmp);
        if (child != null) {
            child.remove(obj);
        } else {
            this.objects.removeValue(obj, true);
        }
        if (getTotalObjectCount() <= this.maxObjectsPerNode) {
            unsplit();
        }
    }

    public void clear() {
        this.objects.clear();
        if (this.bottomLeftChild != null) {
            this.bottomLeftChild.clear();
        }
        if (this.bottomRightChild != null) {
            this.bottomRightChild.clear();
        }
        if (this.topLeftChild != null) {
            this.topLeftChild.clear();
        }
        if (this.topRightChild != null) {
            this.topRightChild.clear();
        }
    }

    private QuadTree<T> getFittingChild(Rectangle boundingBox) {
        boolean topQuadrant;
        boolean bottomQuadrant;
        float verticalMidpoint = this.bounds.x + (this.bounds.width / 2.0f);
        float horizontalMidpoint = this.bounds.y + (this.bounds.height / 2.0f);
        if (boundingBox.y > horizontalMidpoint) {
            topQuadrant = true;
        } else {
            topQuadrant = false;
        }
        if (boundingBox.y >= horizontalMidpoint || boundingBox.y + boundingBox.height >= horizontalMidpoint) {
            bottomQuadrant = false;
        } else {
            bottomQuadrant = true;
        }
        if (boundingBox.x >= verticalMidpoint || boundingBox.x + boundingBox.width >= verticalMidpoint) {
            if (boundingBox.x > verticalMidpoint) {
                if (topQuadrant) {
                    return this.topRightChild;
                }
                if (bottomQuadrant) {
                    return this.bottomRightChild;
                }
            }
        } else if (topQuadrant) {
            return this.topLeftChild;
        } else {
            if (bottomQuadrant) {
                return this.bottomLeftChild;
            }
        }
        return null;
    }

    public QuadTree<T> getNodeAt(float x, float y) {
        if (!this.bounds.contains(x, y)) {
            return null;
        }
        if (this.leaf) {
            return this;
        }
        if (this.topLeftChild.bounds.contains(x, y)) {
            return this.topLeftChild.getNodeAt(x, y);
        }
        if (this.topRightChild.bounds.contains(x, y)) {
            return this.topRightChild.getNodeAt(x, y);
        }
        if (this.bottomLeftChild.bounds.contains(x, y)) {
            return this.bottomLeftChild.getNodeAt(x, y);
        }
        if (this.bottomRightChild.bounds.contains(x, y)) {
            return this.bottomRightChild.getNodeAt(x, y);
        }
        return null;
    }

    public void getIntersect(Array<T> out, Rectangle toCheck) {
        if (!this.leaf) {
            if (this.topLeftChild.bounds.overlaps(toCheck)) {
                this.topLeftChild.getIntersect(out, toCheck);
            }
            if (this.topRightChild.bounds.overlaps(toCheck)) {
                this.topRightChild.getIntersect(out, toCheck);
            }
            if (this.bottomLeftChild.bounds.overlaps(toCheck)) {
                this.bottomLeftChild.getIntersect(out, toCheck);
            }
            if (this.bottomRightChild.bounds.overlaps(toCheck)) {
                this.bottomRightChild.getIntersect(out, toCheck);
            }
        }
        out.addAll((Array<? extends T>) this.objects);
    }

    public boolean isLeaf() {
        return this.leaf;
    }

    public QuadTree<T> getBottomLeftChild() {
        return this.bottomLeftChild;
    }

    public QuadTree<T> getBottomRightChild() {
        return this.bottomRightChild;
    }

    public QuadTree<T> getTopLeftChild() {
        return this.topLeftChild;
    }

    public QuadTree<T> getTopRightChild() {
        return this.topRightChild;
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    public Array<T> getObjects() {
        return this.objects;
    }

    public int getTotalObjectCount() {
        int count = this.objects.size;
        if (!this.leaf) {
            return count + this.topLeftChild.getTotalObjectCount() + this.topRightChild.getTotalObjectCount() + this.bottomLeftChild.getTotalObjectCount() + this.bottomRightChild.getTotalObjectCount();
        }
        return count;
    }

    public void getAllChildren(Array<T> out) {
        out.addAll((Array<? extends T>) this.objects);
        if (!this.leaf) {
            this.topLeftChild.getAllChildren(out);
            this.topRightChild.getAllChildren(out);
            this.bottomLeftChild.getAllChildren(out);
            this.bottomRightChild.getAllChildren(out);
        }
    }
}
