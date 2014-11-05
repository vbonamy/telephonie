'use strict';

telephonieApp.factory('Telephone', function ($resource) {
        return $resource('app/rest/telephones/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });
