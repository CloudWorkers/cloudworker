'''Output'''

import logging as log


class Output(object):
    '''Output'''

    def __init__(self, server, node):
        '''Init '''
        self.server = server
        self.node = node


    def send(self, message):
        '''Send Output to server'''
        return self.send_w(message, None)


    def send_w(self, message, worker):
        '''Send Output to server (with Worker info)'''

        log.info('Sending Output to Server: %s', message)

        data = {'message': message,
                'node': self.node.to_serializable(),
                'worker': worker}

        data = self.server.post('/api/outputs', data)


