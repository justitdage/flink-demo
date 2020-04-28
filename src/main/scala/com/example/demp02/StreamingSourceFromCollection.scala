package com.example.demp02

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object StreamingSourceFromCollection {

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val array: Array[String] = Array("hadoop java", "hadoop hadoop", "scala")


    import org.apache.flink.api.scala._

    val source: DataStream[String] = env.fromCollection(array)

    val result: DataStream[(String, Int)] = source.flatMap(_.split(" "))
      .map(((_, 1)))
      .keyBy(0)
      .sum(1)

    result.print()

    env.execute()



  }
}
