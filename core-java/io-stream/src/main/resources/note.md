#File 类

    java.io.File类：文件或文件目录路径的抽象表示形式，与平台无关
    
    File 能新建、删除、重命名文件和目录，但 File 不能访问文件内容本身。
    如果需要访问文件内容本身，则需要使用输入/输出流。 
    
    想要在Java程序中表示一个真实存在的文件或目录，那么必须有一个File对 象，
    但是Java程序中的一个File对象，可能没有一个真实存在的文件或目录。 
    
    File对象可以作为参数传递给流的构造器
    
    常用构造器
    
    public File(String pathname)
    以pathname为路径创建File对象，可以是绝对路径或者相对路径，
    如果pathname是相对路径，则默认的当前路径在系统属性user.dir中存储。
    
    public File(String parent,String child)
    以parent为父路径，child为子路径创建File对象。 
    
    public File(File parent,String child)
    根据一个父File对象和子文件路径创建File对象
    
#IO流及流的分类

    I/O是Input/Output的缩写， I/O技术是非常实用的技术，用于
    处理设备之间的数据传输。如读/写文件，网络通讯等。
    
    Java程序中，对于数据的输入/输出操作以“流(stream)” 的
    方式进行。
    
    java.io包下提供了各种“流”类和接口，用以获取不同种类的
    数据，并通过标准的方法输入或输出数据
    
    流的分类
    
    按操作数据单位不同分为：字节流(8 bit)，字符流(16 bit)
    按数据流的流向不同分为：输入流，输出流
    按流的角色的不同分为：节点流，处理流
    
              字节流         字符流
    输入流 InputStream       Reader
    输出流 OutputStream      Writer
    
    节点流：直接从数据源或目的地读写数据
    处理流：不直接连接到数据源或目的地，而是“连接”在已存
    在的流（节点流或处理流）之上，通过对数据的处理为程序提
    供更为强大的读写功能。
    
##InputStream & Reader
    
    InputStream 和 Reader 是所有输入流的基类。
    
    InputStream（典型实现：FileInputStream） 
    int read()
    int read(byte[] b)
    int read(byte[] b, int off, int len)
    
    Reader（典型实现：FileReader） 
    int read()
    int read(char [] c)
    int read(char [] c, int off, int len) 
    
    程序中打开的文件 IO 资源不属于内存里的资源，垃圾回收机制无法回收该资
    源，所以应该显式关闭文件 IO 资源。 
    
    FileInputStream 从文件系统中的某个文件中获得输入字节。FileInputStream 
    用于读取非文本数据之类的原始字节流。要读取字符流，需要使用 FileReader
    
###InputStream

    int read()
    从输入流中读取数据的下一个字节。返回 0 到 255 范围内的 int 字节值。如果因
    为已经到达流末尾而没有可用的字节，则返回值 -1。 
    
    int read(byte[] b)
    从此输入流中将最多 b.length 个字节的数据读入一个 byte 数组中。如果因为已
    经到达流末尾而没有可用的字节，则返回值 -1。否则以整数形式返回实际读取
    的字节数。 
    int read(byte[] b, int off,int len)
    将输入流中最多 len 个数据字节读入 byte 数组。尝试读取 len 个字节，但读取
    的字节也可能小于该值。以整数形式返回实际读取的字节数。如果因为流位于
    文件末尾而没有可用的字节，则返回值 -1。 
    
    public void close() throws IOException
    关闭此输入流并释放与该流关联的所有系统资源。
    
###Reader

    int read()
    读取单个字符。作为整数读取的字符，范围在 0 到 65535 之间 (0x00-0xffff)（2个
    字节的Unicode码），如果已到达流的末尾，则返回 -1 
    
    int read(char[] cbuf)
    将字符读入数组。如果已到达流的末尾，则返回 -1。否则返回本次读取的字符数。 
    
    int read(char[] cbuf,int off,int len)
    将字符读入数组的某一部分。存到数组cbuf中，从off处开始存储，最多读len个字
    符。如果已到达流的末尾，则返回 -1。否则返回本次读取的字符数。 
    
    public void close() throws IOException
    关闭此输入流并释放与该流关联的所有系统资源。
    
    
