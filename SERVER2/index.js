var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
var bullets = [];
server.listen(8080,function(){
	console.log("Servidor esperando Conexiones");
});

io.on('connection', function(socket){
	console.log("Jugador Conectado!");
	socket.emit('socketID', { id: socket.id });
	socket.emit('getPlayers',players);
	socket.emit('getBullets',bullets);
	socket.broadcast.emit('newPlayer',{id : socket.id });

    /*socket.on('joinGame'. function(player){
        console.log(player.id+ ' Se ha unido al juego');
        var x = getRandomInt(10,600);
        var y = getRandomInt(10,440);
        socket.emit()
    });*/

	socket.on('playerMoved',function(data){
	    data.id = socket.id;
	    socket.broadcast.emit('playerMoved',data);
	    console.log("playerMoved: "+ "ID: "+data.id+ " X: "+data.x + " Y: "+data.y+ " textura: "+data.texture);

	    for (var i = 0; i< players.length ; i++){
	        if(players[i].id== data.id){
	            players[i].x = data.x;
	            players[i].y = data.y;
	            players[i].texture = data.texture;
	        }
	    }
	});
	socket.on('playerShoot',function(data){
    	    data.id = socket.id;
    	    socket.broadcast.emit('playerShoot',data);
    	    console.log("playerShoot: "+ "ID: "+data.id+ " X: "+data.x + " Y: "+data.y + " DIRECTION "+ data.direction);

    	    for (var i = 0; i< bullets.length ; i++){
    	        if(bullets[i].id== data.id){
    	            bullets[i].x = data.x;
    	            bullets[i].y = data.y;
    	            bullets[i].direction = data.direction;
    	        }
    	    }
    	});

	socket.on('disconnect', function(){
		console.log("Jugador Desconectado");
		socket.broadcast.emit('playerDisconnected',{id : socket.id});
		for(var i = 0; i< players.length; i++){
		    if(players[i].id == socket.id){
		        players.splice(i,1);
		    }
		}
	});
	players.push(new player(socket.id,0,0));
});

function player(id, x, y){
	this.id = id;
	this.x = x;
	this.y = y;
}
function bullet(id, x, y ,direction){
	this.id = id;
	this.x = x;
	this.y = y;
	this.direction = direction;
}