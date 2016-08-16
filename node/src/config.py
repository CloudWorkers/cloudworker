'''Config'''

import logging as log

class Config(object):
    '''Config'''

    def __init__(self, server, node):
        '''Init '''
        self.server = server
        self.node = node
        self.config = self._get_config(node)


    def get(self, key):
        '''Get configuration item'''
        return self.config[key]

    def refresh(self):
        '''Get new config from server'''
        self.config = self._get_config(self.node)

    def _get_config(self, node):
        '''Get config for node'''

        log.info('Getting Node Config')

        endpoint = '/api/configs/node/%d' % node.get_id()
        data = self.server.get(endpoint, None)
        #Unpack results
        result = {}
        for item in data:
            result[item['item']] = int(item['value'])
        log.info('Got Config: %s', result)
        return result

