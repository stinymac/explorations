#Redis是什么

    开源基于键值的支持多种数据结构的功能丰富且高效的存储服务系统
    
#Redis的特性

    高性能
    
        10W OPS
        C语言实现的内存数据库
        
    持久化
    
        数据更新异步地写入磁盘
        
    多种数据结构
    
        String 
        HashTable  
        LinkedList  
        Set 
        SortSet 
        
        BitMaps 
        HyperLoglog 可以实现唯一值计数
        GEO
    
    支持多语言
    
    功能丰富
        
        发布订阅
        Lua
        事务
        pipeline
        
    简单易用
    
        单线程
        
    主从模式
    
    
    高可用 分布式
    
        Redis-Sentinel
        Redis-Cluster
        
#Redis应用

    缓存
    计数器
    消息队列
    TopN排行榜
    社交应用
    实时系统
 
#Redis常见命令

    keys 
    dbSize
    exists key
    del key [key...]
    expire key seconds  / ttl (-1 key 存在且未设置过期时间) / persist
    type key 
    
#Redis数据结构和内部编码
                     raw
             string  int
                     embstr
                     
                    hashtable
             hash   
                    ziplist
                    
                    linkedlist
       key   list
                    ziplist
                    
                    hashtable
             set
                    intset
                    
                    skiplist
             zset   
                    ziplist
        
#Redis数据类型

    字符串
    
        key      value
        hi       world
        counter  1
        bits     10101010
        
        size限制512M         
        
        get     key  
        set     key value 
        del     key [keys...]  
        incr    key //key自增1 不存在则自增后 get key =  1
        decr    key //key自减1 不存在则自减后 get key = -1
        incrby  key
        decrby  key
        
        使用实例: 计数器 缓存 分布式id生成器
        
        set   key 
        setnx key value //不存在才能设置成功
       
        
        mget //一定范围内可以节省网络时间
        mset
        
        getset 设置新值 返回旧值
        append
        strlen
        
        incrbyfloat
        getrange key start end
        setrange
        
    hash 
    
        hget    key feild 
        hset    key field value
        hsetnx  key field value
        hdel    key feild 
        
        hexists key feild
        hlen key
        
        hmget key feild....
        hmset key feild1 v1 key feild2 v2...
        
        hgetall
        hvals
        hkeys
        
        使用实例: hincrby user:1:info pageview 1 //用户访问计数
        
    list
        
        rpush         key v1...vn
        lpush         key v1...vn
        linsert       key before|after value newValue
        
        lpop          key
        rpop          key
        lrem          key count value 
        ltrim         key start end      
        lrange        key start end(包含end)
        lindex        key index
        llen          key
        
        lset          key indx newValue
        
    set
    
        sadd key v //存在则失败
        srem 
        
        scard
        sismember
        srandmember  
        smembers
        spop
        
        应用实例:用户标签
        
        sdiff
        sinter
        sunion
        
    zset
    
        key score value
        
        
        zadd       key score element ...
        zrem       key element
        zscore     key elemnt
        zincrby    key score element
        zcard      key
        zrang      key start end [withscores]
        
        zrangbyscore     key minscore maxscore [withscores]
        zcount           key minscore maxscore
        zremrangbyrank   key start end
        zremrangbyscore  key minscore maxscore
        
#Redis其他功能

    慢查询

        慢查询发生在redis命令执行阶段
        客户端超时不一定有慢查询 慢查询是客户端发生超时的一种可能因素
        
        慢查询相关的两个配置
        
        slowlog-max-len
        
        先进先出队列
        固定长度
        保存在内存
        默认值 = 128 (通常设置为1000左右)
        
        slowlog-log-slower-than
        
        慢查询阀值(单位 微秒)
        slowlog-log-slower-than = 0 记录所有命令
        slowlog-log-slower-than < 0 不记录任何命令
        默认值 = 10000 微秒 = 10 ms (通常设置为1ms)
        
        慢查询命令
        slowlog get [n] 获取慢查询队列
        slowlog len     
        slowlog reset 
        
    pipeline 
    
        多个命令打包一次执行从而使得时间消耗为一次网络时间加n次命令时间
    
        类似M操作 但M操作是原子的 pipeline 是非原子的 只能作用在一个redis节点
        
    发布订阅
    
        发布者 publisher
        订阅者 subscriber
        频道   channel
        
        发布订阅命令
        
        publish channel message
        subscribe channel
        unsubscribe channel
        
    bitmaps
        
        命令
        
        setbit key offset value
        getbit key offset
        
        bitcount key [start end]
        bitop op destkey key [key] // 多个bitmap的 and(交集) or(并集) not(非) xor(异或)
                                   // 操作结果保存到destkey
        bitpos key targetBit [start] [end]
        
        使用案例:独立用户(量特别大时 较小时用set更优)统计 
        
    hyperloglog
    
        pfadd key element [element...] 向hyperloglog添加元素
        pfcount key [key...] 计算hyperloglog的独立总数
        pfmerge destkey sourcekey [sourcekey...] 合并多个hyperloglog
        
        hyperloglog 是有错误率的(0.81%)
        
    geo
    
        地理信息定位 存储经纬度 计算两地距离 范围等
        
        命令
        
        geo key longitude latitude member [longitude latitude member...]
        geopos key member [member...]
        geodist key member1 member2 [unit] 两个地理位置的距离
        georadius 
        