##OutputStream & Writer

    OutputStream 和 Writer 也非常相似：
    void write(int b/int c);
    void write(byte[] b/char[] cbuf);
    void write(byte[] b/char[] buff, int off, int len);
    void flush();
    void close(); 需要先刷新，再关闭此流
    
    因为字符流直接以字符作为操作单位，所以 Writer 可以用字符串来替换字符数组，
    即以 String 对象作为参数
    void write(String str);
    void write(String str, int off, int len);
    
    FileOutputStream 从文件系统中的某个文件中获得输出字节。FileOutputStream 
    用于写出非文本数据之类的原始字节流。要写出字符流，需要使用 FileWriter
    
###OutputStream

    void write(int b)
    将指定的字节写入此输出流。write 的常规协定是：向输出流写入一个字节。要写
    入的字节是参数 b 的八个低位。b 的 24 个高位将被忽略。 即写入0~255范围的。 
    
    void write(byte[] b)
    将 b.length 个字节从指定的 byte 数组写入此输出流。write(b) 的常规协定是：应该
    与调用 write(b, 0, b.length) 的效果完全相同。
     
    void write(byte[] b,int off,int len)
    将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此输出流。 
    
    public void flush()throws IOException
    刷新此输出流并强制写出所有缓冲的输出字节，调用此方法指示应将这些字节立
    即写入它们预期的目标。 
    
    public void close() throws IOException
    关闭此输出流并释放与该流关联的所有系统资源。
    
###Writer
    void write(int c)
    写入单个字符。要写入的字符包含在给定整数值的 16 个低位中，16 高位被忽略。 即
    写入0 到 65535 之间的Unicode码。 
    
    void write(char[] cbuf)
    写入字符数组。 
    
    void write(char[] cbuf,int off,int len)
    写入字符数组的某一部分。从off开始，写入len个字符
    
    void write(String str)
    写入字符串。 
    
    void write(String str,int off,int len)
    写入字符串的某一部分。 
    
    void flush()
    刷新该流的缓冲，则立即将它们写入预期目标。 
    public void close() throws IOException
    关闭此输出流并释放与该流关联的所有系统资源。
    
    
##节点流(或文件流)

##缓冲流

     为了提高数据读写的速度，Java API提供了带缓冲功能的流类，在使用这些流类
     时，会创建一个内部缓冲区数组，缺省使用8192个字节(8Kb)的缓冲区。
     
     缓冲流要“套接”在相应的节点流之上，根据数据操作单位可以把缓冲流分为：
     BufferedInputStream 和 BufferedOutputStream
     BufferedReader 和 BufferedWriter
     
     当读取数据时，数据按块读入缓冲区，其后的读操作则直接访问缓冲区
     当使用BufferedInputStream读取字节文件时，BufferedInputStream会一次性从
     文件中读取8192个(8Kb)，存在缓冲区中，直到缓冲区装满了，才重新从文件中
     读取下一个8192个字节数组。
      
     向流中写入字节时，不会直接写到文件，先写到缓冲区中直到缓冲区写满，
     BufferedOutputStream才会把缓冲区中的数据一次性写到文件里。使用方法
     flush()可以强制将缓冲区的内容全部写入输出流
     
     关闭流的顺序和打开流的顺序相反。只要关闭最外层流即可，关闭最外层流也
     会相应关闭内层节点流
     
     flush()方法的使用：手动将buffer中内容写入文件
     如果是带缓冲区的流对象的close()方法，不但会关闭流，还会在关闭流之前刷
     新缓冲区，关闭后不能再写出
     
##转换流

    转换流提供了在字节流和字符流之间的转换
    
    Java API提供了两个转换流：
    InputStreamReader：将InputStream转换为Reader
    OutputStreamWriter：将Writer转换为OutputStream
    
    字节流中的数据都是字符时，转成字符流操作更高效。 
    
    很多时候我们使用转换流来处理文件乱码问题。实现编码和解码的功能。
    
    InputStreamReader
    
    实现将字节的输入流按指定字符集转换为字符的输入流。
    需要和InputStream“套接”。
    
    构造器
    public InputStreamReader(InputStream in)
    public InputSreamReader(InputStream in,String charsetName)
    
    OutputStreamWriter
    
    实现将字符的输出流按指定字符集转换为字节的输出流。
    需要和OutputStream“套接”。
    
    构造器 
    public OutputStreamWriter(OutputStream out)
    public OutputSreamWriter(OutputStream out,String charsetName)

