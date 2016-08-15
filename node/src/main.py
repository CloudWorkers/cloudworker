#!/usr/bin/env python
'''CloudWorker Node'''

from src.constants import Constants as C

from src.server import Server
from src.node import Node
from src.config import Config
from src.action import Action
from src.worker import Worker
from src.output import Output

import logging
import time

def configure_log():
    '''Configure Logging for the application'''
    logging.basicConfig(level=logging.INFO,
                        format='%(asctime)-15s %(levelname)-6s %(message)-80s (%(filename)s:%(lineno)s)',
                        datefmt='%b %d %H:%M:%S')
    #Stop libraries from logging too much
    logging.getLogger("requests").setLevel(logging.WARNING)
    logging.getLogger("urllib3").setLevel(logging.WARNING)
    log = logging.getLogger(C.APP)
    return log


def start():
    '''Start the Application'''

    log = configure_log()
    log.info('Starting Cloud Worker Node Agent')
    log.info('--------------------------')

    credentials = {'secret': C.NODE_SECRET,
                   'client_id': C.CLIENT_ID,
                   'client_secret': C.CLIENT_SECRET,
                   'username': C.USERNAME,
                   'password': C.PASSWORD}

    server = Server(C.BASE_URL, credentials)

    node = Node(server)

    #Send the hostname, ip etc to the server
    node.send_info()

    #Update the node status to ready
    node.update_node_status(C.STATUS_READY)

    #Get Config
    config = Config(server, node)

    actions = Action(server, node)
    workers = Worker(server, node)
    output = Output(server, node)

    #Loop forever
    while True:
        log.info('Looping')
        log.info('--------------------------')

        #Update last seen date
        node.update_node_date()

        #Get config
        config.refresh()

        #Get actions
        num_pending = actions.get_pending()

        #Respond to actions
        if actions.has_pending():
            message = 'Responding to %d Actions ...' % len(num_pending)
            output.send(message)

            actions.respond_to_pending()


        #Get workers/commands
        workers.refresh()

        #TODO
        #Respond to/run commands
        #Send output to server


        log.info('Sleeping for %d seconds ...', 
                 config.get(C.CONFIG_POLL_PERIOD))
        time.sleep(config.get(C.CONFIG_POLL_PERIOD))








