'use strict';

angular.module('cloudworkerApp')
    .controller('CommandController', function ($scope, $state, Command) {

        $scope.commands = [];
        $scope.loadAll = function() {
            Command.query(function(result) {
               $scope.commands = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.command = {
                description: null,
                command: null,
                args: null,
                id: null
            };
        };
    });
