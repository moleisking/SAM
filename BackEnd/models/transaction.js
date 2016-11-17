var model = require('nodejs-model');
var bCrypt = require('bcrypt-nodejs');

var transaction = new model("Transaction")
    .attr('id')    
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
                maximum: 2000,
                messages: {
                    tooShort: 'value is too low!',
                    tooLong: 'value is too high!'
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