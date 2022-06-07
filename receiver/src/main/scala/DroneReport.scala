case class DroneReport(
  reportID: Int,
  droneID: Int,
  time: String,
  longitude: Double,
  latitude: Double,
  heardWords: Array[String],
  peaceScores: Map[Int, Int]
)

case class Id[Ressource](value: String) extends AnyVal