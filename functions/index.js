
let functions = require('firebase-functions');

let admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.sendActivitionForTrip = functions.database.ref('/Trips/{TripsId}/').onWrite((change, context) =>{
	// if (change.before.exists()) {
	// 	console.log("change.before.exists");
 //        return null;
 //      }
      // Exit when the data is deleted.
      if (!change.after.exists()) {
      	console.log("change.after.exists");
        return null;
      }

      const original = change.after.val();
      const tripName = original.name;
      var activation = original.activion;
      if (activation === 1 ){
      	var provider = original.providerId;
      	var providerRef = admin.database().ref('/Users/'+provider);
      	console.log(provider);
      	providerRef.on('value',function(snap){ 
			var registrationToken = snap.child('tokinID').val();
      		console.log(registrationToken);

      		var message = {
              data: {
              'id':'1',
              'firstName': tripName,
              'secondName': provider,
              },
                token: registrationToken
            };


            admin.messaging().send(message) 
       		 	.then((response) => { //--------------------------
          // Response is a message ID string.
          		return console.log('Successfully sent message:', response);
        		})
        		.catch((error) => {
          		return console.log('Error sending message:', error);
        		});


      	});

      }
    return console.log("");

});

exports.pushNotifications = functions.database.ref('/Requests/{RequestsId}')
.onCreate((snap,context) => {
  const data = snap.val();
    const tickets = data.tickets;
    const buyerUid = data.userUid;
    console.log('buyerUid',buyerUid);
    // console.log("tickets",tickets.length);
     // get the buyer information 
   var buyerRef = admin.database().ref('/Users/'+buyerUid);
    buyerRef.on('value',function(snap){      //--------------------------
        var bayerName = snap.child('username').val();
        // var bayerPhnoe = snapshot.child('phoneNumber').value();
        console.log('bayerName',bayerName);
        // console.log(bayerPhnoe);
        tickets.forEach(function(value){
  //       // var order = value.val();
        const uid = value.providerID;
        const pric = value.price;
        const tripName = value.prodictName;

        console.log('UID',value.providerID);
        console.log('UID',pric);
        console.log('UID',tripName);

        var ref = admin.database().ref('/Users/'+uid);
        ref.on('value',function(snap){ //--------------------------
            var registrationToken = snap.child('tokinID').val();
            // console.log("snap",registrationToken);

            var message = {
              data: {
              'id':'2',
              'firstName': buyerUid,
              'secondName': tripName,
              'provider': uid,
              'tickerNum':value.quantity.toString(),
              'total': pric.toString()      
              },
                token: registrationToken
            };
// Send a message to the device corresponding to the provided
// registration token.
      admin.messaging().send(message) 
        .then((response) => { //--------------------------
          // Response is a message ID string.
          return console.log('Successfully sent message:', response);
        })
        .catch((error) => {
          return console.log('Error sending message:', error);
        });
      });
    });
    });
});





