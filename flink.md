### Flink

```shell
1.介绍
	Flink 整个系统主要由两个组件组成，分别为 JobManager 和 TaskManager，Flink ，JobManager 为 Master 节点，TaskManager 为 Worker（Slave）节点。所有组件之间的通信都是借助于 Akka Framework，包括任务的状态以及 Checkpoint 触发等信息

2.启动模式
Yarn Session 这个Flink集群会常驻在Yarn集群中，除非手动停止
yarn-cluster 每次提交job都会创建一个新的Flink集群

3.启动脚本
bin/flink run -m yarn-cluster -yn 2 -yjm 1024 -ytm 1024 \
examples/batch/WordCount.jar \
-input hdfs://node01:8020/words.txt \
-output hdfs://node01:8020/output1
注意：client端必须要设置YARN_CONF_DIR或者HADOOP_CONF_DIR或者HADOOP_HOME环境变量，通过这个环境变量来读取YARN和HDFS的配置信息，否则启动会失败。

4.代码
     //对数据进行处理
     val result: DataStream[(String, Int)] = sourceStream
          .flatMap(x => x.split(" ")) //按照空格切分
          .map(x => (x, 1))           //每个单词计为1
          .keyBy(0)                   //按照下标为0的单词进行分组      
          .timeWindow(Time.seconds(2),Time.seconds(1)) //每隔1s处理2s的数据
          .sum(1)            //按照下标为1累加相同单词出现的次数    

5.并行度
* Operator Level（算子级别）
* Execution Environment Level（执行环境级别）
* Client Level（客户端级别）
* System Level（系统级别）

6.自定义数据源
自定义单并行度数据源 继承SourceFunction来自定义单并行度source
自定义多并行度数据源 继承ParallelSourceFunction来自定义多并行度的source
         
7.算子
DataStream 转换算子
map、filter、flatMap、keyBy、sum、reduce、 union、connect、split、select
重分区算子
         随机分区 dataStream.shuffle() 
         均匀分区 dataStream.rebalance()
         dataStream.rescale() 跟rebalance有点类似，单不是全局的，这种方式仅发生在一个单一的节点，因此没有跨网络的数据传输
         自定义分区 自定义分区需要实现Partitioner接口  

DataSet 转换算子
* Map* FlatMap* MapPartition* Filter* Reduce
* Aggregate
  * sum、max、min等
* Distinct* Join* OuterJoin* Cross* Union* First-n* Sort Partition

8. 广播变量
（1）：初始化数据
DataSet<Integer> toBroadcast = env.fromElements(1, 2, 3)
（2）：广播数据
.withBroadcastSet(toBroadcast, "broadcastSetName");
（3）：获取数据
Collection<Integer> broadcastSet = getRuntimeContext().getBroadcastVariable("broadcastSetName");

9.  Flink之Counter（计数器/累加器）
(1)：创建累加器
private IntCounter numLines = new IntCounter(); 
(2)：注册累加器
getRuntimeContext().addAccumulator("num-lines", this.numLines);
(3)：使用累加器
this.numLines.add(1); 
(4)：获取累加器的结果
myJobExecutionResult.getAccumulatorResult("num-lines")

9.分布式缓存
(1)：使用Flink运行环境调用registerCachedFile注册一个分布式缓存
env.registerCachedFile("hdfs:///path/to/your/file", "hdfsFile")  
(2): 获取分布式缓存
File myFile = getRuntimeContext().getDistributedCache().getFile("hdfsFile");

10.state概述
	Flink 是一个默认就有状态的分析引擎，前面的WordCount 案例可以做到单词的数量的累加，其实是因为在内存中保证了每个单词的出现的次数，这些数据其实就是状态数据。但是如果一个 Task 在处理过程中挂掉了，那么它在内存中的状态都会丢失，所有的数据都需要重新计算。从容错和消息处理的语义（At -least-once 和 Exactly-once）上来说，Flink引入了State 和 CheckPoint。
	
State一般指一个具体的 Task/Operator 的状态，State数据默认保存在 Java 的堆内存中

Flink中有两种基本类型的State, Keyed State和Operator State
	operator state是task级别的state，说白了就是每个task对应一个state
	顾名思义就是基于KeyedStream上的状态，这个状态是跟特定的Key 绑定的。KeyedStream流上的每一个Key，都对应一个State。Flink针对 Keyed State 提供了以下可以保存State的数据结构.
         
         
    
    

    
```