#Redis持久化

    RDB
        RBD文件创建
        
        执行save命令(同步) 生成RBD文件 替换旧的RDB文件
        执行bgsave命令(异步) 生成RBD文件 替换旧的RDB文件
        自动生成RDB文件 
        
        默认配置
        dbfilename dunmp.rdb //文件名
        dir ./
        stop-writes-on-bgsave-error yes
        rdbcompression yes
        rdbchecksum yes
        
        //     seconds   changes
        save   900       1
        save   300       10
        save   60        10000
        
        一般会关闭自动生成RDB文件
        
        触发机制
        
        全量复制
        debug load
        shutdown
        
    AOF
       
       RDB的问题 
       
       消耗时间 性能
       不可控 丢失数据
       
       AOF 操作命令追加到文件中
       
       AOF 三种策略
       
       always   把缓冲区fsync到硬盘
       everysec 每秒把缓冲区fsync到硬盘
       no       OS决定fsync到硬盘   
        
       AOF重写
       
       相当于对记录的命令筛选出最终有效的命令  
       
       默认配置
       
       appendonly yes
       appendfilename "appendonly.aof
       appendfsync everysec
       dir ./
       no-appendfsync-on-rewrite yes //重写时关闭aof
       auto-aof-rewrite-min-size 64mb
       auto-aof-rewrite-percentage 100
       
       AOF重写命令
       
       bgrewriteaof
       
       AOF重写配置
       
       auto-aof-rewrite-min-size   AOF文件重写需要的尺寸
       auto-aof-rewrite-percentage AOF文件增长率
       
#Redis持久化开发运维常见问题

    fork操作
    
    同步操作
    与内存信息量相关 内存越大 耗时越长
    info: last_fork_usec //配置中 上次fork耗时
    
    子进程开销和优化
    
    CPU RDB或AOF文件生成 
        优化:不做CPU绑定 不做CPU密集类型部署
        
    内存 fork内存开销 copy-on-write
         优化: echo never > /sys/kernel/mm/transparent_hugepage/enable
         
    AOF追加阻塞


#Redis复制的原理与优化
    
    一个master可以有多个slave
    一个slave只能有一个master
    数据流向是单向的 master到slave
    
    主从复制配置
    
    命令操作方式
    复制命令 slaveof 
    取消命令 slaveof no one
    
    从节点配置方式
    slaveof ip port
    slave-read-only yes
    
    全量复制
    
    psync ? -1
    
    FULLRESYNC{runId} {offset}
    
    save masterInfo
    
    bgsave
    
    send RDB
    
    send buffer
    
    flush old data
    
    loadRDB
    
    部分复制
    
    connection lost
    
    send buffer
    
    connnection to master
    
    psync {offset} {runId}
    
    CONTINUE
    
    send partial data
    
    故障处理
    
    slave宕机-故障转移 
    
    master宕机-故障处理
    
    主从复制在开发运维中的使用及问题
    
    读写分离   读流量分摊到从节点 但可能会出现数据复制延迟 读到过期数据 从节点故障
    
    配置不一致 
    
    规避全量复制
    
    第一次全量复制不可避免
    其后采用小的主节点 低峰时复制
    ...
    
    规避复制风暴
    
    较多的从节点同时复制
    
