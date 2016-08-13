'use strict';

angular.module('cloudworkerApp').controller('OutputDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Output', 'Node', 'Worker',
        function($scope, $stateParams, $uibModalInstance, entity, Output, Node, Worker) {

        $scope.output = entity;
        $scope.nodes = Node.query();
        $scope.workers = Worker.query();
        $scope.load = function(id) {
            Output.get({id : id}, function(result) {
                $scope.output = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('cloudworkerApp:outputUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.output.id != null) {
                Output.update($scope.output, onSaveSuccess, onSaveError);
            } else {
                Output.save($scope.output, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
