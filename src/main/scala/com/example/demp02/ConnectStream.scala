package com.example.demp02

import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction
import org.apache.flink.streaming.api.scala.{ConnectedStreams, DataStream, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

/**
 * 连接不同类型的流
 */
object ConnectStream {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._

    val firstStream: DataStream[String] = env.fromCollection(Array("hello world","spark flink"))

    val secondStream: DataStream[Int] = env.fromCollection(Array(1,2,3,4))

    val source: ConnectedStreams[String, Int] = firstStream.connect(secondStream)

    val mapResult: DataStream[Any] = source.map(x => x + "abc", y => y * 2)


    val flatMapResult: DataStream[String] = source.flatMap(new CoFlatMapFunction[String, Int, String] {
      override def flatMap1(value: String, out: Collector[String]) = {
        out.collect(value.toUpperCase())
      }

      override def flatMap2(value: Int, out: Collector[String]) = {
        out.collect(value * 2 + "")
      }
    })

    mapResult.print()
    flatMapResult.print()
    env.execute()

  }

}
