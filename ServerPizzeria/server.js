var express = require('express');
var servidor = express();
servidor.use(express.bodyParser());

pizzas= [
			{
				nombre:'Hawaianna',
				ingredientes:'Jamon, Queso, Piña',
				precio:50
			},
			{
				nombre:'Peperoni',
				ingredientes:'Peperoni, Queso',
				precio:60
			},
			{
				nombre:'Mexicana',
				ingredientes:'Chile, Queso',
				precio:70
			},
			{
				nombre:'Americana',
				ingredientes:'Salchicha, Queso',
				precio:75
			},
			{
				nombre:'Queso',
				ingredientes:'Queso',
				precio:40
			}

		];

pedidos = [];
		
servidor.get('/',function(peticion,respuesta){
	respuesta.send("Bienvenido a la Pizzeria");
});

servidor.get('/pizzas', function(peticion, respuesta){
	respuesta.send(pizzas);
});

servidor.get('/pedidos', function(peticion, respuesta){
	respuesta.send(pedidos);
});

servidor.get('/pizzas/:id', function(peticion, respuesta){
		respuesta.send(pizzas[peticion.params.id]);					
});

servidor.get('/pedidos/:id', function(peticion, respuesta){
		respuesta.send(pedidos[peticion.params.id]);					
});

servidor.post('/pizzas',function(peticion, respuesta){
	var nueva_pizza = 
	{
		nombre:peticion.body.nombre,
		ingredientes:peticion.body.ingredientes,
		precio:peticion.body.precio		
	}
	pizzas.push(nueva_pizza);
	respuesta.send("Se ha agregado una nueva pizza en la posicion: "+(pizzas.length-1));
	console.log("Se ha agregado una nueva pizza en la posicion: "+(pizzas.length-1));
	console.log(pizzas);
});

servidor.post('/pedidos',function(peticion, respuesta){
	var nuevo_pedido = 
	{
		nombre:peticion.body.nombre,
		lista:peticion.body.lista,	
	}
	pedidos.push(nuevo_pedido);
	respuesta.send("Se ha agregado un pedido en la posicion: "+(pedidos.length-1));
	console.log("Se ha agregado un pedido en la posicion: "+(pedidos.length-1));
	console.log(pedidos);
});


servidor.delete('/pizzas/:id',function(peticion, respuesta){
	var eliminado = pizzas.splice(peticion.params.id,1);
	if (eliminado){
		respuesta.send("Se ha eliminado la pizza de la posicion: "+peticion.params.id);
		console.log("Se ha eliminado la pizza en la posicion: "+peticion.params.id);
		console.log(pizzas);
	}
	else
	{
		respuesta.send("No se ha eliminado la pizza");
	}
});

servidor.delete('/pedidos/:id',function(peticion, respuesta){
	var eliminado = pedidos.splice(peticion.params.id,1);
	if (eliminado){
		respuesta.send("Se ha eliminado el pedido de la posicion: "+peticion.params.id);
		console.log("Se ha eliminado el pedido de la posicion: "+peticion.params.id);
		console.log(pedidos);
	}
	else
	{
		respuesta.send("No se ha eliminado el pedido");
	}
});


servidor.put('/pizzas/:id',function(peticion,respuesta){
	var pizza_actualizada = 
	{
		nombre:peticion.body.nombre,
		ingredientes:peticion.body.ingredientes,
		precio:peticion.body.precio		
	}
	pizzas[peticion.params.id]=pizza_actualizada;
	respuesta.send("Se ha actualizado la pizza en la posicion: "+peticion.params.id);
	console.log("Se ha actualizado la pizza en la posicion: "+peticion.params.id);
	console.log(pizzas);
});

servidor.put('/pedidos/:id',function(peticion,respuesta){
	var pedido_actualizado = 
	{
		nombre:peticion.body.nombre,
		lista:peticion.body.lista,	
	}
	pedidos[peticion.params.id]=pedido_actualizado;
	respuesta.send("Se ha actualizado el pedido en la posicion: "+peticion.params.id);
	console.log("Se ha actualizado el pedido en la posicion: "+peticion.params.id);
	console.log(pedidos);
});
		
		
servidor.listen(8080);
console.log("Pizzeria esperando clientes");

