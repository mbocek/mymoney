(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TransferDialogController', TransferDialogController);

    TransferDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Transfer', 'PersonalBankAccount', 'Tag'];

    function TransferDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Transfer, PersonalBankAccount, Tag) {
        var vm = this;

        vm.transfer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.personalbankaccounts = PersonalBankAccount.query();
        vm.tags = Tag.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transfer.id !== null) {
                Transfer.update(vm.transfer, onSaveSuccess, onSaveError);
            } else {
                Transfer.save(vm.transfer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:transferUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.transactionDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
