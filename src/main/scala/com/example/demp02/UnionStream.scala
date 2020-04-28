package com.example.demp02

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * 相同类型的流
 */
object UnionStream {

  def main(args: Array[String]): Unit = {

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._
    val firstStream: DataStream[String] = environment.fromCollection(Array("hello spark","hello flink"))
    val secondStream: DataStream[String] = environment.fromCollection(Array("hadoop spark","hive flink"))

    val source: DataStream[String] = firstStream.union(secondStream)


    source.print()
    environment.execute()


  }

}
