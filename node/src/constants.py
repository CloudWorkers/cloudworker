'''Constants'''

class Constants(object):
    '''Constants'''

    APP = 'CloudWorker'

    #API Config
    CLIENT_ID = 'cloudworkernode'
    CLIENT_SECRET = 'nodeSecret1'
    USERNAME = 'node' #You can login to the Server UI with this
    PASSWORD = 'nodePassword'

    #API Enums
    STATUS_STOPPED = 'STOPPED'
    STATUS_STARTING = 'STARTING'
    STATUS_WAITING = 'WAITING'
    STATUS_RUNNING = 'RUNNING'
    STATUS_READY = 'READY'
    STATUS_DISABLED = 'DISABLED'

    CONFIG_MAX_WORKERS = 'MAX_WORKERS'
    CONFIG_POLL_PERIOD = 'POLL_PERIOD'

    ACTION_KILL = 'KILL'
    ACTION_START_WORKER = 'START_WORKER'
    ACTION_STOP_WORKER = 'STOP_WORKER'

    ACTION_STATUS_PENDING = 'PENDING'
    ACTION_STATUS_COMPLETED = 'COMPLETED'
