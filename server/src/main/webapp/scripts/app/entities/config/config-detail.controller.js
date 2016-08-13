'use strict';

angular.module('cloudworkerApp')
    .controller('ConfigDetailController', function ($scope, $rootScope, $stateParams, entity, Config, Node) {
        $scope.config = entity;
        $scope.load = function (id) {
            Config.get({id: id}, function(result) {
                $scope.config = result;
            });
        };
        var unsubscribe = $rootScope.$on('cloudworkerApp:configUpdate', function(event, result) {
            $scope.config = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
