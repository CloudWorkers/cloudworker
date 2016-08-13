'use strict';

angular.module('cloudworkerApp')
	.controller('OutputDeleteController', function($scope, $uibModalInstance, entity, Output) {

        $scope.output = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Output.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
