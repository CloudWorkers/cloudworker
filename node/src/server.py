"""Deals with sending and receiving requests from the server."""

import requests
import json
import base64
import logging

class Server(object):
    """ Server """

    def __init__(self, base_url, credentials):
        """Returns a Server which is ready for requests"""
        self.base_url = base_url
        self.credentials = credentials
        self.secret = credentials['secret']
        self.access_token = self._get_token(credentials)


    def _get_token(self, credentials):
        """Gets oauth2 access token from the sever"""

        logging.info('Getting Access Token...')

        token_url = self.base_url + '/oauth/token'
        token_data = {'username': self.credentials['username'],
                      'password': self.credentials['password'],
                      'grant_type': 'password',
                      'scope': 'read write',
                      'client_secret': self.credentials['client_secret'],
                      'client_id': self.credentials['client_id']}
        encoded_credentials = base64.b64encode(self.credentials['client_id'] + ':' + self.credentials['client_secret'])
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


    def get_secret(self):
        return self.secret

    def put(self, endpoint, data):
        """Return data from a PUT request to the server"""
        return self._request('PUT', endpoint, data)

    def get(self, endpoint, data):
        """Return data from a GET request to the server"""
        return self._request('GET', endpoint, data)

    def post(self, endpoint, data):
        """Return data from a POST request to the server"""
        return self._request('POST', endpoint, data)


    def _request(self, verb, endpoint, data):
        '''Generic Request to the server'''
        url = self.base_url + endpoint

        logging.debug("Making %s request to: %s", verb, url)

        headers = {'Authorization': 'bearer ' + self.access_token,
                   'Accept': 'application/json',
                   'Content-Type': 'application/json'}

        if "GET" == verb:
            resp = requests.get(url, headers=headers, data=json.dumps(data))
        elif "PUT" == verb:
            resp = requests.put(url, headers=headers, data=json.dumps(data))
        elif "POST" == verb:
            resp = requests.post(url, headers=headers, data=json.dumps(data))

        #Return the data if the response was ok
        if resp.ok:
            return resp.json()
        else:
            resp.raise_for_status()
