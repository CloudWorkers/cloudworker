'use strict';

angular.module('cloudworkerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


