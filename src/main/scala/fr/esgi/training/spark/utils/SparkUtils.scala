package fr.esgi.training.spark.utils

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkUtils {

  val sparkConf = new SparkConf()

  def initExercice(exercice : String) = {
    sparkConf.set("spark.app.name", exercice)
  }

  def spark(exercice : String) = {
    val sparkConf = initExercice(exercice);
    sparkConf.set("spark.master", "local[4]")
    sparkConf.set("spark.hadoop.validateOutputSpecs","false")
    sparkConf.set("spark.sql.autoBroadcastJoinThreshold","-1")
    //sparkConf.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.AnonymousAWSCredentialsProvider")


    val session = SparkSession.builder()
      .config(sparkConf)
      .getOrCreate()

    session.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.us-east-1.amazonaws.com")
    session.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", "ASIAUG3H43Y4B3GCHKMC")
    session.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", "UjPH7fBwwU4UXPiWa9ksL1P/RmTwWcv+SkdSR+XX")
    session.sparkContext.hadoopConfiguration.set("fs.s3a.session.token", "FwoGZXIvYXdzEM///////////wEaDM0WUpeJRHG+f85Y8yK9AaXbyNo8Izsp9KWyDPiYNcBoqQvippnC/vIMoKPEQtFiKtL0lRC0rLSnAju0n6QpwrSld/qqIWssg0vhikve45MdAgktAlMSiZ9E/RtQ2RP6aGIlaLa7JMFq22TnOzxY2VD/GLWSh3axr34CH3ScG7EIm0IjV3wBK5WnXPvhg3oa+TpDoaylz5DafYCKbIccogyU0FNI/nEHfQ/axBQCoJl2v1tdX9hCBL1zeAs0ZQlnp8nuF73Teg3Fxa6+KSjoia74BTItOE9d6AUOBNIUof/bsqPnDOAq/Usi9xArMHFaH4UM7XG9DeyX1sviNUZyX/Gw")
    session.sparkContext.hadoopConfiguration.set("com.amazonaws.services.s3.enableV4", "true")
    session.sparkContext.hadoopConfiguration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")

    session
  }


}
