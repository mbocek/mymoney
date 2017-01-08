(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TransferDetailController', TransferDetailController);

    TransferDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Transfer', 'PersonalBankAccount', 'Tag'];

    function TransferDetailController($scope, $rootScope, $stateParams, previousState, entity, Transfer, PersonalBankAccount, Tag) {
        var vm = this;

        vm.transfer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:transferUpdate', function(event, result) {
            vm.transfer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
