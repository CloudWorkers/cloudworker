'''Action'''

from src.constants import Constants as C

import sys
import logging

class Action(object):
    '''Action'''

    log = logging.getLogger(C.APP)

    def __init__(self, server, node):
        '''Init '''
        self.server = server
        self.node = node
        self.actions = None


    def get_pending(self):
        '''Get Pending actions from server'''
        self.actions = self._get_pending(self.node)
        return len(self.actions)

    def _get_pending(self, node):
        '''Get pending actions for node'''

        self.log.info('Getting Node Actions')

        endpoint = '/api/actions/pending/%d' % node.get_id()
        data = self.server.get(endpoint, None)

        self.log.info('Got Pending Actions: %s', data)
        return data

    def has_pending(self):
        '''Check if there are pending actions'''

        num_pending = len(self.actions)
        self.log.info('Has %d Pending Actions', num_pending)
        return num_pending > 0

    def update_action_status(self, action, status):
        '''Update status for the action'''

        self.log.info('Updating Action (%s) Status to: %s', action['action'], status)

        action['status'] = status
        return self.server.put('/api/actions', action)


    def respond_to_pending(self):
        '''Respong to all pending actions'''
        for action in self.actions:
            self._respond_to_action(action)


    def _respond_to_action(self, action):
        '''Respond to Actions from the server'''

        action_name = action['action']
        #args = action['args']

        self.log.info('Responding to Action: %s', action)

        #Mark action as completed
        self.update_action_status(action, C.ACTION_STATUS_COMPLETED)

        #TODO respond to all actions
        if action_name == C.ACTION_KILL:
            message = 'Shutting down node.'
            self.log.info(message)
            sys.exit()

