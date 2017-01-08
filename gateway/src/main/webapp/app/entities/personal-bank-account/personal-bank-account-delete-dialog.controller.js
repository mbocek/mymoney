(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PersonalBankAccountDeleteController',PersonalBankAccountDeleteController);

    PersonalBankAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'PersonalBankAccount'];

    function PersonalBankAccountDeleteController($uibModalInstance, entity, PersonalBankAccount) {
        var vm = this;

        vm.personalBankAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PersonalBankAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
