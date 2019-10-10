#导论

##方法设计

    单元：一个类或者一组类（组件）
    
        类采用名词结构
            动词过去式+名词
            ContextRefreshedEvent
            
        动词ing + 名词
            InitializingBean
            
        形容词 + 名词
            ConfigurableApplicationContext
            
    执行：某个方法
        方法命名：动词
            execute
            callback
            run
        方法参数：名词
        
    异常：
        根（顶层）异常
            Throwable
            checked 类型：Exception
            unchecked类型：RuntimeException
            不常见：Error
        Java 1.4 java.lang.StackTraceElement
            添加异常原因（cause）
            反模式：吞掉某个异常
            性能：注意 fillInStackTrace() 方法的开销，避免异常栈调用深度
            方法一：JVM 参数控制栈深度（物理屏蔽）
            方法二：logback 日志框架控制堆栈输出深度（逻辑屏蔽）
            
##函数式接口

    基本特性：
    
        所有的函数式接口都引用一段执行代码
        函数式接口没有固定的类型，固定模式（ SCFP = Supplier + Consumer + Function + Predicate） + Action
        利用方法引用来实现模式匹配
        
#面向对象设计

##Java接口设计

###通用设计-类/接口名称

    模式: (形容词)+名称
    
    eg.
    
    单名称:      java.lang.String
    双名词:      java.util.ArrayList
    形容词+名词: java.util.LinkedList

###通用设计-可访问性

    public：开放 API 使⽤场景
    
    举例：java.lang.String 
    
    （默认）：仅在当前 package 下使⽤，属于私有 API 
    
    举例：java.io.FileSystem   
    
    protected：不能用于修饰最外层 class
    
    private：不能用于修饰最外层 class 
    
###通用设计 - 可继承性
    final：final 不具备继承性，仅⽤于实现类，不能与 abstract 关键字同时修饰类
    举例：java.lang.String 
    
    ⾮ final：最常见/默认的设计⼿段，可继承性依赖于可访问性
    举例：java.io.FileSystem
    
###具体类设计

    常见场景
      - 功能组件
        - HashMap
      - 接⼝/抽象类实现
        - HashMap <- AbstractMap <- Map
      - 数据对象
        - POJO
      - ⼯具辅助
        - *Utils
        - ViewHelper
        - Helper
        
    命名模式
      前缀：“Default”、“Generic”、“Common”、“Basic” 
      后缀：“Impl”
      
###抽象类设计

    常见场景
      接⼝通⽤实现（模板模式）
         - AbstractList
         - AbstractSet
         - AbstractMap

      状态/⾏为继承
      
      ⼯具类
      
    常见模式
    
      抽象程度介于类与接⼝之间（Java 8+ 可完全由接⼝代替）
    
      以 “Abstract” 或 “Base” 类名前缀
      
        java.util.AbstractCollection 
        javax.sql.rowset.BaseRowSet
        
        
###接⼝设计

    常见场景
    
      上下游系统（组件）通讯契约
        API 
        RPC 
        
      常量定义
      
    常见模式
    
      ⽆状态（Stateless） 
      完全抽象（ < Java 8 ） 
          - Serializable
          - Cloneable
          - AutoCloseable
          - EventListener
      局部抽象（ Java 8+ ） 
      单⼀抽象（ Java 8 函数式接⼝）
      
###内置类设计
    
    常见场景
    
      临时数据存储类：java.lang.ThreadLocal.ThreadLocalMap 
      特殊⽤途的 API 实现：java.util.Collections.UnmodifiableCollection 
      Builder 模式（接⼝）：java.util.stream.Stream.Builder
      
##Java枚举设计

###“枚举类” 

     场景：Java 枚举（enum）引⼊之前的模拟枚举实现类
     
     模式：
       成员⽤常亮表示，并且类型为当前类型
       常⽤关键字 final 修饰类
       ⾮ public 构造器
       
###Java枚举

    基本特性
    
      类结构（强类型）
      继承 java.lang.Enum 
      不可显示地继承和被继承
      
      
