package com.example.demp02

import org.apache.flink.streaming.api.functions.source.{ParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object MyMultipartSourceRun {

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._

    val source: DataStream[Long] = env.addSource(new MyParallelSourceFunction).setParallelism(5)

    //处理
    val resultStream: DataStream[Long] = source.filter(x => x %2 ==0)
    resultStream.setParallelism(2).print()
    env.execute()


  }

  class MyParallelSourceFunction extends ParallelSourceFunction[Long]{

    private var num = 0

    private var isRunning = true

    override def run(ctx: SourceFunction.SourceContext[Long]): Unit = {
      while(true){
        num += 1
        ctx.collect(num)

        Thread.sleep(1000)
      }

    }

    override def cancel(): Unit = {
      isRunning = false
    }
  }
}
