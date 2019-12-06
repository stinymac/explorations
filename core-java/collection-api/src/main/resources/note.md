#集合框架

##概览
    
    https://docs.oracle.com/javase/8/docs/technotes/guides/collections/overview.html
    
##集合接⼝

    接⼝分类
    基于 java.util.Collection 接⼝
    基于 java.util.Map 接⼝或其他接⼝
    
###java.util.Collection 接⼝

    通⽤接⼝
    
    java.util.List 
    java.util.Set 
    java.util.SortedSet 
    java.util.NavigableSet（since Java 1.6）
    java.util.Queue（since Java 1.5）
    java.util.Deque（since Java 1.6）
    
    并发接⼝
    
    java.util.concurrent.BlockingQueue（since Java 1.5）
    java.util.concurrent.BlockingDeque（since Java 1.6） 
    java.util.concurrent.TransferQueue （since Java 1.7）
    
###java.util.Map 接⼝

     通⽤接⼝
     
     java.util.SortedMap 
     java.util.NavigableMap（since Java 1.6）
     
     并发接⼝
     
     java.util.concurrent.ConcurrentMap（since Java 1.5）
     java.util.concurrent.ConcurrentNavigableMap（since Java 1.6）
     
 ##集合抽象实现   
     java.util.Collection 接⼝
     
     java.util.AbstractCollection 
     java.util.AbstractList 
     java.util.AbstractSet 
     java.util.AbstractQueue（Since Java 1.5）
     
     java.util.Map 接⼝
     
     java.util.AbstractMap
     
###集合实现

     遗留实现
     java.util.Vector 
     java.util.Stack 
     java.util.Hashtable 
     java.util.Enumeration 
     java.util.BitSet
     
     通⽤实现
     Interface | Hash Table | Resizable Array | Balanced Tree | Linked List	Hash | Table + Linked List
     
     Set	   | HashSet	|  	              | TreeSet	 	  |                  |LinkedHashSet
     List	   | 	        | 	ArrayList	  |               | LinkedList       |
     Deque	   |	        | 	ArrayDeque    |               | LinkedList       |
     Map	   | HashMap	| 		          | TreeMap	      |                  |LinkedHashMap 
     
     并发接⼝
     
     java.util.concurrent.BlockingQueue 
     java.util.concurrent.TransferQueue 
     java.util.concurrent.BlockingDeque 
     java.util.concurrent.ConcurrentMap 
     java.util.concurrent.ConcurrentNavigableMap
     java.util.concurrent.LinkedBlockingQueue 
     java.util.concurrent.ArrayBlockingQueue 
     java.util.concurrent.PriorityBlockingQueue 
     java.util.concurrent.DelayQueue 
     java.util.concurrent.SynchronousQueue
     java.util.concurrent.LinkedBlockingDeque 
     java.util.concurrent.LinkedTransferQueue 
     java.util.concurrent.CopyOnWriteArrayList 
     java.util.concurrent.CopyOnWriteArraySet 
     java.util.concurrent.ConcurrentSkipListSet
     java.util.concurrent.ConcurrentHashMap 
     java.util.concurrent.ConcurrentSkipListMap
     
     
 ##Java集合便利实现
 
     接⼝类型
     
     单例集合接⼝（Collections.singleton*） 
     
         List: Collections.singletonList(T) 
         Set: Collections.singleton(T) 
         Map: Collections.singletonMap(K,V) 
         设计原则：不变集合（Immutable Collection）
         
     空集合接⼝（Collections.empty*） 
     
         枚举：Collections.emptyEnumeration() 
         迭代器：emptyIterator()、emptyListIterator() 
         List：emptyList() 
         Set: emptySet()、emptySortedSet()、emptyNavigableSet() 
         Map：emptyMap()、emptySortedMap()、emptyNavigableMap()
         
     转换集合接⼝（Collections.*、Arrays.*） 
     
          Enumeration: Collections.enumeration(Collection) 
          List: Collections.list(Enumeration<T>)、Arrays.asList(T…) 
          Set: Collections.newSetFromMap(Map<E, Boolean>) 
          Queue: Collections.asLifoQueue(Deque<T>) 
          
          HashCode: Arrays.hashCode(…) 
          String: Arrays.toString(…)
          
     列举集合接⼝（*.of(…)）
     
         java.util.BitSet.valueOf(…) 
         java.util.EnumSet.of(…) （Since 1.5） 
         java.util.Stream.of(…) （Since 1.8） 
         java.util.List.of(…) （Since 9） 
         java.util.Set.of(…) （Since 9） 
         java.util.Map.of(…) （Since 9）
         
