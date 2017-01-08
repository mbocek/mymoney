(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Transfer', Transfer);

    Transfer.$inject = ['$resource', 'DateUtils'];

    function Transfer ($resource, DateUtils) {
        var resourceUrl =  'accountservice/' + 'api/transfers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.transactionDate = DateUtils.convertDateTimeFromServer(data.transactionDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
