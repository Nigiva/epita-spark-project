import java.util
import java.util.Properties
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.logging.log4j.{LogManager, Logger}
// import org.json4s.native.Serialization
import scala.collection.JavaConverters._
import java.time.Duration


object Consumer extends App {
    lazy val logger: Logger = LogManager.getLogger(getClass.getName)
    val props = new Properties()

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    // props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Serialization)
    // props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Serialization)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "0001")
    
    def receiveReport(): Unit = {
        val records: ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(100))
        records.asScala.foreach { record =>
            println(s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")

        receiveReport()
        }
    }

    val topic = "drone-report"
    val consumer = new KafkaConsumer[String, String](props)
    consumer.subscribe(util.Collections.singletonList(topic))

    receiveReport()

}
