"""Action"""

import sys
import logging
from src.constants import Constants

class Action(object):
    """ Action """

    def __init__(self, server, node):
        """Inits"""
        self.server = server
        self.node = node
        self.actions = None


    def get_pending(self):
        self.actions = self._get_pending(self.node)
        return len(self.actions)

    def _get_pending(self, node):
        '''Get pending actions for node'''

        logging.info('Getting Node Actions')

        endpoint = '/api/actions/pending/%d' % node.get_id()
        data = self.server.get(endpoint, None)

        logging.info('Got Pending Actions: %s', data)
        return data

    def has_pending(self):
        '''Check if there are pending actions'''
        num_pending = len(self.actions)
        logging.info('Has %d Pending Actions', num_pending)
        return num_pending > 0

    def update_action_status(self, action, status):
        '''Update status for the action'''

        logging.info('Updating Action (%s) Status to: %s', action['action'], status)

        action['status'] = status
        return self.server.put('/api/actions', action)


    def respond_to_pending(self):
        for action in self.actions:
            self._respond_to_action(action)


    def _respond_to_action(self, action):
        '''Respond to Actions from the server'''

        action_name = action['action']
        args = action['args']

        logging.info('Responding to Action: %s', action)

        #Mark action as completed
        self.update_action_status(action, Constants.ACTION_STATUS_COMPLETED)

        #TODO respond to all actions
        if action_name == Constants.ACTION_KILL:
            message = 'Shutting down node.'
            logging.info(message)
            sys.exit()

