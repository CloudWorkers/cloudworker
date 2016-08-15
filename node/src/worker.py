"""Worker"""

import os
import logging

class Worker(object):
    """ Worker """

    def __init__(self, server, node):
        """Inits"""
        self.server = server
        self.node = node
        self.workers = None


    def get(self):
        self.workers = self._get(self.node)

    def _get(self, node):
        '''Get workers for node'''

        logging.info('Getting Workers')

        endpoint = '/api/workers/node/%d' % node.get_id()
        data = self.server.get(endpoint, None)
        logging.info('Got Workers: %s', data)

        return data

    def _run(self, command):
        '''Run a command locally'''

        logging.info('Running Command: %s', command)
        os.system('%s' % command)