#Redis Sentinel

    @see /explorations/storage/redis/src/main/resources/images/sentinel
    
    实现原理
    
        三个定时任务
        每10秒给个sentinel对master和slave执行info
            发现slave节点
            确认主从关系
        每2秒每个sentinel通过master节点的channel交换信息（pub/sub）
            通过_sentinel_:hello channel交互
            交互对节点的看法和自身信息
            
        每1秒每个sentinel对其他sentinel和redis执行ping
            心跳检测 
                      
        主观下线和客观下线  
        
        配置
        sentinel moniter <masterName> <ip> <port> <quornum> 
        sentinel down-after-milliseconds <masterName> <timeout>//timeout 主观下线
        
        主观下线 每个sentinel节点对redis节点失败的"偏见"
        客观下线 所有sentinel节点对redis节点失败"达成共识" (超过quorum个的统一主观下线)
        
        sentinel is-master-down-by-addr
    
    领导者选举
        通过sentinel is-master-down-by-addr命令希望成为领导者
    每个做主观下线的sentinel节点向其他Sentinel节点发送命令 要求成为领导者
    收到命令的Sentinel节点如果没有同意过其他节点发送的命令 将同意请求
    sentinel节点发现自己的票数超过sentinel集合的半数且超过quorum 他将成为领导者
    如果有多个节点成为了领导者 过段时间重新选举
    
    故障转移
    
        领导者选举成功后 从slave节点中选出一个合适的节点作为新的master节点
        对选取的slave节点执行slaveof no one 命令让其成为master节点
        向剩余的节点发送命令 让他们成为新master节点的slave 复制(规则和parallel-syncs参数有关)
        更新原来的master节点配置为slave 并保持对其的关注 如果原来的master节点恢复 命令去复制master节点
        
        选择 合适的 slave节点
        
        选择slave-priority最高的slave节点 否则继续
        选择复制偏移量最大的节点 否则继续
        选择runId最小的节点
        
        高可用读写分离
        
        @see redis.clients.jedis.JedisSentinelPool
    
#Redis Cluster

    数据分布
    
        顺序分区和哈希分区
        
        @see /explorations/storage/redis/src/main/resources/images/数据分布.png
        
        一致性哈希
        
        @see /explorations/storage/redis/src/main/resources/一致性哈希算法.pdf
        
        虚拟槽分区
        
    搭建集群
    
        基本架构
        @see /explorations/storage/redis/src/main/resources/images/redis-cluster-arth-1.png
        
        meet
        
        @see /explorations/storage/redis/src/main/resources/images/meet.png
        
        指派槽
        @see /explorations/storage/redis/src/main/resources/images/指派槽.png
                
        
        搭建(原生安装)
        
        1.配置        
        @see /explorations/storage/redis/src/main/resources/images/cluster-config/cluster-cfg-1.png
        2.meet(和每个节点meet)
        redis-cli -p <port> cluster meet <ip> <port>
        ...
        3.分配槽
        
        4.分配主从
        
        搭建(官方工具)
        
        
    集群伸缩
    
        扩容集群
        
        准备新节点
        加入集群
        
        cluster meet <ip> <port> / redis-trib.rb
        
        
        迁移槽和数据
        
        @see /explorations/storage/redis/src/main/resources/images/slot-trans.png
        
    客户端路由
    
    moved异常
    ask重定向
    smart客户端
    @see /explorations/storage/redis/src/main/resources/images/smart-cluster-client.png
    @see /explorations/storage/redis/src/main/resources/images/smart-cluster-client-cmd-exec.png
    @see redis.clients.jedis.JedisCluster
    @see redis.clients.jedis.JedisClusterCommand
    
    集群原理
    
    
    
    开发运维中的问题   
    
    故障转移
        故障发现 
        ping/pong消息
        主观下下线和客观下线
        
        故障恢复   
    
#Redis缓存设计与优化

    缓存的收益和成本
    
        收益:加速读写 降低后端负载
        成本:数据不一致问题 代码中增加了缓存逻辑
        
    缓存的更新策略
    
        LRU/LFU/FIFO //redis中即maxmemory-policy的配置
        超时删除 // expire
        主动更新 //开发编码控制
    
    缓存的粒度控制
    
        通用性
        占用空间
        代码维护
    
    缓存穿透优化
    
         大量请求不命中
         
         解决方式 
         缓存空对象(需要更多的键/缓存层和存储层数据短期不一致)
         布隆过滤器拦截
         
    无底洞问题优化
    
        增加机器但性能没有提升反而下降
        
        优化
        优化命令本身
        减少网络通信次数
        降低接入成本 (长连接/连接池/NIO)
    
    缓存雪崩优化
        
        缓存层高可用
        客户端降级
        提前演练
        
    热点key重建优化
    
        问题:
        @see /explorations/storage/redis/src/main/resources/images/hotkey-rebuild-proberlm.png
        
        优化:
        互斥锁
        永不过期(在代码逻辑中异步处理过期重建但这会有一定时间的数据不一致)

            
#Redis云平台CacheCloud    
   
   
#Redis实现分布式布隆过滤器

    问题 
    
    50亿数据中 快速准确的判断10万个数据是否在其中  
    垃圾邮件过滤
    错误单词检测
    网络爬虫重复url检测
    
    布隆过滤器
    
    实现原理:一个很长的二进制向量和若干个哈希函数
    参数: m个二进制向量 n个预备数据  k个hash函数
    
    误差率
    @see /explorations/storage/redis/src/main/resources/images/布隆过滤器误差率.png
    
    redis实现bloom过滤器
    
#Redis开发规范

        
    
    