package com.example.demp02

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}

object ReduceStream {

  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._

    val source: DataStream[(String, Int)] = env.fromElements(("a", 1), ("b", 1), ("a", 5), ("c", 1))

    val tuple: KeyedStream[(String, Int), Tuple] = source.keyBy(0)

    val result: DataStream[(String, Int)] = tuple.reduce((x1, x2) => (x1._1, x1._2 + x2._2))

    result.print()

    env.execute()



  }
}
