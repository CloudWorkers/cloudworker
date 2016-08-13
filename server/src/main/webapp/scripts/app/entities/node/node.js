'use strict';

angular.module('cloudworkerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('node', {
                parent: 'entity',
                url: '/nodes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Nodes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/node/nodes.html',
                        controller: 'NodeController'
                    }
                },
                resolve: {
                }
            })
            .state('node.detail', {
                parent: 'entity',
                url: '/node/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Node'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/node/node-detail.html',
                        controller: 'NodeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Node', function($stateParams, Node) {
                        return Node.get({id : $stateParams.id});
                    }]
                }
            })
            .state('node.new', {
                parent: 'node',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/node/node-dialog.html',
                        controller: 'NodeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    secret: null,
                                    status: null,
                                    date: null,
                                    hostname: null,
                                    ip: null,
                                    os: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('node', null, { reload: true });
                    }, function() {
                        $state.go('node');
                    })
                }]
            })
            .state('node.edit', {
                parent: 'node',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/node/node-dialog.html',
                        controller: 'NodeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Node', function(Node) {
                                return Node.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('node', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('node.delete', {
                parent: 'node',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/node/node-delete-dialog.html',
                        controller: 'NodeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Node', function(Node) {
                                return Node.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('node', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
