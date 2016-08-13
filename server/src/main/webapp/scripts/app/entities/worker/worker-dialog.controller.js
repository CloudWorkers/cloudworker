'use strict';

angular.module('cloudworkerApp').controller('WorkerDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Worker', 'Output', 'Command', 'Node',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Worker, Output, Command, Node) {

        $scope.worker = entity;
        $scope.outputs = Output.query();
        $scope.commands = Command.query({filter: 'worker-is-null'});
        $q.all([$scope.worker.$promise, $scope.commands.$promise]).then(function() {
            if (!$scope.worker.command || !$scope.worker.command.id) {
                return $q.reject();
            }
            return Command.get({id : $scope.worker.command.id}).$promise;
        }).then(function(command) {
            $scope.commands.push(command);
        });
        $scope.nodes = Node.query();
        $scope.load = function(id) {
            Worker.get({id : id}, function(result) {
                $scope.worker = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('cloudworkerApp:workerUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.worker.id != null) {
                Worker.update($scope.worker, onSaveSuccess, onSaveError);
            } else {
                Worker.save($scope.worker, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