##标准输入、输出流
    System.in和System.out分别代表了系统标准的输入和输出设备
    默认输入设备是：键盘，输出设备是：显示器
    
    System.in的类型是InputStream
    System.out的类型是PrintStream，其是OutputStream的子类
    
    FilterOutputStream 的子类
    重定向：通过System类的setIn，setOut方法对默认设备进行改变。
    public static void setIn(InputStream in)
    public static void setOut(PrintStream out)
    
##打印流
    
    实现将基本数据类型的数据格式转化为字符串输出
    打印流：PrintStream和PrintWriter
    提供了一系列重载的print()和println()方法，用于多种数据类型的输出
    PrintStream和PrintWriter的输出不会抛出IOException异常
    PrintStream和PrintWriter有自动flush功能
    PrintStream 打印的所有字符都使用平台的默认字符编码转换为字节。
    在需要写入字符而不是写入字节的情况下，应该使用 PrintWriter 类。
    System.out返回的是PrintStream的实例
    
##数据流

    为了方便地操作Java语言的基本数据类型和String的数据，可以使用数据流。
    数据流有两个类：(用于读取和写出基本数据类型、String类的数据）
    DataInputStream 和 DataOutputStream
    分别“套接”在 InputStream 和 OutputStream 子类的流上 
    
    DataInputStream中的方法
    boolean readBoolean() 
    byte readByte()
    char readChar() 
    float readFloat()
    double readDouble() 
    short readShort()
    long readLong() 
    int readInt()
    String readUTF() 
    void readFully(byte[] b)
    
    DataOutputStream中的方法
    将上述的方法的read改为相应的write即可。

##对象流

    ObjectInputStream和OjbectOutputSteam
    用于存储和读取基本数据类型数据或对象的处理流。它的强大之处就是可
    以把Java中的对象写入到数据源中，也能把对象从数据源中还原回来。
    
    序列化：用ObjectOutputStream类保存基本类型数据或对象的机制
    反序列化：用ObjectInputStream类读取基本类型数据或对象的机制
    
    ObjectOutputStream和ObjectInputStream不能序列化static和transient修
    饰的成员变量
    
    对象序列化机制允许把内存中的Java对象转换成平台无关的二进制流，从
    而允许把这种二进制流持久地保存在磁盘上，或通过网络将这种二进制流传
    输到另一个网络节点。当其它程序获取了这种二进制流，就可以恢复成原
    来的Java对象。
    
    序列化的好处在于可将任何实现了Serializable接口的对象转化为字节数据，
    使其在保存和传输时可被还原
    
    序列化是 RMI（Remote Method Invoke – 远程方法调用）过程的参数和返
    回值都必须实现的机制，而 RMI 是 JavaEE 的基础。因此序列化机制是
    JavaEE 平台的基础
    
    如果需要让某个对象支持序列化机制，则必须让对象所属的类及其属性是可
    序列化的，为了让某个类是可序列化的，该类必须实现如下两个接口之一。
    否则，会抛出NotSerializableException异常
    Serializable
    Externalizable
    
    凡是实现Serializable接口的类都有一个表示序列化版本标识符的静态变量：
    private static final long serialVersionUID;
    serialVersionUID用来表明类的不同版本间的兼容性。简言之，其目的是以序列化对象
    进行版本控制，有关各版本反序列化时是否兼容。
    
    如果类没有显示定义这个静态常量，它的值是Java运行时环境根据类的内部细节自
    动生成的。若类的实例变量做了修改，serialVersionUID 可能发生变化。故建议，
    显式声明。
    
    简单来说，Java的序列化机制是通过在运行时判断类的serialVersionUID来验
    证版本一致性的。在进行反序列化时，JVM会把传来的字节流中的
    serialVersionUID与本地相应实体类的serialVersionUID进行比较，如果相同
    就认为是一致的，可以进行反序列化，否则就会出现序列化版本不一致的异
    常。(InvalidCastException)
    
    若某个类实现了 Serializable 接口，该类的对象就是可序列化的：
    
    创建一个 ObjectOutputStream
    调用 ObjectOutputStream 对象的 writeObject(对象) 方法输出可序列化对象
    注意写出一次，操作flush()一次
    
    反序列化
    创建一个 ObjectInputStream
    调用 readObject() 方法读取流中的对象
    
    如果某个类的属性不是基本数据类型或 String 类型，而是另一个
    引用类型，那么这个引用类型必须是可序列化的，否则拥有该类型的
    Field 的类也不能序列化

##随机存取文件流

    RandomAccessFile 声明在java.io包下，但直接继承于java.lang.Object类。并
    且它实现了DataInput、DataOutput这两个接口，也就意味着这个类既可以读也
    可以写。
    
    RandomAccessFile 类支持 “随机访问” 的方式，程序可以直接跳到文件的任意
    地方来读、写文件
    
    支持只访问文件的部分内容
    可以向已存在的文件后追加内容
    
    RandomAccessFile 对象包含一个记录指针，用以标示当前读写处的位置。
    RandomAccessFile 类对象可以自由移动记录指针：
    long getFilePointer()：获取文件记录指针的当前位置
    void seek(long pos)：将文件记录指针定位到 pos 位置
    
    构造器
    public RandomAccessFile(File file, String mode) 
    public RandomAccessFile(String name, String mode) 
    
    创建 RandomAccessFile 类实例需要指定一个 mode 参数，该参数指
    定 RandomAccessFile 的访问模式：
    
    r: 以只读方式打开
    rw：打开以便读取和写入
    rwd:打开以便读取和写入；同步文件内容的更新
    rws:打开以便读取和写入；同步文件内容和元数据的更新
    如果模式为只读r。则不会创建文件，而是会去读取一个已经存在的文件，
    如果读取的文件不存在则会出现异常。 如果模式为rw读写。如果文件不
    存在则会去创建文件，如果存在则不会创建。
    
    RandomAccessFile这个类，来实现一个多线程断点下载的功能

# NIO.2中Path、Paths、Files类的使用

    Path接口，代表一个平台无关的平台路径，描述了目录结构中文件的位置。
    Path可以看成是File类的升级版本，实际引用的资源也可以不存在。
    
    Paths 类提供的静态 get() 方法用来获取 Path 对象：
    static Path get(String first, String … more) : 用于将多个字符串串连成路径
    static Path get(URI uri): 返回指定uri对应的Path路径
    
    Path 常用方法：
    
    String toString() ： 返回调用 Path 对象的字符串表示形式
    boolean startsWith(String path) : 判断是否以 path 路径开始
    boolean endsWith(String path) : 判断是否以 path 路径结束
    boolean isAbsolute() : 判断是否是绝对路径
    Path getParent() ：返回Path对象包含整个路径，不包含 Path 对象指定的文件路径
    Path getRoot() ：返回调用 Path 对象的根路径
    Path getFileName() : 返回与调用 Path 对象关联的文件名
    int getNameCount() : 返回Path 根目录后面元素的数量
    Path getName(int idx) : 返回指定索引位置 idx 的路径名称
    Path toAbsolutePath() : 作为绝对路径返回调用 Path 对象
    Path resolve(Path p) :合并两个路径，返回合并后的路径对应的Path对象
    File toFile(): 将Path转化为File类的对象
    
    Files常用方法：
    
    Path copy(Path src, Path dest, CopyOption … how) : 文件的复制
    Path createDirectory(Path path, FileAttribute<?> … attr) : 创建一个目录
    Path createFile(Path path, FileAttribute<?> … arr) : 创建一个文件
    void delete(Path path) : 删除一个文件/目录，如果不存在，执行报错
    void deleteIfExists(Path path) : Path对应的文件/目录如果存在，执行删除
    Path move(Path src, Path dest, CopyOption…how) : 将 src 移动到 dest 位置
    long size(Path path) : 返回 path 指定文件的大小
    
    boolean exists(Path path, LinkOption … opts) : 判断文件是否存在
    boolean isDirectory(Path path, LinkOption … opts) : 判断是否是目录
    boolean isRegularFile(Path path, LinkOption … opts) : 判断是否是文件
    boolean isHidden(Path path) : 判断是否是隐藏文件
    boolean isReadable(Path path) : 判断文件是否可读
    boolean isWritable(Path path) : 判断文件是否可写
    boolean notExists(Path path, LinkOption … opts) : 判断文件是否不存在
  
    SeekableByteChannel newByteChannel(Path path, OpenOption…how) : 获取与指定文件的连接，how 指定打开方式。
    DirectoryStream<Path> newDirectoryStream(Path path) : 打开 path 指定的目录
    InputStream newInputStream(Path path, OpenOption…how):获取 InputStream 对象
    OutputStream newOutputStream(Path path, OpenOption…how) : 获取 OutputStream 对象
    
#Java NIO 
 
    NIO与原来的IO有同样的作用和目的，但是使用的方式完全不同，NIO支持面向缓冲区的、基于
    通道的IO操作。NIO将以更加高效的方式进行文件的读写操作。
    
##通道和缓冲区

###缓冲区(Buffer)

    Java NIO系统的核心在于：通道(Channel)和缓冲区(Buffer)。通道表示打开到 IO 设备(例如：文件、
    套接字)的连接。若需要使用 NIO 系统，需要获取用于连接 IO 设备的通道以及用于容纳数据的缓冲区。
    然后操作缓冲区，对数据进行处理。
    
    缓冲区（Buffer）
    缓冲区（Buffer）：一个用于特定基本数据类型的容器。由 java.nio 包定义的，所有缓冲区都是 Buffer 抽象类的子类。
    
    Java NIO 中的 Buffer 主要用于与 NIO 通道进行交互，数据是从通道读入缓冲区，从缓冲区写入通道中的。
    
    Buffer 就像一个数组，可以保存多个相同类型的数据。根据数据类型不同(boolean 除外) ，有以下 Buffer 常用子类：
        ByteBuffer
        CharBuffer
        ShortBuffer
        IntBuffer
        LongBuffer
        FloatBuffer
        DoubleBuffer
    上述 Buffer 类 他们都采用相似的方法进行管理数据，只是各自管理的数据类型不同而已。
    都是通过如下方法获取一个 Buffer对象：
    static XxxBuffer allocate(int capacity) : 创建一个容量为 capacity 的 XxxBuffer 对象
    
    Buffer 中的重要概念：
    容量 (capacity) ：表示 Buffer 最大数据容量，缓冲区容量不能为负，并且创建后不能更改。
    
    限制 (limit)：第一个不应该读取或写入的数据的索引，即位于 limit 后的数据不可读写。缓冲区的限制不能为负，并且不能大于其容量。
    
    位置 (position)：下一个要读取或写入的数据的索引。缓冲区的位置不能为负，并且不能大于其限制
    
    标记 (mark)与重置 (reset)：标记是一个索引，通过 Buffer 中的 mark() 方法指定 Buffer 中一个特定的 position，之后可以通过调用 reset() 方法恢复到这个 position.
    
    标记、位置、限制、容量遵守不变式： 0 <= mark <= position <= limit <= capacity
    
    Buffer 的常用方法:
    
    Buffer clear() 清空缓冲区并返回对缓冲区的引用
    Buffer flip() 将缓冲区的界限设置为当前位置，并将当前位置重置为 0
    int capacity() 返回 Buffer 的 capacity 大小
    boolean hasRemaining() 判断缓冲区中是否还有元素
    int limit() 返回 Buffer 的界限(limit) 的位置
    Buffer limit(int n) 将设置缓冲区界限为 n, 并返回一个具有新 limit 的缓冲区对象
    Buffer mark() 对缓冲区设置标记
    int position() 返回缓冲区的当前位置 position
    Buffer position(int n) 将设置缓冲区的当前位置为 n , 并返回修改后的 Buffer 对象
    int remaining() 返回 position 和 limit 之间的元素个数
    Buffer reset() 将位置 position 转到以前设置的 mark 所在的位置
    Buffer rewind() 将位置设为 0， 取消设置的 mark
    
    缓冲区的数据操作
    
    Buffer 所有子类提供了两个用于数据操作的方法：get()与 put() 方法
    
    获取 Buffer 中的数据
    get() ：读取单个字节
    get(byte[] dst)：批量读取多个字节到 dst 中
    get(int index)：读取指定索引位置的字节(不会移动 position)
    
    放入数据到 Buffer 中
    put(byte b)：将给定单个字节写入缓冲区的当前位置
    put(byte[] src)：将 src 中的字节写入缓冲区的当前位置
    put(int index, byte b)：将指定字节写入缓冲区的索引位置(不会移动 position)
    
    直接与非直接缓冲区
    字节缓冲区要么是直接的，要么是非直接的。如果为直接字节缓冲区，则 Java 虚拟机会尽最大努力直接在
    此缓冲区上执行本机 I/O 操作。也就是说，在每次调用基础操作系统的一个本机 I/O 操作之前（或之后），
    虚拟机都会尽量避免将缓冲区的内容复制到中间缓冲区中（或从中间缓冲区中复制内容）。
    
    直接字节缓冲区可以通过调用 allocateDirect() 工厂方法来创建。此方法返回的缓冲区进行分配和取消
    分配所需成本通常高于非直接缓冲区。直接缓冲区的内容可以驻留在常规的垃圾回收堆之外，因此，它们对
    应用程序的内存需求量造成的影响可能并不明显。所以，建议将直接缓冲区主要分配给那些易受基础系统的
    本机 I/O 操作影响的大型、持久的缓冲区。一般情况下，最好仅在直接缓冲区能在程序性能方面带来明显好
    处时分配它们。
    
    直接字节缓冲区还可以通过 FileChannel 的 map() 方法 将文件区域直接映射到内存中来创建。该方法返回
    MappedByteBuffer 。Java 平台的实现有助于通过 JNI 从本机代码创建直接字节缓冲区。如果以上这些缓冲区
    中的某个缓冲区实例指的是不可访问的内存区域，则试图访问该区域不会更改该缓冲区的内容，并且将会在
    访问期间或稍后的某个时间导致抛出不确定的异常。
    
    字节缓冲区是直接缓冲区还是非直接缓冲区可通过调用其 isDirect() 方法来确定。提供此方法是为了能够在
    性能关键型代码中执行显式缓冲区管理。

###通道(Channel)

    由 java.nio.channels 包定义的。Channel 表示 IO 源与目标打开的连接。Channel 类似于传统的“流”。
    只不过 Channel本身不能直接访问数据，Channel 只能与Buffer 进行交互。
    
    Java 为 Channel 接口提供的最主要实现类如下：
    
    FileChannel：用于读取、写入、映射和操作文件的通道。
    DatagramChannel：通过 UDP 读写网络中的数据通道。
    SocketChannel：通过 TCP 读写网络中的数据。
    ServerSocketChannel：可以监听新进来的 TCP 连接，对每一个新进来的连接都会创建一个 SocketChannel。
    
    FileChannel 的常用方法
    
    int read(ByteBuffer dst) 从 Channel 中读取数据到 ByteBuffer
    long read(ByteBuffer[] dsts) 将 Channel 中的数据“分散”到 ByteBuffer[]
    int write(ByteBuffer src) 将 ByteBuffer 中的数据写入到 Channel
    long write(ByteBuffer[] srcs) 将 ByteBuffer[] 中的数据“聚集”到 Channel
    long position() 返回此通道的文件位置
    FileChannel position(long p) 设置此通道的文件位置
    long size() 返回此通道的文件的当前大小
    FileChannel truncate(long s) 将此通道的文件截取为给定大小
    void force(boolean metaData) 强制将所有对此通道的文件更新写入到存储设备中
  
  
 ## NIO的非阻塞式网络通信
 
    传统的 IO 流都是阻塞式的。当一个线程调用 read() 或 write()时，该线程被阻塞，直到有一些数据被读取或写入，
    该线程在此期间不能执行其他任务。因此，在完成网络通信进行 IO 操作时由于线程会阻塞，所以服务器端必须为每个
    客户端都提供一个独立的线程进行处理，当服务器端需要处理大量客户端时，性能急剧下降。
    
    Java NIO 是非阻塞模式的。当线程从某通道进行读写数据时，若没有数据可用时，该线程可以进行其他任务。线程通常将非阻塞 IO 的空闲时
    间用于在其他通道上执行 IO 操作，所以单独的线程可以管理多个输入和输出通道。因此，NIO 可以让服务器端使用一个或有限几个线程来同时
    处理连接到服务器端的所有客户端。
    
    选择器（Selector）
    
    选择器（Selector） 是 SelectableChannle 对象的多路复用器，Selector 可以同时监控多个 SelectableChannel 的 IO 状况，
    也就是说，利用 Selector可使一个单独的线程管理多个 Channel。Selector 是非阻塞 IO 的核心。
    
    当调用 register(Selector sel, int ops) 将通道注册选择器时，选择器对通道的监听事件，需要通过第二个参数 ops 指定。
    
    可以监听的事件类型（可使用 SelectionKey 的四个常量表示）：
    读 :   SelectionKey.OP_READ （1）
    写 :   SelectionKey.OP_WRITE （4）
    连接 : SelectionKey.OP_CONNECT （8）
    接收 : SelectionKey.OP_ACCEPT （16）
    
    若注册时不止监听一个事件，则可以使用“位或”操作符连接。
    SelectionKey.OP_READ | SelectionKey.OP_WRITE
    
    Selector 的常用方法
    
    Set<SelectionKey> keys() 所有的 SelectionKey 集合。代表注册在该Selector上的Channel
    selectedKeys() 被选择的 SelectionKey 集合。返回此Selector的已选择键集
    int select() 监控所有注册的Channel，当它们中间有需要处理的 IO 操作时，
                 该方法返回，并将对应得的 SelectionKey 加入被选择的SelectionKey 集合中，
                 该方法返回这些 Channel 的数量。
    int select(long timeout) 可以设置超时时长的 select() 操作
    int selectNow() 执行一个立即返回的 select() 操作，该方法不会阻塞线程
    Selector wakeup() 使一个还未返回的 select() 方法立即返回
    void close() 关闭该选择器
    
    SelectionKey 表示 SelectableChannel 和 Selector 之间的注册关系。
    
    int interestOps() 获取感兴趣事件集合
    int readyOps() 获取通道已经准备就绪的操作的集合
    SelectableChannel channel() 获取注册通道
    Selector selector() 返回选择器
    boolean isReadable() 检测 Channal 中读事件是否就绪
    boolean isWritable() 检测 Channal 中写事件是否就绪
    boolean isConnectable() 检测 Channel 中连接是否就绪
    boolean isAcceptable() 检测 Channel 中接收是否就绪
    
#Java网络编程
##Socket是什么
    Sokect是IP地址与端口的描述协议(RFC793) TCP/IP协议相关的API的总称
    是网络API的集合实现 涵盖了Stream Socket / Datagram Socket 
    在网络传输中用于唯一标识两个端点之间的连接
    
##Socket与TCP UDP

    TCP是面向连接的通信协议，所以只能用于端到端的通信
    UDP是面向无连接的通信协议，所以可以实现广播发送
    
###UDP是什么

    UDP(User Datagram Protocol)是一个简单的面向数据报文的传输层协议(RFC 768) 
    是非连接的
    
    Bit# 0          7 8          15 16          23 24          31
             source port           |              destination
             length                |              header and checksum  
             
    UDP包的最大长度
    
    16位即2个字节存储长度信息
    2^16 - 1 = 65536 - 1  = 65535
    自身协议占用64位(8字节)
    UDP包的最大长度 = 65535 - 8 = 65507 Byte
    
    UDP单播 广播 多播
    
    单播-点对点
    多播-组播
    广播-发送给所有设备
    
    广播地址
    
    255.255.255.255为受限广播地址
    C网(C类IP)的广播地址一般为xxx.xxx.xxx.255(192.168.1.255)
    D类IP地址为多播预留地址
    
###TCP是什么
 
    TCP(Transmission Control Protocol)是一种面连接 可靠的 基于字节流的传输控制协议(RFC 793)
    
    TCP机制
    
        三次握手 四次挥手
        具有校验机制 可靠 数据稳定传输
        
    TCP连接可靠性
        
        三次握手
        
        SYN(x = rand()) --------------------------> SYN ACK (x + 1 y = rand())
                                                       |
                                                       |
        ACK   <----------------------------------------|  
        
        x+1 y+1 --------------------------------------->    
        
        四次挥手
        
        ESTABL-ISHED                                     ESTABL-ISHED 
                    FIN =1 seq=u
        FIN-WAIT-1-------------------------------------->CLOSE-WAIT
                      ACK =1 seq=v ack = u+1                 | |
                  <------------------------------------------- |  
                      FIN = 1 ACK = 1 seq = w    ack = u+1     |
        FIN-WAIT-2<---------------------------------------------                                                 
                                                            
                       ACK = 1 seq = u+1 ack = w+1                                     
        TIME-WAIT ---------------------------------------->LAST-ACK
    
    TCP传输可靠性
    
        排序 顺序发送 顺序组装
        丢弃 超时