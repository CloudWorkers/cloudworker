"""Output"""

import logging


class Output(object):
    """ Output """

    def __init__(self, server, node):
        """Inits"""
        self.server = server
        self.node = node


    def send(self, message):
        '''Send Output to server'''
        return self.send_w(message, None)


    def send_w(self, message, worker):
        '''Send Output to server (with Worker info)'''

        logging.info('Sending Output to Server: %s', message)

        data = {'message': message,
                'node': self.node,
                'worker': worker}

        data = self.server.post('/api/outputs', data)


