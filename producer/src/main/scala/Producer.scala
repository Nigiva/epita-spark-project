import ujson._
import scala.io.Source
import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.logging.log4j.{Logger, LogManager}
import scala.collection.mutable.ArrayBuffer
object Producer {
    lazy val logger: Logger = LogManager.getLogger(getClass.getName)

    val props = new Properties()

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")    // il faudra plusieurs broker
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "SerdeDrone")
    // props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serialization)

    // récupère le json sous forme d'array de map des droneReports
    def readJson(filename: String) = {
        val jsonString = Source.fromFile(filename).getLines().mkString
        val data = ujson.read(jsonString)
        data("reports").arr
    }

    // envoie UN report dans la stream
    def sendReport(droneReport: ujson.Value, producer: KafkaProducer[String, SerdeDrone]) = {
        val record = new ProducerRecord[String, SerdeDrone]("drone-report", droneReport("reportId").toString, new SerdeDrone(droneReport))
        producer.send(record, (metadata: RecordMetadata, exception: Exception) => {
            if (exception != null) {
                exception.printStackTrace()
            } else {
                logger.info(s"Metadata about the sent record: $metadata")
            }
        })
    }

    // envoie tous les reports dans l'array dans la stream
    def sendReports(droneReports: ArrayBuffer[ujson.Value]) = {
        val producer = new KafkaProducer[String, SerdeDrone](this.props)

        droneReports.foreach { record => sendReport(record, producer) }
        logger.info("Drone reports send")

        producer.close()
        logger.info("Producer closed")
    }
    
    def run() = {
        // je sais pas comment ils sont rangés et créer là donc faudra vérifier
        val droneReports = readJson("../json/s1.json")


        val test = readJson("../test.json")
        /*println(test.getClass.getName)
        println(test(0).getClass.getName)
        println(test(0).toString)*/
        sendReports(test)
    }
}
