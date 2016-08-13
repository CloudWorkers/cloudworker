'use strict';

angular.module('cloudworkerApp')
    .controller('OutputDetailController', function ($scope, $rootScope, $stateParams, entity, Output, Node, Worker) {
        $scope.output = entity;
        $scope.load = function (id) {
            Output.get({id: id}, function(result) {
                $scope.output = result;
            });
        };
        var unsubscribe = $rootScope.$on('cloudworkerApp:outputUpdate', function(event, result) {
            $scope.output = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
