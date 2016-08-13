'use strict';

angular.module('cloudworkerApp')
	.controller('WorkerDeleteController', function($scope, $uibModalInstance, entity, Worker) {

        $scope.worker = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Worker.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
