'use strict';

angular.module('cloudworkerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('output', {
                parent: 'entity',
                url: '/outputs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Outputs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/output/outputs.html',
                        controller: 'OutputController'
                    }
                },
                resolve: {
                }
            })
            .state('output.detail', {
                parent: 'entity',
                url: '/output/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Output'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/output/output-detail.html',
                        controller: 'OutputDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Output', function($stateParams, Output) {
                        return Output.get({id : $stateParams.id});
                    }]
                }
            })
            .state('output.new', {
                parent: 'output',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/output/output-dialog.html',
                        controller: 'OutputDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    message: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('output', null, { reload: true });
                    }, function() {
                        $state.go('output');
                    })
                }]
            })
            .state('output.edit', {
                parent: 'output',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/output/output-dialog.html',
                        controller: 'OutputDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Output', function(Output) {
                                return Output.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('output', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('output.delete', {
                parent: 'output',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/output/output-delete-dialog.html',
                        controller: 'OutputDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Output', function(Output) {
                                return Output.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('output', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
