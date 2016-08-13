'use strict';

angular.module('cloudworkerApp')
	.controller('CommandDeleteController', function($scope, $uibModalInstance, entity, Command) {

        $scope.command = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Command.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
