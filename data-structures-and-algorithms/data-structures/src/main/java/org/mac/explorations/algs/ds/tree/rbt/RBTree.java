/*
 *          (          (
 *          )\ )  (    )\   )  )     (
 *  (  (   (()/( ))\( ((_| /( /((   ))\
 *  )\ )\   ((_))((_)\ _ )(_)|_))\ /((_)
 * ((_|(_)  _| (_))((_) ((_)__)((_|_))
 * / _/ _ \/ _` / -_|_-< / _` \ V // -_)
 * \__\___/\__,_\___/__/_\__,_|\_/ \___|
 *
 * 东隅已逝，桑榆非晚。(The time has passed,it is not too late.)
 * 虽不能至，心向往之。(Although I can't, my heart is longing for it.)
 *
 */

package org.mac.explorations.algs.ds.tree.rbt;

/**
 * 2-3树
 *
 * 每个节点或者有2个孩子或者有3个节点---2-3树
 *
 * 满足二分搜索树的基本性质
 * 节点可以存放一个元素或2个元素
 *
 *     a           b c
 *    /\          / | \
 *
 * 2-3树是一颗绝对平衡树 如:
 *
 *                              42
 *                         /         \
 *                      17  33       50
 *                    /   |  \      /   \
 *                  6 12 18  37   48  66 88
 *
 * 2-3元素添加(新节点的添加是不会添加到空点的)
 *
 * 依次添加 42 37 12 18 6 11 5
 *                                          37
 * 42 ----> 37 42  ---> 12 37 42  --->    /   \    --->
 *                                      12    42
 *
 *      37                      37                37                12 37
 *    / |  \         --->     /    \   --->     /   \              /  |  \   --->
 *  12 18  42             6 12 18   42        12    42   --->     6  18  42
 *                                           / \
 *                                          6  18
 *
 *      12   37               12   37               12     37         6  12 37
 *    /    |   \   --->     /    |   \      --->   /    |   \  --->  / \   |  \   --->
 *  6 11  18   42        5 6 11 18   42           6    18   42      5  11 18  42
 *                                               / \
 *                                              5  11
 *          12
 *        /   \
 *       6    37
 *     /  \  / \
 *    5  11 18  42
 *
 *
 * 红黑树与2-3树
 *
 *     a                    b c
 *    /\                   / | \
 *
 *     a(black)                c (black)
 *    /\                    /      \
 *                        b(red)
 *
 * 将上面的2-3树转换为红黑树
 *
 *                               42
 *                             /    \
 *                           33     50
 *                          /  \    / \
 *                       17(r) 37  48 88
 *                      / \          /
 *                    12   18       66(r)
 *                    /
 *                   6(r)
 * 红黑树定义:
 *
 * 1.红黑树是在二分搜索树的基础上定义的，每个节点或者是红色或者是黑色
 * 2.根节点是黑色的
 * 3.每一个叶子节点(这里指最后的NULL节点)是黑色的
 * 4.若一个节点是红色的则它的孩子节点都是黑色的
 * 5.从任意一个节点到叶子节点经过的黑色节点都是一样的 (红黑树是黑色节点是绝对平衡的二叉树)
 *
 * 向红黑树中添加元素
 *
 *   让新添加的元素为红色节点 相当于2-3树中不向叶子节点添加子节点而是先融合进叶子节点
 *
 *   42(r) ---> 42(b) --->   42(b)
 *                           /
 *                         37(r)
 *
 *                                 左旋转
 *  37(r) ---> 37(b) ---> 37(b)   --------->  42(b)
 *                         \                  /
 *                         42(r)            37(r)
 *
 *
 * 42(r) ---> 42(b) --->   42(b)   --->    42(b)   --->      42(b)      --->   42(r)
 *                         /              /   \              /    \           /    \
 *                       37(r)          37(r) 66(r)         37(b) 66(b)     37(b) 66(b)
 *
 *                                            右旋转
 *   42(r) ---> 42(b) ---> 42(b) --->  42(b)  ------>      37(r)     --->     37(b)
 *                         /          /                   /    \             /    \
 *                       37(r)       37(r)              12(r)  42(b)       12(r)  42(r)
 *                                  /
 *                                 12(r)
 *
 *                                 左旋转                           左旋转          右旋转
 *   37(r) ---> 37(b) ---> 37(b)   --------->  42(b)  --->  42(b)  ------->  42(b) ------>  37(b)     --->  37(r)
 *                            \                  /          /                /              /    \         /    \
 *                           42(r)            37(r)       37(r)             37(r)         40(r)  42(r)   40(b)  42(b)
 *                                                          \               /
 *                                                          40(r)          40(r)
 *
 * @auther mac
 * @date 2019-11-07
 */
