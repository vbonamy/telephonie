'use strict';

telephonieApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/telephone', {
                    templateUrl: 'views/telephones.html',
                    controller: 'TelephoneController',
                    resolve:{
                        resolvedTelephone: ['Telephone', function (Telephone) {
                            return Telephone.query().$promise;
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });
