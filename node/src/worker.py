'''Worker '''

from src.processor import Processor

import multiprocessing
import logging as log

class Worker(object):
    '''Worker '''

    def __init__(self, server, node):
        '''Init '''
        self.server = server
        self.node = node
        self.workers = None
        self.current_workers = {}
        self.processor = Processor()

    def refresh(self):
        '''Get Workers'''
        self.workers = self._get(self.node)
        log.info('Got %d Workers', len(self.workers))


    def to_serializable(self):
        '''Allows object to be serialized'''
        #TODO Probably need to implement
        pass


    def _get(self, node):
        '''Get workers for node'''

        log.info('Getting Workers')

        endpoint = '/api/workers/node/%d' % node.get_id()
        return self.server.get(endpoint, None)


    def process_workers(self):
        '''Process Workers'''

        for worker in self.workers:
            self._process_worker(worker)


    def _process_worker(self, worker):
        '''Process a worker'''
        w_id = worker['id']
        new_status = worker['status']


        log.info('Processing Worker %d', w_id)

        #Does worker already exist in process
        if w_id in self.current_workers:
            log.info('Worker is in current workers')
            old_worker = self.current_workers[w_id]
            #Have the command or args changed
            if not self._worker_commands_equal(worker, old_worker):
                #Has the status changed
                #TODO Worker process status change 
                pass
        else:
            log.info('Adding Worker to current workers')
            self.current_workers[w_id] = worker

            #TODO Manage Worker state changes

        #Run the worker!
        self.processor.start(worker)

    def _worker_commands_equal(self, worker1, worker2):
        '''Compares the commands/args of two workers'''
        if worker1['command']['command'] == worker2['command']['command']:
            if worker1['command']['args'] == worker2['command']['args']:
                return True
        return False


