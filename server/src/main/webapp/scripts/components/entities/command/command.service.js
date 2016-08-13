'use strict';

angular.module('cloudworkerApp')
    .factory('Command', function ($resource, DateUtils) {
        return $resource('api/commands/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
