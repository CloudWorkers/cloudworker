'use strict';

angular.module('cloudworkerApp')
	.controller('NodeDeleteController', function($scope, $uibModalInstance, entity, Node) {

        $scope.node = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Node.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
