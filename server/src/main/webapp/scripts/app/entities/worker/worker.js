'use strict';

angular.module('cloudworkerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('worker', {
                parent: 'entity',
                url: '/workers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Workers'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/worker/workers.html',
                        controller: 'WorkerController'
                    }
                },
                resolve: {
                }
            })
            .state('worker.detail', {
                parent: 'entity',
                url: '/worker/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Worker'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/worker/worker-detail.html',
                        controller: 'WorkerDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Worker', function($stateParams, Worker) {
                        return Worker.get({id : $stateParams.id});
                    }]
                }
            })
            .state('worker.new', {
                parent: 'worker',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/worker/worker-dialog.html',
                        controller: 'WorkerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('worker', null, { reload: true });
                    }, function() {
                        $state.go('worker');
                    })
                }]
            })
            .state('worker.edit', {
                parent: 'worker',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/worker/worker-dialog.html',
                        controller: 'WorkerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Worker', function(Worker) {
                                return Worker.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('worker', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('worker.delete', {
                parent: 'worker',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/worker/worker-delete-dialog.html',
                        controller: 'WorkerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Worker', function(Worker) {
                                return Worker.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('worker', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
