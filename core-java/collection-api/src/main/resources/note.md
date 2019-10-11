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