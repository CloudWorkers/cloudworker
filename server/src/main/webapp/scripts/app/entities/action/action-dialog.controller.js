'use strict';

angular.module('cloudworkerApp').controller('ActionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Action', 'Node',
        function($scope, $stateParams, $uibModalInstance, entity, Action, Node) {

        $scope.action = entity;
        $scope.nodes = Node.query();
        $scope.load = function(id) {
            Action.get({id : id}, function(result) {
                $scope.action = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('cloudworkerApp:actionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.action.id != null) {
                Action.update($scope.action, onSaveSuccess, onSaveError);
            } else {
                Action.save($scope.action, onSaveSuccess, onSaveError);
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
