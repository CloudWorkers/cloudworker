#!/usr/bin/env python
'''CloudWorker Node'''

import logging
import time

from src.constants import Constants
from src.server import Server
from src.node import Node
from src.config import Config
from src.action import Action
from src.worker import Worker
from src.output import Output


def start():
    '''Run the Application'''
    logging.basicConfig(level=logging.INFO,
                        format='%(asctime)-15s %(levelname)-6s %(message)s',
                        datefmt='%b %d %H:%M:%S')

    logging.info('Starting Cloud Worker Node')
    logging.info('--------------------------')


    credentials = {'secret': Constants.NODE_SECRET,
                   'client_id': Constants.CLIENT_ID,
                   'client_secret': Constants.CLIENT_SECRET,
                   'username': Constants.USERNAME,
                   'password': Constants.PASSWORD}

    server = Server(Constants.BASE_URL, credentials)

    node = Node(server)

    #Send the hostname, ip etc to the server
    node.send_info()

    #Update the node status to ready
    node.update_node_status(Constants.STATUS_READY)

    #Get Config
    config = Config(server, node)

    actions = Action(server, node)
    workers = Worker(server, node)
    output = Output(server, node)

    #Loop forever
    while True:
        logging.info('Looping')

        #Update last seen date
        node.update_node_date()

        #Get config
        config.refresh()

        #Get actions
        num_pending = actions.get_pending()

        #Respond to actions
        if actions.has_pending():
            message = "Responding to %d Actions ..." % len(num_pending)
            output.send(message)

            actions.respond_to_pending()


        #Get workers/commands
        workers.get()

        #TODO
        #Respond to/run commands
        #Send output to server


        logging.info('Sleeping for %d seconds ...', \
                     config.get(Constants.CONFIG_POLL_PERIOD))
        time.sleep(config.get(Constants.CONFIG_POLL_PERIOD))








