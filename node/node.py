#!/usr/bin/env python
'''CloudWorker Node'''

import multiprocessing
import os
import requests
import sys
import base64


#Command Line Args
BASE_URL = 'http://localhost:8080'
NODE_SECRET = 'todo'

#API Config
CLIENT_ID = 'cloudworkernode'
CLIENT_SECRET = 'nodeSecret1'
USERNAME = 'admin'
PASSWORD = 'admin'

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

    if (response.ok):
        access_token = response.json().get('access_token')
        expires = response.json().get('expires_in')
        #refresh_token = response.json().get('refresh_token')
        return access_token
    else:
        response.raise_for_status()


def get_nodes(base_url, access_token):
    '''Get nodes'''
    url = base_url + '/api/nodes'
    headers = {'Authorization': 'bearer ' + access_token,
               'Accept': 'application/json'}
    response = requests.get(url, headers=headers)
    resonse_json = response.json()
    return resonse_json


def run(command):
    '''Run a command '''
    os.system('%s' % command)




def start():
    '''Run the Application'''
    print 'Starting Cloud Worker Node'

    token = get_token(BASE_URL, CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD)
    print 'Got Access Token: %s' % (token)

    print get_nodes(BASE_URL, token)

    #Check secret key with api
    #Update node info

    #Loop forever
      #Update time on server
      #Get config

      #Get actions
      #Respond to actions

      #Send output to server


if __name__ == '__main__':
    start()
    sys.exit()




