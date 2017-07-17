var timer;var status=0;var arr;var arrl;
function oload(){
  
    var xmlhttp = new XMLHttpRequest();
var url = "http://54.68.124.173:8080/SmartParking/api/sensor/getAllSensors";

xmlhttp.onreadystatechange=function() {
  
    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
        myFunction(xmlhttp.responseText);
    }
}
xmlhttp.open("POST", url, true);
xmlhttp.send();

}

function myFunction(response) {
    arr = JSON.parse(response);
    alert("Sensor database updated with "+arr.sensorList.length+" sensors");
    arrl=arr.sensorList.length    
    }
    


 function timerFunct() {
  mydiv = document.getElementById("mydiv");
  var p =  Math.floor(((Math.random() * 1000)%arrl)+1);
  if(p<25){
   butt=document.getElementById("button"+(p+1));
   }

  if(arr.sensorList[p].status==2){
   restapi(p+1,1); 
     if(p<25){
      butt.innerHTML="Occupy";
    }
    arr.sensorList[p].status=1;
    mydiv.innerHTML+="Sensor Id:"+arr.sensorList[p].idSensor+" is Available"+"<br";
    
   }

  else{
   restapi(p+1,2);
   if(p<25){
     butt.innerHTML="Vacate";
    }
     mydiv.innerHTML+="Sensor Id:"+arr.sensorList[p].idSensor+" is Occupied"+"<br";
     arr.sensorList[p].status=2;
   }
   var tx=Math.floor((Math.random() * 10)+1);
   mydiv.innerHTML+="("+tx+" secs)"+"<br>";
  timer=setTimeout(timerFunct,1000);
 }
 
 function funct(){
  if(status==1){
   status=0;
   clearTimeout(timer);
   }
  else{
   status=1;
   timerFunct();
   }
 } 

 function bfunct(x){
  mydiv = document.getElementById("mydiv");
  butt=document.getElementById("button"+x);
    if(arr.sensorList[(x-1)].status==1){ 
     restapi(x,2);
     butt.innerHTML="Vacate";     
     arr.sensorList[(x-1)].status=2;
     mydiv.innerHTML+="Sensor Id:"+x+" is Occupied"+"<br>";
     alert("Sensor Id:"+x+" is Occupied");
    }
  
    else{    
    butt.innerHTML="Occupy";    
    mydiv.innerHTML+="Sensor Id:"+x+" is Available"+"<br>";
    restapi(x,1);
    arr.sensorList[(x-1)].status=1;
    alert("Sensor Id:"+x+" is Available");
   }
 }

 function restapi(id,stat)
{
xhr = new XMLHttpRequest();
var url = "http://54.68.124.173:8080/SmartParking/api/sensor/updateSensorStatus";
xhr.open("POST", url, true);
xhr.setRequestHeader("Content-type", "application/json");
var data = JSON.stringify({"idSensor":id,"status":stat});
xhr.send(data);
}
 
 
