'use strict';

angular.module('cloudworkerApp')
    .controller('WorkerDetailController', function ($scope, $rootScope, $stateParams, entity, Worker, Output, Command, Node) {
        $scope.worker = entity;
        $scope.load = function (id) {
            Worker.get({id: id}, function(result) {
                $scope.worker = result;
            });
        };
        var unsubscribe = $rootScope.$on('cloudworkerApp:workerUpdate', function(event, result) {
            $scope.worker = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