##Java集合包装实现

    功能性添加，⽐如同步以及其他实现
    
    设计原则：Wrapper 模式原则，⼊参集合类型与返回类型相同或者其⼦类
    
    包装接⼝类型
    
    同步包装接⼝（java.util.Collections.synchronized*） 
    只读包装接⼝（java.util.Collections.unmodifiable*） 
    类型安全包装接⼝（java.util.Collections.checked*）
    
    
##Java集合特殊实现

    为特殊场景设计实现，这些实现表现出⾮标准性能特征、使⽤限制或者⾏为。
    
    弱引⽤ Map 
    
    java.util.WeakHashMap 
    java.lang.ThreadLocal.ThreadLocalMap 
    
    对象鉴定 Map 
    java.util.IdentityHashMap
    
    优先级 Queue 
    
    java.util.PriorityQueue
     
    枚举 Set 
    java.util.EnumSet
    
    
    
### some source code

```
    ArrayList
    
    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            // elementData 声明为Object[] 而 c.toArray()返回的实际类型可以是Object任意子类类型的数组
            /**
               Object[] a = new Object[10];
               a = new String[]{"1","2"};
               System.out.println(a[0]);
               a[0] = 1;  // --> java.lang.ArrayStoreException: java.lang.Integer
            */
            if (elementData.getClass() != Object[].class)
                // 返回的不是Object数组 调用native方法安全的转型为Object数组
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }
    
    ===添加===
    
    public boolean add(E e) {
        // 确保元素添加有空间 - 当前容量已满 扩容到当前容量的1.5倍
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }
    
    ===查找===
    
     public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
     }
     
     ===clone===
     public Object clone() {
         try {
             // 浅拷贝
             ArrayList<?> v = (ArrayList<?>) super.clone();
             v.elementData = Arrays.copyOf(elementData, size);
             v.modCount = 0;
             return v;
         } catch (CloneNotSupportedException e) {
             // this shouldn't happen, since we are Cloneable
             throw new InternalError(e);
         }
     }
     
     ===删除===
     public E remove(int index) {
         rangeCheck(index);
 
         modCount++;
         E oldValue = elementData(index);
 
         int numMoved = size - index - 1;
         if (numMoved > 0)
             System.arraycopy(elementData, index+1, elementData, index,
                              numMoved);
         elementData[--size] = null; // clear to let GC do its work
 
         return oldValue;
     }
     
     LinkedList
     
     ===添加===
     private void linkFirst(E e) {
         final Node<E> f = first;
         final Node<E> newNode = new Node<>(null, e, f);
         first = newNode;
         if (f == null)
             last = newNode;
         else
             f.prev = newNode;
         size++;
         modCount++;
     }
     
     void linkLast(E e) {
         final Node<E> l = last;
         final Node<E> newNode = new Node<>(l, e, null);
         last = newNode;
         if (l == null)
             first = newNode;
         else
             l.next = newNode;
         size++;
         modCount++;
     }
     
     void linkBefore(E e, Node<E> succ) {
         // assert succ != null;
         final Node<E> pred = succ.prev;
         final Node<E> newNode = new Node<>(pred, e, succ);
         succ.prev = newNode;
         if (pred == null)
             first = newNode;
         else
             pred.next = newNode;
         size++;
         modCount++;
     }

     ===删除===
      private E unlinkFirst(Node<E> f) {
         // assert f == first && f != null;
         final E element = f.item;
         final Node<E> next = f.next;
         f.item = null;
         f.next = null; // help GC
         first = next;
         if (next == null)
             last = null;
         else
             next.prev = null;
         size--;
         modCount++;
         return element;
     }
     
     private E unlinkLast(Node<E> l) {
         // assert l == last && l != null;
         final E element = l.item;
         final Node<E> prev = l.prev;
         l.item = null;
         l.prev = null; // help GC
         last = prev;
         if (prev == null)
             first = null;
         else
             prev.next = null;
         size--;
         modCount++;
         return element;
     }
     
     E unlink(Node<E> x) {
         // assert x != null;
         final E element = x.item;
         final Node<E> next = x.next;
         final Node<E> prev = x.prev;
 
         if (prev == null) {
             first = next;
         } else {
             prev.next = next;
             x.prev = null;
         }
 
         if (next == null) {
             last = prev;
         } else {
             next.prev = prev;
             x.next = null;
         }
 
         x.item = null;
         size--;
         modCount++;
         return element;
     }
     
     ===索引定位===
     
     Node<E> node(int index) {
         // assert isElementIndex(index);
         // 二分
         if (index < (size >> 1)) {
             Node<E> x = first;
             for (int i = 0; i < index; i++)
                 x = x.next;
             return x;
         } else {
             Node<E> x = last;
             for (int i = size - 1; i > index; i--)
                 x = x.prev;
             return x;
         }
     }
     ===删除===
     // 删除第一个发现/出现的对象o
     public boolean removeFirstOccurrence(Object o) {
         return remove(o);
     }
     
     HashMap
     
     /**
      * The default initial capacity - MUST be a power of two.
      */
     static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
     
     /**
      * The load factor used when none specified in constructor.
      *
      * 节点出现的频率在hash桶中遵循泊松分布，当桶中元素到达8个的时候，概率已经变得非常小，
      * 也就是说用0.75作为加载因子，每个碰撞位置的链表长度超过８个是几乎不可能的。
      */
     static final float DEFAULT_LOAD_FACTOR = 0.75f;
     
     static final int TREEIFY_THRESHOLD = 8;
     
     static final int UNTREEIFY_THRESHOLD = 6;
     
     static final int MIN_TREEIFY_CAPACITY = 64;
     
     =====哈希函数=====
     
     static final int hash(Object key) {
         int h;
         return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
     }
     
     以上哈希函数称为:'扰动函数'
     key.hashCode() 返回int整数范围为 -2147483648到2147483648
     
     确定哈希表哈希索引位置下标的方法为 h & (capacity - 1)
     哈希表的容量为2的整数次幂(capacity - 1 其二进制表示中低位全为1)
     相当于一个“低位掩码”。“与”操作的结果就是散列值的高位全部归零，只保留低位值，
     即将取模运算转换为了位运算来计算出数组下标。
     
     以初始capacity = 16 为例 capacity - 1 = 15 则:
     
     h                      1100 1100 1001 0110 0101 0110 0001 0010
     &                      
     capacity - 1           0000 0000 0000 0000 0000 0000 0000 1111
     
                            0000 0000 0000 0000 0000 0000 0000 0010
                            
     这样的问题是无论hash code本身如何松散 只要其低位相同，在确定哈希表位置索引时都会发生碰撞
     这时对哈希码使用扰动函数 则有:
     h                      1100 1100 1001 0110 1001 1010 1000 0100
     ^(异或)
     h >>> 16               0000 0000 0000 0000 1100 1100 1001 0110
     
                            1100 1100 1001 0110 0101 0110 0001 0010
                            
     即相当于哈希码的低16位与高16位异或 混合原始哈希码的高位和低位，以此来加大低位的随机性
     
     ===构造函数===
     
     public HashMap(int initialCapacity, float loadFactor) {
         if (initialCapacity < 0)
             throw new IllegalArgumentException("Illegal initial capacity: " +
                                                initialCapacity);
         if (initialCapacity > MAXIMUM_CAPACITY)
             initialCapacity = MAXIMUM_CAPACITY;
         if (loadFactor <= 0 || Float.isNaN(loadFactor))
             throw new IllegalArgumentException("Illegal load factor: " +
                                                loadFactor);
         this.loadFactor = loadFactor;
         // tableSizeFor(initialCapacity) -> 2^n 此时数组空间还未开辟 this.threshold = fixedInitialCapacity = 2^n
         this.threshold = tableSizeFor(initialCapacity);
     }
     
     =========================================增===========================================
     ===添加元素===
     
     final V putVal(int hash, K key, V value, boolean onlyIfAbsent,boolean evict) {
         Node<K,V>[] tab; Node<K,V> p; int n, i;
         // 构造函数未初始化 table[] --- 首先对哈希表进行初始化
         if ((tab = table) == null || (n = tab.length) == 0)
             n = (tab = resize()).length;
         // hash到的位置上无元素 直接给该位置赋值新建节点
         if ((p = tab[i = (n - 1) & hash]) == null)
             tab[i] = newNode(hash, key, value, null);
         else { // 该位置上已有元素 
             Node<K,V> e; K k;
             if (p.hash == hash &&
                 ((k = p.key) == key || (key != null && key.equals(k))))
                 e = p; // 该位置的第一个元素 是相同的key重复添加 用新值覆盖旧值
             // 该位置是一颗树
             else if (p instanceof TreeNode)
                 e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
             // 迭代链表
             else {
                 for (int binCount = 0; ; ++binCount) {
                     e = p.next
                     if (e == null) {
                         p.next = newNode(hash, key, value, null);
                         if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st --> binCount = 0
                             treeifyBin(tab, hash);
                         break;
                     }
                     //相同的key重复添加 用新值覆盖旧值
                     if (e.hash == hash && 
                         ((k = e.key) == key || (key != null && key.equals(k))))
                         break;
                     p = e;
                 }
             }
             if (e != null) { // existing mapping for key
                 V oldValue = e.value;
                 if (!onlyIfAbsent || oldValue == null)
                     e.value = value;
                 afterNodeAccess(e);
                 return oldValue;
             }
         }
         ++modCount;
         if (++size > threshold)
             resize();
         afterNodeInsertion(evict);
         return null;
     }
     
     // 初始化 table[] 或将其扩容至原来的2倍
     final Node<K,V>[] resize() {
         Node<K,V>[] oldTab = table;
         int oldCap = (oldTab == null) ? 0 : oldTab.length;
         int oldThr = threshold;
         int newCap, newThr = 0;
         if (oldCap > 0) {
             if (oldCap >= MAXIMUM_CAPACITY) {
                 threshold = Integer.MAX_VALUE;
                 return oldTab;
             }
             else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                      oldCap >= DEFAULT_INITIAL_CAPACITY)
                 newThr = oldThr << 1; // double threshold
         }
         else if (oldThr > 0) // initial capacity was placed in threshold
             newCap = oldThr;
         else {               // zero initial threshold signifies using defaults
             newCap = DEFAULT_INITIAL_CAPACITY;
             newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
         }
         if (newThr == 0) {
             float ft = (float)newCap * loadFactor;
             newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                       (int)ft : Integer.MAX_VALUE);
         }
         threshold = newThr;
         @SuppressWarnings({"rawtypes","unchecked"})
             Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
         table = newTab;
         if (oldTab != null) {
             // 将旧的table中的数据拷贝到扩容的新table中
             for (int j = 0; j < oldCap; ++j) {
                 Node<K,V> e;
                 if ((e = oldTab[j]) != null) {
                     oldTab[j] = null;
               
                     if (e.next == null)
                         newTab[e.hash & (newCap - 1)] = e; //e.hash == hash(key)
                     //TreeNode<K,V> extends LinkedHashMap.Entry<K,V> extends HashMap.Node<K,V> 
                     else if (e instanceof TreeNode)
                         // 拆分树 ---> 链表树化后既是一棵树又是一个双向链表
                         // 很容易分为两部分 分为两部分后 根据大小确定是否退化为链表 还是继续构建为新的红黑树
                         ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                     else { // preserve order
                         Node<K,V> loHead = null, loTail = null;
                         Node<K,V> hiHead = null, hiTail = null;
                         Node<K,V> next;
                         do {
                             next = e.next;
                             /**
                              * eg.
                              *
                              *     11001100100101100101011000010010
                              *
                              * &   00000000000000000000000000010000    -> 16
                              *______________________________________
                              *     00000000000000000000000000010000
                              *
                              * (e.hash & oldCap) == 2^log capacity(n) 或 0 
                              *
                              * 确定计算元素在table中位置索引 新增的一位是0 还是 1
                              * 如果是0 则元素在新table中的索引不变 如果是1 则元素(在旧table中碰撞的元素)
                              * 在新table中的索引为 (原索引 + oldCapacity)
                              * 
                              * eg.
                              * 
                              *       1100 1100 1001 0110 0101 0110 0001 0010
                              *   
                              *   &   0000 0000 0000 0000 0000 0000 0000 1111   -> 16
                              * _____________________________________________
                              *       0000 0000 0000 0000 0000 0000 0000 0010
                              *       
                              *       
                              *       1100 1100 1001 0110 0101 0110 0000 0010
                              *                                  
                              *   &   0000 0000 0000 0000 0000 0000 0000 1111  -> 16
                              * _____________________________________________
                              *       0000 0000 0000 0000 0000 0000 0000 0010
                              * 
                              *
                              *       1100 1100 1001 0110 0101 0110 0001 0010
                              *   
                              *   &   0000 0000 0000 0000 0000 0000 0001 1111   -> 32
                              * _____________________________________________
                              *       0000 0000 0000 0000 0000 0000 0001 0010
                              *       
                              *       
                              *       1100 1100 1001 0110 0101 0110 0000 0010
                              *                                  
                              *   &   0000 0000 0000 0000 0000 0000 0001 1111  -> 32
                              * _____________________________________________
                              *       0000 0000 0000 0000 0000 0000 0000 0010
                              * 
                              * 容量扩大原来的2倍，即原容量向左移一位，即(原容量-1)的下一位
                              * 高位置为1 变为 (新容量 - 1)
                              * key的hash码确定索引位置的位也就新增了一位，因此对于这新增的
                              * 确定索引位置的位的值为0 则确定的新索引和原来一致 值为0 则相当于
                              * 原索引值的下一高位的值被置为1 即值增大为 oldCapacity 所以
                              * 新索引为 (原索引 + oldCapacity)
                              */            
                             if ((e.hash & oldCap) == 0) { 
          
                                 if (loTail == null)
                                     loHead = e;
                                 else
                                     loTail.next = e;
                                 loTail = e;
                             }
                             else {
                                 if (hiTail == null)
                                     hiHead = e;
                                 else
                                     hiTail.next = e;
                                 hiTail = e;
                             }
                         } while ((e = next) != null);
                         if (loTail != null) {
                             loTail.next = null;
                             newTab[j] = loHead;
                         }
                         if (hiTail != null) {
                             hiTail.next = null;
                             newTab[j + oldCap] = hiHead;
                         }
                     }
                 }
             }
         }
         return newTab;
     }
     
     ===碰撞链表树化===
     final void treeifyBin(Node<K,V>[] tab, int hash) {
         int n, index; Node<K,V> e;
         if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)// MIN_TREEIFY_CAPACITY = 64
             resize();
         else if ((e = tab[index = (n - 1) & hash]) != null) {
             // 节点替换为树节点
             TreeNode<K,V> hd = null, tl = null;
             do {
                 TreeNode<K,V> p = replacementTreeNode(e, null);
                 if (tl == null)
                     hd = p;
                 else {
                     p.prev = tl;
                     tl.next = p;
                 }
                 tl = p;
             } while ((e = e.next) != null);
             
             if ((tab[index] = hd) != null)
                 hd.treeify(tab);
         }
     }
     
     ===链表树化===
     final void treeify(Node<K,V>[] tab) {
         TreeNode<K,V> root = null;
         for (TreeNode<K,V> x = this, next; x != null; x = next) {
             next = (TreeNode<K,V>)x.next;
             x.left = x.right = null;
             if (root == null) {
                 x.parent = null;
                 x.red = false;
                 root = x;
             }
             else {
                 K k = x.key;
                 int h = x.hash;
                 Class<?> kc = null;
                 for (TreeNode<K,V> p = root;;) {
                     int dir, ph;
                     K pk = p.key;
                     // 比较key的hash值
                     if ((ph = p.hash) > h)
                         dir = -1;
                     else if (ph < h)
                         dir = 1;
                     // hash 码相等时 考察key是否可比价 可比较 比较key 
                     // 否则取System.identityHashCode比较
                     else if ((kc == null &&
                               (kc = comparableClassFor(k)) == null) ||
                              (dir = compareComparables(kc, k, pk)) == 0)
                         // 对象的地址值-System.identityHashCode
                         dir = tieBreakOrder(k, pk);

                     TreeNode<K,V> xp = p;
                     // 根据以上确定的hash值大小 向节点的左节点或有节点插入先节点
                     // 新节点插入后 检查维护树的平衡
                     if ((p = (dir <= 0) ? p.left : p.right) == null) {
                         x.parent = xp;
                         if (dir <= 0)
                             xp.left = x;
                         else
                             xp.right = x;
                         //检查维护树的平衡
                         root = balanceInsertion(root, x);
                         break;
                     }
                 }
             }
         }
         moveRootToFront(tab, root);
     }
     
     ===树链表化===
     final Node<K,V> untreeify(HashMap<K,V> map) {
             Node<K,V> hd = null, tl = null;
             for (Node<K,V> q = this; q != null; q = q.next) {
                 Node<K,V> p = map.replacementNode(q, null);
                 if (tl == null)
                     hd = p;
                 else
                     tl.next = p;
                 tl = p;
             }
             return hd;
     }
     
     =========================================查===========================================
     ===查找key===
     final Node<K,V> getNode(int hash, Object key) {
         Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
         if ((tab = table) != null && (n = tab.length) > 0 &&
             (first = tab[(n - 1) & hash]) != null) {
             if (first.hash == hash && // always check first node
                 ((k = first.key) == key || (key != null && key.equals(k))))
                 return first;
             if ((e = first.next) != null) {
                 if (first instanceof TreeNode)
                     return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                 do {
                     if (e.hash == hash &&
                         ((k = e.key) == key || (key != null && key.equals(k))))
                         return e;
                 } while ((e = e.next) != null);
             }
         }
         return null;
     }     
     ===查找树节点===
     final TreeNode<K,V> find(int h, Object k, Class<?> kc) {
         TreeNode<K,V> p = this;
         do {
             int ph, dir; K pk;
             TreeNode<K,V> pl = p.left, pr = p.right, q;
             if ((ph = p.hash) > h)
                 p = pl;
             else if (ph < h)
                 p = pr;
             else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                 return p;
             else if (pl == null)
                 p = pr;
             else if (pr == null)
                 p = pl;
             else if ((kc != null ||
                       (kc = comparableClassFor(k)) != null) &&
                      (dir = compareComparables(kc, k, pk)) != 0)
                 p = (dir < 0) ? pl : pr;
             else if ((q = pr.find(h, k, kc)) != null)
                 return q;
             else
                 p = pl;
         } while (p != null);
         return null;
     }
     
     
     =========================================删===========================================
     
     final Node<K,V> removeNode(int hash, Object key, Object value,
                                    boolean matchValue, boolean movable) {
         Node<K,V>[] tab; Node<K,V> p; int n, index;
         if ((tab = table) != null && (n = tab.length) > 0 &&
             (p = tab[index = (n - 1) & hash]) != null) {
             Node<K,V> node = null, e; K k; V v;
             if (p.hash == hash &&
                 ((k = p.key) == key || (key != null && key.equals(k))))
                 node = p;
             else if ((e = p.next) != null) {
                 if (p instanceof TreeNode)
                     node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
                 else {
                     do {
                         if (e.hash == hash &&
                             ((k = e.key) == key ||
                              (key != null && key.equals(k)))) {
                             node = e;
                             break;
                         }
                         p = e;
                     } while ((e = e.next) != null);
                 }
             }
             if (node != null && (!matchValue || (v = node.value) == value ||
                                  (value != null && value.equals(v)))) {
                 if (node instanceof TreeNode)
                     ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
                 else if (node == p)
                     tab[index] = node.next;
                 else
                     p.next = node.next;
                 ++modCount;
                 --size;
                 afterNodeRemoval(node);
                 return node;
             }
         }
         return null;
     }
     
     LinkedHashMap
     
     // HashMap的子类
     public class LinkedHashMap<K,V> extends HashMap<K,V> implements Map<K,V>
     
     ===添加===
     Node<K,V> newNode(int hash, K key, V value, Node<K,V> e) {
         LinkedHashMap.Entry<K,V> p = new LinkedHashMap.Entry<K,V>(hash, key, value, e);
         linkNodeLast(p);
         return p;
     }
     
     // 在HashMap添加的同时维护一个链表
     private void linkNodeLast(LinkedHashMap.Entry<K,V> p) {
         LinkedHashMap.Entry<K,V> last = tail;
         tail = p;
         if (last == null)
             head = p;
         else {
             p.before = last;
             last.after = p;
         }
     }
     
     ===访问过的元素移动到链表尾-则链表头为(LRU)-如果设定为需要逐出则删除链表头元素===
     void afterNodeAccess(Node<K,V> e) { // move node to last
         LinkedHashMap.Entry<K,V> last;
         if (accessOrder && (last = tail) != e) {
             LinkedHashMap.Entry<K,V> p =
                 (LinkedHashMap.Entry<K,V>)e, b = p.before, a = p.after;
             p.after = null;
             if (b == null)
                 head = a;
             else
                 b.after = a;
             if (a != null)
                 a.before = b;
             else
                 last = b;
             if (last == null)
                 head = p;
             else {
                 p.before = last;
                 last.after = p;
             }
             tail = p;
             ++modCount;
         }
     }
     
     TreeMap
     
     红黑树实现 有序 实现SortedMap接口
     
     比较器
     private final Comparator<? super K> comparator;
     
     ===添加===
      public V put(K key, V value) {
         Entry<K,V> t = root;
         if (t == null) {
             // comparator == null 则默认K implment  Comparator<K> 否则抛出类转换异常
             compare(key, key); // type (and possibly null) check
 
             root = new Entry<>(key, value, null);
             size = 1;
             modCount++;
             return null;
         }
         int cmp;
         Entry<K,V> parent;
         // split comparator and comparable paths
         Comparator<? super K> cpr = comparator;
         // comparator 是否为null 定位新节点的插入位置
         if (cpr != null) {
             do {
                 parent = t;
                 cmp = cpr.compare(key, t.key);
                 if (cmp < 0)
                     t = t.left;
                 else if (cmp > 0)
                     t = t.right;
                 else
                     return t.setValue(value);
             } while (t != null);
         }
         else {
             if (key == null)
                 throw new NullPointerException();
             @SuppressWarnings("unchecked")
                 Comparable<? super K> k = (Comparable<? super K>) key;
             do {
                 parent = t;
                 cmp = k.compareTo(t.key);
                 if (cmp < 0)
                     t = t.left;
                 else if (cmp > 0)
                     t = t.right;
                 else
                     return t.setValue(value);
             } while (t != null);
         }
         Entry<K,V> e = new Entry<>(key, value, parent);
         if (cmp < 0)
             parent.left = e;
         else
             parent.right = e;
         // 使树平衡
         fixAfterInsertion(e);
         size++;
         modCount++;
         return null;
      }
      
      
      PriorityQueue
      
      // 从集合构建堆
      public PriorityQueue(Collection<? extends E> c) {
          if (c instanceof SortedSet<?>) {
              SortedSet<? extends E> ss = (SortedSet<? extends E>) c;
              this.comparator = (Comparator<? super E>) ss.comparator();
              // 有序的set就是一个最小堆
              initElementsFromCollection(ss);
          }
          else if (c instanceof PriorityQueue<?>) {
              PriorityQueue<? extends E> pq = (PriorityQueue<? extends E>) c;
              this.comparator = (Comparator<? super E>) pq.comparator();
              initFromPriorityQueue(pq);
          }
          else {
              this.comparator = null;
              initFromCollection(c);
          }
      }
      
      ===数组堆化-下沉===
      private void siftDownUsingComparator(int k, E x) {
          int half = size >>> 1;
          // half  是最后一个元素的父节点 下一个兄弟节点
          while (k < half) {
              int child = (k << 1) + 1;
              Object c = queue[child];
              int right = child + 1;
              if (right < size &&
                  comparator.compare((E) c, (E) queue[right]) > 0)
                  c = queue[child = right];
              if (comparator.compare(x, (E) c) <= 0)
                  break;
              queue[k] = c;
              k = child;
          }
          queue[k] = x;
      }
      
      ===元素入队===
      
      public boolean offer(E e) {
          if (e == null)
              throw new NullPointerException();
          modCount++;
          int i = size;
          if (i >= queue.length)
              // 数组扩容
              grow(i + 1);
          size = i + 1;
          if (i == 0)
              queue[0] = e;
          else
              // 元素“添加”到最后一位 然后上浮
              siftUp(i, e);
          return true;
      }
      
      ===上浮===
      private void siftUpUsingComparator(int k, E x) {
          while (k > 0) {
              int parent = (k - 1) >>> 1;
              Object e = queue[parent];
              if (comparator.compare(x, (E) e) >= 0)
                  break;
              queue[k] = e;
              k = parent;
          }
          queue[k] = x;
      }
      
      ===元素出队===
      public E poll() {
          if (size == 0)
              return null;
          int s = --size;
          modCount++;
          E result = (E) queue[0];
          // 用堆中最后一个元素覆盖队头(堆顶)元素 然后检查下沉
          E x = (E) queue[s];
          queue[s] = null;
          if (s != 0)
              siftDown(0, x);
          return result;
      }
      
      ===元素删除===
      
      private E removeAt(int i) {
          // assert i >= 0 && i < size;
          modCount++;
          int s = --size;
          if (s == i) // removed last element
              queue[i] = null;
          else {
              // 用堆中最后一个元素覆盖删除的元素
              // 然后 上浮 或 下沉
              E moved = (E) queue[s];
              queue[s] = null;
              
              siftDown(i, moved);
              if (queue[i] == moved) { // 不需要下沉 
                  siftUp(i, moved);
                  if (queue[i] != moved)
                      return moved;
              }
          }
          return null;
      }
```