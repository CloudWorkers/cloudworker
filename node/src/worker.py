'''Worker '''

from src.constants import Constants as C

import os
import logging

class Worker(object):
    '''Worker '''

    log = logging.getLogger(C.APP)

    def __init__(self, server, node):
        '''Init '''
        self.server = server
        self.node = node
        self.workers = None

    def refresh(self):
        '''Get Workers'''
        self.workers = self._get(self.node)
        self.log.info('Got %d Workers', len(self.workers))


    def to_serializable(self):
        '''Allows object to be serialized'''
        #TODO Probably need to implement
        pass


    def _get(self, node):
        '''Get workers for node'''

        self.log.info('Getting Workers')

        endpoint = '/api/workers/node/%d' % node.get_id()
        return self.server.get(endpoint, None)


    def _run(self, command):
        '''Run a command locally'''

        self.log.info('Running Command: %s', command)
        os.system('%s' % command)

