(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('personal-bank-account', {
            parent: 'entity',
            url: '/personal-bank-account',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gatewayApp.personalBankAccount.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personal-bank-account/personal-bank-accounts.html',
                    controller: 'PersonalBankAccountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personalBankAccount');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('personal-bank-account-detail', {
            parent: 'entity',
            url: '/personal-bank-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gatewayApp.personalBankAccount.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personal-bank-account/personal-bank-account-detail.html',
                    controller: 'PersonalBankAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personalBankAccount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PersonalBankAccount', function($stateParams, PersonalBankAccount) {
                    return PersonalBankAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'personal-bank-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('personal-bank-account-detail.edit', {
            parent: 'personal-bank-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personal-bank-account/personal-bank-account-dialog.html',
                    controller: 'PersonalBankAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PersonalBankAccount', function(PersonalBankAccount) {
                            return PersonalBankAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personal-bank-account.new', {
            parent: 'personal-bank-account',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personal-bank-account/personal-bank-account-dialog.html',
                    controller: 'PersonalBankAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                owner: null,
                                name: null,
                                number: null,
                                code: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('personal-bank-account', null, { reload: 'personal-bank-account' });
                }, function() {
                    $state.go('personal-bank-account');
                });
            }]
        })
        .state('personal-bank-account.edit', {
            parent: 'personal-bank-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personal-bank-account/personal-bank-account-dialog.html',
                    controller: 'PersonalBankAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PersonalBankAccount', function(PersonalBankAccount) {
                            return PersonalBankAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personal-bank-account', null, { reload: 'personal-bank-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personal-bank-account.delete', {
            parent: 'personal-bank-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personal-bank-account/personal-bank-account-delete-dialog.html',
                    controller: 'PersonalBankAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PersonalBankAccount', function(PersonalBankAccount) {
                            return PersonalBankAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personal-bank-account', null, { reload: 'personal-bank-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
