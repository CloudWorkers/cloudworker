'''Processor '''

import multiprocessing
import logging as log
from subprocess import Popen, PIPE

class Processor(object):
    '''Processor '''

    def __init__(self):
        '''Init '''
        self.processes = {}


    def stop(self, worker):
        '''Stop a Process'''
        w_id = worker['id']

        log.info('Stopping Process for Worker %d', w_id)

        self.processes[w_id].terminate()

    def start(self, worker):
        '''Start a Process'''
        w_id = worker['id']
        
        log.info('Starting Process for Worker %d', w_id)

        #Start a process
        process = multiprocessing.Process(target=self._run, args=(worker,))
        self.processes[w_id] = process
        process.start()

    def exitcode(self, worker):
        '''Return Process Exit Code'''
        w_id = worker['id']
        return self.processes[w_id].exitcode

    def is_alive(self, worker):
        '''Check if Process is alive'''
        w_id = worker['id']
        return self.processes[w_id].is_alive()

    def _get_command_line(self, worker):
        '''Extract command line from worker'''
        command = worker['command']['command']
        args = worker['command']['args']
        if args:
            return '%s %s' % (command, args)
        return command

    def _run(self, worker):
        '''Run a command locally'''
        w_id = worker['id']
        command = self._get_command_line(worker)
        log.info('Running Command: %s', command)

        output_file = 'out-%s.txt' % w_id
        error_file = 'error-%s.txt' % w_id

        p = Popen(command,
                  stdout=open(output_file, 'a'),
                  stderr=open(error_file, 'a'),
                  stdin=PIPE)


