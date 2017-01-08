(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PersonalBankAccountController', PersonalBankAccountController);

    PersonalBankAccountController.$inject = ['$scope', '$state', 'PersonalBankAccount'];

    function PersonalBankAccountController ($scope, $state, PersonalBankAccount) {
        var vm = this;

        vm.personalBankAccounts = [];

        loadAll();

        function loadAll() {
            PersonalBankAccount.query(function(result) {
                vm.personalBankAccounts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
