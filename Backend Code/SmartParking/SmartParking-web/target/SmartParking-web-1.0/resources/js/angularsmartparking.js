(function() {
  var smartParkingApp = angular.module('smartParkingApp', ['GoogleMapsNative']);

  smartParkingApp.controller('PanelController', ['$scope', '$rootScope', function($scope, $rootScope){
    $rootScope.tab = 1;
    $rootScope.selectedRange = "3";
    $rootScope.latitude = 0;
    $rootScope.longtitude = 0;
    $rootScope.destinationLatitude = 0;
    $rootScope.destinationLongtitude = 0;
    $rootScope.isShowInfo = false;
    $rootScope.infoVisibility = [];
    $rootScope.infoVisibilityOccupied = [];
    //$rootScope.askFreeParkingLotURL = "http://54.68.124.173:8080/SmartParking/api/user/requestFreeParkingSpots";
    //$rootScope.askFreeParkingLotURL = "http://localhost:8080/SmartParking/api/user/requestFreeParkingSpots";
    //$rootScope.askFreeParkingLotURL = "http://54.68.124.173:8080/SmartParking/api/sensor/getAllSensors";
    //$rootScope.askFreeParkingLotURL = "http://localhost:8080/SmartParking/api/sensor/getAllSensors";
    $rootScope.askFreeParkingLotURL = "http://54.68.124.173:8080/SmartParking/api/user/getAllSensorsByRange";
    //$rootScope.askFreeParkingLotURL = "http://localhost:8080/SmartParking/api/user/getAllSensorsByRange";
    $rootScope.showOptions = true;
    
    $rootScope.parkingLots = [];
    $rootScope.occupiedParkingLots = [];


    this.selectTab = function(setTab){
      $rootScope.tab = setTab;
    };
  
    this.isSelected = function(checkTab){
      return $rootScope.tab === checkTab;
    };

    $scope.goToTab = function(tabNum){
      $rootScope.tab = tabNum;
      $rootScope.$apply();
    };

    this.isShowParkingInfomation = function(){
      return $rootScope.isShowInfo;
    };

    this.hideParkingInformation = function(){
      $rootScope.isShowInfo = false;    
    };
    
    this.isShowOptionsLookUp = function(){
      return $rootScope.showOptions === true;
    };
    
    this.changeOptionsStatus = function(){
      $rootScope.showOptions = true;
    };

  }]);

  smartParkingApp.controller('LocationController', ['$scope', '$rootScope', function($scope, $rootScope){
    $scope.showAddressLookUp = false;
    $scope.inputAddress = '';
    
    this.getCurrentPositionSuccessCallBack = function(position){
      $rootScope.parkingLots = [];
      //Save the location
      $rootScope.latitude = position.coords.latitude;
      $rootScope.longtitude = position.coords.longitude;
      $rootScope.tab = 2;

      var xhr = new XMLHttpRequest();
      xhr.open("POST", $rootScope.askFreeParkingLotURL, false);
      xhr.setRequestHeader("Content-type", "application/json");
      var data = JSON.stringify({"location":$rootScope.latitude + "," + $rootScope.longtitude, "range":$rootScope.selectedRange});
      xhr.send(data);
       
      if(xhr.status === 200){
         var availableParkingLots = JSON.parse(xhr.responseText);
            $rootScope.parkingLots.splice(0, $rootScope.parkingLots.length);
            $rootScope.occupiedParkingLots.splice(0, $rootScope.occupiedParkingLots.length);

            for(i = 0;i < availableParkingLots.sensorList.length;i++){
                if(availableParkingLots.sensorList[i].status == 1){
                $rootScope.parkingLots[i] = [];
                $rootScope.parkingLots[i]["number"] = parseInt(i);
                $rootScope.parkingLots[i]["information"] = "Id: "+parseInt(i)+", Cost: "+availableParkingLots.sensorList[i].cost.toString();
                $rootScope.parkingLots[i]["status"] = availableParkingLots.sensorList[i].status;
                $rootScope.parkingLots[i]["position"] = [];
                var thisLocation = availableParkingLots.sensorList[i].location.split(", ");
                $rootScope.parkingLots[i]["position"][0] = parseFloat(thisLocation[0]);
                $rootScope.parkingLots[i]["position"][1] = parseFloat(thisLocation[1]);
              }
              
              else if(availableParkingLots.sensorList[i].status == 2){
                $rootScope.occupiedParkingLots[i] = [];
                $rootScope.occupiedParkingLots[i]["number"] = parseInt(i);
                $rootScope.occupiedParkingLots[i]["information"] = "Id: "+parseInt(i)+", Cost: "+availableParkingLots.sensorList[i].cost.toString();
                $rootScope.occupiedParkingLots[i]["status"] = availableParkingLots.sensorList[i].status;
                $rootScope.occupiedParkingLots[i]["position"] = [];
                var thisLocation = availableParkingLots.sensorList[i].location.split(", ");
                $rootScope.occupiedParkingLots[i]["position"][0] = parseFloat(thisLocation[0]);
                $rootScope.occupiedParkingLots[i]["position"][1] = parseFloat(thisLocation[1]);
             }
            }            
        }
        $rootScope.showOptions = false;
        $rootScope.$apply();
    };

    this.getCurrentPositionErrorCallBack = function(err){
      if (err.code === 1) {//User denied location request
      //send to address page;
        ;
      }
    };

    this.getCurrentPosition = function(){
      navigator.geolocation.getCurrentPosition(this.getCurrentPositionSuccessCallBack, this.getCurrentPositionErrorCallBack);
    };

    this.showHideAddressLookUp = function(){
      $scope.showAddressLookUp = !$scope.showAddressLookUp;
    };

    this.isShowAddressLookUp = function(){
      return $scope.showAddressLookUp === true;
    };
    
    
    this.lookUpAddress = function(){

      var xmlhttp = new XMLHttpRequest();
      var url = "http://maps.googleapis.com/maps/api/geocode/json?address=" + $scope.inputAddress + "&sensor=false";

      xmlhttp.onreadystatechange = function(){
                                      if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                                        var myArr = JSON.parse(xmlhttp.responseText);
                                        addressCallBack(myArr);
                                      }
                                  };
        
      xmlhttp.open("GET", url, true);
      xmlhttp.send();

      function addressCallBack(arr) {
        $rootScope.parkingLots = [];
        $rootScope.latitude = arr.results[0].geometry.location.lat;
        $rootScope.longtitude = arr.results[0].geometry.location.lng;
        $rootScope.tab = 2;

        var xhr = new XMLHttpRequest();
        xhr.open("POST", $rootScope.askFreeParkingLotURL, false);
        xhr.setRequestHeader("Content-type", "application/json");
        var data = JSON.stringify({"location":$rootScope.latitude + "," + $rootScope.longtitude, "range":$rootScope.selectedRange});
        xhr.send(data);
       
        if(xhr.status === 200){
            var availableParkingLots = JSON.parse(xhr.responseText);
            $rootScope.parkingLots.splice(0, $rootScope.parkingLots.length);
            $rootScope.occupiedParkingLots.splice(0, $rootScope.occupiedParkingLots.length);

            for(i = 0;i < availableParkingLots.sensorList.length;i++){
              if(availableParkingLots.sensorList[i].status == 1){
                $rootScope.parkingLots[i] = [];
                $rootScope.parkingLots[i]["number"] = parseInt(i);
                $rootScope.parkingLots[i]["information"] = "Id: "+parseInt(i)+", Cost: "+availableParkingLots.sensorList[i].cost.toString();
                $rootScope.parkingLots[i]["status"] = availableParkingLots.sensorList[i].status;
                $rootScope.parkingLots[i]["position"] = [];
                var thisLocation = availableParkingLots.sensorList[i].location.split(", ");
                $rootScope.parkingLots[i]["position"][0] = parseFloat(thisLocation[0]);
                $rootScope.parkingLots[i]["position"][1] = parseFloat(thisLocation[1]);
              }
              
              else if(availableParkingLots.sensorList[i].status == 2){
                $rootScope.occupiedParkingLots[i] = [];
                $rootScope.occupiedParkingLots[i]["number"] = parseInt(i);
                $rootScope.occupiedParkingLots[i]["information"] = "Id: "+parseInt(i)+", Cost: "+availableParkingLots.sensorList[i].cost.toString();
                $rootScope.occupiedParkingLots[i]["status"] = availableParkingLots.sensorList[i].status;
                $rootScope.occupiedParkingLots[i]["position"] = [];
                var thisLocation = availableParkingLots.sensorList[i].location.split(", ");
                $rootScope.occupiedParkingLots[i]["position"][0] = parseFloat(thisLocation[0]);
                $rootScope.occupiedParkingLots[i]["position"][1] = parseFloat(thisLocation[1]);
             }
            }
        }
        $rootScope.showOptions = false;
        $rootScope.$apply();
      }
    };

  }]);

  smartParkingApp.controller('GooglaMapController', ['$scope', '$rootScope', function($scope, $rootScope){
    this.parkingLotClick = function(parkingLotNum){
      for (i = 0; i < $rootScope.infoVisibility.length; i++){
        $rootScope.infoVisibility[i] = false;
      }
      
      for (i = 0; i < $rootScope.infoVisibilityOccupied.length; i++){
        $rootScope.infoVisibilityOccupied[i] = false;
      }
      
      $rootScope.destinationLatitude = $rootScope.parkingLots[parkingLotNum].position[0];
      $rootScope.destinationLongtitude = $rootScope.parkingLots[parkingLotNum].position[1];

      $rootScope.infoVisibility[parkingLotNum] = true;
      $rootScope.$apply();
    };
    
    this.occupiedParkingLotClick = function(parkingLotNum){
      for (i = 0; i < $rootScope.infoVisibility.length; i++){
        $rootScope.infoVisibility[i] = false;
      }
      
      for (i = 0; i < $rootScope.infoVisibilityOccupied.length; i++){
        $rootScope.infoVisibilityOccupied[i] = false;
      }
      
      $rootScope.destinationLatitude = $rootScope.occupiedParkingLots[parkingLotNum].position[0];
      $rootScope.destinationLongtitude = $rootScope.occupiedParkingLots[parkingLotNum].position[1];

      $rootScope.infoVisibilityOccupied[parkingLotNum] = true;
      $rootScope.$apply();
    };


    this.showNavigation = function()
    {
      location.href='http://maps.google.com/maps?saddr=' + $rootScope.latitude + ',' + $rootScope.longtitude + '&daddr=' + $rootScope.destinationLatitude + ',' + $rootScope.destinationLongtitude;
    };

    this.updateParkingLot = function(){
      var xhr = new XMLHttpRequest();
      xhr.open("POST", $rootScope.askFreeParkingLotURL, false);
      xhr.setRequestHeader("Content-type", "application/json");
      var data = JSON.stringify({"location":$rootScope.latitude + "," + $rootScope.longtitude, "range":$rootScope.selectedRange});
      xhr.send(data);
     
      if(xhr.status === 200){
          var availableParkingLots = JSON.parse(xhr.responseText);

          for(i = 0;i < availableParkingLots.sensorList.length;i++){
            if(availableParkingLots.sensorList[i].status == 1){
                $rootScope.parkingLots[i] = [];
                $rootScope.parkingLots[i]["number"] = parseInt(i);
                $rootScope.parkingLots[i]["information"] = availableParkingLots.sensorList[i].cost.toString();
                $rootScope.parkingLots[i]["status"] = availableParkingLots.sensorList[i].status;
                $rootScope.parkingLots[i]["position"] = [];
                var thisLocation = availableParkingLots.sensorList[i].location.split(", ");
                $rootScope.parkingLots[i]["position"][0] = parseFloat(thisLocation[0]);
                $rootScope.parkingLots[i]["position"][1] = parseFloat(thisLocation[1]);
              }
              
              else if(availableParkingLots.sensorList[i].status == 2){
                $rootScope.occupiedParkingLots[i] = [];
                $rootScope.occupiedParkingLots[i]["number"] = parseInt(i);
                $rootScope.occupiedParkingLots[i]["information"] = availableParkingLots.sensorList[i].cost.toString();
                $rootScope.occupiedParkingLots[i]["status"] = availableParkingLots.sensorList[i].status;
                $rootScope.occupiedParkingLots[i]["position"] = [];
                var thisLocation = availableParkingLots.sensorList[i].location.split(", ");
                $rootScope.occupiedParkingLots[i]["position"][0] = parseFloat(thisLocation[0]);
                $rootScope.occupiedParkingLots[i]["position"][1] = parseFloat(thisLocation[1]);
             }
          }
      }

      $rootScope.$apply();
    };

    $scope.setPanel = function (renderer){
      renderer.setPanel(document.getElementById('routes'));
    };

  }]);

})()