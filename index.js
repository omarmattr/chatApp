var socketIO = require('socket.io'),
    http = require('http'),
    port = process.env.PORT || 2000,
    ip = process.env.IP || '10.7.12.138', //My IP address. I try to "127.0.0.1" but it the same => don't run
   //My IP address. I try to "127.0.0.1" but it the same => don't run
    server = http.createServer().listen(port, ip, function() {
        console.log("IP = " , ip);
        console.log("start socket successfully");
});
var array=[];
io = socketIO.listen(server);
//io.set('match origin protocol', true);
io.set('origins', '*:*');
var mysql = require('mysql');

var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "",
database: "dpchat"
});
   con.connect(function(err) {
        console.log("Connected!");
  if (err) throw err;});


var run = function(socket){

    socket.on("message", function(sId,mId,data,type) {
        console.log(sId);
       io.sockets.emit(sId, {sId:sId,mId:mId,data: data,type:type});
        
    });
socket.on("user-get", function(id) {
    var newArrary=[]
       for( var i = 0; i < array.length; i++){ 
         var jsonObj=JSON.parse(array[i])
        if ( jsonObj["unline"] == true) { 
           newArrary.push(array[i])
              console.log("user-online"+array[i]);
            
        }
      }
     io.sockets.emit("new-users", {array: newArrary});
    
});
    socket.on("user-join", function(user) {
          var jsonObj = JSON.parse(user);
          var id=jsonObj["id"];
    var isLog =true;
        for( var i = 0; i < array.length; i++){ 
         var jsonObj=JSON.parse(array[i])
        if ( jsonObj["id"] == id) { 
            isLog =false;
            break;
        }
      }
   if(isLog)  array.push(user);
         console.log("user-join");
        console.log(array.length);
    
});
      socket.on("user-online", function(user) {
          var jsonObj = JSON.parse(user);
          var id=jsonObj["id"];
    
        for( var i = 0; i < array.length; i++){ 
         var jsonObj=JSON.parse(array[i])
        if ( jsonObj["id"] == id) { 
            array[i] =user;
              console.log("user-online");
            
        }
      }
          
    
});
    socket.on("user-insert", function(name,email,password,img) {

  var sql = "INSERT INTO users (name, email,password,img(column USING utf8)) VALUES ('"+name+"', '"+email+"', '"+password+"', '"+img+"')";
  con.query(sql, function (err, result) {
    if (err) throw err;
    console.log("### INSERT 1 record inserted");
});
    
    });
        socket.on("user-login", function(name,password) {
           
    con.query("SELECT * FROM users WHERE  name = '"+name+"' AND  password =  '"+password+"'",function (err, result, fields) {
    if (err){ throw err; }
          console.log("user-login");
         console.log(array.length);
           io.sockets.emit("login-result",name, result);
   // console.log(result);
       
  });

 
    
});
            socket.on("user-get-data", function(name) {
             console.log("user-get-data");
     
    con.query("SELECT * FROM users WHERE  name = '"+name+"';",function (err, result, fields) {
    if (err){ throw err; }else{io.sockets.emit(name, result);}
   // console.log(result);
       
  });

 
    
});
            socket.on("load-user-img", function(name,img) {
             console.log("load-user-img");
     
    con.query("UPDATE users SET img = '"+img+"' WHERE  name = '"+name+"';",function (err, result, fields) {
    if (err){ throw err; }else{
        
      //  io.sockets.emit(name, result);
    }
   
  });

    
});
    

//        socket.on("user-leave", function(value) {
//            
//                for( var i = 0; i < array.length; i++){ 
//                      console.log(array[i]);
//        if ( array[i] === value) { 
//            array.splice(i,2); 
//        }
//      }
//             console.log("###user "+value + " leave###");
//        
//        });
        
    }
              
io.sockets.on('connection', run);