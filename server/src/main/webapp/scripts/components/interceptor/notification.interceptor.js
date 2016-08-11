 'use strict';

angular.module('cloudworkerApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-cloudworkerApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-cloudworkerApp-params')});
                }
                return response;
            }
        };
    });
