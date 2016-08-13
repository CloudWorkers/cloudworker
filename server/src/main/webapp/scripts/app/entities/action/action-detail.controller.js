'use strict';

angular.module('cloudworkerApp')
    .controller('ActionDetailController', function ($scope, $rootScope, $stateParams, entity, Action, Node) {
        $scope.action = entity;
        $scope.load = function (id) {
            Action.get({id: id}, function(result) {
                $scope.action = result;
            });
        };
        var unsubscribe = $rootScope.$on('cloudworkerApp:actionUpdate', function(event, result) {
            $scope.action = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
