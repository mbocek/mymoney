(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('PersonalBankAccount', PersonalBankAccount);

    PersonalBankAccount.$inject = ['$resource'];

    function PersonalBankAccount ($resource) {
        var resourceUrl =  'accountservice/' + 'api/personal-bank-accounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
