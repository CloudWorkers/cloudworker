"""Node"""

from src.constants import Constants
import platform
import socket
import logging


class Node(object):
    """ Node """

    def __init__(self, server):
        """Inits a Node"""
        self.server = server
        self.node_details = self._get_node_details()
        logging.info(self.node_details)

    def _get_node_details(self):
        '''Get details for this node'''

        logging.info('Getting Node Details')

        endpoint = '/api/nodes/secret/%s' % self.server.get_secret()
        return self.server.get(endpoint, None)


    def send_info(self):
        self.node_details['os'] = "%s %s" % (platform.system(), platform.release())
        self.node_details['hostname'] = socket.gethostname()
        self.node_details['ip'] = 'todo'
        self.node_details['status'] = Constants.STATUS_STARTING
        self.update_node_details()


    def get_id(self):
        return self.node_details['id']

    def update_node_details(self):
        '''Update details for the node'''

        logging.info('Updating Node Details')

        return self.server.put('/api/nodes', self.node_details)


    def update_node_date(self):
        '''Update the date for the node'''

        logging.info('Updating Node Date')

        endpoint = '/api/nodes/date/%d' % self.get_id()
        return self.server.put(endpoint, None)


    def update_node_status(self, status):
        '''Update status for the node'''

        logging.info('Updating Node Status to: %s', status)

        self.node_details['status'] = status
        return self.server.put('/api/nodes', self.node_details)


