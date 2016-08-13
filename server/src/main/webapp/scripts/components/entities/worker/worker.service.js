'use strict';

angular.module('cloudworkerApp')
    .factory('Worker', function ($resource, DateUtils) {
        return $resource('api/workers/:id', {}, {
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
