'use strict';

describe('Command Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCommand, MockWorker;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCommand = jasmine.createSpy('MockCommand');
        MockWorker = jasmine.createSpy('MockWorker');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Command': MockCommand,
            'Worker': MockWorker
        };
        createController = function() {
            $injector.get('$controller')("CommandDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'cloudworkerApp:commandUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
