'''Deals with sending and receiving requests from the server.'''

from src.constants import Constants as C

import requests
import json
import base64
import logging

class Server(object):
    '''Server '''

    log = logging.getLogger(C.APP)

    def __init__(self, settings):
        '''Returns a Server which is ready for requests'''
        self.settings = settings
        self.access_token = self._get_token()


    def _get_token(self):
        '''Gets oauth2 access token from the sever'''

        self.log.info('Getting Access Token...')

        token_url = self.settings['base_url'] + '/oauth/token'

        token_data = {'username': self.settings['username'],
                      'password': self.settings['password'],
                      'grant_type': 'password',
                      'scope': 'read write',
                      'client_secret': self.settings['client_secret'],
                      'client_id': self.settings['client_id']}

        encoded_credentials = base64.b64encode(\
                    self.settings['client_id'] +\
                    ':' +\
                    self.settings['client_secret'])

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
            self.log.info('Got Access Token: %s', access_token)
            return access_token
        else:
            self.log.error('Error getting token from sever!')
            response.raise_for_status()


    def get_secret(self):
        '''Returns the Node Secret'''
        return self.settings['secret']

    def put(self, endpoint, data):
        '''Return data from a PUT request to the server'''
        return self._request('PUT', endpoint, data)

    def get(self, endpoint, data):
        '''Return data from a GET request to the server'''
        return self._request('GET', endpoint, data)

    def post(self, endpoint, data):
        '''Return data from a POST request to the server'''
        return self._request('POST', endpoint, data)


    def _request(self, verb, endpoint, data):
        '''Generic Request to the server'''
        url = self.settings['base_url'] + endpoint

        self.log.debug('Making %s request to: %s', verb, url)

        headers = {'Authorization': 'bearer ' + self.access_token,
                   'Accept': 'application/json',
                   'Content-Type': 'application/json'}

        if 'GET' == verb:
            resp = requests.get(url, headers=headers, data=json.dumps(data))
        elif 'PUT' == verb:
            resp = requests.put(url, headers=headers, data=json.dumps(data))
        elif 'POST' == verb:
            resp = requests.post(url, headers=headers, data=json.dumps(data))

        #Return the data if the response was ok
        if resp.ok:
            return resp.json()
        else:
            resp.raise_for_status()
