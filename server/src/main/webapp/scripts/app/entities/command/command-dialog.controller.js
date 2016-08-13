'use strict';

angular.module('cloudworkerApp').controller('CommandDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Command', 'Worker',
        function($scope, $stateParams, $uibModalInstance, entity, Command, Worker) {

        $scope.command = entity;
        $scope.workers = Worker.query();
        $scope.load = function(id) {
            Command.get({id : id}, function(result) {
                $scope.command = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('cloudworkerApp:commandUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.command.id != null) {
                Command.update($scope.command, onSaveSuccess, onSaveError);
            } else {
                Command.save($scope.command, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
