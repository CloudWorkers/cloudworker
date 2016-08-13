'use strict';

describe('Config Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockConfig, MockNode;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockConfig = jasmine.createSpy('MockConfig');
        MockNode = jasmine.createSpy('MockNode');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Config': MockConfig,
            'Node': MockNode
        };
        createController = function() {
            $injector.get('$controller')("ConfigDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'cloudworkerApp:configUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
