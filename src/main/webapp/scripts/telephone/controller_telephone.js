'use strict';

telephonieApp.controller('TelephoneController', function ($scope, resolvedTelephone, Telephone) {

        $scope.telephones = resolvedTelephone;

        $scope.create = function () {
            Telephone.save($scope.telephone,
                function () {
                    $scope.telephones = Telephone.query();
                    $('#saveTelephoneModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.telephone = Telephone.get({id: id});
            $('#saveTelephoneModal').modal('show');
        };

        $scope.delete = function (id) {
            Telephone.delete({id: id},
                function () {
                    $scope.telephones = Telephone.query();
                });
        };

        $scope.clear = function () {
            $scope.telephone = {numero: null, proprietaire: null, pin: null, id: null};
        };
    });