##Java泛型设计

    泛型使⽤场景
    
      编译时强类型检查
      
      避免类型强转
      
      实现通⽤算法
      
    类型参数命名约定
    
      E：表示集合元素（Element）
      V：表示数值（Value） 
      K：表示键（Key） 
      T：表示类型
      
    泛型有界类型参数
    
      单界限
      
      多界限
      
      泛型⽅法和有界类型参数
      
      
###Java⽅法设计

    ⽅法命名设计
    
        // 方法表示执行动作，通常是动词
        Runnable#run()
        Action#execute()
        Callable#call()

      

        // 动词+形容词
        // 副词形容动词或形容词
        Stream.of(1, 2, 3).forEachOrdered(System.out::println);

        
        Lock lock = new ReentrantLock();
        try {
            lock.lockInterruptibly(); // 动词(verb.)+副词(adv.)
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        
        // 动词+名词
        public List<String> getValues() {
            return Collections.emptyList();
        }
    
        // 动词+名词+副词
        public List<String> getValuesSynchronously() {
            return Collections.emptyList();
        }
        public void render() { // 单一动词
        
        }
        
        // 同步渲染
        public void renderSynchronously() { // 动词+副词
        
        }
        
        // 并发渲染
        public void renderConcurrently() { // 动词+副词
        
        }
    ⽅法返回类型设计
        // 方法返回值（多态、封装）
        // 原则一：返回类型需要抽象（强类型），除 Object
        // 抽象返回类型的意义，调用方（接受方）容易处理
        // 越具体，越难通用。
    
        例如:返回一个有序的，去重的字符串
        public TreeSet<String> getValues() { // 错误范例
            return new TreeSet<>();
        }
    
        // 动词 + 形容 + 名词
        public SortedSet<String> getSortedValues() { // 正确范例
            return new TreeSet<>();
        }
        // 如果返回的类型是集合的话，Collection 优于List 或 Set
        // 如果不考虑写操作，Iterable 优于 Collection
        
        // 原则二：尽可能地返回 Java 集合框架内的接口，尽量避免数组
        // 1. Collection 比较 [] 而言，拥有更多的操作方法，比如 add
        // 2. Collection 接口返回时，可以限制只读，而 [] 不行。
        
        // 原则三：确保集合返回接口只读
        private static List<Integer> getNumbers() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            return Collections.unmodifiableList(numbers);
        }
    
        // 原则四：如果需要非只读集合返回的话，那么确保返回快照
        private static List<Integer> getNumbersSnapshot() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            return new ArrayList<>(numbers);
        }
    
        // 技巧：如果需要返回快照，尽可能地选择 ArrayList
    
    ⽅法参数设计
    
       // arguments  通常形容方法参数
       // parameters 通常是指外部媒介传递过来，请求参数
      
       // 方法参数名称设计
       public boolean equals(Object that) {
          return this.value == ((MethodArguments) that).value;
       }
      
       public boolean equals(Object another) {
           return this.value == ((MethodArguments) another).value;
       }
       
       // 单项传输 - 参数类型对等方式
       public void copy(Object source, Object target) {
       
       }
       
       // 单项传输 - 参数类型非对等
       public void add(Collection<Object> collection, Object element) {
       
       }
       
       // 多项传输 - 参数类型非对等
       /**
         * @param collection collection
         * @param elements   zero or more elements (0..n)
         */
       public void add(Collection<Object> collection, Object... elements) {
       
       }
       
       // 多项传输 - 参数类型非对等
       /**
         * @param collection collection
         * @param one        one element
         * @param others     more elements (0..n)
         */
       public void add(Collection<Object> collection, Object one, Object... others) {
       
       }
       
       // 多项传输 - 参数类型对等
       public void addAll(Collection<Object> destination, Collection<Object> source) {
           destination.addAll(source);
       }
       
       // Effective Java 建议不要超过四个参数
       // Java 8 Lambda 告诉用户，最多使用三个
       // Runnable(Action) 零个
       // Consumer 一个
       // Function BiConsumer 两个
       // BiFunction 三个