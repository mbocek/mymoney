'use strict';

describe('Controller Tests', function() {

    describe('PersonalBankAccount Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPersonalBankAccount, MockTransfer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPersonalBankAccount = jasmine.createSpy('MockPersonalBankAccount');
            MockTransfer = jasmine.createSpy('MockTransfer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PersonalBankAccount': MockPersonalBankAccount,
                'Transfer': MockTransfer
            };
            createController = function() {
                $injector.get('$controller')("PersonalBankAccountDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gatewayApp:personalBankAccountUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
