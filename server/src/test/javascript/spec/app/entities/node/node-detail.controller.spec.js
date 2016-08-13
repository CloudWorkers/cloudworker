'use strict';

describe('Node Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockNode, MockWorker, MockAction, MockOutput, MockConfig;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockNode = jasmine.createSpy('MockNode');
        MockWorker = jasmine.createSpy('MockWorker');
        MockAction = jasmine.createSpy('MockAction');
        MockOutput = jasmine.createSpy('MockOutput');
        MockConfig = jasmine.createSpy('MockConfig');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Node': MockNode,
            'Worker': MockWorker,
            'Action': MockAction,
            'Output': MockOutput,
            'Config': MockConfig
        };
        createController = function() {
            $injector.get('$controller')("NodeDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'cloudworkerApp:nodeUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
