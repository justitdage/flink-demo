package com.example.demo01

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time

object TestParallelism {


  def main(args: Array[String]): Unit = {

    //使用createLocalEnvironmentWithWebUI方法，构建本地流式处理环境
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI()
    //执行环境级别
    //environment.setParallelism(4)

    import org.apache.flink.api.scala._

    //接受socket数据
    val sourceStream: DataStream[String] = environment.socketTextStream("node01", 9999)

    val countStream: DataStream[(String, Int)] = sourceStream
      .flatMap(x => x.split(" ")).setParallelism(5) //算子级别
      .map(x => (x, 1))
      .keyBy(0)
      .timeWindow(Time.seconds(2), Time.seconds(1))
      .sum(1)

    countStream.print()

    environment.execute()

    }
  }
