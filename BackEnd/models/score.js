var model = require('nodejs-model');
var bCrypt = require('bcrypt-nodejs');

var score = new model("score")   
    .attr('id', {
        validations: {
            presence: {
                message: 'Id is required!'
            }
        }
    })   
    .attr('judgeid') 
    .attr('userid')
    .attr('value', {
        validations: {
            presence: {
                message: 'A value is required!'
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

module.exports = rating;
