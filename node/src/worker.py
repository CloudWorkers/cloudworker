'''Worker '''

from src.constants import Constants as C

import logging as log

class Worker(object):
    '''Worker '''

    def __init__(self, server, node, processor):
        '''Init '''
        self.server = server
        self.node = node
        self.processor = processor

        self.workers = {}
        self.current_workers = {}
        

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
        '''Process a worker
        if was enabled and still enabled:
            if command/args have changed:
                stop process (if running)
                start new process (with new command)
            if process is running:
                do nothing
            if process has failed/finished:
                set status disabled
        if was enabled and now disabled:
            stop the process
        if was disabled and now enabled:
            start the process
        if was disabled and still disabled:
            do nothing
        '''

        w_id = worker['id']
        new_status = worker['status']
        log.info('Checking Worker %d State', w_id)

        #Does worker already exist
        if w_id in self.current_workers:
            log.info('Worker is in current workers')
            old_worker = self.current_workers[w_id]
            old_status = old_worker['status']

            # if was enabled and still enabled
            if C.WORKER_STATUS_ENABLED == new_status and new_status == old_status:
                #Have the command or args changed
                if not self._worker_commands_equal(worker, old_worker):
                    #bounce the process
                    self.processor.stop(worker)
                    self.processor.start(worker)
                #Otherwise check if process is still alive
                elif not self.processor.is_alive(worker):
                    #TODO Change status to disabled
                    pass
            # if was enabled and now disabled
            elif C.WORKER_STATUS_DISABLED == new_status and new_status != old_status:
                #Stop the process
                self.processor.stop(worker)
            # if was disabled and now enabled
            elif C.WORKER_STATUS_ENABLED == new_status and new_status != old_status:
                #Start the process
                self.processor.start(worker)
            # Swap
            self.current_workers[w_id] = worker
        #Totally new worker
        else:
            log.info('Adding Worker to current workers')
            self.current_workers[w_id] = worker

            if C.WORKER_STATUS_ENABLED == new_status:
                #Run the worker!
                self.processor.start(worker)


    def _worker_commands_equal(self, worker1, worker2):
        '''Compares the commands/args of two workers'''
        if worker1['command']['command'] == worker2['command']['command']:
            if worker1['command']['args'] == worker2['command']['args']:
                return True
        return False


