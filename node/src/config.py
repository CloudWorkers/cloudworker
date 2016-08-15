"""Config"""

import logging

class Config(object):
    """ Config """

    def __init__(self, server, node):
        """Inits"""
        self.server = server
        self.node = node
        self.config = self._get_config(node)


    def get(self, key):
        return self.config[key]

    def refresh(self):
        self.config = self._get_config(self.node)

    def _get_config(self, node):
        '''Get config for node'''

        logging.info('Getting Node Config')

        endpoint = '/api/configs/node/%d' % node.get_id()
        data = self.server.get(endpoint, None)
        #Unpack results
        result = {}
        for item in data:
            result[item['item']] = int(item['value'])
        logging.info('Got Config: %s', result)
        return result

