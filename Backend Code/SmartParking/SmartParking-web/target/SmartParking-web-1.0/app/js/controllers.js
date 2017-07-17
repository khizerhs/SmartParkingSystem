'use strict';

/* Controllers */

var phonecatControllers = angular.module('phonecatControllers', []);

phonecatControllers.controller('PhoneListCtrl', ['$scope', 'Phone',
    function ($scope, Phone) {
        $scope.phones = Phone.query();
        $scope.orderProp = 'age';
    }]);

phonecatControllers.controller('PhoneDetailCtrl', ['$scope', '$routeParams', 'Phone',
    function ($scope, $routeParams, Phone) {
        $scope.phone = Phone.get({phoneId: $routeParams.phoneId}, function (phone) {
            $scope.mainImageUrl = phone.images[0];
        });

        $scope.setImage = function (imageUrl) {
            $scope.mainImageUrl = imageUrl;
        };
    }]);


var smartParkingDashBoard = angular.module('smartParkingDashBoard', []);

smartParkingDashBoard.controller('DashBoardController', ['$scope',
    function ($scope) {
        $scope.numberFree = '';
        $scope.numberOccupied = '';
        $scope.numberUnavailable = '';
        $scope.numberInactive = '';
        $scope.serviceUrl = '';
        $scope.dates = [{
                name: 'Today',
                data: JSON.stringify({"date": "2015-12-06"})
            }, {
                name: 'Yesterday',
                data: JSON.stringify({"date": "2015-12-05"})
            }];

        $scope.date = $scope.dates[0];



        $scope.init = function () {
            initCharts();
            $scope.getNumberSensorsByStatus();
            $scope.getNumberSensorsByTime($scope.date);

        };

        $scope.getNumberSensorsByStatus = function () {

            var xhr = new XMLHttpRequest();
            //var url = "http://localhost:8080/SmartParking/api/admin/getNumberSensorsByStatus";
            var url = "http://54.68.124.173:8080/SmartParking/api/admin/getNumberSensorsByStatus";
            var xhr = new XMLHttpRequest();
            xhr.open("POST", url, false);
            xhr.setRequestHeader("Content-type", "application/json");
            var data = JSON.stringify({"status": 1});
            xhr.send(data);

            if (xhr.status === 200) {
                var numberSensorByStatus = JSON.parse(xhr.responseText);
                $scope.numberFree = numberSensorByStatus.numberSensorsFree;
                $scope.numberOccupied = numberSensorByStatus.numberSensorsOccupied;
                $scope.numberUnavailable = numberSensorByStatus.numberSensorsUnavailable;
                $scope.numberInactive = numberSensorByStatus.numberSensorsInactive;
                $scope.donut = Morris.Donut({
                    element: 'morris-donut-chart',
                    data: [
                        {label: "Free Sensors", value: $scope.numberFree},
                        {label: "Occupied Sensors", value: $scope.numberOccupied},
                        {label: "Unavailable Sensors", value: $scope.numberUnavailable},
                        {label: "Inactive Sensors", value: $scope.numberInactive}
                    ],
                    colors: [
                        '#4CB1CF',
                        '#F0433D',
                        '#f0ad4e',
                        '#635F44'
                    ],
                });

            }

        };

        $scope.getNumberSensorsByTime = function (date) {

            var xhr = new XMLHttpRequest();
            //var url = "http://localhost:8080/SmartParking/api/admin/getNumberSensorsByTime";
            var url = "http://54.68.124.173:8080/SmartParking/api/admin/getNumberSensorsByTime";
            var xhr = new XMLHttpRequest();
            xhr.open("POST", url, false);
            xhr.setRequestHeader("Content-type", "application/json");
            //var data = JSON.stringify({"date": "2015-12-05"});
            xhr.send(date.data);

            if (xhr.status === 200) {
                var numberSensorsByTime = JSON.parse(xhr.responseText);
                var data = [];

                for (var i = 0; i < numberSensorsByTime.sensorDataWrapperList.length; i++) {
                    if (i < 10) {
                        data[i] = {y: '0' + i + ':00', a: numberSensorsByTime.sensorDataWrapperList[i].numberSensorsFree, b: numberSensorsByTime.sensorDataWrapperList[i].numberSensorsOccupied};
                    } else {
                        data[i] = {y: i + ':00', a: numberSensorsByTime.sensorDataWrapperList[i].numberSensorsFree, b: numberSensorsByTime.sensorDataWrapperList[i].numberSensorsOccupied};
                    }
                }


            }
            //$scope.line.setData(data);
            $scope.bar.setData(data);
        };

        var initCharts = function () {
            var data = [];
            var config = {
                data: data,
                xkey: 'y',
                ykeys: ['a', 'b'],
                labels: ['Sensors Free', 'Sensors Occupied'],
                fillOpacity: 0.6,
                hideHover: 'auto',               
                resize: true,                
                barColors: ['#4CB1CF', '#F0433D']   
            };
            config.element = 'morris-bar-chart';
            $scope.bar = Morris.Bar(config);
            //config.element = 'morris-line-chart';
            //$scope.line = Morris.Line(config);
            

        };



    }]);


