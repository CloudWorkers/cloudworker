'use strict';

describe('Worker Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockWorker, MockOutput, MockCommand, MockNode;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockWorker = jasmine.createSpy('MockWorker');
        MockOutput = jasmine.createSpy('MockOutput');
        MockCommand = jasmine.createSpy('MockCommand');
        MockNode = jasmine.createSpy('MockNode');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Worker': MockWorker,
            'Output': MockOutput,
            'Command': MockCommand,
            'Node': MockNode
        };
        createController = function() {
            $injector.get('$controller')("WorkerDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'cloudworkerApp:workerUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
