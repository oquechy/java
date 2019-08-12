package ru.spbau.mit.oquechy.myTreeSet;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;

public class IMyTreeSet<E> extends AbstractSet<E> implements MyTreeSet<E> {

    private Node root = null;
    private int size = 0;

    private Comparator<? super E> comparator = null;
    private IMyDescSet descSet = new IMyDescSet();

    class IMyDescSet extends AbstractSet<E> implements MyTreeSet<E> {
        @Override
        public Iterator<E> descendingIterator() {
            return IMyTreeSet.this.iterator();
        }

        @Override
        public MyTreeSet<E> descendingSet() {
            return IMyTreeSet.this;
        }

        @Override
        public E first() {
            return IMyTreeSet.this.last();
        }

        @Override
        public E last() {
            return IMyTreeSet.this.first();
        }

        @Override
        public E lower(E e) {
            return IMyTreeSet.this.higher(e);
        }

        @Override
        public E floor(E e) {
            return IMyTreeSet.this.ceiling(e);
        }

        @Override
        public E ceiling(E e) {
            return IMyTreeSet.this.floor(e);
        }

        @Override
        public E higher(E e) {
            return IMyTreeSet.this.lower(e);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public boolean contains(Object o) {
            return IMyTreeSet.this.contains(o);
        }

        @Override
        public Iterator<E> iterator() {
            return IMyTreeSet.this.descendingIterator();
        }

        @Override
        public boolean add(E e) {
            return IMyTreeSet.this.add(e);
        }

        @Override
        public boolean remove(Object o) {
            return IMyTreeSet.this.remove(o);
        }
    }

    public IMyTreeSet() {
        comparator = (Comparator<E>) (e1, e2) -> ((Comparable) e1).compareTo(e2);
    }

    private boolean naturalOrder = true;

    public IMyTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
        naturalOrder = false;
    }

    private class Node {
        E e;
        Node right = null;
        Node left = null;

        Node next;
        Node prev;

        public Node(E e) {
            this.e = e;
        }

        public boolean canMove(Object x) {
            if (naturalOrder) {
                Comparable<E> ex = (Comparable) x;
                return ex.compareTo(e) < 0 && left != null && ex.compareTo(left.e) != 0
                        || ex.compareTo(e) > 0 && right != null && ex.compareTo(right.e) != 0;
            } else {
                E ex = (E) x;
                return comparator.compare(ex, e) < 0 && left != null && comparator.compare(ex, left.e) != 0
                        || comparator.compare(ex, e) > 0 && right != null && comparator.compare(ex, right.e) != 0;
            }
        }

        public Node move(Object x) {
            if (naturalOrder) {
                Comparable<E> ex = (Comparable) x;
                return ex.compareTo(e) < 0 ? left : right;
            } else {
                E ex = (E) x;
                return comparator.compare(ex, e) < 0 ? left : right;
            }
        }

        public void addChild(E x) {
            if (comparator.compare(x, e) < 0) {
                left = new Node(x);
                if (prev != null) {
                    prev.next = left;
                }
                    left.prev = prev;
                    left.next = this;
                    prev = left;

            } else {
                right = new Node(x);
                if (next != null) {
                    next.prev = right;
                }
                right.prev = this;
                right.next = next;
                next = right;
            }
        }

        public void removeChild(Object x) {
            if ((naturalOrder ? ((Comparable) x).compareTo(e) : comparator.compare((E) x, e)) < 0) {
                removeChild(true);
            } else {
                removeChild(false);
            }
        }

        private void removeChild(boolean r) {
            Node child = r ? right : left;

            if (child.left == null && r) {
                right = right.right;
                return;
            } else if (child.left == null) {
                left = left.right;
                return;
            }

            Node newChild = child.left.getGreatest(true);
            if (r) {
                newChild.left = right.left;
                newChild.right = right.right;


                right = newChild;
            } else {
                newChild.left = left.left;
                newChild.right = left.right;
                left = newChild;
            }
        }

        private Node getGreatest(boolean remove) {
            Node cur = this;
            for (; cur.right != null && cur.right.right != null; cur = cur.right);

            if (cur.right == null) {
                return cur;
            } else {
                Node ans = right;
                if (remove) {
                    next = right.next;
                    if (right.next != null) {
                        right.next.prev = this;
                    }
                    right = null;
                }
                return ans;
            }
        }

        private Node getLeast() {
            Node cur = this;
            for (; cur.left != null && cur.left.left != null; cur = cur.left);

            if (cur.left == null) {
                return cur;
            } else {
                return left;
            }
        }

        public E next() {
            return null;
        }

        public E prev() {
            return null;
        }
    }

    @Override
    public boolean add(E e) {
        if (root == null) {
            root = new Node(e);
            return true;
        }

        Node cur;
        for (cur = root; cur.canMove(e); cur = cur.move(e));

        boolean res = cur.move(e) == null;
        cur.addChild(e);
        size += res ? 1 : 0;
        return res;
    }

    @Override
    public boolean remove(Object e) {
        if (root == null) {
            return false;
        }

        Node cur;
        for (cur = root; cur.canMove(e); cur = cur.move(e));

        boolean res = cur.move(e) == null;
        cur.removeChild(e);
        size -= res ? 1 : 0;
        return res;
    }

    @Override
    public boolean contains(Object e) {
        if (root == null) {
            return false;
        }

        Node cur;
        for (cur = root; cur.canMove(e); cur = cur.move(e));

        return cur.move(e) == null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node cur;

            @Override
            public boolean hasNext() {
                return cur.next() == null;
            }

            @Override
            public E next() {
                return cur.next();
            }
        };
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            Node cur = root.getGreatest(true);

            @Override
            public boolean hasNext() {
                return cur.prev() == null;
            }

            @Override
            public E next() {
                return cur.prev();
            }
        };
    }

    @Override
    public MyTreeSet<E> descendingSet() {
        return descSet;
    }

    @Override
    public E first() {
        return root.getLeast().e;
    }

    @Override
    public E last() {
        return root.getGreatest(false).e;
    }

    @Override
    public E lower(E e) {
        return null;
    }

    @Override
    public E floor(E e) {
        return null;
    }

    @Override
    public E ceiling(E e) {
        return null;
    }

    @Override
    public E higher(E e) {
        return null;
    }

}
