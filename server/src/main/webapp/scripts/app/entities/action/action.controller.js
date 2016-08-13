'use strict';

angular.module('cloudworkerApp')
    .controller('ActionController', function ($scope, $state, Action) {

        $scope.actions = [];
        $scope.loadAll = function() {
            Action.query(function(result) {
               $scope.actions = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.action = {
                action: null,
                args: null,
                status: null,
                date: null,
                id: null
            };
        };
    });
