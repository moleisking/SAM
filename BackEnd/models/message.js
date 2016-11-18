var model = require('nodejs-model');
var bCrypt = require('bcrypt-nodejs');

var message = new model("Message")
    .attr('id')  
    .attr('from_id')    
    .attr('from_name')    
    .attr('from_url')   
    .attr('from_email', {
        validations: {
            presence: {
                message: 'From email is required!'
            }
        }
    }) 
    .attr('to_id')    
    .attr('to_name')    
    .attr('to_url')   
    .attr('to_email', {
        validations: {
            presence: {
                message: 'To is required!'
            }
        }
    })
    .attr('text', {
        validations: {
            presence: {
                message: 'Text is required!'
            },
            length: {
                minimum: 3,
                maximum: 500,
                messages: {
                    tooShort: 'Text is too short!',
                    tooLong: 'Text is too long!'
                }
            }
        }
    })
    .attr('datetimestamp', {
        validations: {
            presence: {
                message: 'DateStamp is required!'
            }
        }
    });

module.exports = message;

