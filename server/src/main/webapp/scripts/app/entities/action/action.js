'use strict';

angular.module('cloudworkerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('action', {
                parent: 'entity',
                url: '/actions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Actions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/action/actions.html',
                        controller: 'ActionController'
                    }
                },
                resolve: {
                }
            })
            .state('action.detail', {
                parent: 'entity',
                url: '/action/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Action'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/action/action-detail.html',
                        controller: 'ActionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Action', function($stateParams, Action) {
                        return Action.get({id : $stateParams.id});
                    }]
                }
            })
            .state('action.new', {
                parent: 'action',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/action/action-dialog.html',
                        controller: 'ActionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    action: null,
                                    args: null,
                                    status: null,
                                    date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('action', null, { reload: true });
                    }, function() {
                        $state.go('action');
                    })
                }]
            })
            .state('action.edit', {
                parent: 'action',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/action/action-dialog.html',
                        controller: 'ActionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Action', function(Action) {
                                return Action.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('action', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('action.delete', {
                parent: 'action',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/action/action-delete-dialog.html',
                        controller: 'ActionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Action', function(Action) {
                                return Action.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('action', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
