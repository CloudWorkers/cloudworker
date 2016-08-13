'use strict';

angular.module('cloudworkerApp').controller('NodeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Node', 'Worker', 'Action', 'Output', 'Config',
        function($scope, $stateParams, $uibModalInstance, entity, Node, Worker, Action, Output, Config) {

        $scope.node = entity;
        $scope.workers = Worker.query();
        $scope.actions = Action.query();
        $scope.outputs = Output.query();
        $scope.configs = Config.query();
        $scope.load = function(id) {
            Node.get({id : id}, function(result) {
                $scope.node = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('cloudworkerApp:nodeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.node.id != null) {
                Node.update($scope.node, onSaveSuccess, onSaveError);
            } else {
                Node.save($scope.node, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDate = {};

        $scope.datePickerForDate.status = {
            opened: false
        };

        $scope.datePickerForDateOpen = function($event) {
            $scope.datePickerForDate.status.opened = true;
        };
}]);
