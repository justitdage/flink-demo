package com.example.demo01

import org.apache.flink.api.scala.{AggregateDataSet, DataSet, ExecutionEnvironment}

object FlinkFileCount {

  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    val source: DataSet[String] = env.readTextFile("e:\\words.txt", "utf-8")

    import org.apache.flink.api.scala._

    val result: AggregateDataSet[(String, Int)] = source.flatMap(_.split(" "))
      .map((_, 1))
      .groupBy(0)
      .sum(1)

    result.print()

    result.writeAsText("e:\\result.txt")

    env.execute("FlinkFileCount")


  }


}
