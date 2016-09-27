var model = require('nodejs-model');
var bCrypt = require('bcrypt-nodejs');

var transaction = new model("Transaction")
    .attr('userid', {
        validations: {
            presence: {
                message: 'Userid is required!'
            }
        }
    })
    .attr('name', {
        validations: {
            presence: {
                message: 'Name is required!'
            }
        }
    })
    .attr('value', {
        validations: {
            presence: {
                message: 'Value is required!'
            },
            length: {
                minimum: 1,
                maximum: 20,
                messages: {
                    tooShort: 'password is too short!',
                    tooLong: 'password is too long!'
                }
            }
        },
        // tags: ['private'] //this tags the accessibility as _private_ 
    })
    .attr('datetimestamp', {
        validations: {
            presence: {
                message: 'DateTimeStamp is required!'
            }
        }
    });    

module.exports = transaction;