package com.example.demp02

import org.apache.flink.api.scala.ExecutionEnvironment

import scala.collection.mutable.ArrayBuffer

object MapPartitionDataSet {
  def main(args: Array[String]): Unit = {
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._

    val arrayBuffer = new ArrayBuffer[String]()
    arrayBuffer.+=("hello world1")
    arrayBuffer.+=("hello world2")
    arrayBuffer.+=("hello world3")
    arrayBuffer.+=("hello world4")
    val collectionDataSet: DataSet[String] = environment.fromCollection(arrayBuffer)

    val resultPartition: DataSet[String] = collectionDataSet.mapPartition(eachPartition => {
      eachPartition.map(eachLine => {
        val returnValue = eachLine + " result"
        returnValue
      })
    })
    resultPartition.print()

  }

}
