'use strict';

angular.module('cloudworkerApp').controller('ConfigDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Config', 'Node',
        function($scope, $stateParams, $uibModalInstance, entity, Config, Node) {

        $scope.config = entity;
        $scope.nodes = Node.query();
        $scope.load = function(id) {
            Config.get({id : id}, function(result) {
                $scope.config = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('cloudworkerApp:configUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.config.id != null) {
                Config.update($scope.config, onSaveSuccess, onSaveError);
            } else {
                Config.save($scope.config, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
