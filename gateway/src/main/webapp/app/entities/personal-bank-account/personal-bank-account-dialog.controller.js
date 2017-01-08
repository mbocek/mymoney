(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PersonalBankAccountDialogController', PersonalBankAccountDialogController);

    PersonalBankAccountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PersonalBankAccount', 'Transfer'];

    function PersonalBankAccountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PersonalBankAccount, Transfer) {
        var vm = this;

        vm.personalBankAccount = entity;
        vm.clear = clear;
        vm.save = save;
        vm.transfers = Transfer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.personalBankAccount.id !== null) {
                PersonalBankAccount.update(vm.personalBankAccount, onSaveSuccess, onSaveError);
            } else {
                PersonalBankAccount.save(vm.personalBankAccount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:personalBankAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
