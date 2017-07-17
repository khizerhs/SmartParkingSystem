(function() {
  var smartParkingAdminApp = angular.module('smartParkingAdminApp', ['GoogleMapsNative']);

  smartParkingAdminApp.controller('SensorManagementPanelController', ['$scope', '$rootScope', function($scope, $rootScope){
    $scope.inputSensor = {ID:"", Latitude:"", Longtitude:"", cost:"", status:""};

    $scope.showMap = false;
    $scope.showStatusRadio = false;

    $scope.inputModalTitle = "";
    
    $scope.InputModal = {ADD_SENSOR: 0, UPDATE_SENSOR:1};
    $scope.currentInputModal = $scope.InputModal.ADD_SENSOR;

    $scope.parkingLotInformation = [];
    
    $scope.ErrorCode = '';

    $scope.statusLabels = ['UndefineLable', 'label label-primary', 'label label-warning', 'label label-danger', 'label label-default'];
    $scope.statusNames = ['Undefine','Free', 'Occupied', 'Unavailable', 'Inactive'];
    $scope.statusOption = [ {id: 1, label: 'Free'}, 
                            {id: 2, label: 'Occupied'}, 
                            {id: 3, label: 'Unavailable'},
                            {id: 4, label: 'Inactive'}];

    $scope.selectedStatus = $scope.statusOption[0];
    
    $rootScope.askAllParkingLotURL = "http://54.68.124.173:8080/SmartParking/api/sensor/getAllSensors";
    //$rootScope.askAllParkingLotURL = "http://localhost:8080/SmartParking/api/sensor/getAllSensors";

    $rootScope.addParkingLotURL = "http://54.68.124.173:8080/SmartParking/api/admin/addNewSensor";
    //$rootScope.addParkingLotURL = "http://localhost:8080/SmartParking/api/admin/addNewSensor";

    $rootScope.updateParkingLotURL = "http://54.68.124.173:8080/SmartParking/api/admin/updateSensor";
    //$rootScope.updateParkingLotURL = "http://localhost:8080/SmartParking/api/admin/updateSensor";

    $rootScope.deleteParkingLotURL = "http://54.68.124.173:8080/SmartParking/api/admin/deleteSensor";
    //$rootScope.deleteParkingLotURL = "http://localhost:8080/SmartParking/api/admin/deleteSensor";


    $scope.init = function () {
      $scope.askAllParkingLot();
    };

    $scope.askAllParkingLot = function(){
      var xhr = new XMLHttpRequest();
      xhr.open("POST", $rootScope.askAllParkingLotURL, false);
      xhr.setRequestHeader("Content-type", "application/json");
      xhr.send();
     
      if(xhr.status === 200){
        var allParkingLotsJSON = JSON.parse(xhr.responseText);
        
        $scope.parkingLotInformation.splice(0, $scope.parkingLotInformation.length);

        for(i = 0;i < allParkingLotsJSON.sensorList.length;i++){
          $scope.parkingLotInformation[i] = [];
          $scope.parkingLotInformation[i]["index"] = i;
          $scope.parkingLotInformation[i]["sensorID"] = allParkingLotsJSON.sensorList[i].idSensor.toString();
          $scope.parkingLotInformation[i]["status"] = allParkingLotsJSON.sensorList[i].status;
          $scope.parkingLotInformation[i]["position"] = [];
          var thisLocation = allParkingLotsJSON.sensorList[i].location.split(", ");
          $scope.parkingLotInformation[i]["position"][0] = parseFloat(thisLocation[0]);
          $scope.parkingLotInformation[i]["position"][1] = parseFloat(thisLocation[1]);
          $scope.parkingLotInformation[i]["cost"] = allParkingLotsJSON.sensorList[i].cost.toString();
        }

        $scope.apply();
      }
    };

    this.clickLogOut = function(){      
      for (var key in $scope.inputSensor) {
        if($scope.inputSensor.hasOwnProperty(key)) {
          $scope.inputSensor[key] = "";
        }
      }

      for (var key in $scope.selectedSensor) {
        if($scope.selectedSensor.hasOwnProperty(key)) {
          $scope.selectedSensor[key] = "";
        }
      }

      $scope.parkingLotInformation.splice(0, $scope.parkingLotInformation.length);

      $scope.currentInputModal = $scope.InputModal.ADD_SENSOR;
      $scope.ErrorCode = '';
      $scope.$apply();    
    };

    this.addSensorRequest = function(){
      var xhr = new XMLHttpRequest();
      xhr.open("POST", $rootScope.addParkingLotURL, false);
      xhr.setRequestHeader("Content-type", "application/json");
      //"idSensor":$scope.inputSensor.sensorID, 
      var data = JSON.stringify({ "location":$scope.inputSensor.Latitude + ", " + $scope.inputSensor.Longtitude,
                                  "status":$scope.inputSensor.status,
                                  "cost":$scope.inputSensor.cost,
                                  "zipcode":"1"});
      xhr.send(data);

     
      if(xhr.status === 200){
        //var allParkingLotsJSON = JSON.parse(xhr.responseText);
        return true;
      }

      $scope.ErrorCode = xhr.status.toString();
      return false;
    };

    this.updateSensorRequest = function(){
      var xhr = new XMLHttpRequest();
      xhr.open("POST", $rootScope.updateParkingLotURL, false);
      xhr.setRequestHeader("Content-type", "application/json");
      var data = JSON.stringify({ "idSensor":$scope.inputSensor.sensorID, 
                                  "location":$scope.inputSensor.Latitude + ", " + $scope.inputSensor.Longtitude,
                                  "status":parseInt($scope.selectedStatus.id),
                                  "cost":$scope.inputSensor.cost,
                                  "zipcode":"1"});
      xhr.send(data);
      if(xhr.status === 200){
        //var allParkingLotsJSON = JSON.parse(xhr.responseText);
        
        return true;
      }

      return false;
    };

    this.deleteSensorRequest = function(sensorID){
      var xhr = new XMLHttpRequest();
      xhr.open("POST", $rootScope.deleteParkingLotURL, false);
      xhr.setRequestHeader("Content-type", "application/json");
      var data = JSON.stringify({"idSensor":sensorID});
      xhr.send(data);
     
      if(xhr.status === 200){
        //var allParkingLotsJSON = JSON.parse(xhr.responseText);
        
        return true;
      }
      $scope.ErrorCode = xhr.status.toString();
      return false;
    };

    this.clickAdd = function(){
      $('#myModal').modal('toggle');
      $scope.inputModalTitle = "Add Sensor";
      
      $scope.inputSensor.sensorID = "";
      $scope.inputSensor.Latitude = "";
      $scope.inputSensor.Longtitude = "";
      $scope.inputSensor.status = 1;//default state is available
      $scope.inputSensor.cost = "";
      
      $scope.showMap = true;
      $scope.showStatusRadio = false;
      
      $scope.currentInputModal = $scope.InputModal.ADD_SENSOR;

      $scope.apply();
    };

    this.clickDelete = function(sensor){
      var result = confirm("Delete sensor : " + sensor.sensorID + " anyway?");
      
      if (result){
        if(this.deleteSensorRequest(sensor.sensorID)){
          $scope.askAllParkingLot();
          $scope.apply();
        }
        
        else{
          alert("Delete sensor error : " + $scope.ErrorCode);
        }
      }        
    };

    this.isPaused = function(){
      return $scope.selectedSensor.Status === $rootScope.statusIndex.pause;
    };

    this.clickUpdate = function(sensor){
      $scope.inputModalTitle = "Update Sensor"
      
      $scope.inputSensor.sensorID = sensor.sensorID;
      $scope.inputSensor.Latitude = sensor.position[0];
      $scope.inputSensor.Longtitude = sensor.position[1];
      //$scope.inputSensor.status = sensor.status;
      $scope.selectedStatus = $scope.statusOption[sensor.status - 1];
    
      $scope.inputSensor.cost = sensor.cost;
      
      $scope.showMap = true;
      $scope.showStatusRadio = true;

      $scope.currentInputModal = $scope.InputModal.UPDATE_SENSOR;

      
      $scope.apply();
    };

    this.clickOK = function(){
      switch($scope.currentInputModal){
        case $scope.InputModal.ADD_SENSOR:
          //Send add request
          if(!this.addSensorRequest()){
            alert("Add sensor error : " + $scope.ErrorCode);
            return;
          }
          break;
        case $scope.InputModal.UPDATE_SENSOR:
          //Send update request
          if(!this.updateSensorRequest()){
            alert("Update sensor error : " + $scope.ErrorCode);
            return;
          }
          break;

        default:
          break;
      }

      //request all sensor data to update table
      $scope.askAllParkingLot();
      $scope.apply();
    };

    this.clickCancel = function(){

      if($scope.currentInputModal === $scope.InputModal.UPDATE_SENSOR){
        //restore value
        for (var key in $scope.selectedSensor) {
          if($scope.selectedSensor.hasOwnProperty(key)) {
            $scope.backUpSelectedSensor[key] = $scope.selectedSensor[key];
          }
        }
      }

    };

  }]);

})();
