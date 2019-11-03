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

package org.mac.explorations.algs.ds.tree;

/**
 * @auther mac
 * @date 2019-10-31
 */
public class Main {

    public static void main(String[] args) {
       /* BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(28);
        tree.add(26);
        tree.add(42);
        tree.add(37);
        tree.add(50);

        System.out.println(tree);
        tree.preorderTraversalNonRecursion();
        tree.levelorderTraversal();

        System.out.println(tree.min()+"..."+tree.max());

        //tree.removeMin();
        //tree.removeMax();
        tree.remove(42);
        tree.levelorderTraversal();*/

      /*  BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.add(41);
        bst.add(22);
        bst.add(58);
        bst.add(15);
        bst.add(33);
        bst.add(50);
        bst.add(63);
        bst.add(13);
        bst.add(37);
        bst.add(42);
        bst.add(53);

        System.out.println(bst);
        bst.levelorderTraversal();
        System.out.println(bst.floor(45));
        System.out.println(bst.ceil(45));*/

       SegmentTree<Integer> st = new SegmentTree<>(new Integer[]{1,2,3,4,5,6,7,8},(l,r) -> l+r);
       System.out.println(st);
       System.out.println(st.query(1,6));
    }
}
