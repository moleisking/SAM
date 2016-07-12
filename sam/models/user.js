var model = require('nodejs-model');
var bCrypt = require('bcrypt-nodejs');

var user = new model("User")
    .attr('name', {
        validations: {
            presence: {
                message: 'Name is required!'
            }
        }
    })
    .attr('pass', {
        validations: {
            presence: {
                message: 'Password is required!'
            },
            length: {
                minimum: 5,
                maximum: 20,
                messages: {
                    tooShort: 'password is too short!',
                    tooLong: 'password is too long!'
                }
            }
        },
        // tags: ['private'] //this tags the accessibility as _private_ 
    })
    .attr('email');
    // .attr('age');

module.exports = user;


module.exports.generateHash = function (password) {
    return bCrypt.hashSync(password, bCrypt.genSaltSync(10), null);
};

module.exports.validPassword = function (user, password) {
    return bCrypt.compareSync(password, user.password);
};

// var model = require('nodejs-model');

// //create a new model definition _User_ and define _name_/_password_ attributes 
// var User = model("User").attr('name', {
//   validations: {
//     presence: {
//       message: 'Name is required!'
//     }
//   }
// }).attr('password', {
//   validations: {
//     length: {
//       minimum: 5,
//       maximum: 20,
//       messages: {
//         tooShort: 'password is too short!',
//         tooLong: 'password is too long!'
//       }
//     }
//   },
//   //this tags the accessibility as _private_ 
//   tags: ['private']
// });

// var u1 = User.create();
// //getters are generated automatically 
// u1.name('foo');
// u1.password('password');

// console.log(u1.name());
// //prints _foo_ 

// //Invoke validations and wait for the validations to fulfill 
// u1.validate(function() {
//   if u1.isValid {
//      //validated, perform business logic 
//   } else {
//      //validation failed, dump validation errors to the console 
//      console.log(p1.errors)
//   }
// });

// //get object as a plain object, ready for JSON 
// console.log(u1.toJSON());
// //produces: { name: 'foo' } 

// //now also with attributes that were tagged with 'private' 
// console.log(u1.toJSON('private'));
// //produces: { name: 'foo' } { password: 'password' } 