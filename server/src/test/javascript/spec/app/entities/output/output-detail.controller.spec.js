'use strict';

describe('Output Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockOutput, MockNode, MockWorker;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockOutput = jasmine.createSpy('MockOutput');
        MockNode = jasmine.createSpy('MockNode');
        MockWorker = jasmine.createSpy('MockWorker');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Output': MockOutput,
            'Node': MockNode,
            'Worker': MockWorker
        };
        createController = function() {
            $injector.get('$controller')("OutputDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'cloudworkerApp:outputUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
