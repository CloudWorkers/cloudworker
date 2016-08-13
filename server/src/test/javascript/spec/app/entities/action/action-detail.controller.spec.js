'use strict';

describe('Action Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAction, MockNode;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAction = jasmine.createSpy('MockAction');
        MockNode = jasmine.createSpy('MockNode');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Action': MockAction,
            'Node': MockNode
        };
        createController = function() {
            $injector.get('$controller')("ActionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'cloudworkerApp:actionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
