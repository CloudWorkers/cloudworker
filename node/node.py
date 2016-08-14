#!/usr/bin/env python
'''CloudWorker Node'''

import multiprocessing
import os
import requests
import sys
import base64
import json
import socket
import platform
import logging
import time


#Command Line Args
BASE_URL = 'http://localhost:8080'
NODE_SECRET = '1753bb75-f3da-4fd7-9227-475f6b95dbaa'

#API Config
CLIENT_ID = 'cloudworkernode'
CLIENT_SECRET = 'nodeSecret1'
USERNAME = 'node' #You can login to the Server UI with this
PASSWORD = 'nodePassword'

#Important temporary session details
TOKEN = ''
NODE_ID = ''

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


def run(command):
    '''Run a command locally'''
    logging.info('Running Command: %s', command)
    os.system('%s' % command)


def get_token(base_url, client_id, client_secret, username, password):
    '''Gets the OAuth Access Token '''

    logging.info('Getting Access Token...')

    token_url = base_url + '/oauth/token'
    token_data = {'username': username,
                  'password': password,
                  'grant_type': 'password',
                  'scope': 'read write',
                  'client_secret': client_secret,
                  'client_id': client_id}
    encoded_credentials = base64.b64encode(client_id + ':' + client_secret)
    headers = {'Content-Type': 'application/x-www-form-urlencoded',
               'Accept': 'application/json',
               'Authorization': 'Basic ' + encoded_credentials
              }

    #Request the access token
    response = requests.post(token_url, headers=headers, data=token_data)

    if response.ok:
        access_token = response.json().get('access_token')
        #expires = response.json().get('expires_in')
        #refresh_token = response.json().get('refresh_token')
        logging.info('Got Access Token: %s', access_token)
        return access_token
    else:
        logging.error('There was an error getting the token from the sever!')
        response.raise_for_status()


def server_request(verb, access_token, base_url, endpoint, data):
    '''Generic Request to the server'''
    url = base_url + endpoint
    headers = {'Authorization': 'bearer ' + access_token,
               'Accept': 'application/json',
               'Content-Type': 'application/json'}

    if "GET" == verb:
        response = requests.get(url, headers=headers, data=json.dumps(data))
    elif "PUT" == verb:
        response = requests.put(url, headers=headers, data=json.dumps(data))
    elif "POST" == verb:
        response = requests.post(url, headers=headers, data=json.dumps(data))

    #Return the data if the response was ok
    if response.ok:
        return response.json()
    else:
        response.raise_for_status()


def get_node_details(base_url, access_token, secret):
    '''Get details for this node'''

    logging.info('Getting Node Details')

    endpoint = '/api/nodes/secret/%s' % secret
    return server_request('GET', access_token, base_url,
                          endpoint, None)

def get_node_config(base_url, access_token, node_details):
    '''Get config for node'''

    logging.info('Getting Node Config')

    endpoint = '/api/configs/node/%d' % node_details['id']
    response_data = server_request('GET', access_token, base_url,
                                   endpoint, None)
    #Unpack results
    result = {}
    for item in response_data:
        result[item['item']] = int(item['value'])
    logging.info('Got Config: %s', result)
    return result

def get_node_actions(base_url, access_token, node_details):
    '''Get pending actions for node'''

    logging.info('Getting Node Actions')

    endpoint = '/api/actions/pending/%d' % node_details['id']
    response_data = server_request('GET', access_token, base_url,
                                   endpoint, None)
    logging.info('Got Pending Actions: %s', response_data)
    return response_data

def get_workers(base_url, access_token, node_details):
    '''Get workers for node'''

    logging.info('Getting Workers')

    endpoint = '/api/workers/node/%d' % node_details['id']
    response_data = server_request('GET', access_token, base_url,
                                   endpoint, None)
    logging.info('Got Workers: %s', response_data)
    return response_data


def update_node_details(base_url, access_token, node_details):
    '''Update details for the node'''

    logging.info('Updating Node Details')

    return server_request('PUT', access_token, base_url,
                          '/api/nodes', node_details)

def update_node_date(base_url, access_token, node_details):
    '''Update the date for the node'''

    logging.info('Updating Node Date')

    endpoint = '/api/nodes/date/%d' % node_details['id']
    return server_request('PUT', access_token, base_url,
                          endpoint, None)

def update_node_status(base_url, access_token, node_details, status):
    '''Update status for the node'''

    logging.info('Updating Node Status to: %s', status)

    node_details['status'] = status
    return server_request('PUT', access_token, base_url,
                          '/api/nodes', node_details)


def send_output(base_url, access_token, node_details, worker_details, message):
    '''Send Output to server'''

    logging.info('Sending Output to Server: %s', message)

    data = {'message': message,
            'node': node_details,
            'worker': worker_details}

    return server_request('POST', access_token, base_url,
                          '/api/outputs', data)


def update_action_status(base_url, access_token, action_details, status):
    '''Update status for the action'''

    logging.info('Updating Action (%s) Status to: %s', action_details['action'], status)

    action_details['status'] = status
    return server_request('PUT', access_token, base_url,
                          '/api/actions', action_details)


def respond_to_action(base_url, access_token, action_details):
    '''Respond to Actions from the server'''

    action = action_details['action']
    args = action_details['args']

    logging.info('Responding to Action: %s', action)

    #Mark action as completed
    update_action_status(base_url, access_token,
                         action_details, ACTION_STATUS_COMPLETED)

    #TODO respond to all actions
    if action == ACTION_KILL:
        message = 'Shutting down node.'
        logging.info(message)
        sys.exit()


def start():
    '''Run the Application'''
    logging.basicConfig(level=logging.INFO,
                        format='%(asctime)-15s %(levelname)-6s %(message)s',
                        datefmt='%b %d %H:%M:%S')

    logging.info('Starting Cloud Worker Node')
    logging.info('--------------------------')

    #Get Access Token
    TOKEN = get_token(BASE_URL, CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD)

    #Get Node Details
    node_details = get_node_details(BASE_URL, TOKEN, NODE_SECRET)
    #Save the Node ID to use in the future
    NODE_ID = node_details.get('id')

    #Update Node Details
    node_details['os'] = "%s %s" %(platform.system(), platform.release())
    node_details['hostname'] = socket.gethostname()
    node_details['ip'] = 'todo'
    node_details['status'] = STATUS_STARTING
    update_node_details(BASE_URL, TOKEN, node_details)

    #Update Status
    update_node_status(BASE_URL, TOKEN, node_details, STATUS_READY)

    #Get Config
    config = get_node_config(BASE_URL, TOKEN, node_details)


    #Loop forever
    while True:
        logging.info('Looping')

        #Update last seen date
        update_node_date(BASE_URL, TOKEN, node_details)

        #Get config
        config = get_node_config(BASE_URL, TOKEN, node_details)

        #Get actions
        pending_actions = get_node_actions(BASE_URL, TOKEN, node_details)

        #Respond to actions
        if len(pending_actions) > 0:
            message = "Responding to %d Actions ..." % len(pending_actions)
            send_output(BASE_URL, TOKEN, node_details, None, message)

        for action_details in pending_actions:
            respond_to_action(BASE_URL, TOKEN, action_details)


        #Get workers/commands
        workers = get_workers(BASE_URL, TOKEN, node_details)

        #TODO
        #Respond to/run commands
        #Send output to server


        logging.info('Sleeping for %d seconds ...', config[CONFIG_POLL_PERIOD])
        time.sleep(config[CONFIG_POLL_PERIOD])


if __name__ == '__main__':
    start()
    sys.exit()




