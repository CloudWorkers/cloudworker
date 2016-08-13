'use strict';

angular.module('cloudworkerApp')
    .controller('CommandDetailController', function ($scope, $rootScope, $stateParams, entity, Command, Worker) {
        $scope.command = entity;
        $scope.load = function (id) {
            Command.get({id: id}, function(result) {
                $scope.command = result;
            });
        };
        var unsubscribe = $rootScope.$on('cloudworkerApp:commandUpdate', function(event, result) {
            $scope.command = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
