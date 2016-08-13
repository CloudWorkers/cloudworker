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


#Command Line Args
BASE_URL = 'http://localhost:8080'
NODE_SECRET = '1753bb75-f3da-4fd7-9227-475f6b95dbaa'

#API Config
CLIENT_ID = 'cloudworkernode'
CLIENT_SECRET = 'nodeSecret1'
USERNAME = 'user'
PASSWORD = 'user'

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


def run(command):
    '''Run a command locally'''
    os.system('%s' % command)


def get_token(base_url, client_id, client_secret, username, password):
    '''Gets the OAuth Access Token '''

    token_url = base_url + '/oauth/token'
    token_data = {'username': password,
                  'password': username,
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
        return access_token
    else:
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
    endpoint = '/api/nodes/secret/' + secret
    return server_request('GET', access_token, base_url,
                          endpoint, None)

def update_node_details(base_url, access_token, node_details):
    '''Update details for the node'''
    return server_request('PUT', access_token, base_url,
                          '/api/nodes', node_details)

def update_node_status(base_url, access_token, node_details, status):
    '''Update status for the node'''
    node_details['status'] = status
    return server_request('PUT', access_token, base_url,
                          '/api/nodes', node_details)



def start():
    '''Run the Application'''
    print 'Starting Cloud Worker Node'

    #Get Access Token
    TOKEN = get_token(BASE_URL, CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD)
    print 'Got Access Token: %s' % (TOKEN)

    #Get Node Details
    node_details = get_node_details(BASE_URL, TOKEN, NODE_SECRET)
    #Save the Node ID to use in the future
    NODE_ID = node_details.get('id')
    print 'Got Node ID: %d' % (NODE_ID)

    #Update Node Details
    node_details['os'] = "%s %s" %(platform.system(), platform.release())
    node_details['hostname'] = socket.gethostname()
    node_details['ip'] = 'todo'
    node_details['status'] = STATUS_STARTING

    print 'Updating Node Details'
    update_node_details(BASE_URL, TOKEN, node_details)



    print 'Set Node Status to READY'
    update_node_status(BASE_URL, TOKEN, node_details, STATUS_READY)


    #Loop forever
      #Update time on server
      #Get config

      #Get actions
      #Respond to actions

      #Send output to server


if __name__ == '__main__':
    start()
    sys.exit()




