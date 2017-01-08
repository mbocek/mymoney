(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PersonalBankAccountDetailController', PersonalBankAccountDetailController);

    PersonalBankAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PersonalBankAccount', 'Transfer'];

    function PersonalBankAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, PersonalBankAccount, Transfer) {
        var vm = this;

        vm.personalBankAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:personalBankAccountUpdate', function(event, result) {
            vm.personalBankAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
