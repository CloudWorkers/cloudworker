'use strict';

angular.module('cloudworkerApp')
    .factory('Output', function ($resource, DateUtils) {
        return $resource('api/outputs/:id', {}, {
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