public class RBTree <K extends Comparable<K>,V> {

    private static class Node<K extends Comparable<K>,V> {

        static final boolean COLOR_RED   = true;
        static final boolean COLOR_BLACK = false;

        private K key;
        private V value;
        private boolean color;

        private Node<K ,V> left;
        private Node<K ,V> right;

        public Node(K key, V value, Node<K, V> left, Node<K, V> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.color = COLOR_RED;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    private Node<K ,V> root;
    private int size;

    /**
     * 节点是否是red节点
     * 空节点返回black
     *
     * @param node
     * @return
     */
    private boolean isRed(Node<K,V> node) {

        if (node == null) {
            return Node.COLOR_BLACK;
        }
        return node.color;
    }

    /**
     *         node               x
     *        /   \             /   \
     *       T1    x   --->   node  T3
     *            / \          / \
     *           T2 T3        T1 T2
     * @param node
     * @return
     */
    private Node<K,V> rotateLeft (Node<K,V> node) {
        Node<K,V> x = node.right;
        node.right = x.left;
        x.left = node;

        x.color = node.color;
        node.color = Node.COLOR_RED;

        return x;
    }

    /**
     *          node                   x
     *         /   \                 /  \
     *        x    T2     --->      T3  node
     *       / \                        / \
     *      T3 T1                      T1 T2
     *
     * @param node
     * @return
     */
    private Node<K,V> rotateRight (Node<K,V> node) {
        Node<K,V> x = node.left;
        node.left = x.right;
        x.right = node;

        x.color = node.color;
        node.color = Node.COLOR_RED;

        return x;
    }

    /**
     * 相当于2-3树临时4节点拆分后向上提升
     *
     * @param node
     */
    private void flipColors(Node node) {
        node.color = Node.COLOR_RED;
        node.left.color = Node.COLOR_BLACK;
        node.right.color = Node.COLOR_BLACK;
    }

    public V put(K key, V value) {

        Node<K, V> newNode = new  Node<>(key,value,null,null);
        root =  putNode(root,newNode);
        /**
         * 设置根节点为黑色节点
         */
        root.color = Node.COLOR_BLACK;
        return value;
    }

    private  Node<K, V> putNode(Node<K, V> root,  Node<K, V> kvEntry) {

        if (root == null) {
            size++;
            return kvEntry;
        }

        if (kvEntry.key.compareTo(root.key) < 0) {
            root.left = putNode(root.left,kvEntry);
        }
        else if (kvEntry.key.compareTo(root.key) > 0) {
            root.right =  putNode(root.right,kvEntry);
        }
        else { // 重复的Key
            root.setValue(kvEntry.value);
        }

        return  balancedRedBlack(root);
    }

    /**
     * 黑节点平衡
     *
     *     ------------------------
     *     ↑                      ↓
     *     b            b         b         b   颜色翻转        r
     *    /  ------>   /   --->  /  --->   / \ ----------->  /  \
     *   r            r         r         r  r              b    b
     *  ↓             \        /           ↑
     *  ↓              r      r            ↑
     *  ------------------------------------
     *
     * @param root
     * @return
     */
    private Node<K, V> balancedRedBlack(Node<K, V> root) {
        if (isRed(root.right) && !isRed(root.left))
            root = rotateLeft(root);

        if (isRed(root.left) && isRed(root.left.left))
            root = rotateRight(root);

        if (isRed(root.left) && isRed(root.right))
            flipColors(root);
        return root;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        toString(root,builder);
        return builder.toString();
    }

    private void toString(Node<K, V> root, StringBuilder builder) {

        if (root == null){
            builder.append("");
            return;
        }

        toString(root.left, builder);
        builder.append("["+root.key+":"+root.value+"]->");
        toString(root.right, builder);
    }
}
