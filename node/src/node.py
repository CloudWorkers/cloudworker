'''Node'''

from src.constants import Constants as C

import platform
import socket
import logging as log


class Node(object):
    '''Node'''

    def __init__(self, server):
        '''Inits a Node'''
        self.server = server
        self.node_details = self._get_node_details()

    def _get_node_details(self):
        '''Get details for this node'''

        log.info('Getting Node Details')

        endpoint = '/api/nodes/secret/%s' % self.server.get_secret()
        return self.server.get(endpoint, None)


    def to_serializable(self):
        '''Allows object to be serialized'''
        return self.node_details


    def send_info(self):
        '''Send Node (OS Level) Information to the server'''
        self.node_details['os'] = "%s %s"\
                % (platform.system(), platform.release())
        self.node_details['hostname'] = socket.gethostname()
        self.node_details['ip'] = 'todo'
        self.node_details['status'] = C.STATUS_STARTING
        self.update_node_details()


    def get_id(self):
        '''Return the Node ID'''
        return self.node_details['id']

    def update_node_details(self):
        '''Update details for the node'''

        log.info('Updating Node Details')

        return self.server.put('/api/nodes', self.to_serializable())


    def update_node_date(self):
        '''Update the date for the node'''

        log.info('Updating Node Date')

        endpoint = '/api/nodes/date/%d' % self.get_id()
        return self.server.put(endpoint, None)


    def update_node_status(self, status):
        '''Update status for the node'''

        log.info('Updating Node Status to: %s', status)

        self.node_details['status'] = status
        return self.server.put('/api/nodes', self.to_serializable())


