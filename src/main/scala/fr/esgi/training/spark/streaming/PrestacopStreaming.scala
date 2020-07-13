package fr.esgi.training.spark.prestacop

import fr.esgi.training.spark.utils.SparkUtils
import org.apache.spark.sql.functions.{col, _}
import org.apache.spark.sql.types.DataTypes

case
object PrestacopStreaming {

  val spark = SparkUtils.spark("projectPrestacop");

  def readStream() = {
    val df = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load();

    df
  }

  def alertMessage() = {

  }


  def main(args: Array[String]): Unit = {


    val df = PrestacopStreaming.readStream();

    val aggregateDF = df.withColumn("split_col", split(col("value"), ","))

    val prestacopDataDF = aggregateDF.withColumn("prestacop", col("split_col")).select(
      col("prestacop").getItem(0).as("Summons Number").cast(DataTypes.IntegerType),
      col("prestacop").getItem(1).as("Plate ID").cast(DataTypes.StringType),
      col("prestacop").getItem(2).as("Registration State").cast(DataTypes.StringType),
      col("prestacop").getItem(3).as("Plate Type").cast(DataTypes.StringType),
      col("prestacop").getItem(4).as("Issue Date").cast(DataTypes.DateType),
      col("prestacop").getItem(5).as("Violation Code").cast(DataTypes.IntegerType),
      col("prestacop").getItem(6).as("Vehicle Body Type").cast(DataTypes.StringType),
      col("prestacop").getItem(7).as("Vehicle Make").cast(DataTypes.StringType),
      col("prestacop").getItem(8).as("Issuing Agency").cast(DataTypes.StringType),
      col("prestacop").getItem(9).as("Street Code1").cast(DataTypes.IntegerType),
      col("prestacop").getItem(10).as("Street Code2").cast(DataTypes.IntegerType),
      col("prestacop").getItem(11).as("Street Code3").cast(DataTypes.IntegerType),
      col("prestacop").getItem(12).as("Vehicle Expiration Date").cast(DataTypes.IntegerType),
      col("prestacop").getItem(13).as("Violation Location").cast(DataTypes.StringType),
      col("prestacop").getItem(14).as("Violation Precinct").cast(DataTypes.IntegerType),
      col("prestacop").getItem(15).as("Issuer Precinct").cast(DataTypes.IntegerType),
      col("prestacop").getItem(16).as("Issuer Code").cast(DataTypes.IntegerType),
      col("prestacop").getItem(17).as("Issuer Command").cast(DataTypes.StringType),
      col("prestacop").getItem(18).as("Issuer Squad").cast(DataTypes.StringType),
      col("prestacop").getItem(19).as("Violation Time").cast(DataTypes.StringType),
      col("prestacop").getItem(20).as("Time First Observed").cast(DataTypes.StringType),
      col("prestacop").getItem(21).as("Violation County").cast(DataTypes.StringType),
      col("prestacop").getItem(22).as("Violation In Front Of Or Opposite").cast(DataTypes.StringType),
      col("prestacop").getItem(23).as("House Number").cast(DataTypes.StringType),
      col("prestacop").getItem(24).as("Street Name").cast(DataTypes.StringType),
      col("prestacop").getItem(25).as("Intersecting Street").cast(DataTypes.StringType),
      col("prestacop").getItem(26).as("Date First Observed").cast(DataTypes.IntegerType),
      col("prestacop").getItem(27).as("Law Section").cast(DataTypes.IntegerType),
      col("prestacop").getItem(28).as("Sub Division").cast(DataTypes.StringType),
      col("prestacop").getItem(29).as("Violation Legal Code").cast(DataTypes.StringType),
      col("prestacop").getItem(30).as("Days Parking In Effec").cast(DataTypes.StringType),
      col("prestacop").getItem(31).as("From Hours In Effect").cast(DataTypes.StringType),
      col("prestacop").getItem(32).as("To Hours In Effect").cast(DataTypes.StringType),
      col("prestacop").getItem(33).as("Vehicle Color").cast(DataTypes.StringType),
      col("prestacop").getItem(34).as("Unregistered Vehicle").cast(DataTypes.StringType),
      col("prestacop").getItem(35).as("Vehicle Year").cast(DataTypes.IntegerType),
      col("prestacop").getItem(36).as("Meter Number").cast(DataTypes.StringType),
      col("prestacop").getItem(37).as("Feet From Curb").cast(DataTypes.IntegerType),
      col("prestacop").getItem(38).as("Violation Post Code").cast(DataTypes.StringType),
      col("prestacop").getItem(39).as("Violation Description").cast(DataTypes.StringType),
      col("prestacop").getItem(40).as("No Standing or Stopping Violation").cast(DataTypes.StringType),
      col("prestacop").getItem(41).as("Hydrant Violation").cast(DataTypes.StringType),
      col("prestacop").getItem(42).as("Double Parking Violation").cast(DataTypes.StringType)

    )


    prestacopDataDF.writeStream
      .foreachBatch { (batchDf, batchId) =>
        batchDf.foreach(r =>
          if (r(5) == "requires_human_intervention") {
            alertMessage()
            println(r(5))
          })
      }
      .format("json")
      .outputMode("append")
      .option("path", "s3a://projet-spark-prestacop/")
      .start()


  }
}
