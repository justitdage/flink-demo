package com.example.demo01

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object FlinkStream {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val data: DataStream[String] = env.socketTextStream("node01", 9999)


    //导入隐式转换
    import org.apache.flink.api.scala._

    val result: DataStream[(String, Int)] = data.flatMap(x => x.split(" "))
      .map(x => (x, 1))
      .keyBy(0)
      .sum(1)

    result.print()

    env.execute("FlinkStream1")



  }
}
