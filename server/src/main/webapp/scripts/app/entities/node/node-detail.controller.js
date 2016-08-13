'use strict';

angular.module('cloudworkerApp')
    .controller('NodeDetailController', function ($scope, $rootScope, $stateParams, entity, Node, Worker, Action, Output, Config) {
        $scope.node = entity;
        $scope.load = function (id) {
            Node.get({id: id}, function(result) {
                $scope.node = result;
            });
        };
        var unsubscribe = $rootScope.$on('cloudworkerApp:nodeUpdate', function(event, result) {
            $scope.node = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
