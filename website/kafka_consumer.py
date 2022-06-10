# ---------------------------------------------------------------------------- #
#                                    IMPORTS                                   #
# ---------------------------------------------------------------------------- #

import logging
from kafka import KafkaConsumer
import json

from config import parameters

# ---------------------------------------------------------------------------- #
#                                  PARAMETERS                                  #
# ---------------------------------------------------------------------------- #
KAFKA_ADDRESS = parameters["kafka_adress"]
KAFKA_PORT = parameters["kafka_port"]

# ---------------------------------------------------------------------------- #
#                                   FUNCTIONS                                  #
# ---------------------------------------------------------------------------- #
def get_alert_and_report():
    try:
        consumer = KafkaConsumer(
            "alerts", bootstrap_servers=["{}:{}".format(KAFKA_ADDRESS, KAFKA_PORT)]
        )
        new_messages = [json.loads(message)[0] for message in consumer]
    except:
        logging.warning("Could not connect to Kafka")
        return ([], [])
    alert = []
    report = []
    return alert, report
