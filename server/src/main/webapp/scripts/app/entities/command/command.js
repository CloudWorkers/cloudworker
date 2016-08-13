'use strict';

angular.module('cloudworkerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('command', {
                parent: 'entity',
                url: '/commands',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Commands'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/command/commands.html',
                        controller: 'CommandController'
                    }
                },
                resolve: {
                }
            })
            .state('command.detail', {
                parent: 'entity',
                url: '/command/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Command'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/command/command-detail.html',
                        controller: 'CommandDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Command', function($stateParams, Command) {
                        return Command.get({id : $stateParams.id});
                    }]
                }
            })
            .state('command.new', {
                parent: 'command',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/command/command-dialog.html',
                        controller: 'CommandDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    description: null,
                                    command: null,
                                    args: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('command', null, { reload: true });
                    }, function() {
                        $state.go('command');
                    })
                }]
            })
            .state('command.edit', {
                parent: 'command',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/command/command-dialog.html',
                        controller: 'CommandDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Command', function(Command) {
                                return Command.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('command', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('command.delete', {
                parent: 'command',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/command/command-delete-dialog.html',
                        controller: 'CommandDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Command', function(Command) {
                                return Command.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('command', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
