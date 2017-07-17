(function() {
  var smartParkingAdminApp = angular.module('smartParkingAdminApp', []);

  smartParkingAdminApp.controller('UserManagementPanelController', ['$scope', '$rootScope', function($scope, $rootScope){
    $scope.inputUser = {id:"", name:"", address:"", loginName:"", password:"", passwordAgain:""};
    $rootScope.updateSensor = {id:"", name:"", address:"", loginName:"", password:"", passwordAgain:""};
    
    $scope.inputModalTitle = "";
    
    $scope.InputModal = {ADD_USER: 0, UPDATE_USER:1};
    $scope.currentInputModal = $scope.InputModal.ADD_USER;

    $scope.userInformation = [];
    
    $scope.ErrorCode = '';

    $rootScope.askAllUsersURL = "http://54.68.124.173:8080/SmartParking/api/admin/getAllUsers";
    //$rootScope.askAllUsersURL = "http://localhost:8080/SmartParking/api/admin/getAllUsers";

    $rootScope.addUserURL = "http://54.68.124.173:8080/SmartParking/api/admin/addNewUser";
    //$rootScope.addUserURL = "http://localhost:8080/SmartParking/api/admin/addNewUser";

    $rootScope.updateUserURL = "http://54.68.124.173:8080/SmartParking/api/admin/updateUser";
    //$rootScope.updateUserURL = "http://localhost:8080/SmartParking/api/admin/updateUser";

    $rootScope.deleteUserURL = "http://54.68.124.173:8080/SmartParking/api/admin/deleteUser";
    //$rootScope.deleteUserURL = "http://localhost:8080/SmartParking/api/admin/deleteUser";


    $scope.init = function () {
      $scope.askAllUser();
    };

    $scope.askAllUser = function(){
      var xhr = new XMLHttpRequest();
      xhr.open("POST", $rootScope.askAllUsersURL, false);
      xhr.setRequestHeader("Content-type", "application/json");
      xhr.send();
     
      if(xhr.status === 200){
        var allUsersJSON = JSON.parse(xhr.responseText);
        
        $scope.userInformation.splice(0, $scope.userInformation.length);

        for(i = 0;i < allUsersJSON.userList.length;i++){
          $scope.userInformation[i] = [];
          $scope.userInformation[i]["id"] = allUsersJSON.userList[i].idUser;
          $scope.userInformation[i]["name"] = allUsersJSON.userList[i].name;
          $scope.userInformation[i]["address"] = allUsersJSON.userList[i].address;
          $scope.userInformation[i]["loginName"] = allUsersJSON.userList[i].loginName;
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

    this.addUserRequest = function(){
      var xhr = new XMLHttpRequest();
      xhr.open("POST", $rootScope.addUserURL, false);
      xhr.setRequestHeader("Content-type", "application/json");
      var data = JSON.stringify({ "name":$scope.inputUser.name, 
                                  "address":$scope.inputUser.address,
                                  "loginName":$scope.inputUser.loginName,
                                  "password":$scope.inputUser.password});
      xhr.send(data);

      if(xhr.status === 200){
        //var allParkingLotsJSON = JSON.parse(xhr.responseText);
        
        return true;
      }

      return false;
    };

    this.updateUserRequest = function(){
      var xhr = new XMLHttpRequest();
      xhr.open("POST", $rootScope.updateUserURL, false);
      xhr.setRequestHeader("Content-type", "application/json");
      var data = JSON.stringify({ "idUser":$scope.inputUser.id, 
                                  "name":$scope.inputUser.name, 
                                  "address":$scope.inputUser.address,
                                  "loginName":$scope.inputUser.loginName,
                                  "password":$scope.inputUser.password});
      xhr.send(data);
     
      if(xhr.status === 200){
        return true;
      }

      return false;
    };

    this.deleteUserRequest = function(user){
      var xhr = new XMLHttpRequest();
      xhr.open("POST", $rootScope.deleteUserURL, false);
      xhr.setRequestHeader("Content-type", "application/json");
      var data = JSON.stringify({"idUser":user.id});
      xhr.send(data);
     
      if(xhr.status === 200){
        //var allParkingLotsJSON = JSON.parse(xhr.responseText);
        
        return true;
      }

      return false;
    };

    this.clickAdd = function(){
      $('#myModal').modal('toggle');
      $scope.inputModalTitle = "Add User";
      
      $scope.inputUser.name = "";
      $scope.inputUser.address = "";
      $scope.inputUser.loginName = "";
      $scope.inputUser.password = "";//default state is available
      $scope.inputUser.passwordAgain = "";

      $scope.currentInputModal = $scope.InputModal.ADD_USER;

      $scope.apply();
    };

    this.clickDelete = function(user){
      var result = confirm("Delete user : " + user.loginName + " anyway?");
      
      if (result){
        this.deleteUserRequest(user);
        $scope.askAllUser();
        $scope.apply();
      }        
    };

    this.isPaused = function(){
      return $scope.selectedSensor.Status === $rootScope.statusIndex.pause;
    };

    this.clickUpdate = function(sensor){
      $scope.inputModalTitle = "Update User"
      
      $scope.inputUser.id = sensor.id;
      $scope.inputUser.name = sensor.name;
      $scope.inputUser.address = sensor.address;
      $scope.inputUser.loginName = sensor.loginName;
      $scope.inputUser.password = "";//default state is available
      $scope.inputUser.passwordAgain = "";

      $scope.currentInputModal = $scope.InputModal.UPDATE_USER;
      
      $scope.apply();
    };

    this.clickOK = function(){
      switch($scope.currentInputModal){
        case $scope.InputModal.ADD_USER:
          if( $scope.inputUser.name === "" || 
              $scope.inputUser.address === "" || 
              $scope.inputUser.loginName === "" ||
              $scope.inputUser.password === "" ||
              $scope.inputUser.passwordAgain === ""){
            alert("Some field missing");
            return;
          }

          if($scope.inputUser.password != $scope.inputUser.passwordAgain){
            alert("Password mismatch");
            $('#myModal').modal('toggle');
            return;
          }
          //Send add request
          if(this.addUserRequest()){
            $scope.askAllUser();
            $('#myModal').modal('toggle');
            $scope.inputUser.name = "";
            $scope.inputUser.address = "";
            $scope.inputUser.loginName = "";
            $scope.inputUser.password = "";//default state is available
            $scope.inputUser.passwordAgain = "";
          }

          break;
        case $scope.InputModal.UPDATE_USER:
          if( $scope.inputUser.name === "" || 
              $scope.inputUser.address === "" || 
              $scope.inputUser.loginName === "" ||
              $scope.inputUser.password === "" ||
              $scope.inputUser.passwordAgain === ""){
            alert("Some field missing");
            return;
          }

          if($scope.inputUser.password != $scope.inputUser.passwordAgain){
            alert("Password mismatch");
            $('#myModal').modal('toggle');
            return;
          }
          //Send add request
          if(this.updateUserRequest()){
            $scope.askAllUser();
            $('#myModal').modal('toggle');
            $scope.inputUser.name = "";
            $scope.inputUser.address = "";
            $scope.inputUser.loginName = "";
            $scope.inputUser.password = "";//default state is available
            $scope.inputUser.passwordAgain = "";

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