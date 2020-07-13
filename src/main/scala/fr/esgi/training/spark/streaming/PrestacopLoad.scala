package fr.esgi.training.spark.prestacop

import fr.esgi.training.spark.utils.SparkUtils
import org.apache.spark.sql.functions.col

  case object PrestacopLoad{

  val spark = SparkUtils.spark("projectPrestacop");


  def main(args: Array[String]): Unit = {

    val df = spark
      .read
      .option("header", "true")
      .csv("s3a://projet-spark-prestacop/Parking_Violations_Issued_-_Fiscal_Year_2015.csv")

    //Question1 : Qu'est-ce que est le violation le plus violé
    val countByViolationCode = df.groupBy(col("Violation Code")).count()
    val maxCountViolationCode = countByViolationCode
      .describe("count")
      .filter("summary = 'max'")
        .select("count")
        .collect()(0)(0)
    val maxViolationCode = countByViolationCode.where(col("count") === maxCountViolationCode)
    maxViolationCode.show() // c'est Violation Code : 21, et le nombre est : 1630912

    //Question2 : Plus de violations pendant le matin ou l'après-midi
    val withDaytime = df.withColumn("Daytime", df("Violation Time").substr(-1, 1))
    val countDayTime = withDaytime
      .groupBy("Daytime")
      .count()
      .where(col("Daytime") === 'A' or col("Daytime") === 'P')
    countDayTime.show() // la nombre du matin : 5915666, la nombre de l'apres midi : 5891844. Il n'a pas trop de difference

    //Question3 : la nombre violations par apport a l'annee de voiture
    val groupeByYear = df
      .groupBy("Vehicle Year")
      .count()
      .orderBy(col("count").desc)
    groupeByYear.show()
    // l'ordre descoissant est 2013,2014,2012,2007..., Il n'y a pas trop de relation par apport a l'anneede voiture
    // mais le voiture qui a violé plustot sont des voiture nouveaux

    //Question4 : quel voutoire est le plus souvent violé
    val groupByPlateID = df.groupBy("Plate ID").count().orderBy(col("count").desc)
    // groupByPlateID.show() // le premier et le deuxieme sont n'import quoi
    val maxCountPlateID = groupByPlateID
      .select("count")
      .collect()(2)(0)
    val maxPlateID = groupByPlateID.where(col("count") === maxCountPlateID)
    maxPlateID.show() // Le plus est le voiture qui numéro de plaque d'immatriculation est 44491JW, le nombre total est 1727


  }
}
